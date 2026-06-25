package DFP.collegeWithAI.repository;

import DFP.collegeWithAI.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClassroomRepositoryTests {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department;
    private Subject subject;
    private Professor professor;
    private Student student;
    private Classroom classroom;

    @BeforeEach
    public void setUp() {
        department = Department.builder()
                .departmentName("Departamento de Matematica e Informatica")
                .build();
        department = departmentRepository.save(department);

        subject = Subject.builder()
                .name("Estructuras de Datos")
                .internCode("INF-201")
                .description("Pilas, colas, arboles")
                .isElective(false)
                .totalHours(64)
                .department(department)
                .build();
        subject = subjectRepository.save(subject);

        professor = Professor.builder()
                .name("Grace")
                .surname("Hopper")
                .dni("88888888H")
                .email("grace.hopper@computer.org")
                .phone(688888888)
                .schedule(Schedule.MORNING)
                .salary(4500.0)
                .department(department)
                .build();
        professor = professorRepository.save(professor);

        student = Student.builder()
                .name("John")
                .surname("McCarthy")
                .dni("77777777M")
                .email("lisp.creator@mit.edu")
                .phone(677777777)
                .schedule(Schedule.MORNING)
                .dateJoining(new Date())
                .build();
        student = studentRepository.save(student);

        classroom = Classroom.builder()
                .student(student)
                .subject(subject)
                .professor(professor)
                .grade(9.5)
                .build();
        classroom = classroomRepository.save(classroom);
    }

    @AfterEach
    public void tearDown() {
        classroomRepository.delete(classroom);
        studentRepository.delete(student);
        professorRepository.delete(professor);
        subjectRepository.delete(subject);
        departmentRepository.delete(department);
    }

    @Test
    public void testFindByStudent_Email() {
        List<Classroom> found = classroomRepository.findByStudent_Email("lisp.creator@mit.edu");
        assertFalse(found.isEmpty());
        assertEquals("Estructuras de Datos", found.get(0).getSubject().getName());
    }

    @Test
    public void testFindBySubject_InternCode() {
        List<Classroom> found = classroomRepository.findBySubject_InternCode("INF-201");
        assertFalse(found.isEmpty());
        assertEquals("lisp.creator@mit.edu", found.get(0).getStudent().getEmail());
    }

    @Test
    public void testFindByProfessor_Email() {
        List<Classroom> found = classroomRepository.findByProfessor_Email("grace.hopper@computer.org");
        assertFalse(found.isEmpty());
        assertEquals("Estructuras de Datos", found.get(0).getSubject().getName());
    }
}
