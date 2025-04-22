package com.PoseidonCapitalSolutions.TradingApp.service;

import com.PoseidonCapitalSolutions.TradingApp.domain.RuleName;
import com.PoseidonCapitalSolutions.TradingApp.dto.RuleNameDTO;
import com.PoseidonCapitalSolutions.TradingApp.exception.ResourceNotFoundException;
import com.PoseidonCapitalSolutions.TradingApp.mapper.RuleNameMapper;
import com.PoseidonCapitalSolutions.TradingApp.repository.RuleNameRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class RuleNameService {

    private final RuleNameRepository ruleNameRepository;
    private final RuleNameMapper ruleNameMapper;

    public RuleNameDTO findById(Integer id) {
        return ruleNameRepository.findById(id)
                .map(ruleNameMapper::toRuleNameDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid rule name id: " + id));
    }

    public List<RuleNameDTO> findAll() {
        return ruleNameRepository.findAll().stream()
                .map(ruleNameMapper::toRuleNameDTO)
                .toList();
    }

    @Transactional
    public void create(RuleNameDTO ruleNameDTO) {
        ruleNameRepository.save(ruleNameMapper.toRuleName(ruleNameDTO));
    }

    @Transactional
    public void update(Integer id, RuleNameDTO updatedRuleNameDTO) {
        RuleName existingRuleName = ruleNameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid rule name id: " + id));

        ruleNameMapper.updateRuleNameFromDto(updatedRuleNameDTO, existingRuleName);
        ruleNameRepository.save(existingRuleName);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(Integer id) {
        RuleName ruleNameToDelete = ruleNameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid rule name id: " + id));

        ruleNameRepository.delete(ruleNameToDelete);
    }
}
