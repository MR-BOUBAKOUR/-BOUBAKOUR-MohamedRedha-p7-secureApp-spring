package com.PoseidonCapitalSolutions.TradingApp.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {

    private Integer id;

    @Size(max = 125, message = "Moody's rating must not exceed 125 characters")
    private String moodysRating;

    @Size(max = 125, message = "S&P rating must not exceed 125 characters")
    private String sandPRating;

    @Size(max = 125, message = "Fitch rating must not exceed 125 characters")
    private String fitchRating;

    private Byte orderNumber;
}
