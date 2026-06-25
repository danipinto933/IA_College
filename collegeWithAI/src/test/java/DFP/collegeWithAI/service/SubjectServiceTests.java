package DFP.collegeWithAI.service;

import DFP.collegeWithAI.dto.response.SubjectDto;
import DFP.collegeWithAI.exception.custom.BadRequestException;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.mapper.SubjectMapper;
import DFP.collegeWithAI.model.Department;
import DFP.collegeWithAI.model.Subject;
import DFP.collegeWithAI.repository.DepartmentRepository;
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
public class SubjectServiceTests {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private SubjectMapper subjectMapper;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    private Department department;
    private Subject subject;
    private SubjectDto subjectDto;

    @BeforeEach
    public void setUp() {
        department = Department.builder()
                .id(1L)
                .departmentName("Ciencia de la Computacion")
                .build();

        subject = Subject.builder()
                .id(1L)
                .name("Programacion Avanzada")
                .internCode("PROG-102")
                .description("Curso avanzado de programacion orientada a objetos")
                .isElective(false)
                .totalHours(64)
                .department(department)
                .build();

        subjectDto = SubjectDto.builder()
                .name("Programacion Avanzada")
                .internCode("PROG-102")
                .description("Curso avanzado de programacion orientada a objetos")
                .isElective(false)
                .totalHours(64)
                .departmentName("Ciencia de la Computacion")
                .build();
    }

    // ==========================================
    // Tests for addSubject
    // ==========================================

    @Test
    public void addSubject_Success() {
        // Arrange
        when(subjectMapper.toEntity(subjectDto)).thenReturn(subject);
        when(departmentRepository.findByDepartmentName("Ciencia de la Computacion")).thenReturn(department);
        when(subjectRepository.save(subject)).thenReturn(subject);
        when(subjectMapper.toDto(subject)).thenReturn(subjectDto);

        // Act
        SubjectDto result = subjectService.addSubject(subjectDto);

        // Assert
        assertNotNull(result);
        assertEquals(subjectDto.getName(), result.getName());
        assertEquals(subjectDto.getInternCode(), result.getInternCode());
        verify(subjectMapper).toEntity(subjectDto);
        verify(departmentRepository).findByDepartmentName("Ciencia de la Computacion");
        verify(subjectRepository).save(subject);
        verify(subjectMapper).toDto(subject);
    }

    @Test
    public void addSubject_ThrowsBadRequestException_WhenDtoIsNull() {
        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> 
                subjectService.addSubject(null)
        );
        assertEquals("Los datos de la asignatura no pueden ser nulos", exception.getMessage());
        verifyNoInteractions(subjectMapper, departmentRepository, subjectRepository);
    }

    @Test
    public void addSubject_ThrowsBadRequestException_WhenMapperReturnsNull() {
        // Arrange
        when(subjectMapper.toEntity(subjectDto)).thenReturn(null);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> 
                subjectService.addSubject(subjectDto)
        );
        assertEquals("Materia no válida", exception.getMessage());
        verify(subjectMapper).toEntity(subjectDto);
        verifyNoInteractions(departmentRepository, subjectRepository);
    }

    @Test
    public void addSubject_ThrowsResourceNotFoundException_WhenDepartmentNotFound() {
        // Arrange
        when(subjectMapper.toEntity(subjectDto)).thenReturn(subject);
        when(departmentRepository.findByDepartmentName("Ciencia de la Computacion")).thenReturn(null);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> 
                subjectService.addSubject(subjectDto)
        );
        assertEquals("Departmento no encontrado: Ciencia de la Computacion", exception.getMessage());
        verify(subjectMapper).toEntity(subjectDto);
        verify(departmentRepository).findByDepartmentName("Ciencia de la Computacion");
        verifyNoInteractions(subjectRepository);
    }

    // ==========================================
    // Tests for findAllSubjects
    // ==========================================

    @Test
    public void findAllSubjects_Success() {
        // Arrange
        List<Subject> list = List.of(subject);
        when(subjectRepository.findAll()).thenReturn(list);
        when(subjectMapper.toDto(subject)).thenReturn(subjectDto);

        // Act
        List<SubjectDto> result = subjectService.findAllSubjects();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(subjectDto.getName(), result.get(0).getName());
        verify(subjectRepository).findAll();
        verify(subjectMapper).toDto(subject);
    }

    @Test
    public void findAllSubjects_EmptyList() {
        // Arrange
        when(subjectRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<SubjectDto> result = subjectService.findAllSubjects();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(subjectRepository).findAll();
        verifyNoInteractions(subjectMapper);
    }

    // ==========================================
    // Tests for findAllFull
    // ==========================================

    @Test
    public void findAllFull_Success() {
        // Arrange
        List<Subject> list = List.of(subject);
        when(subjectRepository.findAll()).thenReturn(list);

        // Act
        List<Subject> result = subjectService.findAllFull();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(subject.getName(), result.get(0).getName());
        verify(subjectRepository).findAll();
    }

    // ==========================================
    // Tests for findById
    // ==========================================

    @Test
    public void findById_Success() {
        // Arrange
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(subjectMapper.toDto(subject)).thenReturn(subjectDto);

        // Act
        SubjectDto result = subjectService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(subjectDto.getName(), result.getName());
        verify(subjectRepository).findById(1L);
    }

    @Test
    public void findById_NotFound() {
        // Arrange
        when(subjectRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> 
                subjectService.findById(99L)
        );
        assertEquals("Materia no encontrada con el ID: 99", exception.getMessage());
        verify(subjectRepository).findById(99L);
        verifyNoInteractions(subjectMapper);
    }

    // ==========================================
    // Tests for findByName
    // ==========================================

    @Test
    public void findByName_Success() {
        // Arrange
        when(subjectRepository.findByName("Programacion Avanzada")).thenReturn(Optional.of(subject));
        when(subjectMapper.toDto(subject)).thenReturn(subjectDto);

        // Act
        SubjectDto result = subjectService.findByName("Programacion Avanzada");

        // Assert
        assertNotNull(result);
        assertEquals(subjectDto.getName(), result.getName());
        verify(subjectRepository).findByName("Programacion Avanzada");
    }

    @Test
    public void findByName_NotFound() {
        // Arrange
        when(subjectRepository.findByName("Inexistente")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> 
                subjectService.findByName("Inexistente")
        );
        assertEquals("Materia no encontrada con el nombre: Inexistente", exception.getMessage());
        verify(subjectRepository).findByName("Inexistente");
    }

    // ==========================================
    // Tests for findByInternCode
    // ==========================================

    @Test
    public void findByInternCode_Success() {
        // Arrange
        when(subjectRepository.findByInternCode("PROG-102")).thenReturn(Optional.of(subject));
        when(subjectMapper.toDto(subject)).thenReturn(subjectDto);

        // Act
        SubjectDto result = subjectService.findByInternCode("PROG-102");

        // Assert
        assertNotNull(result);
        assertEquals(subjectDto.getInternCode(), result.getInternCode());
        verify(subjectRepository).findByInternCode("PROG-102");
    }

    @Test
    public void findByInternCode_NotFound() {
        // Arrange
        when(subjectRepository.findByInternCode("ERR-404")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> 
                subjectService.findByInternCode("ERR-404")
        );
        assertEquals("Materia no encontrada con el código interno: ERR-404", exception.getMessage());
        verify(subjectRepository).findByInternCode("ERR-404");
    }

    // ==========================================
    // Tests for findByIsElective
    // ==========================================

    @Test
    public void findByIsElective_Success() {
        // Arrange
        List<Subject> list = List.of(subject);
        when(subjectRepository.findByIsElective(false)).thenReturn(list);
        when(subjectMapper.toDto(subject)).thenReturn(subjectDto);

        // Act
        List<SubjectDto> result = subjectService.findByIsElective(false);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subjectRepository).findByIsElective(false);
    }

    // ==========================================
    // Tests for findByTotalHours
    // ==========================================

    @Test
    public void findByTotalHours_Success() {
        // Arrange
        List<Subject> list = List.of(subject);
        when(subjectRepository.findByTotalHoursLessThanEqual(80)).thenReturn(list);
        when(subjectMapper.toDto(subject)).thenReturn(subjectDto);

        // Act
        List<SubjectDto> result = subjectService.findByTotalHours(80);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subjectRepository).findByTotalHoursLessThanEqual(80);
    }

    // ==========================================
    // Tests for findByDepartmentName
    // ==========================================

    @Test
    public void findByDepartmentName_Success() {
        // Arrange
        List<Subject> list = List.of(subject);
        when(subjectRepository.findByDepartment_DepartmentName("Ciencia de la Computacion")).thenReturn(list);
        when(subjectMapper.toDto(subject)).thenReturn(subjectDto);

        // Act
        List<SubjectDto> result = subjectService.findByDepartmentName("Ciencia de la Computacion");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subjectRepository).findByDepartment_DepartmentName("Ciencia de la Computacion");
    }

    // ==========================================
    // Tests for updateSubject
    // ==========================================

    @Test
    public void updateSubject_Success() {
        // Arrange
        SubjectDto updateDto = SubjectDto.builder()
                .name("Programacion Avanzada II")
                .internCode("PROG-102")
                .description("Objetos y patrones")
                .isElective(true)
                .totalHours(72)
                .departmentName("Ciencia de la Computacion")
                .build();

        Subject updatedSubject = Subject.builder()
                .id(1L)
                .name("Programacion Avanzada II")
                .internCode("PROG-102")
                .description("Objetos y patrones")
                .isElective(true)
                .totalHours(72)
                .department(department)
                .build();

        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        doNothing().when(subjectMapper).updateEntityFromDto(updateDto, subject);
        when(departmentRepository.findByDepartmentName("Ciencia de la Computacion")).thenReturn(department);
        when(subjectRepository.save(subject)).thenReturn(updatedSubject);
        when(subjectMapper.toDto(updatedSubject)).thenReturn(updateDto);

        // Act
        SubjectDto result = subjectService.updateSubject(1L, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("Programacion Avanzada II", result.getName());
        assertTrue(result.getIsElective());
        assertEquals(72, result.getTotalHours());
        verify(subjectRepository).findById(1L);
        verify(subjectMapper).updateEntityFromDto(updateDto, subject);
        verify(departmentRepository).findByDepartmentName("Ciencia de la Computacion");
        verify(subjectRepository).save(subject);
    }

    @Test
    public void updateSubject_ThrowsBadRequestException_WhenDtoIsNull() {
        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> 
                subjectService.updateSubject(1L, null)
        );
        assertEquals("Los datos para actualizar la asignatura no pueden ser nulos", exception.getMessage());
        verifyNoInteractions(subjectRepository, departmentRepository, subjectMapper);
    }

    @Test
    public void updateSubject_ThrowsResourceNotFoundException_WhenSubjectNotFound() {
        // Arrange
        when(subjectRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> 
                subjectService.updateSubject(99L, subjectDto)
        );
        assertEquals("Materia no encontrada con el ID: 99", exception.getMessage());
        verify(subjectRepository).findById(99L);
        verifyNoMoreInteractions(subjectRepository);
        verifyNoInteractions(departmentRepository, subjectMapper);
    }

    @Test
    public void updateSubject_ThrowsResourceNotFoundException_WhenDepartmentNotFound() {
        // Arrange
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        doNothing().when(subjectMapper).updateEntityFromDto(subjectDto, subject);
        when(departmentRepository.findByDepartmentName("Ciencia de la Computacion")).thenReturn(null);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> 
                subjectService.updateSubject(1L, subjectDto)
        );
        assertEquals("Departmento no encontrado: Ciencia de la Computacion", exception.getMessage());
        verify(subjectRepository).findById(1L);
        verify(subjectMapper).updateEntityFromDto(subjectDto, subject);
        verify(departmentRepository).findByDepartmentName("Ciencia de la Computacion");
        verifyNoMoreInteractions(subjectRepository);
    }

    // ==========================================
    // Tests for deleteSubject
    // ==========================================

    @Test
    public void deleteSubject_Success() {
        // Arrange
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        doNothing().when(subjectRepository).delete(subject);

        // Act
        assertDoesNotThrow(() -> subjectService.deleteSubject(1L));

        // Assert
        verify(subjectRepository).findById(1L);
        verify(subjectRepository).delete(subject);
    }

    @Test
    public void deleteSubject_ThrowsResourceNotFoundException_WhenNotFound() {
        // Arrange
        when(subjectRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> 
                subjectService.deleteSubject(99L)
        );
        assertEquals("Materia no encontrada con el ID: 99", exception.getMessage());
        verify(subjectRepository).findById(99L);
        verifyNoMoreInteractions(subjectRepository);
    }
}
