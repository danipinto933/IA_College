package DFP.collegeWithAI.service;

import DFP.collegeWithAI.dto.response.StudentDto;
import DFP.collegeWithAI.dto.response.SubjectDto;
import DFP.collegeWithAI.exception.custom.BadRequestException;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.mapper.StudentMapper;
import DFP.collegeWithAI.model.Schedule;
import DFP.collegeWithAI.model.Student;
import DFP.collegeWithAI.model.Subject;
import DFP.collegeWithAI.repository.StudentRepository;
import DFP.collegeWithAI.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTests {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private StudentDto studentDto;
    private Subject subject;
    private SubjectDto subjectDto;

    @BeforeEach
    public void setUp() {
        subject = Subject.builder()
                .id(1L)
                .name("Algebra Lineal")
                .internCode("MAT-101")
                .build();

        subjectDto = SubjectDto.builder()
                .name("Algebra Lineal")
                .internCode("MAT-101")
                .build();

        student = Student.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .dni("11111111A")
                .email("john.doe@student.edu")
                .phone(611111111)
                .schedule(Schedule.MORNING)
                .dateJoining(new Date())
                .subjects(new ArrayList<>(List.of(subject)))
                .build();

        studentDto = StudentDto.builder()
                .name("John")
                .surname("Doe")
                .dni("11111111A")
                .email("john.doe@student.edu")
                .phone(611111111)
                .schedule(Schedule.MORNING)
                .dateJoining(new Date())
                .subjects(new ArrayList<>(List.of(subjectDto)))
                .build();
    }

    // ==========================================
    // Tests for addStudent
    // ==========================================

    @Test
    public void addStudent_Success() {
        // Arrange
        when(studentMapper.toEntity(studentDto)).thenReturn(student);
        when(subjectRepository.findByName("Algebra Lineal")).thenReturn(Optional.of(subject));
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        // Act
        StudentDto result = studentService.addStudent(studentDto);

        // Assert
        assertNotNull(result);
        assertEquals(studentDto.getName(), result.getName());
        verify(studentMapper).toEntity(studentDto);
        verify(subjectRepository).findByName("Algebra Lineal");
        verify(studentRepository).save(student);
        verify(studentMapper).toDto(student);
    }

    @Test
    public void addStudent_ThrowsBadRequestException_WhenDtoIsNull() {
        assertThrows(BadRequestException.class, () -> studentService.addStudent(null));
        verifyNoInteractions(studentMapper, subjectRepository, studentRepository);
    }

    @Test
    public void addStudent_ThrowsBadRequestException_WhenMapperReturnsNull() {
        when(studentMapper.toEntity(studentDto)).thenReturn(null);

        assertThrows(BadRequestException.class, () -> studentService.addStudent(studentDto));
        verifyNoInteractions(subjectRepository, studentRepository);
    }

    @Test
    public void addStudent_ThrowsResourceNotFoundException_WhenSubjectNotFound() {
        when(studentMapper.toEntity(studentDto)).thenReturn(student);
        when(subjectRepository.findByName("Algebra Lineal")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.addStudent(studentDto));
        verify(subjectRepository).findByName("Algebra Lineal");
        verifyNoInteractions(studentRepository);
    }

    // ==========================================
    // Tests for findById & searches
    // ==========================================

    @Test
    public void findById_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        StudentDto result = studentService.findById(1L);
        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    public void findById_NotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.findById(99L));
    }

    @Test
    public void findByName_Success() {
        when(studentRepository.findByName("John")).thenReturn(List.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        List<StudentDto> result = studentService.findByName("John");
        assertEquals(1, result.size());
    }

    @Test
    public void findBySurname_Success() {
        when(studentRepository.findBySurname("Doe")).thenReturn(List.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        List<StudentDto> result = studentService.findBySurname("Doe");
        assertEquals(1, result.size());
    }

    @Test
    public void findByDni_Success() {
        when(studentRepository.findByDni("11111111A")).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        StudentDto result = studentService.findByDni("11111111A");
        assertNotNull(result);
    }

    @Test
    public void findByEmail_Success() {
        when(studentRepository.findByEmail("john.doe@student.edu")).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        StudentDto result = studentService.findByEmail("john.doe@student.edu");
        assertNotNull(result);
    }

    @Test
    public void findByPhone_Success() {
        when(studentRepository.findByPhone(611111111)).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        StudentDto result = studentService.findByPhone(611111111);
        assertNotNull(result);
    }

    @Test
    public void findBySchedule_Success() {
        when(studentRepository.findBySchedule(Schedule.MORNING)).thenReturn(List.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        List<StudentDto> result = studentService.findBySchedule(Schedule.MORNING);
        assertEquals(1, result.size());
    }

    @Test
    public void findByDateJoining_Success() {
        Date date = new Date();
        when(studentRepository.findByDateJoiningLessThanEqual(date)).thenReturn(List.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        List<StudentDto> result = studentService.findByDateJoining(date);
        assertEquals(1, result.size());
    }

    // ==========================================
    // Tests for updateStudent
    // ==========================================

    @Test
    public void updateStudent_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        doNothing().when(studentMapper).updateEntityFromDto(studentDto, student);
        when(subjectRepository.findByName("Algebra Lineal")).thenReturn(Optional.of(subject));
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        StudentDto result = studentService.updateStudent(1L, studentDto);
        assertNotNull(result);
        verify(studentRepository).save(student);
    }

    @Test
    public void updateStudent_ThrowsBadRequestException_WhenDtoIsNull() {
        assertThrows(BadRequestException.class, () -> studentService.updateStudent(1L, null));
    }

    @Test
    public void updateStudent_ThrowsResourceNotFoundException_WhenStudentNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.updateStudent(99L, studentDto));
    }

    // ==========================================
    // Tests for deleteStudent
    // ==========================================

    @Test
    public void deleteStudent_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).delete(student);

        assertDoesNotThrow(() -> studentService.deleteStudent(1L));
        verify(studentRepository).delete(student);
    }

    @Test
    public void deleteStudent_ThrowsResourceNotFoundException_WhenNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(99L));
    }
}
