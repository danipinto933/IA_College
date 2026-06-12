package DFP.collegeWithAI.mapper;

import DFP.collegeWithAI.dto.response.ClassroomDto;
import DFP.collegeWithAI.model.Classroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClassroomMapper {

    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private SubjectMapper subjectMapper;
    
    @Autowired
    private ProfessorMapper professorMapper;

    public ClassroomDto toDto(Classroom classroom) {
        if (classroom == null)
            return null;

        return ClassroomDto.builder()
                .student(studentMapper.toDto(classroom.getStudent()))
                .subject(subjectMapper.toDto(classroom.getSubject()))
                .professor(professorMapper.toDto(classroom.getProfessor()))
                .grade(classroom.getGrade())
                .build();
    }

    public Classroom toEntity(ClassroomDto classroomDto) {
        if (classroomDto == null)
            return null;

        return Classroom.builder()
                .student(studentMapper.toEntity(classroomDto.getStudent()))
                .subject(subjectMapper.toEntity(classroomDto.getSubject()))
                .professor(professorMapper.toEntity(classroomDto.getProfessor()))
                .grade(classroomDto.getGrade())
                .build();
    }
}
