package com.PoseidonCapitalSolutions.TradingApp.mapper;

import com.PoseidonCapitalSolutions.TradingApp.domain.Trade;
import com.PoseidonCapitalSolutions.TradingApp.dto.TradeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TradeMapper {

    TradeDTO toTradeDTO(Trade trade);
    Trade toTrade(TradeDTO tradeDTO);

    @Mapping(target = "id", ignore = true)
    void updateTradeFromDto(TradeDTO dto, @MappingTarget Trade entity);
}
