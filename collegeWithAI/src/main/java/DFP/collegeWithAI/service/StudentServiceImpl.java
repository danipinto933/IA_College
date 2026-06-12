package DFP.collegeWithAI.service;

import DFP.collegeWithAI.dto.response.StudentDto;
import DFP.collegeWithAI.exception.custom.BadRequestException;
import DFP.collegeWithAI.exception.custom.ResourceNotFoundException;
import DFP.collegeWithAI.mapper.StudentMapper;
import DFP.collegeWithAI.model.Student;
import DFP.collegeWithAI.repository.StudentRepository;
import DFP.collegeWithAI.repository.SubjectRepository;
import DFP.collegeWithAI.model.Subject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import DFP.collegeWithAI.model.Schedule;

@AllArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final StudentMapper studentMapper;

    @Override
    public StudentDto addStudent(StudentDto studentDto) {
        if (studentDto == null) {
            throw new BadRequestException("Los datos del estudiante no pueden ser nulos");
        }

        Student student = studentMapper.toEntity(studentDto);
        if (student == null) {
            throw new BadRequestException("Estudiante no válido");
        }

        if (studentDto.getSubjects() != null) {
            List<Subject> managedSubjects = new ArrayList<>();
            for (var subDto : studentDto.getSubjects()) {
                Subject sub = subjectRepository.findByName(subDto.getName())
                        .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada: " + subDto.getName()));
                managedSubjects.add(sub);
            }
            student.setSubjects(managedSubjects);
        }

        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDto(savedStudent);
    }

    @Override
    public List<StudentDto> findAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(studentMapper::toDto)
                .toList();
    }

    @Override
    public List<Student> findAllFull() {
        return studentRepository.findAll();
    }

    @Override
    public StudentDto findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con el ID: " + id));
        return studentMapper.toDto(student);
    }

    @Override
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        if (studentDto == null) {
            throw new BadRequestException("Los datos para actualizar el estudiante no pueden ser nulos");
        }

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con el ID: " + id));

        studentMapper.updateEntityFromDto(studentDto, student);

        if (studentDto.getSubjects() != null) {
            List<Subject> managedSubjects = new ArrayList<>();
            for (var subDto : studentDto.getSubjects()) {
                Subject sub = subjectRepository.findByName(subDto.getName())
                        .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada: " + subDto.getName()));
                managedSubjects.add(sub);
            }
            student.setSubjects(managedSubjects);
        }

        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toDto(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con el ID: " + id));
        studentRepository.delete(student);
    }

    @Override
    public List<StudentDto> findByName(String name) {
        List<Student> students = studentRepository.findByName(name);
        return students.stream().map(studentMapper::toDto).toList();
    }

    @Override
    public List<StudentDto> findBySurname(String surname) {
        List<Student> students = studentRepository.findBySurname(surname);
        return students.stream().map(studentMapper::toDto).toList();
    }

    @Override
    public StudentDto findByDni(String dni) {
        Student student = studentRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con el DNI: " + dni));
        return studentMapper.toDto(student);
    }

    @Override
    public StudentDto findByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con el email: " + email));
        return studentMapper.toDto(student);
    }

    @Override
    public StudentDto findByPhone(Integer phone) {
        Student student = studentRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con el teléfono: " + phone));
        return studentMapper.toDto(student);
    }

    @Override
    public List<StudentDto> findBySchedule(Schedule schedule) {
        List<Student> students = studentRepository.findBySchedule(schedule);
        return students.stream().map(studentMapper::toDto).toList();
    }

    @Override
    public List<StudentDto> findByDateJoining(Date dateJoining) {
        List<Student> students = studentRepository.findByDateJoiningLessThanEqual(dateJoining);
        return students.stream().map(studentMapper::toDto).toList();
    }
}
