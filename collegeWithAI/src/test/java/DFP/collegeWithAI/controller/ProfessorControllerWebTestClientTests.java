package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.ProfessorDto;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.model.Professor;
import DFP.collegeWithAI.model.Schedule;
import DFP.collegeWithAI.service.ProfessorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ProfessorControllerWebTestClientTests {

    @Autowired
    private WebTestClient webTestClient;

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

        // Act & Assert
        webTestClient.post()
                .uri("/api/v1/professors")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(professorDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Alan")
                .jsonPath("$.dni").isEqualTo("12345678A");

        verify(professorService).addProfessor(any(ProfessorDto.class));
    }

    @Test
    public void testFindById_Success() {
        // Arrange
        when(professorService.findById(1L)).thenReturn(professorDto);

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/professors/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Alan");

        verify(professorService).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange
        when(professorService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Profesor no encontrado con el ID: 99"));

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/professors/99")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.message").isEqualTo("Profesor no encontrado con el ID: 99");

        verify(professorService).findById(99L);
    }
}
