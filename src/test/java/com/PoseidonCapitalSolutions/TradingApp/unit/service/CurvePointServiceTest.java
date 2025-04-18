package com.PoseidonCapitalSolutions.TradingApp.unit.service;

import com.PoseidonCapitalSolutions.TradingApp.domain.CurvePoint;
import com.PoseidonCapitalSolutions.TradingApp.dto.CurvePointDTO;
import com.PoseidonCapitalSolutions.TradingApp.exception.ResourceNotFoundException;
import com.PoseidonCapitalSolutions.TradingApp.mapper.CurvePointMapper;
import com.PoseidonCapitalSolutions.TradingApp.repository.CurvePointRepository;
import com.PoseidonCapitalSolutions.TradingApp.service.CurvePointService;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurvePointServiceTest {

    @Mock
    private CurvePointRepository curvePointRepository;

    @Mock
    private CurvePointMapper curvePointMapper;

    @InjectMocks
    private CurvePointService curvePointService;

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
        CurvePoint entity = new CurvePoint();
        CurvePointDTO dto = new CurvePointDTO();

        when(curvePointRepository.findById(id)).thenReturn(Optional.of(entity));
        when(curvePointMapper.toCurvePointDTO(entity)).thenReturn(dto);

        CurvePointDTO result = curvePointService.findById(id);

        assertEquals(dto, result);
        verify(curvePointRepository).findById(id);
        verify(curvePointMapper).toCurvePointDTO(entity);
    }

    @Test
    void findById_exception() {
        when(curvePointRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> curvePointService.findById(id));
        verify(curvePointRepository).findById(id);
    }

    @Test
    void findAll_success() {
        List<CurvePoint> entities = List.of(new CurvePoint(), new CurvePoint());

        when(curvePointRepository.findAll()).thenReturn(entities);
        when(curvePointMapper.toCurvePointDTO(any())).thenReturn(new CurvePointDTO());

        List<CurvePointDTO> result = curvePointService.findAll();

        assertEquals(2, result.size());
        verify(curvePointRepository).findAll();
        verify(curvePointMapper, times(2)).toCurvePointDTO(any());
    }

    @Test
    void create_success() {
        CurvePointDTO dto = new CurvePointDTO();
        CurvePoint entity = new CurvePoint();

        when(curvePointMapper.toCurvePoint(dto)).thenReturn(entity);

        curvePointService.create(dto);

        verify(curvePointMapper).toCurvePoint(dto);
        verify(curvePointRepository).save(entity);
    }

    @Test
    void update_success() {
        CurvePointDTO dto = new CurvePointDTO();
        CurvePoint existingEntity = new CurvePoint();

        when(curvePointRepository.findById(id)).thenReturn(Optional.of(existingEntity));

        curvePointService.update(id, dto);

        verify(curvePointMapper).updateCurvePointFromDto(dto, existingEntity);
        verify(curvePointRepository).save(existingEntity);
    }

    @Test
    void update_exception() {
        CurvePointDTO dto = new CurvePointDTO();

        when(curvePointRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> curvePointService.update(id, dto));
        verify(curvePointRepository).findById(id);
    }

    @Test
    void delete_success() {
        curvePointService.delete(id);

        verify(curvePointRepository).deleteById(id);
    }
}
