package com.PoseidonCapitalSolutions.TradingApp.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeDTO {

    private Integer id;

    @NotBlank(message = "Account is mandatory")
    @Size(max = 30, message = "Account must not exceed 30 characters")
    private String account;

    @NotBlank(message = "Type is mandatory")
    @Size(max = 30, message = "Type must not exceed 30 characters")
    private String type;

    private Double buyQuantity;

}
