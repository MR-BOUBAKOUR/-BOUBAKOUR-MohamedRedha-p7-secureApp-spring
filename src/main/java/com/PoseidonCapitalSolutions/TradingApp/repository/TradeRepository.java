package com.PoseidonCapitalSolutions.TradingApp.repository;

import com.PoseidonCapitalSolutions.TradingApp.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
