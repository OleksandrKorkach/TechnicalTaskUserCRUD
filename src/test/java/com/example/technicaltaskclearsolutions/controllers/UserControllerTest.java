package com.example.technicaltaskclearsolutions.controllers;

import com.example.technicaltaskclearsolutions.dto.UserRegisterDto;
import com.example.technicaltaskclearsolutions.models.User;
import com.example.technicaltaskclearsolutions.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void testCreateUserStatusIsCreated() throws Exception {
        var userRegisterDto = new UserRegisterDto(
                "Oleksandr14@gmail.com",
                "Oleksandr",
                "Moroz",
                LocalDate.ofYearDay(2005, 15),
                "Kyiv, Glushkove 4G",
                "+380668442568"
        );
        String userRegisterDtoJson = objectMapper.writeValueAsString(userRegisterDto);

        when(userService.createUser(any(UserRegisterDto.class))).thenReturn(any(User.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRegisterDtoJson))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testCreateEventStatusBadRequest(String validatedField) throws Exception {
        var userRegisterDto = new UserRegisterDto(
                validatedField,
                validatedField,
                validatedField,
                LocalDate.ofYearDay(2005, 15),
                "Kyiv, Glushkove 4G",
                "+380668442568"
        );
        String userRegisterDtoJson = objectMapper.writeValueAsString(userRegisterDto);
        var user = UserRegisterDto.toUser(userRegisterDto);

        when(userService.createUser(userRegisterDto)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRegisterDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateEvent() throws Exception {
        Long id = 12L;
        var userRegisterDto = new UserRegisterDto(
                "Oleksandr14@gmail.com",
                "Oleksandr",
                "Moroz",
                LocalDate.ofYearDay(2005, 15),
                "Kyiv, Glushkove 4G",
                "+380668442568"
        );
        var user = UserRegisterDto.toUser(userRegisterDto);

        String userRegisterDtoJson = objectMapper.writeValueAsString(userRegisterDto);

        when(userService.updateUser(id, userRegisterDto)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/{id}", id)
                        .content(userRegisterDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testUpdateEventStatusBadRequest(String validatedField) throws Exception {
        Long id = 12L;

        var userRegisterDto = new UserRegisterDto(
                validatedField,
                validatedField,
                validatedField,
                LocalDate.ofYearDay(2005, 15),
                "Kyiv, Glushkove 4G",
                "+380668442568"
        );
        String userRegisterDtoJson = objectMapper.writeValueAsString(userRegisterDto);
        var user = UserRegisterDto.toUser(userRegisterDto);

        when(userService.updateUser(id, userRegisterDto)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRegisterDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPartialUpdateEvent() throws Exception {
        Long id = 12L;
        var userRegisterDto = new UserRegisterDto(
                null,
                null,
                null,
                LocalDate.ofYearDay(2005, 15),
                "Kyiv, Glushkove 4G",
                "+380668442568"
        );
        var user = UserRegisterDto.toUser(userRegisterDto);

        String userRegisterDtoJson = objectMapper.writeValueAsString(userRegisterDto);

        when(userService.updateUser(id, userRegisterDto)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/{id}", id)
                        .content(userRegisterDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUser() throws Exception {
        Long userId = 12L;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(eq(userId));
    }

}
