package com.PoseidonCapitalSolutions.TradingApp.service;

import com.PoseidonCapitalSolutions.TradingApp.domain.BidList;
import com.PoseidonCapitalSolutions.TradingApp.dto.BidListDTO;
import com.PoseidonCapitalSolutions.TradingApp.exception.ResourceNotFoundException;
import com.PoseidonCapitalSolutions.TradingApp.mapper.BidListMapper;
import com.PoseidonCapitalSolutions.TradingApp.repository.BidListRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type Bid list service.
 */
@AllArgsConstructor
@Service
public class BidListService {

    private final BidListRepository bidListRepository;
    private final BidListMapper bidListMapper;

    /**
     * Find by id bid list dto.
     *
     * @param id the id
     * @return the bid list dto
     */
    public BidListDTO findById(Integer id) {
        return bidListRepository.findById(id)
                .map(bidListMapper::toBidListDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid bid list id: " + id));
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<BidListDTO> findAll() {
        return bidListRepository.findAll().stream()
                .map(bidListMapper::toBidListDTO)
                .toList();
    }

    /**
     * Create.
     *
     * @param bidListDTO the bid list dto
     */
    @Transactional
    public void create(BidListDTO bidListDTO) {
        bidListRepository.save(bidListMapper.toBidList(bidListDTO));
    }

    /**
     * Update.
     *
     * @param id                the id
     * @param updatedBidListDTO the updated bid list dto
     */
    @Transactional
    public void update(Integer id, BidListDTO updatedBidListDTO) {
        BidList existingBidList =  bidListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid bid list id: " + id));

        bidListMapper.updateBidListFromDto(updatedBidListDTO, existingBidList);
        bidListRepository.save(existingBidList);
    }

    /**
     * Delete.
     *
     * @param id the id
     */
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(Integer id) {
        BidList bidListToDelete = bidListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid bid list id: " + id));

        bidListRepository.delete(bidListToDelete);
    }

}
