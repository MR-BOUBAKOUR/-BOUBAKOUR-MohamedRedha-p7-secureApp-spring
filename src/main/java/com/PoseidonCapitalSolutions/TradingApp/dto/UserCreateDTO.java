package com.PoseidonCapitalSolutions.TradingApp.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {

    private Integer id;

    @NotBlank(message = "Username is mandatory")
    @Size(max = 125, message = "Username must not exceed 125 characters")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(max = 125, message = "Password must not exceed 125 characters")
    private String password;

    @Size(max = 125, message = "Full name must not exceed 125 characters")
    private String fullname;

    @NotBlank(message = "Role is mandatory")
    @Size(max = 125, message = "Role must not exceed 125 characters")
    private String role;
}
