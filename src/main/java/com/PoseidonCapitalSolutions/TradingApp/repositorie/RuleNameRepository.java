package com.PoseidonCapitalSolutions.TradingApp.repositorie;

import com.PoseidonCapitalSolutions.TradingApp.domain.RuleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleNameRepository extends JpaRepository<RuleName, Integer> {
}
