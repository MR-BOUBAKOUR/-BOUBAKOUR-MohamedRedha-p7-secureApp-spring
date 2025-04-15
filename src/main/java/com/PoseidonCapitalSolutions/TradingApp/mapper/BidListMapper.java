package com.PoseidonCapitalSolutions.TradingApp.mapper;

import com.PoseidonCapitalSolutions.TradingApp.domain.BidList;
import com.PoseidonCapitalSolutions.TradingApp.dto.BidListDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BidListMapper {

    BidListDTO toBidListDTO(BidList bidList);
    BidList toBidList(BidListDTO bidListDTO);

    @Mapping(target = "id", ignore = true)
    void updateBidListFromDto(BidListDTO dto, @MappingTarget BidList entity);
}