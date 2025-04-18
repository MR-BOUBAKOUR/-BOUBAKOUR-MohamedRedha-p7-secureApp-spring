package com.PoseidonCapitalSolutions.TradingApp.unit.service;

import com.PoseidonCapitalSolutions.TradingApp.config.SecurityUtils;
import com.PoseidonCapitalSolutions.TradingApp.domain.User;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserCreateDTO;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserResponseDTO;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserUpdateDTO;
import com.PoseidonCapitalSolutions.TradingApp.exception.LastAdminException;
import com.PoseidonCapitalSolutions.TradingApp.exception.ResourceNotFoundException;
import com.PoseidonCapitalSolutions.TradingApp.mapper.UserMapper;
import com.PoseidonCapitalSolutions.TradingApp.repository.UserRepository;
import com.PoseidonCapitalSolutions.TradingApp.service.UserService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private UserService userService;

    private AutoCloseable closeable;
    private Integer id;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        id = 1;
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void findAll_success() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toUserResponseDTO(any())).thenReturn(new UserResponseDTO());

        List<UserResponseDTO> result = userService.findAll();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
        verify(userMapper, times(2)).toUserResponseDTO(any());
    }

    @Test
    void findById_success() {
        User user = new User();
        UserResponseDTO dto = new UserResponseDTO();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDTO(user)).thenReturn(dto);

        UserResponseDTO result = userService.findById(id);

        assertEquals(dto, result);
        verify(userRepository).findById(id);
        verify(userMapper).toUserResponseDTO(user);
    }

    @Test
    void findById_exception() {
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(id));
        verify(userRepository).findById(id);
    }

    @Test
    void create_success() {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setPassword("password");
        User user = new User();

        when(userMapper.toUser(userCreateDTO)).thenReturn(user);
        when(passwordEncoder.encode(userCreateDTO.getPassword())).thenReturn("encodedPassword");

        userService.create(userCreateDTO);

        verify(userMapper).toUser(userCreateDTO);
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(user);
    }

    @Test
    void update_success() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        User existingUser = new User();

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

        userService.update(id, userUpdateDTO, null, null);

        assertEquals(userUpdateDTO.getUsername(), existingUser.getUsername());
        assertEquals(userUpdateDTO.getFullname(), existingUser.getFullname());
        verify(userRepository).save(existingUser);
    }

    @Test
    void update_exception() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.update(id, userUpdateDTO, null, null));
        verify(userRepository).findById(id);
    }

    @Test
    void delete_success() {
        User user = new User();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.countByRole("ADMIN")).thenReturn(2L);
        when(securityUtils.getCurrentUserId()).thenReturn(2);

        userService.delete(id, null, null);

        verify(userRepository).deleteById(id);
    }

    @Test
    void delete_last_admin_exception() {
        User user = new User();
        user.setId(id);
        user.setRole("ADMIN");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.countByRole("ADMIN")).thenReturn(1L);

        assertThrows(LastAdminException.class, () -> userService.delete(id, null, null));
        verify(userRepository).findById(id);
        verify(userRepository).countByRole("ADMIN");
    }

    @Test
    void delete_self_deletion() {
        User user = new User();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.countByRole("ADMIN")).thenReturn(2L);
        when(securityUtils.getCurrentUserId()).thenReturn(id);

        userService.delete(id, null, null);

        verify(userRepository).deleteById(id);
    }
}