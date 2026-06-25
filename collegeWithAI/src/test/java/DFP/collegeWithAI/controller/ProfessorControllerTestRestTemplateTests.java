package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.ProfessorDto;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.model.Professor;
import DFP.collegeWithAI.model.Schedule;
import DFP.collegeWithAI.service.ProfessorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfessorControllerTestRestTemplateTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ProfessorService professorService;

    private Professor professor;
    private ProfessorDto professorDto;

    @BeforeEach
    public void setUp() {
        professor = Professor.builder()
                .id(1L)
                .name("Alan")
                .surname("Turing")
                .dni("12345678A")
                .email("alan@college.edu")
                .phone(600123456)
                .schedule(Schedule.MORNING)
                .salary(3500.0)
                .build();

        professorDto = ProfessorDto.builder()
                .name("Alan")
                .surname("Turing")
                .dni("12345678A")
                .email("alan@college.edu")
                .phone(600123456)
                .schedule(Schedule.MORNING)
                .salary(3500.0)
                .departmentName("Computacion")
                .build();
    }

    @Test
    public void testAddProfessor() {
        // Arrange
        when(professorService.addProfessor(any(ProfessorDto.class))).thenReturn(professor);

        // Act
        ResponseEntity<Professor> response = restTemplate.postForEntity(
                "/api/v1/professors",
                professorDto,
                Professor.class
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Alan", response.getBody().getName());
        verify(professorService).addProfessor(any(ProfessorDto.class));
    }

    @Test
    public void testFindById_Success() {
        // Arrange
        when(professorService.findById(1L)).thenReturn(professorDto);

        // Act
        ResponseEntity<ProfessorDto> response = restTemplate.getForEntity(
                "/api/v1/professors/1",
                ProfessorDto.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Alan", response.getBody().getName());
        verify(professorService).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange
        when(professorService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Profesor no encontrado con el ID: 99"));

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/v1/professors/99",
                String.class
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Profesor no encontrado con el ID: 99"));
        verify(professorService).findById(99L);
    }
}
