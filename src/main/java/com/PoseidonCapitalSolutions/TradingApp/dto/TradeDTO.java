package com.PoseidonCapitalSolutions.TradingApp.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.Instant;

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
    private Double sellQuantity;
    private Double buyPrice;
    private Double sellPrice;

    private Instant tradeDate;

    @Size(max = 125, message = "Security must not exceed 125 characters")
    private String security;

    @Size(max = 10, message = "Status must not exceed 10 characters")
    private String status;

    @Size(max = 125, message = "Trader must not exceed 125 characters")
    private String trader;

    @Size(max = 125, message = "Benchmark must not exceed 125 characters")
    private String benchmark;

    @Size(max = 125, message = "Book must not exceed 125 characters")
    private String book;

    @Size(max = 125, message = "Creation name must not exceed 125 characters")
    private String creationName;

    private Instant creationDate;

    @Size(max = 125, message = "Revision name must not exceed 125 characters")
    private String revisionName;

    private Instant revisionDate;

    @Size(max = 125, message = "Deal name must not exceed 125 characters")
    private String dealName;

    @Size(max = 125, message = "Deal type must not exceed 125 characters")
    private String dealType;

    @Size(max = 125, message = "Source list ID must not exceed 125 characters")
    private String sourceListId;

    @Size(max = 125, message = "Side must not exceed 125 characters")
    private String side;
}
