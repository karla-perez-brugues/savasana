package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void findById_ok() throws Exception {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setEmail("karla@mail.com");
        user.setLastName("Pérez");
        user.setFirstName("Karla");
        user.setPassword("password");
        user.setAdmin(false);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void findById_notFound() throws Exception {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void findById_badRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", "wrongId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "karla@mail.com", password = "test!1234", roles = {"ADMIN"})
    public void delete_ok() throws Exception {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setEmail("karla@mail.com");
        user.setLastName("Pérez");
        user.setFirstName("Karla");
        user.setPassword("test!1234");
        user.setAdmin(true);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void delete_notFound() throws Exception {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void delete_unauthorized() throws Exception {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setEmail("karla@mail.com");
        user.setLastName("Pérez");
        user.setFirstName("Karla");
        user.setPassword("test!1234");
        user.setAdmin(true);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void delete_badRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", "wrongId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
