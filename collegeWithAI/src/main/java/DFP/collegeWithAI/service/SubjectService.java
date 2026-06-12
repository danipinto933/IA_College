package DFP.collegeWithAI.service;

import DFP.collegeWithAI.dto.response.SubjectDto;
import DFP.collegeWithAI.model.Subject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubjectService {
    SubjectDto addSubject(SubjectDto subjectDto);

    List<SubjectDto> findAllSubjects();
    List<Subject> findAllFull();

    SubjectDto findById(Long id);

    SubjectDto findByName(String name);

    SubjectDto findByInternCode(String internCode);

    List<SubjectDto> findByIsElective(Boolean isElective);

    List<SubjectDto> findByTotalHours(Integer totalHours);
    
    List<SubjectDto> findByDepartmentName(String departmentName);

    SubjectDto updateSubject(Long id, SubjectDto subjectDto);

    void deleteSubject(Long id);
}
