package com.PoseidonCapitalSolutions.TradingApp.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {

    private Integer id;

    @NotBlank(message = "MoodysRating is mandatory")
    @Size(max = 125, message = "Moody's rating must not exceed 125 characters")
    private String moodysRating;

    @NotBlank(message = "SandPRating is mandatory")
    @Size(max = 125, message = "S&P rating must not exceed 125 characters")
    private String sandPRating;

    @NotBlank(message = "FitchRating is mandatory")
    @Size(max = 125, message = "Fitch rating must not exceed 125 characters")
    private String fitchRating;

    private Byte orderNumber;
}
