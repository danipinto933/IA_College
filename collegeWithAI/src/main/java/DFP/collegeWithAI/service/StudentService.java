package DFP.collegeWithAI.service;

import DFP.collegeWithAI.dto.response.StudentDto;
import DFP.collegeWithAI.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;
import DFP.collegeWithAI.model.Schedule;

@Service
public interface StudentService {
    StudentDto addStudent(StudentDto studentDto);

    List<StudentDto> findAllStudents();
    List<Student> findAllFull();

    StudentDto findById(Long id);

    List<StudentDto> findByName(String name);

    List<StudentDto> findBySurname(String surname);

    StudentDto findByDni(String dni);

    StudentDto findByEmail(String email);

    StudentDto findByPhone(Integer phone);

    List<StudentDto> findBySchedule(Schedule schedule);

    List<StudentDto> findByDateJoining(Date dateJoining);

    StudentDto updateStudent(Long id, StudentDto studentDto);

    void deleteStudent(Long id);
}
