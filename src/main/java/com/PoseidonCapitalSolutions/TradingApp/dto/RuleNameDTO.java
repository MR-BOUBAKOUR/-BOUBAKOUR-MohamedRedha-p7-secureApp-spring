package com.PoseidonCapitalSolutions.TradingApp.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleNameDTO {

    private Integer id;

    @NotBlank(message = "name is mandatory")
    @Size(max = 125, message = "Name must not exceed 125 characters")
    private String name;

    @Size(max = 125, message = "Description must not exceed 125 characters")
    private String description;

    @Size(max = 125, message = "JSON content must not exceed 125 characters")
    private String json;

    @Size(max = 512, message = "Template must not exceed 512 characters")
    private String template;

    @Size(max = 125, message = "SQL string must not exceed 125 characters")
    private String sqlStr;

    @Size(max = 125, message = "SQL part must not exceed 125 characters")
    private String sqlPart;
}
