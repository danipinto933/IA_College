package DFP.collegeWithAI.mapper;

import DFP.collegeWithAI.dto.response.SubjectDto;
import DFP.collegeWithAI.model.Subject;
import DFP.collegeWithAI.model.Department;
import org.springframework.stereotype.Component;

@Component
public class SubjectMapper {

    public SubjectDto toDto(Subject subject) {
        if (subject == null)
            return null;

        return SubjectDto.builder()
                .name(subject.getName())
                .internCode(subject.getInternCode())
                .description(subject.getDescription())
                .isElective(subject.getIsElective())
                .totalHours(subject.getTotalHours())
                .departmentName(subject.getDepartment() != null ? subject.getDepartment().getDepartmentName() : null)
                .build();
    }

    public Subject toEntity(SubjectDto subjectDto) {
        if (subjectDto == null)
            return null;

        return Subject.builder()
                .name(subjectDto.getName())
                .internCode(subjectDto.getInternCode())
                .description(subjectDto.getDescription())
                .isElective(subjectDto.getIsElective())
                .totalHours(subjectDto.getTotalHours())
                .department(subjectDto.getDepartmentName() != null ? Department.builder().departmentName(subjectDto.getDepartmentName()).build() : null)
                .build();
    }

    public void updateEntityFromDto(SubjectDto subjectDto, Subject subject) {
        if (subjectDto.getName() != null)
            subject.setName(subjectDto.getName());
        if (subjectDto.getInternCode() != null)
            subject.setInternCode(subjectDto.getInternCode());
        if (subjectDto.getDescription() != null)
            subject.setDescription(subjectDto.getDescription());
        if (subjectDto.getIsElective() != null)
            subject.setIsElective(subjectDto.getIsElective());
        if (subjectDto.getTotalHours() != null)
            subject.setTotalHours(subjectDto.getTotalHours());
        if (subjectDto.getDepartmentName() != null)
            subject.setDepartment(Department.builder().departmentName(subjectDto.getDepartmentName()).build());
    }
}
