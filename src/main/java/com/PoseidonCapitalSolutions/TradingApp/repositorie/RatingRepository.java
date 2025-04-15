package com.PoseidonCapitalSolutions.TradingApp.repositorie;

import com.PoseidonCapitalSolutions.TradingApp.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {

}
