package DFP.collegeWithAI.service;

import DFP.collegeWithAI.dto.response.ClassroomDto;
import DFP.collegeWithAI.exception.custom.BadRequestException;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.mapper.ClassroomMapper;
import DFP.collegeWithAI.model.Classroom;
import DFP.collegeWithAI.model.Professor;
import DFP.collegeWithAI.model.Student;
import DFP.collegeWithAI.model.Subject;
import DFP.collegeWithAI.repository.ClassroomRepository;
import DFP.collegeWithAI.repository.ProfessorRepository;
import DFP.collegeWithAI.repository.StudentRepository;
import DFP.collegeWithAI.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceTests {

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private ClassroomMapper classroomMapper;

    @InjectMocks
    private ClassroomServiceImpl classroomService;

    private Student student;
    private Subject subject;
    private Professor professor;
    private Classroom classroom;
    private ClassroomDto classroomDto;

    @BeforeEach
    public void setUp() {
        student = Student.builder()
                .id(1L)
                .email("student@college.edu")
                .build();

        subject = Subject.builder()
                .id(2L)
                .internCode("PROG-1")
                .build();

        professor = Professor.builder()
                .id(3L)
                .email("prof@college.edu")
                .build();

        classroom = Classroom.builder()
                .id(4L)
                .student(student)
                .subject(subject)
                .professor(professor)
                .grade(8.5)
                .build();

        classroomDto = ClassroomDto.builder()
                .grade(8.5)
                .build();
    }

    // ==========================================
    // Tests for addClassroom
    // ==========================================

    @Test
    public void addClassroom_Success() {
        // Arrange
        when(studentRepository.findByEmail("student@college.edu")).thenReturn(Optional.of(student));
        when(subjectRepository.findByInternCode("PROG-1")).thenReturn(Optional.of(subject));
        when(professorRepository.findByEmail("prof@college.edu")).thenReturn(Optional.of(professor));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        // Act
        Classroom result = classroomService.addClassroom("student@college.edu", "prof@college.edu", "PROG-1", 8.5);

        // Assert
        assertNotNull(result);
        assertEquals(8.5, result.getGrade());
        verify(studentRepository).findByEmail("student@college.edu");
        verify(subjectRepository).findByInternCode("PROG-1");
        verify(professorRepository).findByEmail("prof@college.edu");
        verify(classroomRepository).save(any(Classroom.class));
    }

    @Test
    public void addClassroom_ThrowsBadRequestException_WhenFieldsNullOrEmpty() {
        assertThrows(BadRequestException.class, () -> 
                classroomService.addClassroom("", "prof@college.edu", "PROG-1", 8.5)
        );
        assertThrows(BadRequestException.class, () -> 
                classroomService.addClassroom("student@college.edu", "prof@college.edu", null, 8.5)
        );
        assertThrows(BadRequestException.class, () -> 
                classroomService.addClassroom("student@college.edu", "  ", "PROG-1", 8.5)
        );
    }

    @Test
    public void addClassroom_ThrowsResourceNotFoundException_WhenStudentNotFound() {
        when(studentRepository.findByEmail("nonexistent@college.edu")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
                classroomService.addClassroom("nonexistent@college.edu", "prof@college.edu", "PROG-1", 8.5)
        );
        verify(studentRepository).findByEmail("nonexistent@college.edu");
        verifyNoInteractions(subjectRepository, professorRepository, classroomRepository);
    }

    @Test
    public void addClassroom_ThrowsResourceNotFoundException_WhenSubjectNotFound() {
        when(studentRepository.findByEmail("student@college.edu")).thenReturn(Optional.of(student));
        when(subjectRepository.findByInternCode("INVALID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
                classroomService.addClassroom("student@college.edu", "prof@college.edu", "INVALID", 8.5)
        );
        verify(subjectRepository).findByInternCode("INVALID");
        verifyNoInteractions(professorRepository, classroomRepository);
    }

    @Test
    public void addClassroom_ThrowsResourceNotFoundException_WhenProfessorNotFound() {
        when(studentRepository.findByEmail("student@college.edu")).thenReturn(Optional.of(student));
        when(subjectRepository.findByInternCode("PROG-1")).thenReturn(Optional.of(subject));
        when(professorRepository.findByEmail("nonexistent@college.edu")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
                classroomService.addClassroom("student@college.edu", "nonexistent@college.edu", "PROG-1", 8.5)
        );
        verify(professorRepository).findByEmail("nonexistent@college.edu");
        verifyNoInteractions(classroomRepository);
    }

    // ==========================================
    // Tests for other service methods
    // ==========================================

    @Test
    public void findAllClassrooms_Success() {
        when(classroomRepository.findAll()).thenReturn(List.of(classroom));
        when(classroomMapper.toDto(classroom)).thenReturn(classroomDto);

        List<ClassroomDto> result = classroomService.findAllClassrooms();
        assertEquals(1, result.size());
    }

    @Test
    public void findById_Success() {
        when(classroomRepository.findById(4L)).thenReturn(Optional.of(classroom));
        when(classroomMapper.toDto(classroom)).thenReturn(classroomDto);

        ClassroomDto result = classroomService.findById(4L);
        assertNotNull(result);
        assertEquals(8.5, result.getGrade());
    }

    @Test
    public void findById_NotFound() {
        when(classroomRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> classroomService.findById(99L));
    }

    @Test
    public void findByStudentEmail_Success() {
        when(classroomRepository.findByStudent_Email("student@college.edu")).thenReturn(List.of(classroom));
        when(classroomMapper.toDto(classroom)).thenReturn(classroomDto);

        List<ClassroomDto> result = classroomService.findByStudentEmail("student@college.edu");
        assertEquals(1, result.size());
    }

    @Test
    public void updateGrade_Success() {
        when(classroomRepository.findById(4L)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(classroom)).thenReturn(classroom);
        classroomDto.setGrade(9.0);
        when(classroomMapper.toDto(classroom)).thenReturn(classroomDto);

        ClassroomDto result = classroomService.updateGrade(4L, 9.0);
        assertNotNull(result);
        assertEquals(9.0, result.getGrade());
    }

    @Test
    public void deleteClassroom_Success() {
        when(classroomRepository.findById(4L)).thenReturn(Optional.of(classroom));
        doNothing().when(classroomRepository).delete(classroom);

        assertDoesNotThrow(() -> classroomService.deleteClassroom(4L));
        verify(classroomRepository).delete(classroom);
    }
}
