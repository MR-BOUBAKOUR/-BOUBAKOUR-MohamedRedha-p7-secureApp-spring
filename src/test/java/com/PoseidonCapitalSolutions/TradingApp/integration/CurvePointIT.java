package com.PoseidonCapitalSolutions.TradingApp.integration;

import com.PoseidonCapitalSolutions.TradingApp.domain.CurvePoint;
import com.PoseidonCapitalSolutions.TradingApp.repository.CurvePointRepository;
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
public class CurvePointIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CurvePointRepository curvePointRepository;

    @BeforeEach
    void setUp() {
        curvePointRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        curvePointRepository.deleteAll();
    }

    // Helper method used to create and save a curve point in the TestContainer
    private CurvePoint createAndSaveCurvePoint() {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setTerm(1.0);
        curvePoint.setValue(10.0);
        curvePoint.setCreationDate(LocalDateTime.now());
        return curvePointRepository.save(curvePoint);
    }

    @Test
    @WithMockUser
    void givenNoCurvePoints_whenGetCurvePointList_thenReturnEmptyList() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(view().name("curvePoint/list"));

        assertEquals(0, curvePointRepository.count());
    }

    @Test
    @WithMockUser
    void givenValidCurvePoint_whenPostValidate_thenCreateAndRedirectToList() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .param("term", "2.5")
                        .param("value", "30.0")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        assertEquals(1, curvePointRepository.count());
        CurvePoint createdCurvePoint = curvePointRepository.findAll().getFirst();
        assertEquals(2.5, createdCurvePoint.getTerm());
        assertEquals(30.0, createdCurvePoint.getValue());
    }

    @Test
    @WithMockUser
    void givenInvalidCurvePoint_whenPostValidate_thenShowFormWithErrors() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .param("term", "-1.0")
                        .param("value", "invalid")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("curvePoint/add"));

        assertEquals(0, curvePointRepository.count());
    }

    @Test
    @WithMockUser
    void givenValidCurvePoint_whenPostUpdate_thenUpdateAndRedirectToList() throws Exception {
        CurvePoint curvePoint = createAndSaveCurvePoint();

        mockMvc.perform(post("/curvePoint/update/{id}", curvePoint.getId())
                        .param("term", "2.0")
                        .param("value", "20.0")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        CurvePoint updatedCurvePoint = curvePointRepository.findAll().getFirst();
        assertEquals(2.0, updatedCurvePoint.getTerm());
        assertEquals(20.0, updatedCurvePoint.getValue());
    }

    @Test
    @WithMockUser
    void givenInvalidCurvePoint_whenPostUpdate_thenReturnFormWithErrors() throws Exception {
        CurvePoint curvePoint = createAndSaveCurvePoint();

        mockMvc.perform(post("/curvePoint/update/{id}", curvePoint.getId())
                        .param("term", "-2.0")
                        .param("value", "invalid")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("curvePoint/update"));

        CurvePoint unchangedCurvePoint = curvePointRepository.findAll().getFirst();
        assertEquals(1.0, unchangedCurvePoint.getTerm());
        assertEquals(10.0, unchangedCurvePoint.getValue());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenAdminUser_whenDeleteCurvePoint_thenDeleteAndRedirectToList() throws Exception {
        CurvePoint curvePoint = createAndSaveCurvePoint();

        mockMvc.perform(get("/curvePoint/delete/{id}", curvePoint.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        assertFalse(curvePointRepository.existsById(curvePoint.getId()));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void givenUserWithoutAdminRole_whenDeleteCurvePoint_thenReturnForbidden() throws Exception {
        CurvePoint curvePoint = createAndSaveCurvePoint();

        mockMvc.perform(get("/curvePoint/delete/{id}", curvePoint.getId())
                        .with(csrf()))
                .andExpect(status().isForbidden());

        assertTrue(curvePointRepository.existsById(curvePoint.getId()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenNonExistentCurvePoint_whenDeleteCurvePoint_thenReturnNotFound() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/{id}", 9999)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}