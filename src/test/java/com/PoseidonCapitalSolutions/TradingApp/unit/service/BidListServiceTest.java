package com.PoseidonCapitalSolutions.TradingApp.unit.service;

import com.PoseidonCapitalSolutions.TradingApp.domain.BidList;
import com.PoseidonCapitalSolutions.TradingApp.dto.BidListDTO;
import com.PoseidonCapitalSolutions.TradingApp.exception.ResourceNotFoundException;
import com.PoseidonCapitalSolutions.TradingApp.mapper.BidListMapper;
import com.PoseidonCapitalSolutions.TradingApp.repository.BidListRepository;
import com.PoseidonCapitalSolutions.TradingApp.service.BidListService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BidListServiceTest {

    @Mock
    private BidListRepository bidListRepository;

    @Mock
    private BidListMapper bidListMapper;

    @InjectMocks
    private BidListService bidListService;


    private AutoCloseable closeable;
    private Integer id;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        id = 1;
    }
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void findById_success() {

        BidList bid = new BidList();
        BidListDTO dto = new BidListDTO();
        when(bidListRepository.findById(id)).thenReturn(Optional.of(bid));
        when(bidListMapper.toBidListDTO(bid)).thenReturn(dto);

        BidListDTO result = bidListService.findById(id);

        assertEquals(dto, result);
        verify(bidListRepository).findById(id);
        verify(bidListMapper).toBidListDTO(bid);
    }

    @Test
    void findById_exception() {
        when(bidListRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bidListService.findById(id));

        verify(bidListRepository).findById(id);
    }

    @Test
    void findAll_success() {
        List<BidList> bidList = List.of(new BidList(), new BidList());

        when(bidListRepository.findAll()).thenReturn(bidList);
        when(bidListMapper.toBidListDTO(any())).thenReturn(new BidListDTO());

        List<BidListDTO> result = bidListService.findAll();

        assertEquals(2, result.size());

        verify(bidListRepository).findAll();
        verify(bidListMapper, times(2)).toBidListDTO(any());
    }

    @Test
    void create_success() {
        BidListDTO dto = new BidListDTO();
        BidList entity = new BidList();

        when(bidListMapper.toBidList(dto)).thenReturn(entity);

        bidListService.create(dto);

        verify(bidListMapper).toBidList(dto);
        verify(bidListRepository).save(entity);
    }

    @Test
    void update_success() {
        BidListDTO updatedDto = new BidListDTO();
        BidList existingEntity = new BidList();

        when(bidListRepository.findById(id)).thenReturn(Optional.of(existingEntity));

        bidListService.update(id, updatedDto);

        verify(bidListMapper).updateBidListFromDto(updatedDto, existingEntity);
        verify(bidListRepository).save(existingEntity);
    }

    @Test
    void update_exception() {
        BidListDTO updatedDto = new BidListDTO();

        when(bidListRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bidListService.update(id, updatedDto));
        verify(bidListRepository).findById(id);
    }

    @Test
    void delete_success() {
        bidListService.delete(id);

        verify(bidListRepository).deleteById(id);
    }
}
