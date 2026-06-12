package DFP.collegeWithAI.repository;

import DFP.collegeWithAI.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.util.Date;
import DFP.collegeWithAI.model.Schedule;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByDni(String dni);

    List<Student> findByName(String name);

    List<Student> findBySurname(String surname);

    Optional<Student> findByEmail(String email);

    Optional<Student> findByPhone(Integer phone);

    List<Student> findBySchedule(Schedule schedule);

    List<Student> findByDateJoiningLessThanEqual(Date dateJoining);
}
