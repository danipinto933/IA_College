package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.SubjectDto;
import DFP.collegeWithAI.exception.custom.BadRequestException;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.model.Subject;
import DFP.collegeWithAI.service.SubjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectController.class)
public class SubjectControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectService subjectService;

    @Autowired
    private ObjectMapper objectMapper;

    private Subject subject;
    private SubjectDto subjectDto;

    @BeforeEach
    public void setUp() {
        subject = Subject.builder()
                .id(1L)
                .name("Algebra Lineal")
                .internCode("MAT-101")
                .description("Algebra basica")
                .isElective(false)
                .totalHours(60)
                .build();

        subjectDto = SubjectDto.builder()
                .name("Algebra Lineal")
                .internCode("MAT-101")
                .description("Algebra basica")
                .isElective(false)
                .totalHours(60)
                .departmentName("Matematica")
                .build();
    }

    // ==========================================
    // Tests for addSubject (POST)
    // ==========================================

    @Test
    public void addSubject_Success() throws Exception {
        when(subjectService.addSubject(any(SubjectDto.class))).thenReturn(subjectDto);

        mockMvc.perform(post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subjectDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Algebra Lineal")))
                .andExpect(jsonPath("$.internCode", is("MAT-101")));

        verify(subjectService).addSubject(any(SubjectDto.class));
    }

    @Test
    public void addSubject_BadRequest_ValidationFailed() throws Exception {
        // Missing name and internCode
        SubjectDto invalidDto = SubjectDto.builder()
                .name("")
                .internCode("")
                .description("Algebra basica")
                .isElective(false)
                .totalHours(60)
                .departmentName("Matematica")
                .build();

        mockMvc.perform(post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("no puede estar vacio")));

        verifyNoInteractions(subjectService);
    }

    @Test
    public void addSubject_BadRequest_ServiceThrowsException() throws Exception {
        when(subjectService.addSubject(any(SubjectDto.class)))
                .thenThrow(new BadRequestException("Materia no válida"));

        mockMvc.perform(post("/api/v1/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subjectDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Materia no válida")));

        verify(subjectService).addSubject(any(SubjectDto.class));
    }

    // ==========================================
    // Tests for findAllSubjects (GET)
    // ==========================================

    @Test
    public void findAllSubjects_Success() throws Exception {
        List<SubjectDto> list = List.of(subjectDto);
        when(subjectService.findAllSubjects()).thenReturn(list);

        mockMvc.perform(get("/api/v1/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Algebra Lineal")));

        verify(subjectService).findAllSubjects();
    }

    // ==========================================
    // Tests for findAllFull (GET)
    // ==========================================

    @Test
    public void findAllFull_Success() throws Exception {
        List<Subject> list = List.of(subject);
        when(subjectService.findAllFull()).thenReturn(list);

        mockMvc.perform(get("/api/v1/subjects/full"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Algebra Lineal")));

        verify(subjectService).findAllFull();
    }

    // ==========================================
    // Tests for findById (GET)
    // ==========================================

    @Test
    public void findById_Success() throws Exception {
        when(subjectService.findById(1L)).thenReturn(subjectDto);

        mockMvc.perform(get("/api/v1/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Algebra Lineal")));

        verify(subjectService).findById(1L);
    }

    @Test
    public void findById_NotFound() throws Exception {
        when(subjectService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Materia no encontrada con el ID: 99"));

        mockMvc.perform(get("/api/v1/subjects/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Materia no encontrada con el ID: 99")));

        verify(subjectService).findById(99L);
    }

    // ==========================================
    // Tests for findByName (GET)
    // ==========================================

    @Test
    public void findByName_Success() throws Exception {
        when(subjectService.findByName("Algebra Lineal")).thenReturn(subjectDto);

        mockMvc.perform(get("/api/v1/subjects/name/Algebra Lineal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Algebra Lineal")));

        verify(subjectService).findByName("Algebra Lineal");
    }

    // ==========================================
    // Tests for findByInternCode (GET)
    // ==========================================

    @Test
    public void findByInternCode_Success() throws Exception {
        when(subjectService.findByInternCode("MAT-101")).thenReturn(subjectDto);

        mockMvc.perform(get("/api/v1/subjects/internCode/MAT-101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.internCode", is("MAT-101")));

        verify(subjectService).findByInternCode("MAT-101");
    }

    // ==========================================
    // Tests for findByIsElective (GET)
    // ==========================================

    @Test
    public void findByIsElective_Success() throws Exception {
        List<SubjectDto> list = List.of(subjectDto);
        when(subjectService.findByIsElective(false)).thenReturn(list);

        mockMvc.perform(get("/api/v1/subjects/isElective/false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(subjectService).findByIsElective(false);
    }

    // ==========================================
    // Tests for findByTotalHours (GET)
    // ==========================================

    @Test
    public void findByTotalHours_Success() throws Exception {
        List<SubjectDto> list = List.of(subjectDto);
        when(subjectService.findByTotalHours(60)).thenReturn(list);

        mockMvc.perform(get("/api/v1/subjects/totalHours/60"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(subjectService).findByTotalHours(60);
    }

    // ==========================================
    // Tests for findByDepartmentName (GET)
    // ==========================================

    @Test
    public void findByDepartmentName_Success() throws Exception {
        List<SubjectDto> list = List.of(subjectDto);
        when(subjectService.findByDepartmentName("Matematica")).thenReturn(list);

        mockMvc.perform(get("/api/v1/subjects/department/Matematica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(subjectService).findByDepartmentName("Matematica");
    }

    // ==========================================
    // Tests for updateSubject (PUT)
    // ==========================================

    @Test
    public void updateSubject_Success() throws Exception {
        SubjectDto updateDto = SubjectDto.builder()
                .name("Algebra Lineal Avanzada")
                .internCode("MAT-101")
                .description("Algebra basica")
                .isElective(false)
                .totalHours(60)
                .departmentName("Matematica")
                .build();

        when(subjectService.updateSubject(eq(1L), any(SubjectDto.class))).thenReturn(updateDto);

        mockMvc.perform(put("/api/v1/subjects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Algebra Lineal Avanzada")));

        verify(subjectService).updateSubject(eq(1L), any(SubjectDto.class));
    }

    @Test
    public void updateSubject_NotFound() throws Exception {
        when(subjectService.updateSubject(eq(99L), any(SubjectDto.class)))
                .thenThrow(new ResourceNotFoundException("Materia no encontrada con el ID: 99"));

        mockMvc.perform(put("/api/v1/subjects/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subjectDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Materia no encontrada con el ID: 99")));

        verify(subjectService).updateSubject(eq(99L), any(SubjectDto.class));
    }

    // ==========================================
    // Tests for deleteSubject (DELETE)
    // ==========================================

    @Test
    public void deleteSubject_Success() throws Exception {
        doNothing().when(subjectService).deleteSubject(1L);

        mockMvc.perform(delete("/api/v1/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Materia eliminada exitosamente"));

        verify(subjectService).deleteSubject(1L);
    }

    @Test
    public void deleteSubject_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Materia no encontrada con el ID: 99"))
                .when(subjectService).deleteSubject(99L);

        mockMvc.perform(delete("/api/v1/subjects/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Materia no encontrada con el ID: 99")));

        verify(subjectService).deleteSubject(99L);
    }
}
