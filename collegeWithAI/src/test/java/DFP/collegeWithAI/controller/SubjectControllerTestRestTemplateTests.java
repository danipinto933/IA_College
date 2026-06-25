package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.SubjectDto;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.service.SubjectService;
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
public class SubjectControllerTestRestTemplateTests {

    @Autowired
    private TestRestTemplate restTemplate;

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

        // Act
        ResponseEntity<SubjectDto> response = restTemplate.postForEntity(
                "/api/v1/subjects",
                subjectDto,
                SubjectDto.class
        );

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Algebra Lineal", response.getBody().getName());
        verify(subjectService).addSubject(any(SubjectDto.class));
    }

    @Test
    public void testFindById_Success() {
        // Arrange
        when(subjectService.findById(1L)).thenReturn(subjectDto);

        // Act
        ResponseEntity<SubjectDto> response = restTemplate.getForEntity(
                "/api/v1/subjects/1",
                SubjectDto.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Algebra Lineal", response.getBody().getName());
        verify(subjectService).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange
        when(subjectService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Materia no encontrada con el ID: 99"));

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/v1/subjects/99",
                String.class
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Materia no encontrada con el ID: 99"));
        verify(subjectService).findById(99L);
    }
}
