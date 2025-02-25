package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    @BeforeEach
    public void setUp() {
        teacherMapper = Mappers.getMapper(TeacherMapper.class);
    }

    @Test
    public void testDtoListToEntityList() {
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);

        TeacherDto teacherDto3 = new TeacherDto();
        teacherDto3.setId(3L);

        List<TeacherDto> teacherDtoList = List.of(teacherDto1, teacherDto2, teacherDto3);

        List<Teacher> teacherList = teacherMapper.toEntity(teacherDtoList);

        assertNotNull(teacherList);
        assertEquals(teacherDtoList.size(), teacherList.size());
        assertEquals(1L, teacherList.get(0).getId());
        assertEquals(2L, teacherList.get(1).getId());
        assertEquals(3L, teacherList.get(2).getId());
    }

    @Test
    public void testEntityListToDtoList() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);

        Teacher teacher3 = new Teacher();
        teacher3.setId(3L);

        List<Teacher> teacherList = List.of(teacher1, teacher2, teacher3);

        List<TeacherDto> teacherDtoList = teacherMapper.toDto(teacherList);

        assertNotNull(teacherDtoList);
        assertEquals(teacherDtoList.size(), teacherList.size());
        assertEquals(1L, teacherList.get(0).getId());
        assertEquals(2L, teacherList.get(1).getId());
        assertEquals(3L, teacherList.get(2).getId());
    }

    @Test
    public void testDtoToEntity() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("Ramdev");
        teacherDto.setFirstName("Baba");

        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertNotNull(teacher);
        assertEquals(1L, teacher.getId());
        assertEquals("Ramdev", teacher.getLastName());
        assertEquals("Baba", teacher.getFirstName());
    }

    @Test
    public void testEntityToDto() {
        Teacher teacher = new Teacher()
                .setId(1L)
                .setLastName("Ramdev")
                .setFirstName("Baba");

        TeacherDto dto = teacherMapper.toDto(teacher);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Ramdev", dto.getLastName());
        assertEquals("Baba", dto.getFirstName());
    }

}
