package DFP.collegeWithAI.service;

import DFP.collegeWithAI.dto.response.DepartmentDto;
import DFP.collegeWithAI.exception.custom.BadRequestException;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.mapper.DepartmentMapper;
import DFP.collegeWithAI.model.Department;
import DFP.collegeWithAI.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentDto addDepartment(DepartmentDto departmentDto) {
        if (departmentDto == null) {
            throw new BadRequestException("Los datos del departmento no pueden ser nulos");
        }
        
        Department department = departmentMapper.toEntity(departmentDto);
        if (department == null) {
            throw new BadRequestException("Los datos del departmento no son válidos");
        }

        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDto(savedDepartment);
    }

    @Override
    public List<DepartmentDto> findAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(departmentMapper::toDto)
                .toList();
    }

    @Override
    public List<Department> findAllFull() {
        return departmentRepository.findAll();
    }

    @Override
    public DepartmentDto findById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departmento no encontrado con el ID: " + id));
        return departmentMapper.toDto(department);
    }

    @Override
    public DepartmentDto findByDepartmentName(String departmentName) {
        Department department = departmentRepository.findByDepartmentName(departmentName);
        if (department == null) {
            throw new ResourceNotFoundException("Departmento no encontrado con el nombre: " + departmentName);
        }
        return departmentMapper.toDto(department);
    }

    @Override
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto) {
        if (departmentDto == null) {
            throw new BadRequestException("Los datos para actualizar el departmento no pueden ser nulos");
        }

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departmento no encontrado con el ID: " + id));
        
        department.setDepartmentName(departmentDto.getDepartmentName());
        Department updatedDepartment = departmentRepository.save(department);
        
        return departmentMapper.toDto(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departmento no encontrado con el ID: " + id));
        departmentRepository.delete(department);
    }
}
