package DFP.collegeWithAI.mapper;

import DFP.collegeWithAI.dto.response.DepartmentDto;
import DFP.collegeWithAI.model.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper
{
    public DepartmentDto toDto (Department department)
    {
        if (department == null) return null;

        return DepartmentDto.builder()
                .departmentName(department.getDepartmentName())
                .build();
    }

    public Department toEntity (DepartmentDto departmentDto)
    {
        if (departmentDto == null) return null;

        return Department.builder()
                .departmentName(departmentDto.getDepartmentName())
                .build();
    }

    public void updateEntityFromDto (DepartmentDto departmentDto, Department department)
    {
        if (departmentDto.getDepartmentName() != null) department.setDepartmentName(departmentDto.getDepartmentName());
    }
}
