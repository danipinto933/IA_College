package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.StudentDto;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.model.Schedule;
import DFP.collegeWithAI.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class StudentControllerWebTestClientTests {

    @Autowired
    private WebTestClient webTestClient;

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

        webTestClient.post()
                .uri("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(studentDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("John")
                .jsonPath("$.dni").isEqualTo("11111111A");

        verify(studentService).addStudent(any(StudentDto.class));
    }

    @Test
    public void testFindById_Success() {
        when(studentService.findById(1L)).thenReturn(studentDto);

        webTestClient.get()
                .uri("/api/v1/students/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("John");

        verify(studentService).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(studentService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Estudiante no encontrado con el ID: 99"));

        webTestClient.get()
                .uri("/api/v1/students/99")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.message").isEqualTo("Estudiante no encontrado con el ID: 99");

        verify(studentService).findById(99L);
    }
}
