package com.PoseidonCapitalSolutions.TradingApp.repositorie;

import com.PoseidonCapitalSolutions.TradingApp.domain.CurvePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurvePointRepository extends JpaRepository<CurvePoint, Integer> {

}
