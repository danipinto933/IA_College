package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.SubjectDto;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.service.SubjectService;
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
public class SubjectControllerWebTestClientTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SubjectService subjectService;

    private SubjectDto subjectDto;

    @BeforeEach
    public void setUp() {
        subjectDto = SubjectDto.builder()
                .name("Algebra Lineal")
                .internCode("MAT-101")
                .description("Algebra basica")
                .isElective(false)
                .totalHours(60)
                .departmentName("Matematica")
                .build();
    }

    @Test
    public void testAddSubject() {
        // Arrange
        when(subjectService.addSubject(any(SubjectDto.class))).thenReturn(subjectDto);

        // Act & Assert
        webTestClient.post()
                .uri("/api/v1/subjects")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(subjectDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Algebra Lineal")
                .jsonPath("$.internCode").isEqualTo("MAT-101");

        verify(subjectService).addSubject(any(SubjectDto.class));
    }

    @Test
    public void testFindById_Success() {
        // Arrange
        when(subjectService.findById(1L)).thenReturn(subjectDto);

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/subjects/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Algebra Lineal");

        verify(subjectService).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange
        when(subjectService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Materia no encontrada con el ID: 99"));

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/subjects/99")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.message").isEqualTo("Materia no encontrada con el ID: 99");

        verify(subjectService).findById(99L);
    }
}
