package com.PoseidonCapitalSolutions.TradingApp.dto;

import lombok.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurvePointDTO {

    private Integer id;

    private Instant asOfDate;
    private Double term;
    private Double value;
    private Instant creationDate;
}
