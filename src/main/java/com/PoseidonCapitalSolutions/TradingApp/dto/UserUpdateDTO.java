package com.PoseidonCapitalSolutions.TradingApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    private Integer id;

    @NotBlank(message = "Username is mandatory")
    @Size(max = 125, message = "Username must not exceed 125 characters")
    private String username;

    @Size(max = 125, message = "Password must not exceed 125 characters")
    private String password;

    @Size(max = 125, message = "Full name must not exceed 125 characters")
    private String fullname;

    @NotBlank(message = "Role is mandatory")
    @Size(max = 125, message = "Role must not exceed 125 characters")
    private String role;
}
