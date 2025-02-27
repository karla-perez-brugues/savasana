package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
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

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SessionRepository sessionRepository;

    @MockBean
    UserRepository userRepository;

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void findById_shouldReturnSession() throws Exception {
        Long id = 1L;
        Session session = new Session();
        session.setId(id);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(id);

        when(sessionRepository.findById(id)).thenReturn(Optional.of(session));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void findById_shouldReturnNotFound() throws Exception {
        Long id = 1L;

        when(sessionRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void findById_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", "wrongId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void findAll_shouldReturnSessions() throws Exception {
        Session session1 = new Session();
        session1.setId(1L);
        Session session2 = new Session();
        session2.setId(2L);
        Session session3 = new Session();
        session3.setId(3L);

        List<Session> sessions = List.of(session1, session2, session3);

        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        SessionDto sessionDto3 = new SessionDto();
        sessionDto3.setId(3L);

        when(sessionRepository.findAll()).thenReturn(sessions);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(3));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void create_success() throws Exception {
        Long sessionId = 1L;
        String name = "Yoga";
        Date date = new Date();
        Long teacherId = 1L;
        String description = "Yoga for beginners";

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(sessionId);
        sessionDto.setName(name);
        sessionDto.setDate(date);
        sessionDto.setTeacher_id(teacherId);
        sessionDto.setDescription(description);
        sessionDto.setUsers(new ArrayList<>());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(sessionDto);

        Teacher teacher = new Teacher();
        teacher.setId(teacherId);

        Session session = new Session();
        session.setId(sessionId);
        session.setName(name);
        session.setDate(date);
        session.setTeacher(teacher);
        session.setDescription(description);
        session.setUsers(new ArrayList<>());

        when(sessionRepository.save(session)).thenReturn(session);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(description));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void update_success() throws Exception {
        Long sessionId = 12L;
        String name = "Yoga Session";
        Date date = new Date();
        Long teacherId = 1L;
        String description = "Yoga for beginners";

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(sessionId);
        sessionDto.setName(name);
        sessionDto.setDate(date);
        sessionDto.setTeacher_id(teacherId);
        sessionDto.setDescription(description);
        sessionDto.setUsers(new ArrayList<>());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(sessionDto);

        Session session = new Session();
        session.setId(sessionId);
        session.setName(name);
        session.setDate(date);
        session.setDescription(description);
        session.setUsers(new ArrayList<>());

        when(sessionRepository.save(session)).thenReturn(session);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/{id}", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(description));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void update_fail() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Yoga for beginners");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(sessionDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/{id}", "wrongId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void delete_ok() throws Exception {
        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}", sessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void delete_notFound() throws Exception {
        Long id = 1L;

        when(sessionRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void delete_badRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}", "wrongId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void participate_ok() throws Exception {
        Long sessionId = 1L;
        String name = "Yoga Session";
        Date date = new Date();
        String description = "Yoga for beginners";
        List<User> users = new ArrayList<>();

        Session session = new Session();
        session.setId(sessionId);
        session.setName(name);
        session.setDate(date);
        session.setDescription(description);
        session.setUsers(users);

        Long userId = 2L;
        User user = new User();
        user.setId(userId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(sessionRepository.save(session)).thenReturn(session);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/{id}/participate/{userId}", sessionId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void participate_badRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/{id}/participate/{userId}", "wrongSessionId", "wrongUserId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void unParticipate_ok() throws Exception {
        Long userId = 2L;
        User user = new User();
        user.setId(userId);

        Long sessionId = 1L;
        String name = "Yoga Session";
        Date date = new Date();
        String description = "Yoga for beginners";
        List<User> users = List.of(user);

        Session session = new Session();
        session.setId(sessionId);
        session.setName(name);
        session.setDate(date);
        session.setDescription(description);
        session.setUsers(users);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(sessionRepository.save(session)).thenReturn(session);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}/participate/{userId}", sessionId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void unParticipate_badRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/{id}/participate/{userId}", "wrongSessionId", "wrongUserId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
