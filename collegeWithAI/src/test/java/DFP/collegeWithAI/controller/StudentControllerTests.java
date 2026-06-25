package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.StudentDto;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.model.Schedule;
import DFP.collegeWithAI.model.Student;
import DFP.collegeWithAI.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Student student;
    private StudentDto studentDto;

    @BeforeEach
    public void setUp() {
        student = Student.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .dni("11111111A")
                .email("john.doe@student.edu")
                .phone(611111111)
                .schedule(Schedule.MORNING)
                .dateJoining(new Date())
                .build();

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
    public void addStudent_Success() throws Exception {
        when(studentService.addStudent(any(StudentDto.class))).thenReturn(studentDto);

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.dni", is("11111111A")));

        verify(studentService).addStudent(any(StudentDto.class));
    }

    @Test
    public void addStudent_BadRequest_ValidationFailed() throws Exception {
        StudentDto invalidDto = StudentDto.builder()
                .name("")
                .surname("")
                .dni("")
                .email("invalido")
                .build();

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("no puede estar vacio")));

        verifyNoInteractions(studentService);
    }

    @Test
    public void findAllStudents_Success() throws Exception {
        List<StudentDto> list = List.of(studentDto);
        when(studentService.findAllStudents()).thenReturn(list);

        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("John")));

        verify(studentService).findAllStudents();
    }

    @Test
    public void findById_Success() throws Exception {
        when(studentService.findById(1L)).thenReturn(studentDto);

        mockMvc.perform(get("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John")));

        verify(studentService).findById(1L);
    }

    @Test
    public void findById_NotFound() throws Exception {
        when(studentService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Estudiante no encontrado con el ID: 99"));

        mockMvc.perform(get("/api/v1/students/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Estudiante no encontrado con el ID: 99")));

        verify(studentService).findById(99L);
    }

    @Test
    public void updateStudent_Success() throws Exception {
        when(studentService.updateStudent(eq(1L), any(StudentDto.class))).thenReturn(studentDto);

        mockMvc.perform(put("/api/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John")));

        verify(studentService).updateStudent(eq(1L), any(StudentDto.class));
    }

    @Test
    public void deleteStudent_Success() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Estudiante eliminado exitosamente"));

        verify(studentService).deleteStudent(1L);
    }

    @Test
    public void findByName_Success() throws Exception {
        when(studentService.findByName("John")).thenReturn(List.of(studentDto));

        mockMvc.perform(get("/api/v1/students/name/John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void findByDateJoining_Success() throws Exception {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = format.format(now);

        when(studentService.findByDateJoining(any(Date.class))).thenReturn(List.of(studentDto));

        mockMvc.perform(get("/api/v1/students/dateJoining/" + formattedDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
