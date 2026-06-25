package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.DepartmentDto;
import DFP.collegeWithAI.exception.custom.BadRequestException;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.model.Department;
import DFP.collegeWithAI.service.DepartmentService;
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

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

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
    // Tests for addDepartment (POST)
    // ==========================================

    @Test
    public void addDepartment_Success() throws Exception {
        // Arrange
        when(departmentService.addDepartment(any(DepartmentDto.class))).thenReturn(departmentDto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.departmentName", is("Ciencia de la Computacion")));

        verify(departmentService).addDepartment(any(DepartmentDto.class));
    }

    @Test
    public void addDepartment_BadRequest_ValidationFailed() throws Exception {
        // Arrange
        DepartmentDto invalidDto = DepartmentDto.builder().departmentName("").build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Este campo no puede estar vacio")));

        verifyNoInteractions(departmentService);
    }

    @Test
    public void addDepartment_BadRequest_ServiceThrowsException() throws Exception {
        // Arrange
        when(departmentService.addDepartment(any(DepartmentDto.class)))
                .thenThrow(new BadRequestException("Los datos del departmento no son válidos"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Los datos del departmento no son válidos")));

        verify(departmentService).addDepartment(any(DepartmentDto.class));
    }

    // ==========================================
    // Tests for findAllDepartments (GET)
    // ==========================================

    @Test
    public void findAllDepartments_Success() throws Exception {
        // Arrange
        List<DepartmentDto> list = List.of(departmentDto);
        when(departmentService.findAllDepartments()).thenReturn(list);

        // Act & Assert
        mockMvc.perform(get("/api/v1/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].departmentName", is("Ciencia de la Computacion")));

        verify(departmentService).findAllDepartments();
    }

    // ==========================================
    // Tests for findAllFull (GET)
    // ==========================================

    @Test
    public void findAllFull_Success() throws Exception {
        // Arrange
        List<Department> list = List.of(department);
        when(departmentService.findAllFull()).thenReturn(list);

        // Act & Assert
        mockMvc.perform(get("/api/v1/departments/full"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].departmentName", is("Ciencia de la Computacion")));

        verify(departmentService).findAllFull();
    }

    // ==========================================
    // Tests for findById (GET)
    // ==========================================

    @Test
    public void findById_Success() throws Exception {
        // Arrange
        when(departmentService.findById(1L)).thenReturn(departmentDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentName", is("Ciencia de la Computacion")));

        verify(departmentService).findById(1L);
    }

    @Test
    public void findById_NotFound() throws Exception {
        // Arrange
        when(departmentService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Departmento no encontrado con el ID: 99"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/departments/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Departmento no encontrado con el ID: 99")));

        verify(departmentService).findById(99L);
    }

    // ==========================================
    // Tests for findByDepartmentName (GET)
    // ==========================================

    @Test
    public void findByDepartmentName_Success() throws Exception {
        // Arrange
        String name = "Ciencia de la Computacion";
        when(departmentService.findByDepartmentName(name)).thenReturn(departmentDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/departments/name/" + name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentName", is("Ciencia de la Computacion")));

        verify(departmentService).findByDepartmentName(name);
    }

    @Test
    public void findByDepartmentName_NotFound() throws Exception {
        // Arrange
        String name = "Inexistente";
        when(departmentService.findByDepartmentName(name))
                .thenThrow(new ResourceNotFoundException("Departmento no encontrado con el nombre: Inexistente"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/departments/name/" + name))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Departmento no encontrado con el nombre: Inexistente")));

        verify(departmentService).findByDepartmentName(name);
    }

    // ==========================================
    // Tests for updateDepartment (PUT)
    // ==========================================

    @Test
    public void updateDepartment_Success() throws Exception {
        // Arrange
        DepartmentDto updateDto = DepartmentDto.builder().departmentName("Matematicas").build();
        when(departmentService.updateDepartment(eq(1L), any(DepartmentDto.class))).thenReturn(updateDto);

        // Act & Assert
        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentName", is("Matematicas")));

        verify(departmentService).updateDepartment(eq(1L), any(DepartmentDto.class));
    }

    @Test
    public void updateDepartment_BadRequest_ValidationFailed() throws Exception {
        // Arrange
        DepartmentDto invalidDto = DepartmentDto.builder().departmentName("").build();

        // Act & Assert
        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Este campo no puede estar vacio")));

        verifyNoInteractions(departmentService);
    }

    @Test
    public void updateDepartment_NotFound() throws Exception {
        // Arrange
        DepartmentDto updateDto = DepartmentDto.builder().departmentName("Matematicas").build();
        when(departmentService.updateDepartment(eq(99L), any(DepartmentDto.class)))
                .thenThrow(new ResourceNotFoundException("Departmento no encontrado con el ID: 99"));

        // Act & Assert
        mockMvc.perform(put("/api/v1/departments/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Departmento no encontrado con el ID: 99")));

        verify(departmentService).updateDepartment(eq(99L), any(DepartmentDto.class));
    }

    // ==========================================
    // Tests for deleteDepartment (DELETE)
    // ==========================================

    @Test
    public void deleteDepartment_Success() throws Exception {
        // Arrange
        doNothing().when(departmentService).deleteDepartment(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/departments/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Departmento eliminado exitosamente"));

        verify(departmentService).deleteDepartment(1L);
    }

    @Test
    public void deleteDepartment_NotFound() throws Exception {
        // Arrange
        doThrow(new ResourceNotFoundException("Departmento no encontrado con el ID: 99"))
                .when(departmentService).deleteDepartment(99L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/departments/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Departmento no encontrado con el ID: 99")));

        verify(departmentService).deleteDepartment(99L);
    }
}
