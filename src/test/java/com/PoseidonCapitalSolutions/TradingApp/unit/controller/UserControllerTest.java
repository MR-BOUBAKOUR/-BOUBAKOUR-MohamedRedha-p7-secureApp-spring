package com.PoseidonCapitalSolutions.TradingApp.unit.controller;

import com.PoseidonCapitalSolutions.TradingApp.controller.UserController;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserCreateDTO;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserResponseDTO;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserUpdateDTO;
import com.PoseidonCapitalSolutions.TradingApp.service.UserService;
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

@WebMvcTest(controllers = UserController.class)
@WithMockUser
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void testListUsers_Show() throws Exception {
        List<UserResponseDTO> users = List.of();
        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attribute("users", users));

        verify(userService).findAll();
    }

    @Test
    void testValidateUser_Success() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", "User")
                        .param("fullname", "User")
                        .param("role", "USER")
                        .param("password", "Password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService).create(any(UserCreateDTO.class));
    }

    @Test
    void testValidateUser_Error() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));

        verify(userService, never()).create(any(UserCreateDTO.class));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        Integer id = 1;

        mockMvc.perform(post("/user/update/{id}", id)
                        .with(csrf())
                        .param("username", "updatedUser")
                        .param("fullname", "updatedUser")
                        .param("role", "ADMIN")
                        .param("password", "newPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService).update(eq(id), any(UserUpdateDTO.class), any(), any());
    }

    @Test
    void testUpdateUser_Error() throws Exception {
        Integer id = 1;

        mockMvc.perform(post("/user/update/{id}", id)
                        .with(csrf())
                        .param("username", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"));

        verify(userService, never()).update(eq(id), any(UserUpdateDTO.class), any(), any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUser() throws Exception {
        Integer id = 1;

        mockMvc.perform(get("/user/delete/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService).delete(eq(id), any(), any());
    }
}