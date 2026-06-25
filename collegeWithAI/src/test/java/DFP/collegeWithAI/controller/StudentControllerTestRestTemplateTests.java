package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.StudentDto;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.model.Schedule;
import DFP.collegeWithAI.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRestTemplateTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private StudentService studentService;

    private StudentDto studentDto;

    @BeforeEach
    public void setUp() {
        studentDto = StudentDto.builder()
                .name("John")
                .surname("Doe")
                .dni("11111111A")
                .email("john.doe@student.edu")
                .phone(611111111)
                .schedule(Schedule.MORNING)
                .dateJoining(new Date())
                .build();
    }

    @Test
    public void testAddStudent() {
        when(studentService.addStudent(any(StudentDto.class))).thenReturn(studentDto);

        ResponseEntity<StudentDto> response = restTemplate.postForEntity(
                "/api/v1/students",
                studentDto,
                StudentDto.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getName());
        verify(studentService).addStudent(any(StudentDto.class));
    }

    @Test
    public void testFindById_Success() {
        when(studentService.findById(1L)).thenReturn(studentDto);

        ResponseEntity<StudentDto> response = restTemplate.getForEntity(
                "/api/v1/students/1",
                StudentDto.class
            );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getName());
        verify(studentService).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(studentService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Estudiante no encontrado con el ID: 99"));

        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/v1/students/99",
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Estudiante no encontrado con el ID: 99"));
        verify(studentService).findById(99L);
    }
}
