package com.PoseidonCapitalSolutions.TradingApp.service;

import com.PoseidonCapitalSolutions.TradingApp.domain.Trade;
import com.PoseidonCapitalSolutions.TradingApp.dto.TradeDTO;
import com.PoseidonCapitalSolutions.TradingApp.exception.ResourceNotFoundException;
import com.PoseidonCapitalSolutions.TradingApp.mapper.TradeMapper;
import com.PoseidonCapitalSolutions.TradingApp.repository.TradeRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class TradeService {

    private final TradeRepository tradeRepository;
    private final TradeMapper tradeMapper;

    public TradeDTO findById(Integer id) {
        return tradeRepository.findById(id)
                .map(tradeMapper::toTradeDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid trade id: " + id));
    }

    public List<TradeDTO> findAll() {
        return tradeRepository.findAll().stream()
                .map(tradeMapper::toTradeDTO)
                .toList();
    }

    @Transactional
    public void create(TradeDTO tradeDTO) {
        tradeRepository.save(tradeMapper.toTrade(tradeDTO));
    }

    @Transactional
    public void update(Integer id, TradeDTO updatedTradeDTO) {
        Trade existingTrade = tradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid trade id: " + id));

        tradeMapper.updateTradeFromDto(updatedTradeDTO, existingTrade);
        tradeRepository.save(existingTrade);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(Integer id) {
        tradeRepository.deleteById(id);
    }
}
