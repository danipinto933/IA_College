package DFP.collegeWithAI.service;

import DFP.collegeWithAI.dto.response.DepartmentDto;
import DFP.collegeWithAI.model.Department;
import java.util.List;

public interface DepartmentService {
    DepartmentDto addDepartment(DepartmentDto departmentDto);
    List<DepartmentDto> findAllDepartments();
    List<Department> findAllFull();
    DepartmentDto findById(Long id);
    DepartmentDto findByDepartmentName(String departmentName);
    DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto);
    void deleteDepartment(Long id);
}
