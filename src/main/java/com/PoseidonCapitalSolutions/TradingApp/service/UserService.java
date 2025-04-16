package com.PoseidonCapitalSolutions.TradingApp.service;

import com.PoseidonCapitalSolutions.TradingApp.domain.User;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserCreateDTO;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserResponseDTO;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserUpdateDTO;
import com.PoseidonCapitalSolutions.TradingApp.exception.LastAdminException;
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

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponseDTO)
                .toList();
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public UserResponseDTO findById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toUserResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid user id: " + id));
    }

    @Transactional
    public void create(UserCreateDTO userCreateDTO) {
        userCreateDTO.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        userRepository.save(userMapper.toUser(userCreateDTO));
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    public void update(Integer id, UserUpdateDTO userUpdateDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid user id: " + id));

        existingUser.setUsername(userUpdateDTO.getUsername());
        existingUser.setFullname(userUpdateDTO.getFullname());
        existingUser.setRole(userUpdateDTO.getRole());

        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }

        userRepository.save(existingUser);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    public void delete(Integer id) {

        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid user id: " + id));

        if ("ADMIN".equals(userToDelete.getRole())) {
            long adminCount = userRepository.countByRole("ADMIN");

            if (adminCount == 1) {
                throw new LastAdminException("A minimum of one Admin is needed");
            }
        }

        userRepository.deleteById(id);
    }
}
