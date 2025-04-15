package com.PoseidonCapitalSolutions.TradingApp.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseDTO {
    private String errorCode;
    private String errorMessage;
    private Instant timestamp;
}
