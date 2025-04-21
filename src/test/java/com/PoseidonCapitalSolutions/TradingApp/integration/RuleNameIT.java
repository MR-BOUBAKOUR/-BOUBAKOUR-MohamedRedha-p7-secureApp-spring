package com.PoseidonCapitalSolutions.TradingApp.integration;

import com.PoseidonCapitalSolutions.TradingApp.domain.RuleName;
import com.PoseidonCapitalSolutions.TradingApp.repository.RuleNameRepository;
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
public class RuleNameIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RuleNameRepository ruleNameRepository;

    @BeforeEach
    void setUp() {
        ruleNameRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        ruleNameRepository.deleteAll();
    }

    private RuleName createAndSaveRuleName() {
        RuleName ruleName = new RuleName();
        ruleName.setName("name test");
        ruleName.setDescription("description test");
        ruleName.setJson("json test");
        ruleName.setTemplate("template test");
        ruleName.setSqlStr("sqlstr test");
        ruleName.setSqlPart("sqlpart test");
        return ruleNameRepository.save(ruleName);
    }

    @Test
    @WithMockUser
    void shouldGetRuleNameList() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(view().name("ruleName/list"));

        assertEquals(0, ruleNameRepository.count());
    }

    @Test
    @WithMockUser
    void shouldAddValidRuleName() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .param("name", "name test")
                        .param("description", "description test")
                        .param("json", "json test")
                        .param("template", "template test")
                        .param("sqlStr", "sqlstr test")
                        .param("sqlPart", "sqlpart test")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        assertEquals(1, ruleNameRepository.count());
    }

    @Test
    @WithMockUser
    void shouldRejectInvalidRuleName() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .param("name", "")
                        .param("description", "")
                        .param("json", "")
                        .param("template", "")
                        .param("sqlStr", "")
                        .param("sqlPart", "")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("ruleName/add"));

        assertEquals(0, ruleNameRepository.count());
    }

    @Test
    @WithMockUser
    void shouldUpdateValidRuleName() throws Exception {
        RuleName ruleName = createAndSaveRuleName();

        mockMvc.perform(post("/ruleName/update/{id}", ruleName.getId())
                        .param("name", "name updated")
                        .param("description", "description updated")
                        .param("json", "json updated")
                        .param("template", "template updated")
                        .param("sqlStr", "sqlstr updated")
                        .param("sqlPart", "sqlpart updated")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        RuleName updated = ruleNameRepository.findById(ruleName.getId()).orElseThrow();
        assertEquals("name updated", updated.getName());
        assertEquals("description updated", updated.getDescription());
    }

    @Test
    @WithMockUser
    void shouldRejectInvalidUpdate() throws Exception {
        RuleName ruleName = createAndSaveRuleName();

        mockMvc.perform(post("/ruleName/update/{id}", ruleName.getId())
                        .param("name", "")
                        .param("description", "")
                        .param("json", "")
                        .param("template", "")
                        .param("sqlStr", "")
                        .param("sqlPart", "")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("ruleName/update"));

        RuleName unchanged = ruleNameRepository.findById(ruleName.getId()).orElseThrow();
        assertEquals("name test", unchanged.getName());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldDeleteRuleName() throws Exception {
        RuleName ruleName = createAndSaveRuleName();

        mockMvc.perform(get("/ruleName/delete/{id}", ruleName.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        assertFalse(ruleNameRepository.existsById(ruleName.getId()));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void shouldReturnForbiddenWhenDeletingWithoutAdminRole() throws Exception {
        RuleName ruleName = createAndSaveRuleName();

        mockMvc.perform(get("/ruleName/delete/{id}", ruleName.getId())
                        .with(csrf()))
                .andExpect(status().isForbidden());

        assertTrue(ruleNameRepository.existsById(ruleName.getId()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldReturnNotFoundWhenDeletingNonExistentRuleName() throws Exception {
        mockMvc.perform(get("/ruleName/delete/{id}", 9999)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}