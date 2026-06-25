package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.ClassroomDto;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.model.Classroom;
import DFP.collegeWithAI.service.ClassroomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClassroomControllerTestRestTemplateTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ClassroomService classroomService;

    private Classroom classroom;
    private ClassroomDto classroomDto;

    @BeforeEach
    public void setUp() {
        classroom = Classroom.builder()
                .id(1L)
                .grade(9.5)
                .build();

        classroomDto = ClassroomDto.builder()
                .grade(9.5)
                .build();
    }

    @Test
    public void testAddClassroom() {
        // Arrange
        when(classroomService.addClassroom("student@college.edu", "prof@college.edu", "INF-201", 9.5))
                .thenReturn(classroom);

        URI targetUrl = UriComponentsBuilder.fromUriString("/api/v1/classrooms")
                .queryParam("studentEmail", "student@college.edu")
                .queryParam("professorEmail", "prof@college.edu")
                .queryParam("subjectInternCode", "INF-201")
                .queryParam("grade", 9.5)
                .build()
                .toUri();

        // Act
        ResponseEntity<Classroom> response = restTemplate.postForEntity(
                targetUrl,
                null,
                Classroom.class
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(9.5, response.getBody().getGrade());
        verify(classroomService).addClassroom("student@college.edu", "prof@college.edu", "INF-201", 9.5);
    }

    @Test
    public void testFindById_Success() {
        // Arrange
        when(classroomService.findById(1L)).thenReturn(classroomDto);

        // Act
        ResponseEntity<ClassroomDto> response = restTemplate.getForEntity(
                "/api/v1/classrooms/1",
                ClassroomDto.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(9.5, response.getBody().getGrade());
        verify(classroomService).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange
        when(classroomService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Clase/Matrícula no encontrada con el ID: 99"));

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/v1/classrooms/99",
                String.class
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Clase/Matrícula no encontrada con el ID: 99"));
        verify(classroomService).findById(99L);
    }
}
