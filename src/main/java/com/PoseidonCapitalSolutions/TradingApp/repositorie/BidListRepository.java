package com.PoseidonCapitalSolutions.TradingApp.repositorie;

import com.PoseidonCapitalSolutions.TradingApp.domain.BidList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidListRepository extends JpaRepository<BidList, Integer> {

}
