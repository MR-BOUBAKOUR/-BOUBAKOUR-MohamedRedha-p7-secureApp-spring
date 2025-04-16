package com.PoseidonCapitalSolutions.TradingApp.service;

import com.PoseidonCapitalSolutions.TradingApp.domain.User;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserDTO;
import com.PoseidonCapitalSolutions.TradingApp.exception.ResourceNotFoundException;
import com.PoseidonCapitalSolutions.TradingApp.mapper.UserMapper;
import com.PoseidonCapitalSolutions.TradingApp.repositorie.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDTO)
                .toList();
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public UserDTO findById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid user id: " + id));
    }

    @Transactional
    public void create(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(userMapper.toUser(userDTO));
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    public void update(Integer id, UserDTO updatedUserDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid user id: " + id));

        userMapper.updateUserFromDto(updatedUserDTO, existingUser);

        if (updatedUserDTO.getPassword() != null && !updatedUserDTO.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUserDTO.getPassword()));
        }

        userRepository.save(existingUser);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}
