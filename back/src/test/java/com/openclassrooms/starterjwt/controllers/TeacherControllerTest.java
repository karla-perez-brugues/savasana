package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TeacherRepository teacherRepository;

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void findById_ok() throws Exception {
        Long id = 1L;
        String lastName = "Ramdev";
        String firstName = "Baba";

        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setLastName(lastName);
        teacher.setFirstName(firstName);

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(lastName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(firstName));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void findById_notFound() throws Exception {
        Long id = 1L;

        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void findById_badRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/{id}", "wrongId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void findAll_ok() throws Exception {
        Long id1 = 12L;
        Teacher teacher1 = new Teacher();
        teacher1.setId(id1);

        Long id2 = 23L;
        Teacher teacher2 = new Teacher();
        teacher2.setId(id2);

        Long id3 = 34L;
        Teacher teacher3 = new Teacher();
        teacher3.setId(id3);

        List<Teacher> teachers = List.of(teacher1, teacher2, teacher3);

        when(teacherRepository.findAll()).thenReturn(teachers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(12))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(23))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(34));
    }
}
