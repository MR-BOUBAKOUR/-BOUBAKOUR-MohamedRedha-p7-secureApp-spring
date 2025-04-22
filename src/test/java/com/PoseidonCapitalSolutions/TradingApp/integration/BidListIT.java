package com.PoseidonCapitalSolutions.TradingApp.integration;

import com.PoseidonCapitalSolutions.TradingApp.domain.BidList;
import com.PoseidonCapitalSolutions.TradingApp.repository.BidListRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

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
public class BidListIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

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

    // Helper method used to create and save a bid in the TestContainer
    private BidList createAndSaveBid() {
        BidList bidList = new BidList();
        bidList.setAccount("account");
        bidList.setType("type");
        bidList.setBidQuantity(100.0);
        bidList.setCreationDate(LocalDateTime.now());
        return bidListRepository.save(bidList);
    }

    @Test
    @WithMockUser
    void givenNoBids_whenGetBidList_thenReturnEmptyList() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidLists"))
                .andExpect(view().name("bidList/list"));

        assertEquals(0, bidListRepository.count());
    }

    @Test
    @WithMockUser
    void givenValidBid_whenPostValidate_thenCreateAndRedirectToList() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .param("account", "Test Account")
                        .param("type", "Test Type")
                        .param("bidQuantity", "100.0")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        assertEquals(1, bidListRepository.count());
        BidList createdBid = bidListRepository.findAll().getFirst();
        assertEquals("Test Account", createdBid.getAccount());
        assertEquals("Test Type", createdBid.getType());
        assertEquals(100.0, createdBid.getBidQuantity());
    }

    @Test
    @WithMockUser
    void givenInvalidBid_whenPostValidate_thenShowFormWithErrors() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .param("account", "")
                        .param("type", "")
                        .param("bidQuantity", "-10.0")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("bidList/add"));

        assertEquals(0, bidListRepository.count());
    }

    @Test
    @WithMockUser
    void givenValidBid_whenPostUpdate_thenUpdateAndRedirectToList() throws Exception {
        BidList bid = createAndSaveBid();

        mockMvc.perform(post("/bidList/update/{id}", bid.getId())
                        .param("account", "Updated Account")
                        .param("type", "Updated Type")
                        .param("bidQuantity", "200.0")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        BidList updatedBid = bidListRepository.findAll().getFirst();
        assertEquals("Updated Account", updatedBid.getAccount());
        assertEquals("Updated Type", updatedBid.getType());
        assertEquals(200.0, updatedBid.getBidQuantity());
    }

    @Test
    @WithMockUser
    void givenInvalidBid_whenPostUpdate_thenReturnFormWithErrors() throws Exception {
        BidList bid = createAndSaveBid();

        mockMvc.perform(post("/bidList/update/{id}", bid.getId())
                        .param("account", "")
                        .param("type", "Updated Type")
                        .param("bidQuantity", "-50.0")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("bidList/update"));

        BidList unchangedBid = bidListRepository.findAll().getFirst();
        assertEquals("account", unchangedBid.getAccount());
        assertEquals(100.0, unchangedBid.getBidQuantity());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenAdminUser_whenDeleteBid_thenDeleteAndRedirectToList() throws Exception {
        BidList bid = createAndSaveBid();

        mockMvc.perform(get("/bidList/delete/{id}", bid.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        assertFalse(bidListRepository.existsById(bid.getId()));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void givenUserWithoutAdminRole_whenDeleteBid_thenReturnForbidden() throws Exception {
        BidList bid = createAndSaveBid();

        mockMvc.perform(get("/bidList/delete/{id}", bid.getId())
                        .with(csrf()))
                .andExpect(status().isForbidden());

        assertTrue(bidListRepository.existsById(bid.getId()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenNonExistentBidId_whenDeleteBid_thenReturnNotFound() throws Exception {
        mockMvc.perform(get("/bidList/delete/{id}", 9999)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

}