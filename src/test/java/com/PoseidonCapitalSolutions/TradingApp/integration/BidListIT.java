package com.PoseidonCapitalSolutions.TradingApp.integration;

import com.PoseidonCapitalSolutions.TradingApp.dto.BidListDTO;
import com.PoseidonCapitalSolutions.TradingApp.repository.BidListRepository;
import com.PoseidonCapitalSolutions.TradingApp.service.BidListService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BidListIT {

    @Autowired
    private BidListService bidListService;

    @Autowired
    private BidListRepository bidListRepository;

    @BeforeEach
    void setUp() {
        bidListRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        bidListRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyListWhenNoBidsExist() {
        List<BidListDTO> result = bidListService.findAll();

        assertThat(result).isEmpty();
    }

}