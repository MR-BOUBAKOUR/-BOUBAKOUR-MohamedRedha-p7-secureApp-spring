package com.PoseidonCapitalSolutions.TradingApp.mapper;

import com.PoseidonCapitalSolutions.TradingApp.domain.CurvePoint;
import com.PoseidonCapitalSolutions.TradingApp.dto.CurvePointDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CurvePointMapper {

    CurvePointDTO toCurvePointDTO(CurvePoint curvePoint);
    CurvePoint toCurvePoint(CurvePointDTO curvePointDTO);

    @Mapping(target = "id", ignore = true)
    void updateCurvePointFromDto(CurvePointDTO dto, @MappingTarget CurvePoint entity);
}
