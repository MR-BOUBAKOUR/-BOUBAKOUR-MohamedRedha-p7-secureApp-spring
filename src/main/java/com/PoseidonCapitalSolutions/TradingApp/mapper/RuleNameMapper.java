package com.PoseidonCapitalSolutions.TradingApp.mapper;

import com.PoseidonCapitalSolutions.TradingApp.domain.RuleName;
import com.PoseidonCapitalSolutions.TradingApp.dto.RuleNameDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RuleNameMapper {

    RuleNameDTO toRuleNameDTO(RuleName ruleName);
    RuleName toRuleName(RuleNameDTO ruleNameDTO);

    @Mapping(target = "id", ignore = true)
    void updateRuleNameFromDto(RuleNameDTO dto, @MappingTarget RuleName entity);
}
