package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.DepartmentDto;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.service.DepartmentService;
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
public class DepartmentControllerWebTestClientTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DepartmentService departmentService;

    private DepartmentDto departmentDto;

    @BeforeEach
    public void setUp() {
        departmentDto = DepartmentDto.builder()
                .departmentName("Ciencia de la Computacion")
                .build();
    }

    @Test
    public void testAddDepartment() {
        // Arrange
        when(departmentService.addDepartment(any(DepartmentDto.class))).thenReturn(departmentDto);

        // Act & Assert
        webTestClient.post()
                .uri("/api/v1/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(departmentDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.departmentName").isEqualTo("Ciencia de la Computacion");

        verify(departmentService).addDepartment(any(DepartmentDto.class));
    }

    @Test
    public void testFindById_Success() {
        // Arrange
        when(departmentService.findById(1L)).thenReturn(departmentDto);

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/departments/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.departmentName").isEqualTo("Ciencia de la Computacion");

        verify(departmentService).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange
        when(departmentService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Departmento no encontrado con el ID: 99"));

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/departments/99")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.message").isEqualTo("Departmento no encontrado con el ID: 99");

        verify(departmentService).findById(99L);
    }
}
