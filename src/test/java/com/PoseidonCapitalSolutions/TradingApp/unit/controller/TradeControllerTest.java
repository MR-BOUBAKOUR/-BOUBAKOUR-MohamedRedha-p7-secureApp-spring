package com.PoseidonCapitalSolutions.TradingApp.unit.controller;

import com.PoseidonCapitalSolutions.TradingApp.controller.TradeController;
import com.PoseidonCapitalSolutions.TradingApp.dto.TradeDTO;
import com.PoseidonCapitalSolutions.TradingApp.service.TradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TradeController.class)
@WithMockUser
class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TradeService tradeService;

    @Test
    void testHome_Show() throws Exception {
        List<TradeDTO> trades = List.of();
        when(tradeService.findAll()).thenReturn(trades);

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attribute("trades", trades));

        verify(tradeService).findAll();
    }

    @Test
    void testValidateTrade_Success() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("account", "Account1")
                        .param("type", "Type1")
                        .param("buyQuantity", "100.1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService).create(any(TradeDTO.class));
    }

    @Test
    void testValidateTrade_Error() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("account", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));

        verify(tradeService, never()).create(any(TradeDTO.class));
    }

    @Test
    void testUpdateTrade_Success() throws Exception {
        Integer id = 1;

        mockMvc.perform(post("/trade/update/{id}", id)
                        .with(csrf())
                        .param("account", "UpdatedAccount")
                        .param("type", "UpdatedType")
                        .param("buyQuantity", "100.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService).update(eq(id), any(TradeDTO.class));
    }

    @Test
    void testUpdateTrade_Error() throws Exception {
        Integer id = 1;

        mockMvc.perform(post("/trade/update/{id}", id)
                        .with(csrf())
                        .param("account", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"));

        verify(tradeService, never()).update(eq(id), any(TradeDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteTrade_Success() throws Exception {
        Integer id = 1;

        mockMvc.perform(get("/trade/delete/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService).delete(id);
    }
}