package DFP.collegeWithAI.repository;

import DFP.collegeWithAI.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    List<Classroom> findByStudent_Email(String email);
    List<Classroom> findBySubject_InternCode(String internCode);
    List<Classroom> findByProfessor_Email(String email);
}
