package com.PoseidonCapitalSolutions.TradingApp.mapper;

import com.PoseidonCapitalSolutions.TradingApp.domain.Rating;
import com.PoseidonCapitalSolutions.TradingApp.dto.RatingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    RatingDTO toRatingDTO(Rating rating);
    Rating toRating(RatingDTO ratingDTO);

    @Mapping(target = "id", ignore = true)
    void updateRatingFromDto(RatingDTO dto, @MappingTarget Rating entity);
}
