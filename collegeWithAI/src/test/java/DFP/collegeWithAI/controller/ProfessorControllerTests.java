package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.ProfessorDto;
import DFP.collegeWithAI.exception.custom.BadRequestException;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.model.Professor;
import DFP.collegeWithAI.model.Schedule;
import DFP.collegeWithAI.service.ProfessorService;
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

@WebMvcTest(ProfessorController.class)
public class ProfessorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfessorService professorService;

    @Autowired
    private ObjectMapper objectMapper;

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

    // ==========================================
    // Tests for addProfessor (POST)
    // ==========================================

    @Test
    public void addProfessor_Success() throws Exception {
        when(professorService.addProfessor(any(ProfessorDto.class))).thenReturn(professor);

        mockMvc.perform(post("/api/v1/professors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(professorDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Alan")))
                .andExpect(jsonPath("$.dni", is("12345678A")));

        verify(professorService).addProfessor(any(ProfessorDto.class));
    }

    @Test
    public void addProfessor_BadRequest_ValidationFailed() throws Exception {
        ProfessorDto invalidDto = ProfessorDto.builder()
                .name("")
                .surname("")
                .dni("")
                .email("invalido")
                .build();

        mockMvc.perform(post("/api/v1/professors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("no puede estar vacio")));

        verifyNoInteractions(professorService);
    }

    // ==========================================
    // Tests for findAllProfessors (GET)
    // ==========================================

    @Test
    public void findAllProfessors_Success() throws Exception {
        List<ProfessorDto> list = List.of(professorDto);
        when(professorService.findAllProfessors()).thenReturn(list);

        mockMvc.perform(get("/api/v1/professors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Alan")));

        verify(professorService).findAllProfessors();
    }

    // ==========================================
    // Tests for findById (GET)
    // ==========================================

    @Test
    public void findById_Success() throws Exception {
        when(professorService.findById(1L)).thenReturn(professorDto);

        mockMvc.perform(get("/api/v1/professors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Alan")));

        verify(professorService).findById(1L);
    }

    @Test
    public void findById_NotFound() throws Exception {
        when(professorService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Profesor no encontrado con el ID: 99"));

        mockMvc.perform(get("/api/v1/professors/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Profesor no encontrado con el ID: 99")));

        verify(professorService).findById(99L);
    }

    // ==========================================
    // Tests for searches (GET)
    // ==========================================

    @Test
    public void findByName_Success() throws Exception {
        when(professorService.findByName("Alan")).thenReturn(List.of(professorDto));

        mockMvc.perform(get("/api/v1/professors/name/Alan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void findByDni_Success() throws Exception {
        when(professorService.findByDni("12345678A")).thenReturn(professorDto);

        mockMvc.perform(get("/api/v1/professors/dni/12345678A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Alan")));
    }

    // ==========================================
    // Tests for updateProfessor (PUT)
    // ==========================================

    @Test
    public void updateProfessor_Success() throws Exception {
        when(professorService.updateProfessor(eq(1L), any(ProfessorDto.class))).thenReturn(professorDto);

        mockMvc.perform(put("/api/v1/professors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(professorDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Alan")));

        verify(professorService).updateProfessor(eq(1L), any(ProfessorDto.class));
    }

    // ==========================================
    // Tests for deleteProfessor (DELETE)
    // ==========================================

    @Test
    public void deleteProfessor_Success() throws Exception {
        doNothing().when(professorService).deleteProfessor(1L);

        mockMvc.perform(delete("/api/v1/professors/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Profesor eliminado exitosamente"));

        verify(professorService).deleteProfessor(1L);
    }

    // ==========================================
    // Tests for student association (POST / DELETE)
    // ==========================================

    @Test
    public void assignStudentToProfessor_Success() throws Exception {
        when(professorService.assignStudentToProfessor(1L, 5L)).thenReturn(professorDto);

        mockMvc.perform(post("/api/v1/professors/1/students/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Alan")));

        verify(professorService).assignStudentToProfessor(1L, 5L);
    }

    @Test
    public void removeStudentFromProfessor_Success() throws Exception {
        when(professorService.removeStudentFromProfessor(1L, 5L)).thenReturn(professorDto);

        mockMvc.perform(delete("/api/v1/professors/1/students/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Alan")));

        verify(professorService).removeStudentFromProfessor(1L, 5L);
    }
}
