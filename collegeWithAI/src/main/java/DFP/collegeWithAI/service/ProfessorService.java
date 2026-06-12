package DFP.collegeWithAI.service;

import DFP.collegeWithAI.dto.response.ProfessorDto;
import DFP.collegeWithAI.model.Professor;
import DFP.collegeWithAI.model.Schedule;

import java.util.List;

public interface ProfessorService {
    Professor addProfessor(ProfessorDto professorDto);

    List<ProfessorDto> findAllProfessors();

    ProfessorDto findById(Long id);

    List<ProfessorDto> findByName(String name);

    List<ProfessorDto> findBySurname(String surname);

    ProfessorDto findByDni(String dni);

    ProfessorDto findByEmail(String email);

    ProfessorDto findByPhone(Integer phone);

    List<ProfessorDto> findBySchedule(Schedule schedule);

    List<ProfessorDto> findBySalary(Double salary);

    List<ProfessorDto> findByDepartmentName(String departmentName);

    ProfessorDto updateProfessor(Long id, ProfessorDto professorDto);

    void deleteProfessor(Long id);

    ProfessorDto assignStudentToProfessor(Long professorId, Long studentId);
    
    ProfessorDto removeStudentFromProfessor(Long professorId, Long studentId);
}
