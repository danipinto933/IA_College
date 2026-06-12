package DFP.collegeWithAI.repository;

import DFP.collegeWithAI.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByName(String name);
    Optional<Subject> findByInternCode(String internCode);
    List<Subject> findByIsElective(Boolean isElective);
    List<Subject> findByTotalHoursLessThanEqual(Integer totalHours);
    List<Subject> findByDepartment_DepartmentName(String departmentName);
}
