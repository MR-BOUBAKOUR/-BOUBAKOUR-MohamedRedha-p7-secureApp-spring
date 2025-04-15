package com.PoseidonCapitalSolutions.TradingApp.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurvePointDTO {

    private Integer id;

    private Double term;
    private Double value;

}
