package com.PoseidonCapitalSolutions.TradingApp.integration;

import com.PoseidonCapitalSolutions.TradingApp.domain.Trade;
import com.PoseidonCapitalSolutions.TradingApp.repository.TradeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class TradeIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TradeRepository tradeRepository;

    @BeforeEach
    void setUp() {
        tradeRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        tradeRepository.deleteAll();
    }

    // Helper method used to create and save a trade in the TestContainer
    private Trade createAndSaveTrade() {
        Trade trade = new Trade();
        trade.setAccount("Test Account");
        trade.setType("Buy");
        trade.setBuyQuantity(100.0);
        return tradeRepository.save(trade);
    }

    @Test
    @WithMockUser
    void givenNoTrades_whenGetTradeList_thenReturnEmptyList() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trades"))
                .andExpect(view().name("trade/list"));

        assertEquals(0, tradeRepository.count());
    }

    @Test
    @WithMockUser
    void givenValidTrade_whenPostValidate_thenCreateAndRedirectToList() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .param("account", "Test Account")
                        .param("type", "Buy")
                        .param("buyQuantity", "100.0")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        assertEquals(1, tradeRepository.count());
        Trade createdTrade = tradeRepository.findAll().getFirst();
        assertEquals("Test Account", createdTrade.getAccount());
        assertEquals("Buy", createdTrade.getType());
        assertEquals(100.0, createdTrade.getBuyQuantity());
    }

    @Test
    @WithMockUser
    void givenInvalidTrade_whenPostValidate_thenShowFormWithErrors() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .param("account", "")
                        .param("type", "")
                        .param("buyQuantity", "-10.0")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("trade/add"));

        assertEquals(0, tradeRepository.count());
    }

    @Test
    @WithMockUser
    void givenValidTrade_whenPostUpdate_thenUpdateAndRedirectToList() throws Exception {
        Trade trade = createAndSaveTrade();

        mockMvc.perform(post("/trade/update/{id}", trade.getId())
                        .param("account", "Updated Account")
                        .param("type", "Sell")
                        .param("buyQuantity", "200.0")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        Trade updatedTrade = tradeRepository.findAll().getFirst();
        assertEquals("Updated Account", updatedTrade.getAccount());
        assertEquals("Sell", updatedTrade.getType());
        assertEquals(200.0, updatedTrade.getBuyQuantity());
    }

    @Test
    @WithMockUser
    void givenInvalidTrade_whenPostUpdate_thenReturnFormWithErrors() throws Exception {
        Trade trade = createAndSaveTrade();

        mockMvc.perform(post("/trade/update/{id}", trade.getId())
                        .param("account", "")
                        .param("type", "Updated Type")
                        .param("buyQuantity", "-50.0")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("trade/update"));

        Trade unchangedTrade = tradeRepository.findAll().getFirst();
        assertEquals("Test Account", unchangedTrade.getAccount());
        assertEquals(100.0, unchangedTrade.getBuyQuantity());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenAdminUser_whenDeleteTrade_thenDeleteAndRedirectToList() throws Exception {
        Trade trade = createAndSaveTrade();

        mockMvc.perform(get("/trade/delete/{id}", trade.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        assertFalse(tradeRepository.existsById(trade.getId()));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void givenUserWithoutAdminRole_whenDeleteTrade_thenReturnForbidden() throws Exception {
        Trade trade = createAndSaveTrade();

        mockMvc.perform(get("/trade/delete/{id}", trade.getId())
                        .with(csrf()))
                .andExpect(status().isForbidden());

        assertTrue(tradeRepository.existsById(trade.getId()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenNonExistentTradeId_whenDeleteTrade_thenReturnNotFound() throws Exception {
        mockMvc.perform(get("/trade/delete/{id}", 9999)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
