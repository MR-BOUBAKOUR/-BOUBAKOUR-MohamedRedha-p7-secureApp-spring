package com.PoseidonCapitalSolutions.TradingApp.unit.controller;

import com.PoseidonCapitalSolutions.TradingApp.controller.RatingController;
import com.PoseidonCapitalSolutions.TradingApp.dto.RatingDTO;
import com.PoseidonCapitalSolutions.TradingApp.service.RatingService;
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

@WebMvcTest(controllers = RatingController.class)
@WithMockUser
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RatingService ratingService;

    @Test
    void testHome_Show() throws Exception {
        List<RatingDTO> ratings = List.of();
        when(ratingService.findAll()).thenReturn(ratings);

        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attribute("ratings", ratings));

        verify(ratingService).findAll();
    }

    @Test
    void testAddRatingForm_Show() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeExists("rating"));
    }

    @Test
    void testValidateRating_Success() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("moodysRating", "AAA")
                        .param("sandPRating", "AAA")
                        .param("fitchRating", "AAA")
                        .param("orderNumber", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService).create(any(RatingDTO.class));
    }

    @Test
    void testValidateRating_Error() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("moodysRating", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));

        verify(ratingService, never()).create(any(RatingDTO.class));
    }

    @Test
    void testUpdateRatingForm_Show() throws Exception {
        Integer id = 1;
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setId(id);
        ratingDTO.setMoodysRating("AAA");
        ratingDTO.setSandPRating("AAA");
        ratingDTO.setFitchRating("AAA");
        ratingDTO.setOrderNumber((byte) 1);

        when(ratingService.findById(id)).thenReturn(ratingDTO);

        mockMvc.perform(get("/rating/update/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attribute("rating", ratingDTO));

        verify(ratingService).findById(id);
    }

    @Test
    void testUpdateRating_Success() throws Exception {
        Integer id = 1;

        mockMvc.perform(post("/rating/update/{id}", id)
                        .with(csrf())
                        .param("moodysRating", "AA")
                        .param("sandPRating", "AA")
                        .param("fitchRating", "AA")
                        .param("orderNumber", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService).update(eq(id), any(RatingDTO.class));
    }

    @Test
    void testUpdateRating_Error() throws Exception {
        Integer id = 1;

        mockMvc.perform(post("/rating/update/{id}", id)
                        .with(csrf())
                        .param("moodysRating", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"));

        verify(ratingService, never()).update(eq(id), any(RatingDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteRating_Success() throws Exception {
        Integer id = 1;

        mockMvc.perform(get("/rating/delete/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(ratingService).delete(id);
    }
}