package com.PoseidonCapitalSolutions.TradingApp.unit.controller;

import com.PoseidonCapitalSolutions.TradingApp.controller.BidListController;
import com.PoseidonCapitalSolutions.TradingApp.dto.BidListDTO;
import com.PoseidonCapitalSolutions.TradingApp.service.BidListService;
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

@WebMvcTest(controllers = BidListController.class)
@WithMockUser
class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BidListService bidListService;

    @Test
    void testHome_Show() throws Exception {
        List<BidListDTO> bidLists = List.of();
        when(bidListService.findAll()).thenReturn(bidLists);

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attribute("bidLists", bidLists));

        verify(bidListService).findAll();
    }

    @Test
    void testValidateBid_Success() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .with(csrf())
                        .param("account", "Account1")
                        .param("type", "Type1")
                        .param("bidQuantity", "10.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService).create(any(BidListDTO.class));
    }

    @Test
    void testValidateBid_Error() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .with(csrf())
                        .param("account", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));

        verify(bidListService, never()).create(any(BidListDTO.class));
    }

    @Test
    void testUpdateBid_Success() throws Exception {
        Integer id = 1;

        mockMvc.perform(post("/bidList/update/{id}", id)
                        .with(csrf())
                        .param("account", "UpdatedAccount")
                        .param("type", "UpdatedType")
                        .param("bidQuantity", "20.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService).update(eq(id), any(BidListDTO.class));
    }

    @Test
    void testUpdateBid_Error() throws Exception {
        Integer id = 1;

        mockMvc.perform(post("/bidList/update/{id}", id)
                        .with(csrf())
                        .param("account", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"));

        verify(bidListService, never()).update(eq(id), any(BidListDTO.class));
    }

    @Test
    void testDeleteBid_Success() throws Exception {
        Integer id = 1;

        mockMvc.perform(get("/bidList/delete/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService).delete(id);
    }
}