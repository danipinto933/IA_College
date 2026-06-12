package DFP.collegeWithAI.service;

import DFP.collegeWithAI.dto.response.ClassroomDto;
import DFP.collegeWithAI.model.Classroom;

import java.util.List;

public interface ClassroomService {
    Classroom addClassroom(String studentEmail, String professorEmail, String subjectInternCode, Double grade);
    
    List<ClassroomDto> findAllClassrooms();
    
    ClassroomDto findById(Long id);
    
    List<ClassroomDto> findByStudentEmail(String email);
    
    List<ClassroomDto> findBySubjectInternCode(String internCode);
    
    List<ClassroomDto> findByProfessorEmail(String email);

    ClassroomDto updateGrade(Long id, Double grade);
    
    void deleteClassroom(Long id);
}
