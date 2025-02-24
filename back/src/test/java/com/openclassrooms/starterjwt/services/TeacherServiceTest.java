package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;

    TeacherService teacherService;

    @BeforeEach
    public void setUp() {
        teacherService = new TeacherService(teacherRepository);
    }

    @Test
    public void findAll_shouldReturnAllTeachers() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        Teacher teacher3 = new Teacher();
        teacher3.setId(3L);

        List<Teacher> teachers = List.of(teacher1, teacher2, teacher3);

        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.findAll();

        verify(teacherRepository).findAll();
        assertEquals(teachers, result);
    }

    @Test
    public void findById_shouldReturnTeacher() {
        Long id = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(id);

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.findById(id);

        verify(teacherRepository).findById(id);
        assertEquals(teacher, result);
    }

    @Test
    public void findById_shouldReturnNullIfTeacherNotFound() {
        Long id = 1L;

        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        Teacher result = teacherService.findById(id);

        verify(teacherRepository).findById(id);
        assertNull(result);
    }
}
