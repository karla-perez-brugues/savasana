package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SessionMapperTest {

    private SessionMapper sessionMapper;

    @BeforeEach
    public void setUp() {
        sessionMapper = Mappers.getMapper(SessionMapper.class);
    }

    @Test
    public void testDtoListToEntityList() {
        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);

        SessionDto sessionDto3 = new SessionDto();
        sessionDto3.setId(3L);

        List<SessionDto> sessionDtoList = List.of(sessionDto1, sessionDto2, sessionDto3);

        List<Session> sessionList = sessionMapper.toEntity(sessionDtoList);

        assertNotNull(sessionList);
        assertEquals(sessionDtoList.size(), sessionList.size());
        assertEquals(1L, sessionList.get(0).getId());
        assertEquals(2L, sessionList.get(1).getId());
        assertEquals(3L, sessionList.get(2).getId());
    }

    @Test
    public void testEntityListToDtoList() {
        Session session1 = new Session();
        session1.setId(1L);

        Session session2 = new Session();
        session2.setId(2L);

        Session session3 = new Session();
        session3.setId(3L);

        List<Session> sessionList = List.of(session1, session2, session3);

        List<SessionDto> sessionDtoList = sessionMapper.toDto(sessionList);

        assertNotNull(sessionDtoList);
        assertEquals(sessionDtoList.size(), sessionList.size());
        assertEquals(1L, sessionList.get(0).getId());
        assertEquals(2L, sessionList.get(1).getId());
        assertEquals(3L, sessionList.get(2).getId());
    }

    @Test
    public void testDtoToEntity() {
        Date date = new Date();

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga session");
        sessionDto.setDate(date);
        sessionDto.setDescription("Yoga for beginners");

        Session session = sessionMapper.toEntity(sessionDto);

        assertNotNull(session);
        assertEquals(1L, session.getId());
        assertEquals("Yoga session", session.getName());
        assertEquals(date, session.getDate());
        assertEquals("Yoga for beginners", session.getDescription());
    }

    @Test
    public void testEntityToDto() {
        Date date = new Date();

        Session session = new Session()
                .setId(1L)
                .setName("Yoga session")
                .setDate(date)
                .setDescription("Yoga for beginners");

        SessionDto dto = sessionMapper.toDto(session);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Yoga session", dto.getName());
        assertEquals(date, dto.getDate());
        assertEquals("Yoga for beginners", dto.getDescription());
    }

}
