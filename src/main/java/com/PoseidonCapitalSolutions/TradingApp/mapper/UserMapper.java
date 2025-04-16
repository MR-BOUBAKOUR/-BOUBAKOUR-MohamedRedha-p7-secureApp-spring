package com.PoseidonCapitalSolutions.TradingApp.mapper;

import com.PoseidonCapitalSolutions.TradingApp.domain.User;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserCreateDTO;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toUserResponseDTO(User user);
    User toUser(UserCreateDTO userCreateDTO);

}
