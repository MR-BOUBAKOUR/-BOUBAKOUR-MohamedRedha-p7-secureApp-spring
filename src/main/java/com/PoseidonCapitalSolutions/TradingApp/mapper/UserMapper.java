package com.PoseidonCapitalSolutions.TradingApp.mapper;

import com.PoseidonCapitalSolutions.TradingApp.domain.User;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(User user);
    User toUser(UserDTO userDTO);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserDTO dto, @MappingTarget User entity);
}
