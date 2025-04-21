package com.PoseidonCapitalSolutions.TradingApp.integration;

import com.PoseidonCapitalSolutions.TradingApp.domain.Rating;
import com.PoseidonCapitalSolutions.TradingApp.repository.RatingRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class RatingIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RatingRepository ratingRepository;

    @BeforeEach
    void setUp() {
        ratingRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        ratingRepository.deleteAll();
    }

    private Rating createAndSaveRating() {
        Rating rating = new Rating();
        rating.setMoodysRating("Test Moody's");
        rating.setSandPRating("Test S&P");
        rating.setFitchRating("Test Fitch");
        rating.setOrderNumber((byte) 1);
        return ratingRepository.save(rating);
    }

    @Test
    @WithMockUser
    void givenNoRatings_whenGetRatingList_thenReturnEmptyList() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ratings"))
                .andExpect(view().name("rating/list"));

        assertEquals(0, ratingRepository.count());
    }

    @Test
    @WithMockUser
    void givenValidRating_whenPostValidate_thenCreateAndRedirectToList() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .param("moodysRating", "Test Moody's")
                        .param("sandPRating", "Test S&P")
                        .param("fitchRating", "Test Fitch")
                        .param("orderNumber", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        assertEquals(1, ratingRepository.count());
    }

    @Test
    @WithMockUser
    void givenInvalidRating_whenPostValidate_thenShowFormWithErrors() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .param("moodysRating", "")
                        .param("sandPRating", "")
                        .param("fitchRating", "")
                        .param("orderNumber", "-1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("rating/add"));

        assertEquals(0, ratingRepository.count());
    }

    @Test
    @WithMockUser
    void givenValidRating_whenPostUpdate_thenUpdateAndRedirectToList() throws Exception {
        Rating rating = createAndSaveRating();

        mockMvc.perform(post("/rating/update/{id}", rating.getId())
                        .param("moodysRating", "Updated Moody's")
                        .param("sandPRating", "Updated S&P")
                        .param("fitchRating", "Updated Fitch")
                        .param("orderNumber", "2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        Rating updatedRating = ratingRepository.findById(rating.getId()).orElseThrow();
        assertEquals("Updated Moody's", updatedRating.getMoodysRating());
        assertEquals("Updated S&P", updatedRating.getSandPRating());
        assertEquals("Updated Fitch", updatedRating.getFitchRating());
    }

    @Test
    @WithMockUser
    void givenInvalidRating_whenPostUpdate_thenReturnFormWithErrors() throws Exception {
        Rating rating = createAndSaveRating();

        mockMvc.perform(post("/rating/update/{id}", rating.getId())
                        .param("moodysRating", "")
                        .param("sandPRating", "")
                        .param("fitchRating", "")
                        .param("orderNumber", "-1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("rating/update"));

        Rating unchangedRating = ratingRepository.findById(rating.getId()).orElseThrow();
        assertEquals("Test Moody's", unchangedRating.getMoodysRating());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenAdminUser_whenDeleteRating_thenDeleteAndRedirectToList() throws Exception {
        Rating rating = createAndSaveRating();

        mockMvc.perform(get("/rating/delete/{id}", rating.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        assertFalse(ratingRepository.existsById(rating.getId()));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void givenUserWithoutAdminRole_whenDeleteRating_thenReturnForbidden() throws Exception {
        Rating rating = createAndSaveRating();

        mockMvc.perform(get("/rating/delete/{id}", rating.getId())
                        .with(csrf()))
                .andExpect(status().isForbidden());

        assertTrue(ratingRepository.existsById(rating.getId()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenNonExistentRating_whenDelete_thenReturnNotFound() throws Exception {
        mockMvc.perform(get("/rating/delete/{id}", 9999)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
