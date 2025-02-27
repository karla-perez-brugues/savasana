package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    SessionRepository sessionRepository;

    @Mock
    UserRepository userRepository;

    SessionService sessionService;

    @BeforeEach
    public void setUp() {
        sessionService = new SessionService(sessionRepository, userRepository);
    }

    @Test
    public void create_shouldUseRepository_toSave() {
        Session session = new Session();

        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.create(session);

        verify(sessionRepository).save(session);
        assertEquals(session, result);
    }

    @Test
    public void delete_shouldUseRepository_toDelete() {
        Long id = 1L;

        sessionService.delete(id);

        verify(sessionRepository).deleteById(id);
    }

    @Test
    public void findAll_shouldReturnAllSessions() {
        Session session1 = new Session();
        session1.setId(1L);
        Session session2 = new Session();
        session2.setId(2L);
        Session session3 = new Session();
        session3.setId(3L);

        List<Session> sessions = Arrays.asList(session1, session2, session3);

        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = sessionService.findAll();

        verify(sessionRepository).findAll();
        assertEquals(sessions, result);
    }

    @Test
    public void getById_shouldReturnSession() {
        Long id = 1L;
        Session session = new Session();
        session.setId(id);

        when(sessionRepository.findById(id)).thenReturn(Optional.of(session));

        Session result = sessionService.getById(id);

        verify(sessionRepository).findById(id);
        assertEquals(session, result);
    }

    @Test
    public void getById_shouldReturnNullIfSessionNotFound() {
        Long id = 1L;

        when(sessionRepository.findById(id)).thenReturn(Optional.empty());

        Session result = sessionService.getById(id);

        verify(sessionRepository).findById(id);
        assertNull(result);
    }

    @Test
    public void update_shouldUpdateSession() {
        Long sessionId = 1L;
        Session session = new Session();

        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.update(sessionId, session);

        verify(sessionRepository).save(session);
        assertEquals(sessionId, result.getId());
    }

    @Test
    public void participate_shouldAddUserToSession() {
        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>());

        Long userId = 2L;
        User user = new User();
        user.setId(userId);
        List<User> users = Collections.singletonList(user);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        sessionService.participate(sessionId, userId);

        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
        verify(sessionRepository).save(session);
        assertEquals(users, session.getUsers());
    }

    @Test
    public void participate_shouldThrowNotFoundExceptionIfSessionNotFound() {
        Long sessionId = 1L;
        Long userId = 2L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    public void participate_shouldThrowNotFoundExceptionIfUserNotFound() {
        Long sessionId = 1L;
        Long userId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    public void participate_shouldThrowBadRequestExceptionIfUserAlreadyParticipated() {
        Long userId = 2L;
        User user = new User();
        user.setId(userId);

        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(List.of(user));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    public void noLongerParticipate_shouldRemoveUserFromSession() {
        Long userId = 2L;
        User user = new User();
        user.setId(userId);

        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(List.of(user));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(sessionId, userId);

        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository).save(session);
        assertEquals(List.of(), session.getUsers());
    }

    @Test
    public void noLongerParticipate_shouldThrowNotFoundExceptionIfSessionNotFound() {
        Long sessionId = 1L;
        Long userId = 2L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
    }

    @Test
    public void noLongerParticipate_shouldThrowBadRequestExceptionIfUserDoesNotParticipate() {
        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(List.of());

        Long userId = 2L;
        User user = new User();
        user.setId(userId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
    }

}
