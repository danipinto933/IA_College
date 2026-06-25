package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.DepartmentDto;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DepartmentControllerTestRestTemplateTests {

    @Autowired
    private TestRestTemplate restTemplate;

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

        // Act
        ResponseEntity<DepartmentDto> response = restTemplate.postForEntity(
                "/api/v1/departments",
                departmentDto,
                DepartmentDto.class
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Ciencia de la Computacion", response.getBody().getDepartmentName());
        verify(departmentService).addDepartment(any(DepartmentDto.class));
    }

    @Test
    public void testFindById_Success() {
        // Arrange
        when(departmentService.findById(1L)).thenReturn(departmentDto);

        // Act
        ResponseEntity<DepartmentDto> response = restTemplate.getForEntity(
                "/api/v1/departments/1",
                DepartmentDto.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Ciencia de la Computacion", response.getBody().getDepartmentName());
        verify(departmentService).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange
        when(departmentService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Departmento no encontrado con el ID: 99"));

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/v1/departments/99",
                String.class
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Departmento no encontrado con el ID: 99"));
        verify(departmentService).findById(99L);
    }
}
