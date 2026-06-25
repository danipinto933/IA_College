package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.ClassroomDto;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.model.Classroom;
import DFP.collegeWithAI.service.ClassroomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ClassroomControllerWebTestClientTests {

    @Autowired
    private WebTestClient webTestClient;

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

        // Act & Assert
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/classrooms")
                        .queryParam("studentEmail", "student@college.edu")
                        .queryParam("professorEmail", "prof@college.edu")
                        .queryParam("subjectInternCode", "INF-201")
                        .queryParam("grade", "9.5")
                        .build())
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.grade").isEqualTo(9.5);

        verify(classroomService).addClassroom("student@college.edu", "prof@college.edu", "INF-201", 9.5);
    }

    @Test
    public void testFindById_Success() {
        // Arrange
        when(classroomService.findById(1L)).thenReturn(classroomDto);

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/classrooms/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.grade").isEqualTo(9.5);

        verify(classroomService).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange
        when(classroomService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Clase/Matrícula no encontrada con el ID: 99"));

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/classrooms/99")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.message").isEqualTo("Clase/Matrícula no encontrada con el ID: 99");

        verify(classroomService).findById(99L);
    }
}
