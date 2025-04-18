package com.PoseidonCapitalSolutions.TradingApp.unit.controller;

import com.PoseidonCapitalSolutions.TradingApp.controller.CurvePointController;
import com.PoseidonCapitalSolutions.TradingApp.dto.CurvePointDTO;
import com.PoseidonCapitalSolutions.TradingApp.service.CurvePointService;
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

@WebMvcTest(controllers = CurvePointController.class)
@WithMockUser
class CurvePointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurvePointService curvePointService;

    @Test
    void testHome_Show() throws Exception {
        List<CurvePointDTO> curvePoints = List.of();
        when(curvePointService.findAll()).thenReturn(curvePoints);

        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attribute("curvePoints", curvePoints));

        verify(curvePointService).findAll();
    }

    @Test
    void testAddCurveForm_Show() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeExists("curvePoint"));
    }

    @Test
    void testValidateCurve_Success() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .with(csrf())
                        .param("term", "10.0")
                        .param("value", "20.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService).create(any(CurvePointDTO.class));
    }

    @Test
    void testValidateCurve_Error() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .with(csrf())
                        .param("term", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));

        verify(curvePointService, never()).create(any(CurvePointDTO.class));
    }

    @Test
    void testUpdateCurveForm_Show() throws Exception {
        Integer id = 1;
        CurvePointDTO curvePointDTO = new CurvePointDTO();
        curvePointDTO.setId(id);
        curvePointDTO.setTerm(10.0);
        curvePointDTO.setValue(20.0);

        when(curvePointService.findById(id)).thenReturn(curvePointDTO);

        mockMvc.perform(get("/curvePoint/update/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attribute("curvePoint", curvePointDTO));

        verify(curvePointService).findById(id);
    }

    @Test
    void testUpdateCurve_Success() throws Exception {
        Integer id = 1;

        mockMvc.perform(post("/curvePoint/update/{id}", id)
                        .with(csrf())
                        .param("term", "15.0")
                        .param("value", "25.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService).update(eq(id), any(CurvePointDTO.class));
    }

    @Test
    void testUpdateCurve_Error() throws Exception {
        Integer id = 1;

        mockMvc.perform(post("/curvePoint/update/{id}", id)
                        .with(csrf())
                        .param("term", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"));

        verify(curvePointService, never()).update(eq(id), any(CurvePointDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteCurve_Success() throws Exception {
        Integer id = 1;

        mockMvc.perform(get("/curvePoint/delete/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService).delete(id);
    }
}