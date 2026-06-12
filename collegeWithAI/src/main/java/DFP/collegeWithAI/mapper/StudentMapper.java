package DFP.collegeWithAI.mapper;

import DFP.collegeWithAI.dto.response.StudentDto;
import DFP.collegeWithAI.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    @Autowired
    private SubjectMapper subjectMapper;

    public StudentDto toDto(Student student) {
        if (student == null)
            return null;

        return StudentDto.builder()
                .name(student.getName())
                .surname(student.getSurname())
                .dni(student.getDni())
                .email(student.getEmail())
                .phone(student.getPhone())
                .schedule(student.getSchedule())
                .dateJoining(student.getDateJoining())
                .subjects(student.getSubjects() != null ? student.getSubjects().stream().map(subjectMapper::toDto).toList() : null)
                .build();
    }

    public Student toEntity(StudentDto studentDto) {
        if (studentDto == null)
            return null;

        return Student.builder()
                .name(studentDto.getName())
                .surname(studentDto.getSurname())
                .dni(studentDto.getDni())
                .email(studentDto.getEmail())
                .phone(studentDto.getPhone())
                .schedule(studentDto.getSchedule())
                .dateJoining(studentDto.getDateJoining())
                .subjects(studentDto.getSubjects() != null ? studentDto.getSubjects().stream().map(subjectMapper::toEntity).toList() : null)
                .build();
    }

    public void updateEntityFromDto(StudentDto studentDto, Student student) {
        if (studentDto.getName() != null)
            student.setName(studentDto.getName());
        if (studentDto.getSurname() != null)
            student.setSurname(studentDto.getSurname());
        if (studentDto.getDni() != null)
            student.setDni(studentDto.getDni());
        if (studentDto.getEmail() != null)
            student.setEmail(studentDto.getEmail());
        if (studentDto.getPhone() != null)
            student.setPhone(studentDto.getPhone());
        if (studentDto.getSchedule() != null)
            student.setSchedule(studentDto.getSchedule());
        if (studentDto.getDateJoining() != null)
            student.setDateJoining(studentDto.getDateJoining());
        if (studentDto.getSubjects() != null)
            student.setSubjects(studentDto.getSubjects().stream().map(subjectMapper::toEntity).toList());
    }
}
