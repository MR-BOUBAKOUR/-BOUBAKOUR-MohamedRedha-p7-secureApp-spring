package com.PoseidonCapitalSolutions.TradingApp.unit.service;

import com.PoseidonCapitalSolutions.TradingApp.domain.Rating;
import com.PoseidonCapitalSolutions.TradingApp.dto.RatingDTO;
import com.PoseidonCapitalSolutions.TradingApp.exception.ResourceNotFoundException;
import com.PoseidonCapitalSolutions.TradingApp.mapper.RatingMapper;
import com.PoseidonCapitalSolutions.TradingApp.repositorie.RatingRepository;
import com.PoseidonCapitalSolutions.TradingApp.service.RatingService;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingMapper ratingMapper;

    @InjectMocks
    private RatingService ratingService;

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
        Rating entity = new Rating();
        RatingDTO dto = new RatingDTO();

        when(ratingRepository.findById(id)).thenReturn(Optional.of(entity));
        when(ratingMapper.toRatingDTO(entity)).thenReturn(dto);

        RatingDTO result = ratingService.findById(id);

        assertEquals(dto, result);
        verify(ratingRepository).findById(id);
        verify(ratingMapper).toRatingDTO(entity);
    }

    @Test
    void findById_exception() {
        when(ratingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ratingService.findById(id));
        verify(ratingRepository).findById(id);
    }

    @Test
    void findAll_success() {
        List<Rating> entities = List.of(new Rating(), new Rating());

        when(ratingRepository.findAll()).thenReturn(entities);
        when(ratingMapper.toRatingDTO(any())).thenReturn(new RatingDTO());

        List<RatingDTO> result = ratingService.findAll();

        assertEquals(2, result.size());
        verify(ratingRepository).findAll();
        verify(ratingMapper, times(2)).toRatingDTO(any());
    }

    @Test
    void create_success() {
        RatingDTO dto = new RatingDTO();
        Rating entity = new Rating();

        when(ratingMapper.toRating(dto)).thenReturn(entity);

        ratingService.create(dto);

        verify(ratingMapper).toRating(dto);
        verify(ratingRepository).save(entity);
    }

    @Test
    void update_success() {
        RatingDTO dto = new RatingDTO();
        Rating existingEntity = new Rating();

        when(ratingRepository.findById(id)).thenReturn(Optional.of(existingEntity));

        ratingService.update(id, dto);

        verify(ratingMapper).updateRatingFromDto(dto, existingEntity);
        verify(ratingRepository).save(existingEntity);
    }

    @Test
    void update_exception() {
        RatingDTO dto = new RatingDTO();

        when(ratingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ratingService.update(id, dto));
        verify(ratingRepository).findById(id);
    }

    @Test
    void delete_success() {
        ratingService.delete(id);

        verify(ratingRepository).deleteById(id);
    }
}