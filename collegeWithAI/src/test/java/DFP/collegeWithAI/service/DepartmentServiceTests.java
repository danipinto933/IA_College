package DFP.collegeWithAI.service;

import DFP.collegeWithAI.dto.response.DepartmentDto;
import DFP.collegeWithAI.exception.custom.BadRequestException;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.mapper.DepartmentMapper;
import DFP.collegeWithAI.model.Department;
import DFP.collegeWithAI.repository.DepartmentRepository;
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
public class DepartmentServiceTests {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;
    private DepartmentDto departmentDto;

    @BeforeEach
    public void setUp() {
        department = Department.builder()
                .id(1L)
                .departmentName("Ciencia de la Computacion")
                .build();

        departmentDto = DepartmentDto.builder()
                .departmentName("Ciencia de la Computacion")
                .build();
    }

    // ==========================================
    // Tests for addDepartment
    // ==========================================

    @Test
    public void addDepartment_Success() {
        // Arrange
        when(departmentMapper.toEntity(departmentDto)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);
        when(departmentMapper.toDto(department)).thenReturn(departmentDto);

        // Act
        DepartmentDto result = departmentService.addDepartment(departmentDto);

        // Assert
        assertNotNull(result);
        assertEquals(departmentDto.getDepartmentName(), result.getDepartmentName());
        verify(departmentMapper).toEntity(departmentDto);
        verify(departmentRepository).save(department);
        verify(departmentMapper).toDto(department);
    }

    @Test
    public void addDepartment_ThrowsBadRequestException_WhenDtoIsNull() {
        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> 
                departmentService.addDepartment(null)
        );
        assertEquals("Los datos del departmento no pueden ser nulos", exception.getMessage());
        verifyNoInteractions(departmentMapper, departmentRepository);
    }

    @Test
    public void addDepartment_ThrowsBadRequestException_WhenMapperReturnsNull() {
        // Arrange
        when(departmentMapper.toEntity(departmentDto)).thenReturn(null);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> 
                departmentService.addDepartment(departmentDto)
        );
        assertEquals("Los datos del departmento no son válidos", exception.getMessage());
        verify(departmentMapper).toEntity(departmentDto);
        verifyNoInteractions(departmentRepository);
    }

    // ==========================================
    // Tests for findAllDepartments
    // ==========================================

    @Test
    public void findAllDepartments_Success() {
        // Arrange
        List<Department> departments = List.of(department);
        when(departmentRepository.findAll()).thenReturn(departments);
        when(departmentMapper.toDto(department)).thenReturn(departmentDto);

        // Act
        List<DepartmentDto> result = departmentService.findAllDepartments();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(departmentDto.getDepartmentName(), result.get(0).getDepartmentName());
        verify(departmentRepository).findAll();
        verify(departmentMapper).toDto(department);
    }

    @Test
    public void findAllDepartments_EmptyList() {
        // Arrange
        when(departmentRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<DepartmentDto> result = departmentService.findAllDepartments();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(departmentRepository).findAll();
        verifyNoInteractions(departmentMapper);
    }

    // ==========================================
    // Tests for findAllFull
    // ==========================================

    @Test
    public void findAllFull_Success() {
        // Arrange
        List<Department> departments = List.of(department);
        when(departmentRepository.findAll()).thenReturn(departments);

        // Act
        List<Department> result = departmentService.findAllFull();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(department.getDepartmentName(), result.get(0).getDepartmentName());
        verify(departmentRepository).findAll();
    }

    // ==========================================
    // Tests for findById
    // ==========================================

    @Test
    public void findById_Success() {
        // Arrange
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentMapper.toDto(department)).thenReturn(departmentDto);

        // Act
        DepartmentDto result = departmentService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(departmentDto.getDepartmentName(), result.getDepartmentName());
        verify(departmentRepository).findById(1L);
        verify(departmentMapper).toDto(department);
    }

    @Test
    public void findById_ThrowsResourceNotFoundException_WhenNotFound() {
        // Arrange
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> 
                departmentService.findById(99L)
        );
        assertEquals("Departmento no encontrado con el ID: 99", exception.getMessage());
        verify(departmentRepository).findById(99L);
        verifyNoInteractions(departmentMapper);
    }

    // ==========================================
    // Tests for findByDepartmentName
    // ==========================================

    @Test
    public void findByDepartmentName_Success() {
        // Arrange
        String name = "Ciencia de la Computacion";
        when(departmentRepository.findByDepartmentName(name)).thenReturn(department);
        when(departmentMapper.toDto(department)).thenReturn(departmentDto);

        // Act
        DepartmentDto result = departmentService.findByDepartmentName(name);

        // Assert
        assertNotNull(result);
        assertEquals(departmentDto.getDepartmentName(), result.getDepartmentName());
        verify(departmentRepository).findByDepartmentName(name);
        verify(departmentMapper).toDto(department);
    }

    @Test
    public void findByDepartmentName_ThrowsResourceNotFoundException_WhenNotFound() {
        // Arrange
        String name = "Inexistente";
        when(departmentRepository.findByDepartmentName(name)).thenReturn(null);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> 
                departmentService.findByDepartmentName(name)
        );
        assertEquals("Departmento no encontrado con el nombre: Inexistente", exception.getMessage());
        verify(departmentRepository).findByDepartmentName(name);
        verifyNoInteractions(departmentMapper);
    }

    // ==========================================
    // Tests for updateDepartment
    // ==========================================

    @Test
    public void updateDepartment_Success() {
        // Arrange
        DepartmentDto updateDto = DepartmentDto.builder().departmentName("Matematicas").build();
        Department updatedEntity = Department.builder().id(1L).departmentName("Matematicas").build();

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.save(department)).thenReturn(updatedEntity);
        when(departmentMapper.toDto(updatedEntity)).thenReturn(updateDto);

        // Act
        DepartmentDto result = departmentService.updateDepartment(1L, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("Matematicas", result.getDepartmentName());
        verify(departmentRepository).findById(1L);
        verify(departmentRepository).save(department);
        verify(departmentMapper).toDto(updatedEntity);
    }

    @Test
    public void updateDepartment_ThrowsBadRequestException_WhenDtoIsNull() {
        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> 
                departmentService.updateDepartment(1L, null)
        );
        assertEquals("Los datos para actualizar el departmento no pueden ser nulos", exception.getMessage());
        verifyNoInteractions(departmentRepository, departmentMapper);
    }

    @Test
    public void updateDepartment_ThrowsResourceNotFoundException_WhenNotFound() {
        // Arrange
        DepartmentDto updateDto = DepartmentDto.builder().departmentName("Matematicas").build();
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> 
                departmentService.updateDepartment(99L, updateDto)
        );
        assertEquals("Departmento no encontrado con el ID: 99", exception.getMessage());
        verify(departmentRepository).findById(99L);
        verifyNoMoreInteractions(departmentRepository);
        verifyNoInteractions(departmentMapper);
    }

    // ==========================================
    // Tests for deleteDepartment
    // ==========================================

    @Test
    public void deleteDepartment_Success() {
        // Arrange
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        doNothing().when(departmentRepository).delete(department);

        // Act
        assertDoesNotThrow(() -> departmentService.deleteDepartment(1L));

        // Assert
        verify(departmentRepository).findById(1L);
        verify(departmentRepository).delete(department);
    }

    @Test
    public void deleteDepartment_ThrowsResourceNotFoundException_WhenNotFound() {
        // Arrange
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> 
                departmentService.deleteDepartment(99L)
        );
        assertEquals("Departmento no encontrado con el ID: 99", exception.getMessage());
        verify(departmentRepository).findById(99L);
        verifyNoMoreInteractions(departmentRepository);
    }
}
