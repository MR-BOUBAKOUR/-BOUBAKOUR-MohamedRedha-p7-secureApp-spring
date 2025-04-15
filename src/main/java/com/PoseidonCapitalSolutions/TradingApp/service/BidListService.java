package com.PoseidonCapitalSolutions.TradingApp.service;

import com.PoseidonCapitalSolutions.TradingApp.domain.BidList;
import com.PoseidonCapitalSolutions.TradingApp.dto.BidListDTO;
import com.PoseidonCapitalSolutions.TradingApp.exception.ResourceNotFoundException;
import com.PoseidonCapitalSolutions.TradingApp.mapper.BidListMapper;
import com.PoseidonCapitalSolutions.TradingApp.repositorie.BidListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class BidListService {

    private final BidListRepository bidListRepository;
    private final BidListMapper bidListMapper;

    public BidListDTO findById(Integer id) {
        return bidListRepository.findById(id)
                .map(bidListMapper::toBidListDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid bid list id: " + id));
    }

    public List<BidListDTO> findAll() {
        return bidListRepository.findAll().stream()
                .map(bidListMapper::toBidListDTO)
                .toList();
    }

    @Transactional
    public void create(BidListDTO bidListDTO) {
        bidListRepository.save(bidListMapper.toBidList(bidListDTO));
    }

    @Transactional
    public void update(Integer id, BidListDTO updatedBidListDTO) {
        BidList existingBidList =  bidListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid bid list id: " + id));

        bidListMapper.updateBidListFromDto(updatedBidListDTO, existingBidList);
        bidListRepository.save(existingBidList);
    }

    @Transactional
    public void delete(Integer id) {
        bidListRepository.deleteById(id);
    }

}
