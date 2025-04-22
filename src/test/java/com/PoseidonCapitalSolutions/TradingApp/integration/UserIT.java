package com.PoseidonCapitalSolutions.TradingApp.integration;

import com.PoseidonCapitalSolutions.TradingApp.domain.User;
import com.PoseidonCapitalSolutions.TradingApp.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User admin;
    private User user;

    @BeforeEach
    void setUp() {
        userRepository.findAll()
                .forEach(user -> userRepository.deleteById(user.getId()));

        admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123"));
        admin.setFullname("Admin");
        admin.setRole("ADMIN");
        userRepository.save(admin);

        user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("123"));
        user.setFullname("User");
        user.setRole("USER");
        userRepository.save(user);
    }

    private User createAndSaveUser() {
        User user = new User();
        user.setUsername("user2");
        user.setPassword(passwordEncoder.encode("123"));
        user.setFullname("User2");
        user.setRole("USER");
        return userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.findAll()
                .forEach(user -> userRepository.deleteById(user.getId()));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void givenUser_whenGetUserList_thenReturnListView() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("user/list"));
    }

    @Test
    @WithMockUser
    void givenValidUser_whenPostValidate_thenCreateAndRedirectToList() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .param("username", "user test")
                        .param("password", "123")
                        .param("fullname", "User test")
                        .param("role", "USER")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        User createdUser = userRepository.findByUsername("user test").orElse(null);
        assertNotNull(createdUser);
        assertEquals("User test", createdUser.getFullname());
        assertEquals("USER", createdUser.getRole());
        assertTrue(passwordEncoder.matches("123", createdUser.getPassword()));
    }

    @Test
    @WithMockUser
    void givenInvalidUser_whenPostValidate_thenShowFormWithErrors() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .param("username", "")
                        .param("password", "weak")
                        .param("fullname", "")
                        .param("role", "INVALID_ROLE")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("user/add"));

        assertFalse(userRepository.findByUsername("").isPresent());
    }

    @Test
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void givenUser_whenAccessUpdateForm_thenReturnUpdateView() throws Exception {
        mockMvc.perform(get("/user/update/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("user/update"));
    }

    @Test
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void givenDifferentUser_whenAccessOtherUserUpdateForm_thenReturnForbidden() throws Exception {

        User user2 = createAndSaveUser();

        mockMvc.perform(get("/user/update/{id}", user2.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void givenUser_whenPostValidSelfUpdate_thenUpdateAndRedirectToList() throws Exception {
        mockMvc.perform(post("/user/update/{id}", user.getId())
                        .param("username", "user update")
                        .param("password", "123123")
                        .param("fullname", "User Updated")
                        .param("role", "USER")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        User updatedUser = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals("user update", updatedUser.getUsername());
        assertEquals("User Updated", updatedUser.getFullname());
        assertTrue(passwordEncoder.matches("123123", updatedUser.getPassword()));
    }

    @Test
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void givenDifferentUser_whenPostUpdateOtherUser_thenReturnForbidden() throws Exception {

        User user2 = createAndSaveUser();

        mockMvc.perform(post("/user/update/{id}", user2.getId())
                        .param("username", "user update")
                        .param("password", "123123")
                        .param("fullname", "User Updated")
                        .param("role", "USER")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isForbidden());

        User unchangedUser = userRepository.findById(user2.getId()).orElse(null);
        assertNotNull(unchangedUser);
        assertEquals("user2", unchangedUser.getUsername());
        assertEquals("User2", unchangedUser.getFullname());
        assertEquals("USER", unchangedUser.getRole());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenInvalidUser_whenPostUpdate_thenReturnFormWithErrors() throws Exception {

        mockMvc.perform(post("/user/update/{id}", user.getId())
                        .param("username", "")
                        .param("password", "weak")
                        .param("fullname", "")
                        .param("role", "INVALID_ROLE")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("user/update"));

        User unchangedUser = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(unchangedUser);
        assertEquals("user", unchangedUser.getUsername());
        assertEquals("User", unchangedUser.getFullname());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenAdminUser_whenDeleteUser_thenDeleteAndRedirectToList() throws Exception {

        mockMvc.perform(get("/user/delete/{id}", user.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        assertFalse(userRepository.existsById(user.getId()));
    }

    @Test
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void givenSameUser_whenSelfDelete_thenDeleteAndRedirectToList() throws Exception {

        mockMvc.perform(get("/user/delete/{id}", user.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        assertFalse(userRepository.existsById(user.getId()));
    }

    @Test
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void givenDifferentUser_whenDeleteOtherUser_thenReturnForbidden() throws Exception {

        User user2 = createAndSaveUser();

        mockMvc.perform(get("/user/delete/{id}", user2.getId())
                        .with(csrf()))
                .andExpect(status().isForbidden());

        assertTrue(userRepository.existsById(user2.getId()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenLastAdmin_whenDeleteLastAdmin_thenPreventDeleteAndShowError() throws Exception {
        // Delete all admins except one
        long initialAdminCount = userRepository.countByRole("ADMIN");
        if (initialAdminCount > 1) {
            userRepository.findAll().stream()
                    .filter(user -> "ADMIN".equals(user.getRole()))
                    .skip(1)
                    .forEach(user -> userRepository.deleteById(user.getId()));
        }

        // Get the last remaining admin
        User lastAdmin = userRepository.findAll().stream()
                .filter(user -> "ADMIN".equals(user.getRole()))
                .findFirst()
                .orElseThrow();

        mockMvc.perform(get("/user/delete/{id}", lastAdmin.getId())
                        .with(csrf()))
                .andExpect(status().is4xxClientError());

        assertTrue(userRepository.existsById(lastAdmin.getId()));
        assertEquals(1, userRepository.countByRole("ADMIN"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenNonExistentUserId_whenUpdateUser_thenReturnNotFound() throws Exception {
        mockMvc.perform(get("/user/update/{id}", 9999)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void givenNonExistentUserId_whenDeleteUser_thenReturnNotFound() throws Exception {
        mockMvc.perform(get("/user/delete/{id}", 9999)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}