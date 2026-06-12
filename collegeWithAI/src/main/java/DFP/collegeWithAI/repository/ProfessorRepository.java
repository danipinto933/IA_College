package DFP.collegeWithAI.repository;

import DFP.collegeWithAI.model.Professor;
import DFP.collegeWithAI.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    List<Professor> findByName(String name);
    List<Professor> findBySurname(String surname);
    Optional<Professor> findByDni(String dni);
    Optional<Professor> findByEmail(String email);
    Optional<Professor> findByPhone(Integer phone);
    List<Professor> findBySchedule(Schedule schedule);
    List<Professor> findBySalary(Double salary);
    List<Professor> findByDepartment_DepartmentName(String departmentName);
}
