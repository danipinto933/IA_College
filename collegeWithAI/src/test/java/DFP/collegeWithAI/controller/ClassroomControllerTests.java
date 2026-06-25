package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.ClassroomDto;
import DFP.collegeWithAI.dto.response.StudentDto;
import DFP.collegeWithAI.dto.response.SubjectDto;
import DFP.collegeWithAI.dto.response.ProfessorDto;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.model.Classroom;
import DFP.collegeWithAI.model.Student;
import DFP.collegeWithAI.model.Subject;
import DFP.collegeWithAI.model.Professor;
import DFP.collegeWithAI.service.ClassroomService;
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

@WebMvcTest(ClassroomController.class)
public class ClassroomControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClassroomService classroomService;

    private Classroom classroom;
    private ClassroomDto classroomDto;

    @BeforeEach
    public void setUp() {
        Student student = Student.builder().email("student@college.edu").build();
        Subject subject = Subject.builder().internCode("INF-201").build();
        Professor professor = Professor.builder().email("prof@college.edu").build();

        classroom = Classroom.builder()
                .id(1L)
                .student(student)
                .subject(subject)
                .professor(professor)
                .grade(9.5)
                .build();

        classroomDto = ClassroomDto.builder()
                .student(StudentDto.builder().email("student@college.edu").build())
                .subject(SubjectDto.builder().internCode("INF-201").build())
                .professor(ProfessorDto.builder().email("prof@college.edu").build())
                .grade(9.5)
                .build();
    }

    @Test
    public void addClassroom_Success() throws Exception {
        when(classroomService.addClassroom("student@college.edu", "prof@college.edu", "INF-201", 9.5))
                .thenReturn(classroom);

        mockMvc.perform(post("/api/v1/classrooms")
                        .param("studentEmail", "student@college.edu")
                        .param("professorEmail", "prof@college.edu")
                        .param("subjectInternCode", "INF-201")
                        .param("grade", "9.5"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.grade", is(9.5)));

        verify(classroomService).addClassroom("student@college.edu", "prof@college.edu", "INF-201", 9.5);
    }

    @Test
    public void findAllClassrooms_Success() throws Exception {
        List<ClassroomDto> list = List.of(classroomDto);
        when(classroomService.findAllClassrooms()).thenReturn(list);

        mockMvc.perform(get("/api/v1/classrooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].grade", is(9.5)));

        verify(classroomService).findAllClassrooms();
    }

    @Test
    public void findById_Success() throws Exception {
        when(classroomService.findById(1L)).thenReturn(classroomDto);

        mockMvc.perform(get("/api/v1/classrooms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade", is(9.5)));

        verify(classroomService).findById(1L);
    }

    @Test
    public void findById_NotFound() throws Exception {
        when(classroomService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Clase/Matrícula no encontrada con el ID: 99"));

        mockMvc.perform(get("/api/v1/classrooms/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Clase/Matrícula no encontrada con el ID: 99")));

        verify(classroomService).findById(99L);
    }

    @Test
    public void updateGrade_Success() throws Exception {
        when(classroomService.updateGrade(eq(1L), eq(9.0))).thenReturn(classroomDto);

        mockMvc.perform(put("/api/v1/classrooms/1/grade")
                        .param("grade", "9.0"))
                .andExpect(status().isOk());

        verify(classroomService).updateGrade(eq(1L), eq(9.0));
    }

    @Test
    public void deleteClassroom_Success() throws Exception {
        doNothing().when(classroomService).deleteClassroom(1L);

        mockMvc.perform(delete("/api/v1/classrooms/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Clase/Matricula eliminada exitosamente"));

        verify(classroomService).deleteClassroom(1L);
    }
}
