package DFP.collegeWithAI.mapper;

import DFP.collegeWithAI.dto.response.ProfessorDto;
import DFP.collegeWithAI.model.Professor;
import DFP.collegeWithAI.model.Department;
import org.springframework.stereotype.Component;

@Component
public class ProfessorMapper {

    public ProfessorDto toDto(Professor professor) {
        if (professor == null)
            return null;

        return ProfessorDto.builder()
                .name(professor.getName())
                .surname(professor.getSurname())
                .dni(professor.getDni())
                .email(professor.getEmail())
                .phone(professor.getPhone())
                .schedule(professor.getSchedule())
                .salary(professor.getSalary())
                .departmentName(professor.getDepartment() != null ? professor.getDepartment().getDepartmentName() : null)
                .build();
    }

    public Professor toEntity(ProfessorDto professorDto) {
        if (professorDto == null)
            return null;

        return Professor.builder()
                .name(professorDto.getName())
                .surname(professorDto.getSurname())
                .dni(professorDto.getDni())
                .email(professorDto.getEmail())
                .phone(professorDto.getPhone())
                .schedule(professorDto.getSchedule())
                .salary(professorDto.getSalary())
                .department(professorDto.getDepartmentName() != null ? Department.builder().departmentName(professorDto.getDepartmentName()).build() : null)
                .build();
    }

    public void updateEntityFromDto(ProfessorDto professorDto, Professor professor) {
        if (professorDto.getName() != null)
            professor.setName(professorDto.getName());
        if (professorDto.getSurname() != null)
            professor.setSurname(professorDto.getSurname());
        if (professorDto.getDni() != null)
            professor.setDni(professorDto.getDni());
        if (professorDto.getEmail() != null)
            professor.setEmail(professorDto.getEmail());
        if (professorDto.getPhone() != null)
            professor.setPhone(professorDto.getPhone());
        if (professorDto.getSchedule() != null)
            professor.setSchedule(professorDto.getSchedule());
        if (professorDto.getSalary() != null)
            professor.setSalary(professorDto.getSalary());
        if (professorDto.getDepartmentName() != null)
            professor.setDepartment(Department.builder().departmentName(professorDto.getDepartmentName()).build());
    }
}
