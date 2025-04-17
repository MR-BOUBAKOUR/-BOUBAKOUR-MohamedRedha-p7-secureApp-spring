package com.PoseidonCapitalSolutions.TradingApp.unit.controller;

import com.PoseidonCapitalSolutions.TradingApp.controller.RuleNameController;
import com.PoseidonCapitalSolutions.TradingApp.dto.RuleNameDTO;
import com.PoseidonCapitalSolutions.TradingApp.service.RuleNameService;
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

@WebMvcTest(controllers = RuleNameController.class)
@WithMockUser
class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RuleNameService ruleNameService;

    @Test
    void testHome_Show() throws Exception {
        List<RuleNameDTO> ruleNames = List.of();
        when(ruleNameService.findAll()).thenReturn(ruleNames);

        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attribute("ruleNames", ruleNames));

        verify(ruleNameService).findAll();
    }

    @Test
    void testAddRuleForm_Show() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().attributeExists("ruleName"));
    }

    @Test
    void testValidateRule_Success() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())
                        .param("name", "Rule")
                        .param("description", "Description")
                        .param("json", "Value")
                        .param("template", "Template")
                        .param("sqlStr", "SqlStr")
                        .param("sqlPart", "SqlPart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService).create(any(RuleNameDTO.class));
    }

    @Test
    void testValidateRule_Error() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));

        verify(ruleNameService, never()).create(any(RuleNameDTO.class));
    }

    @Test
    void testUpdateRuleForm_Show() throws Exception {
        Integer id = 1;
        RuleNameDTO ruleNameDTO = new RuleNameDTO();
        ruleNameDTO.setId(id);
        ruleNameDTO.setName("Rule");
        ruleNameDTO.setDescription("Description");
        ruleNameDTO.setJson("Value");
        ruleNameDTO.setTemplate("Template");
        ruleNameDTO.setSqlStr("SqlStr");
        ruleNameDTO.setSqlPart("SqlPart");

        when(ruleNameService.findById(id)).thenReturn(ruleNameDTO);

        mockMvc.perform(get("/ruleName/update/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attribute("ruleName", ruleNameDTO));

        verify(ruleNameService).findById(id);
    }

    @Test
    void testUpdateRule_Success() throws Exception {
        Integer id = 1;

        mockMvc.perform(post("/ruleName/update/{id}", id)
                        .with(csrf())
                        .param("name", "UpdatedRule")
                        .param("description", "UpdatedDescription")
                        .param("json", "updatedValue")
                        .param("template", "updatedTemplate")
                        .param("sqlStr", "updatedSqlStr")
                        .param("sqlPart", "updatedSqlStr"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService).update(eq(id), any(RuleNameDTO.class));
    }

    @Test
    void testUpdateRule_Error() throws Exception {
        Integer id = 1;

        mockMvc.perform(post("/ruleName/update/{id}", id)
                        .with(csrf())
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"));

        verify(ruleNameService, never()).update(eq(id), any(RuleNameDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteRule_Success() throws Exception {
        Integer id = 1;

        mockMvc.perform(get("/ruleName/delete/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService).delete(id);
    }
}