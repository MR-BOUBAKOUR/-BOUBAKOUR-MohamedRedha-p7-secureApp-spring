package com.PoseidonCapitalSolutions.TradingApp.unit.service;

import com.PoseidonCapitalSolutions.TradingApp.domain.Trade;
import com.PoseidonCapitalSolutions.TradingApp.dto.TradeDTO;
import com.PoseidonCapitalSolutions.TradingApp.exception.ResourceNotFoundException;
import com.PoseidonCapitalSolutions.TradingApp.mapper.TradeMapper;
import com.PoseidonCapitalSolutions.TradingApp.repository.TradeRepository;
import com.PoseidonCapitalSolutions.TradingApp.service.TradeService;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private TradeMapper tradeMapper;

    @InjectMocks
    private TradeService tradeService;

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
        Trade entity = new Trade();
        TradeDTO dto = new TradeDTO();

        when(tradeRepository.findById(id)).thenReturn(Optional.of(entity));
        when(tradeMapper.toTradeDTO(entity)).thenReturn(dto);

        TradeDTO result = tradeService.findById(id);

        assertEquals(dto, result);
        verify(tradeRepository).findById(id);
        verify(tradeMapper).toTradeDTO(entity);
    }

    @Test
    void findById_exception() {
        when(tradeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tradeService.findById(id));
        verify(tradeRepository).findById(id);
    }

    @Test
    void findAll_success() {
        List<Trade> entities = List.of(new Trade(), new Trade());

        when(tradeRepository.findAll()).thenReturn(entities);
        when(tradeMapper.toTradeDTO(any())).thenReturn(new TradeDTO());

        List<TradeDTO> result = tradeService.findAll();

        assertEquals(2, result.size());
        verify(tradeRepository).findAll();
        verify(tradeMapper, times(2)).toTradeDTO(any());
    }

    @Test
    void create_success() {
        TradeDTO dto = new TradeDTO();
        Trade entity = new Trade();

        when(tradeMapper.toTrade(dto)).thenReturn(entity);

        tradeService.create(dto);

        verify(tradeMapper).toTrade(dto);
        verify(tradeRepository).save(entity);
    }

    @Test
    void update_success() {
        TradeDTO dto = new TradeDTO();
        Trade existingEntity = new Trade();

        when(tradeRepository.findById(id)).thenReturn(Optional.of(existingEntity));

        tradeService.update(id, dto);

        verify(tradeMapper).updateTradeFromDto(dto, existingEntity);
        verify(tradeRepository).save(existingEntity);
    }

    @Test
    void update_exception() {
        TradeDTO dto = new TradeDTO();

        when(tradeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tradeService.update(id, dto));
        verify(tradeRepository).findById(id);
    }

    @Test
    void delete_success() {
        Trade tradeToDelete = new Trade();

        when(tradeRepository.findById(id)).thenReturn(Optional.of(tradeToDelete));

        tradeService.delete(id);

        verify(tradeRepository).delete(tradeToDelete);
    }

    @Test
    void delete_exception() {
        when(tradeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tradeService.delete(id));

        verify(tradeRepository, never()).delete(any());
    }

}