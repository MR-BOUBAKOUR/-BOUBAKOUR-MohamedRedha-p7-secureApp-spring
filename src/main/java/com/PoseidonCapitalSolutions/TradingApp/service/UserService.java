package com.PoseidonCapitalSolutions.TradingApp.service;

import com.PoseidonCapitalSolutions.TradingApp.domain.User;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserDTO;
import com.PoseidonCapitalSolutions.TradingApp.exception.ResourceNotFoundException;
import com.PoseidonCapitalSolutions.TradingApp.mapper.UserMapper;
import com.PoseidonCapitalSolutions.TradingApp.repositorie.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid user id: " + id));
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDTO)
                .toList();
    }

    @Transactional
    public void create(UserDTO userDTO) {
        userRepository.save(userMapper.toUser(userDTO));
    }

    @Transactional
    public void update(Integer id, UserDTO updatedUserDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid user id: " + id));

        userMapper.updateUserFromDto(updatedUserDTO, existingUser);
        userRepository.save(existingUser);
    }

    @Transactional
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}
