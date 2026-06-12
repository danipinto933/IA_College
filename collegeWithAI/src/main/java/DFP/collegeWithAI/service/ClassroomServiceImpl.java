package DFP.collegeWithAI.service;

import DFP.collegeWithAI.dto.response.ClassroomDto;
import DFP.collegeWithAI.exception.custom.BadRequestException;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.mapper.ClassroomMapper;
import DFP.collegeWithAI.model.Classroom;
import DFP.collegeWithAI.model.Professor;
import DFP.collegeWithAI.model.Student;
import DFP.collegeWithAI.model.Subject;
import DFP.collegeWithAI.repository.ClassroomRepository;
import DFP.collegeWithAI.repository.ProfessorRepository;
import DFP.collegeWithAI.repository.StudentRepository;
import DFP.collegeWithAI.repository.SubjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final ProfessorRepository professorRepository;
    private final ClassroomMapper classroomMapper;

    @Override
    public Classroom addClassroom(String studentEmail, String professorEmail, String subjectInternCode, Double grade) {
        if (studentEmail == null || studentEmail.trim().isEmpty()) {
            throw new BadRequestException("Es necesario enviar el email del estudiante");
        }
        if (subjectInternCode == null || subjectInternCode.trim().isEmpty()) {
            throw new BadRequestException("Es necesario enviar el código interno de la asignatura");
        }
        if (professorEmail == null || professorEmail.trim().isEmpty()) {
            throw new BadRequestException("Es necesario enviar el email del profesor");
        }

        Student student = studentRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con el email: " + studentEmail));
        
        Subject subject = subjectRepository.findByInternCode(subjectInternCode)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada con el código: " + subjectInternCode));
        
        Professor professor = professorRepository.findByEmail(professorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor no encontrado con el email: " + professorEmail));

        Classroom classroom = new Classroom();
        classroom.setStudent(student);
        classroom.setSubject(subject);
        classroom.setProfessor(professor);
        classroom.setGrade(grade);

        return classroomRepository.save(classroom);
    }

    @Override
    public List<ClassroomDto> findAllClassrooms() {
        List<Classroom> classrooms = classroomRepository.findAll();
        return classrooms.stream().map(classroomMapper::toDto).toList();
    }

    @Override
    public ClassroomDto findById(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clase/Matrícula no encontrada con el ID: " + id));
        return classroomMapper.toDto(classroom);
    }

    @Override
    public List<ClassroomDto> findByStudentEmail(String email) {
        List<Classroom> classrooms = classroomRepository.findByStudent_Email(email);
        return classrooms.stream().map(classroomMapper::toDto).toList();
    }

    @Override
    public List<ClassroomDto> findBySubjectInternCode(String internCode) {
        List<Classroom> classrooms = classroomRepository.findBySubject_InternCode(internCode);
        return classrooms.stream().map(classroomMapper::toDto).toList();
    }

    @Override
    public List<ClassroomDto> findByProfessorEmail(String email) {
        List<Classroom> classrooms = classroomRepository.findByProfessor_Email(email);
        return classrooms.stream().map(classroomMapper::toDto).toList();
    }

    @Override
    public ClassroomDto updateGrade(Long id, Double grade) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clase/Matrícula no encontrada con el ID: " + id));
        
        classroom.setGrade(grade);
        Classroom updated = classroomRepository.save(classroom);
        return classroomMapper.toDto(updated);
    }

    @Override
    public void deleteClassroom(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clase/Matrícula no encontrada con el ID: " + id));
        classroomRepository.delete(classroom);
    }
}
