package com.PoseidonCapitalSolutions.TradingApp;

import com.PoseidonCapitalSolutions.TradingApp.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class TradingAppApplicationTests {

	@MockitoBean
	private BidListRepository bidListRepository;
	@MockitoBean
	private CurvePointRepository curvePointRepository;
	@MockitoBean
	private RatingRepository ratingRepository;
	@MockitoBean
	private RuleNameRepository ruleNameRepository;
	@MockitoBean
	private TradeRepository tradeRepository;
	@MockitoBean
	private UserRepository userRepository;

	@Test
	void contextLoads() {
	}

}
