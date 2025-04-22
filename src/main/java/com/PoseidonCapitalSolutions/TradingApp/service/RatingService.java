package com.PoseidonCapitalSolutions.TradingApp.service;

import com.PoseidonCapitalSolutions.TradingApp.domain.Rating;
import com.PoseidonCapitalSolutions.TradingApp.dto.RatingDTO;
import com.PoseidonCapitalSolutions.TradingApp.exception.ResourceNotFoundException;
import com.PoseidonCapitalSolutions.TradingApp.mapper.RatingMapper;
import com.PoseidonCapitalSolutions.TradingApp.repository.RatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type Rating service.
 */
@AllArgsConstructor
@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    /**
     * Find by id rating dto.
     *
     * @param id the id
     * @return the rating dto
     */
    public RatingDTO findById(Integer id) {
        return ratingRepository.findById(id)
                .map(ratingMapper::toRatingDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid rating id: " + id));
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<RatingDTO> findAll() {
        return ratingRepository.findAll().stream()
                .map(ratingMapper::toRatingDTO)
                .toList();
    }

    /**
     * Create.
     *
     * @param ratingDTO the rating dto
     */
    @Transactional
    public void create(RatingDTO ratingDTO) {
        ratingRepository.save(ratingMapper.toRating(ratingDTO));
    }

    /**
     * Update.
     *
     * @param id               the id
     * @param updatedRatingDTO the updated rating dto
     */
    @Transactional
    public void update(Integer id, RatingDTO updatedRatingDTO) {
        Rating existingRating = ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid rating id: " + id));

        ratingMapper.updateRatingFromDto(updatedRatingDTO, existingRating);
        ratingRepository.save(existingRating);
    }

    /**
     * Delete.
     *
     * @param id the id
     */
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(Integer id) {
        Rating ratingToDelete = ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid rating id: " + id));

        ratingRepository.delete(ratingToDelete);
    }
}
