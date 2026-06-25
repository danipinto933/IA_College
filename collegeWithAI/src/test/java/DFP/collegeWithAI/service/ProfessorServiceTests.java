package DFP.collegeWithAI.service;

import DFP.collegeWithAI.dto.response.ProfessorDto;
import DFP.collegeWithAI.exception.custom.BadRequestException;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.mapper.ProfessorMapper;
import DFP.collegeWithAI.model.Department;
import DFP.collegeWithAI.model.Professor;
import DFP.collegeWithAI.model.Schedule;
import DFP.collegeWithAI.model.Student;
import DFP.collegeWithAI.repository.DepartmentRepository;
import DFP.collegeWithAI.repository.ProfessorRepository;
import DFP.collegeWithAI.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfessorServiceTests {

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ProfessorMapper professorMapper;

    @InjectMocks
    private ProfessorServiceImpl professorService;

    private Department department;
    private Professor professor;
    private ProfessorDto professorDto;
    private Student student;

    @BeforeEach
    public void setUp() {
        department = Department.builder()
                .id(1L)
                .departmentName("Ciencia de la Computacion")
                .build();

        professor = Professor.builder()
                .id(1L)
                .name("Alan")
                .surname("Turing")
                .dni("12345678A")
                .email("alan.turing@college.edu")
                .phone(600123456)
                .schedule(Schedule.MORNING)
                .salary(3500.0)
                .department(department)
                .students(new ArrayList<>())
                .build();

        professorDto = ProfessorDto.builder()
                .name("Alan")
                .surname("Turing")
                .dni("12345678A")
                .email("alan.turing@college.edu")
                .phone(600123456)
                .schedule(Schedule.MORNING)
                .salary(3500.0)
                .departmentName("Ciencia de la Computacion")
                .build();

        student = Student.builder()
                .id(5L)
                .dni("87654321B")
                .name("Ada")
                .surname("Lovelace")
                .build();
    }

    // ==========================================
    // Tests for addProfessor
    // ==========================================

    @Test
    public void addProfessor_Success() {
        // Arrange
        when(professorMapper.toEntity(professorDto)).thenReturn(professor);
        when(departmentRepository.findByDepartmentName("Ciencia de la Computacion")).thenReturn(department);
        when(professorRepository.save(professor)).thenReturn(professor);

        // Act
        Professor result = professorService.addProfessor(professorDto);

        // Assert
        assertNotNull(result);
        assertEquals(professor.getName(), result.getName());
        verify(professorMapper).toEntity(professorDto);
        verify(departmentRepository).findByDepartmentName("Ciencia de la Computacion");
        verify(professorRepository).save(professor);
    }

    @Test
    public void addProfessor_ThrowsBadRequestException_WhenDtoIsNull() {
        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> 
                professorService.addProfessor(null)
        );
        assertEquals("Los datos del profesor no pueden ser nulos", exception.getMessage());
        verifyNoInteractions(professorMapper, departmentRepository, professorRepository);
    }

    @Test
    public void addProfessor_ThrowsBadRequestException_WhenMapperReturnsNull() {
        // Arrange
        when(professorMapper.toEntity(professorDto)).thenReturn(null);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> 
                professorService.addProfessor(professorDto)
        );
        assertEquals("Profesor no válido", exception.getMessage());
        verify(professorMapper).toEntity(professorDto);
        verifyNoInteractions(departmentRepository, professorRepository);
    }

    @Test
    public void addProfessor_ThrowsResourceNotFoundException_WhenDepartmentNotFound() {
        // Arrange
        when(professorMapper.toEntity(professorDto)).thenReturn(professor);
        when(departmentRepository.findByDepartmentName("Ciencia de la Computacion")).thenReturn(null);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> 
                professorService.addProfessor(professorDto)
        );
        assertEquals("Departmento no encontrado: Ciencia de la Computacion", exception.getMessage());
        verify(professorMapper).toEntity(professorDto);
        verify(departmentRepository).findByDepartmentName("Ciencia de la Computacion");
        verifyNoInteractions(professorRepository);
    }

    // ==========================================
    // Tests for findAllProfessors
    // ==========================================

    @Test
    public void findAllProfessors_Success() {
        // Arrange
        List<Professor> list = List.of(professor);
        when(professorRepository.findAll()).thenReturn(list);
        when(professorMapper.toDto(professor)).thenReturn(professorDto);

        // Act
        List<ProfessorDto> result = professorService.findAllProfessors();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(professorDto.getName(), result.get(0).getName());
        verify(professorRepository).findAll();
        verify(professorMapper).toDto(professor);
    }

    // ==========================================
    // Tests for findById
    // ==========================================

    @Test
    public void findById_Success() {
        // Arrange
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(professorMapper.toDto(professor)).thenReturn(professorDto);

        // Act
        ProfessorDto result = professorService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(professorDto.getName(), result.getName());
        verify(professorRepository).findById(1L);
    }

    @Test
    public void findById_NotFound() {
        // Arrange
        when(professorRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> 
                professorService.findById(99L)
        );
        assertEquals("Profesor no encontrado con el ID: 99", exception.getMessage());
        verify(professorRepository).findById(99L);
    }

    // ==========================================
    // Tests for search methods
    // ==========================================

    @Test
    public void findByName_Success() {
        when(professorRepository.findByName("Alan")).thenReturn(List.of(professor));
        when(professorMapper.toDto(professor)).thenReturn(professorDto);

        List<ProfessorDto> result = professorService.findByName("Alan");
        assertEquals(1, result.size());
        verify(professorRepository).findByName("Alan");
    }

    @Test
    public void findBySurname_Success() {
        when(professorRepository.findBySurname("Turing")).thenReturn(List.of(professor));
        when(professorMapper.toDto(professor)).thenReturn(professorDto);

        List<ProfessorDto> result = professorService.findBySurname("Turing");
        assertEquals(1, result.size());
        verify(professorRepository).findBySurname("Turing");
    }

    @Test
    public void findByDni_Success() {
        when(professorRepository.findByDni("12345678A")).thenReturn(Optional.of(professor));
        when(professorMapper.toDto(professor)).thenReturn(professorDto);

        ProfessorDto result = professorService.findByDni("12345678A");
        assertNotNull(result);
        assertEquals("12345678A", result.getDni());
        verify(professorRepository).findByDni("12345678A");
    }

    @Test
    public void findByDni_NotFound() {
        when(professorRepository.findByDni("00000000X")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> professorService.findByDni("00000000X"));
    }

    @Test
    public void findByEmail_Success() {
        when(professorRepository.findByEmail("alan.turing@college.edu")).thenReturn(Optional.of(professor));
        when(professorMapper.toDto(professor)).thenReturn(professorDto);

        ProfessorDto result = professorService.findByEmail("alan.turing@college.edu");
        assertNotNull(result);
        assertEquals("alan.turing@college.edu", result.getEmail());
    }

    @Test
    public void findByPhone_Success() {
        when(professorRepository.findByPhone(600123456)).thenReturn(Optional.of(professor));
        when(professorMapper.toDto(professor)).thenReturn(professorDto);

        ProfessorDto result = professorService.findByPhone(600123456);
        assertNotNull(result);
        assertEquals(600123456, result.getPhone());
    }

    @Test
    public void findBySchedule_Success() {
        when(professorRepository.findBySchedule(Schedule.MORNING)).thenReturn(List.of(professor));
        when(professorMapper.toDto(professor)).thenReturn(professorDto);

        List<ProfessorDto> result = professorService.findBySchedule(Schedule.MORNING);
        assertEquals(1, result.size());
    }

    @Test
    public void findBySalary_Success() {
        when(professorRepository.findBySalary(3500.0)).thenReturn(List.of(professor));
        when(professorMapper.toDto(professor)).thenReturn(professorDto);

        List<ProfessorDto> result = professorService.findBySalary(3500.0);
        assertEquals(1, result.size());
    }

    @Test
    public void findByDepartmentName_Success() {
        when(professorRepository.findByDepartment_DepartmentName("Ciencia de la Computacion")).thenReturn(List.of(professor));
        when(professorMapper.toDto(professor)).thenReturn(professorDto);

        List<ProfessorDto> result = professorService.findByDepartmentName("Ciencia de la Computacion");
        assertEquals(1, result.size());
    }

    // ==========================================
    // Tests for updateProfessor
    // ==========================================

    @Test
    public void updateProfessor_Success() {
        // Arrange
        ProfessorDto updateDto = ProfessorDto.builder()
                .name("Alan Mathison")
                .surname("Turing")
                .dni("12345678A")
                .email("alan.turing@college.edu")
                .phone(600123456)
                .schedule(Schedule.MORNING)
                .salary(3800.0)
                .departmentName("Ciencia de la Computacion")
                .build();

        Professor updatedEntity = Professor.builder()
                .id(1L)
                .name("Alan Mathison")
                .surname("Turing")
                .dni("12345678A")
                .email("alan.turing@college.edu")
                .phone(600123456)
                .schedule(Schedule.MORNING)
                .salary(3800.0)
                .department(department)
                .build();

        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        doNothing().when(professorMapper).updateEntityFromDto(updateDto, professor);
        when(departmentRepository.findByDepartmentName("Ciencia de la Computacion")).thenReturn(department);
        when(professorRepository.save(professor)).thenReturn(updatedEntity);
        when(professorMapper.toDto(updatedEntity)).thenReturn(updateDto);

        // Act
        ProfessorDto result = professorService.updateProfessor(1L, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("Alan Mathison", result.getName());
        assertEquals(3800.0, result.getSalary());
        verify(professorRepository).findById(1L);
        verify(professorMapper).updateEntityFromDto(updateDto, professor);
        verify(departmentRepository).findByDepartmentName("Ciencia de la Computacion");
        verify(professorRepository).save(professor);
    }

    @Test
    public void updateProfessor_ThrowsBadRequestException_WhenDtoIsNull() {
        assertThrows(BadRequestException.class, () -> professorService.updateProfessor(1L, null));
    }

    @Test
    public void updateProfessor_ThrowsResourceNotFoundException_WhenNotFound() {
        when(professorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> professorService.updateProfessor(99L, professorDto));
    }

    // ==========================================
    // Tests for deleteProfessor
    // ==========================================

    @Test
    public void deleteProfessor_Success() {
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        doNothing().when(professorRepository).delete(professor);

        assertDoesNotThrow(() -> professorService.deleteProfessor(1L));
        verify(professorRepository).delete(professor);
    }

    @Test
    public void deleteProfessor_ThrowsResourceNotFoundException_WhenNotFound() {
        when(professorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> professorService.deleteProfessor(99L));
    }

    // ==========================================
    // Tests for assignStudentToProfessor
    // ==========================================

    @Test
    public void assignStudentToProfessor_Success() {
        // Arrange
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(studentRepository.findById(5L)).thenReturn(Optional.of(student));
        when(professorRepository.save(professor)).thenReturn(professor);
        when(professorMapper.toDto(professor)).thenReturn(professorDto);

        // Act
        ProfessorDto result = professorService.assignStudentToProfessor(1L, 5L);

        // Assert
        assertNotNull(result);
        assertTrue(professor.getStudents().contains(student));
        verify(professorRepository).findById(1L);
        verify(studentRepository).findById(5L);
        verify(professorRepository).save(professor);
    }

    @Test
    public void assignStudentToProfessor_ThrowsResourceNotFoundException_WhenProfessorNotFound() {
        when(professorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> professorService.assignStudentToProfessor(99L, 5L));
        verifyNoInteractions(studentRepository);
    }

    @Test
    public void assignStudentToProfessor_ThrowsResourceNotFoundException_WhenStudentNotFound() {
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> professorService.assignStudentToProfessor(1L, 99L));
        verifyNoMoreInteractions(professorRepository);
    }

    // ==========================================
    // Tests for removeStudentFromProfessor
    // ==========================================

    @Test
    public void removeStudentFromProfessor_Success() {
        // Arrange
        professor.getStudents().add(student);
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(studentRepository.findById(5L)).thenReturn(Optional.of(student));
        when(professorRepository.save(professor)).thenReturn(professor);
        when(professorMapper.toDto(professor)).thenReturn(professorDto);

        // Act
        ProfessorDto result = professorService.removeStudentFromProfessor(1L, 5L);

        // Assert
        assertNotNull(result);
        assertFalse(professor.getStudents().contains(student));
        verify(professorRepository).findById(1L);
        verify(studentRepository).findById(5L);
        verify(professorRepository).save(professor);
    }
}
