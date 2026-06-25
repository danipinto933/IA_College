package DFP.collegeWithAI.repository;

import DFP.collegeWithAI.model.Department;
import DFP.collegeWithAI.model.Subject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SubjectRepositoryTests {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department;
    private Subject subject;

    @BeforeEach
    public void setUp() {
        department = Department.builder()
                .departmentName("Departamento de Matematica y Computacion")
                .build();
        department = departmentRepository.save(department);

        subject = Subject.builder()
                .name("Algebra Lineal")
                .internCode("MAT-101")
                .description("Curso basico de algebra")
                .isElective(false)
                .totalHours(60)
                .department(department)
                .build();
        subject = subjectRepository.save(subject);
    }

    @AfterEach
    public void tearDown() {
        subjectRepository.delete(subject);
        departmentRepository.delete(department);
    }

    @Test
    public void testFindByName_Success() {
        Optional<Subject> found = subjectRepository.findByName("Algebra Lineal");
        assertTrue(found.isPresent());
        assertEquals("MAT-101", found.get().getInternCode());
    }

    @Test
    public void testFindByName_NotFound() {
        Optional<Subject> found = subjectRepository.findByName("Materia Inexistente");
        assertFalse(found.isPresent());
    }

    @Test
    public void testFindByInternCode_Success() {
        Optional<Subject> found = subjectRepository.findByInternCode("MAT-101");
        assertTrue(found.isPresent());
        assertEquals("Algebra Lineal", found.get().getName());
    }

    @Test
    public void testFindByInternCode_NotFound() {
        Optional<Subject> found = subjectRepository.findByInternCode("ERR-101");
        assertFalse(found.isPresent());
    }

    @Test
    public void testFindByIsElective() {
        List<Subject> found = subjectRepository.findByIsElective(false);
        assertFalse(found.isEmpty());
        assertTrue(found.stream().anyMatch(s -> s.getName().equals("Algebra Lineal")));
    }

    @Test
    public void testFindByTotalHoursLessThanEqual() {
        List<Subject> found = subjectRepository.findByTotalHoursLessThanEqual(70);
        assertFalse(found.isEmpty());
        assertTrue(found.stream().anyMatch(s -> s.getName().equals("Algebra Lineal")));

        List<Subject> none = subjectRepository.findByTotalHoursLessThanEqual(20);
        assertTrue(none.stream().noneMatch(s -> s.getName().equals("Algebra Lineal")));
    }

    @Test
    public void testFindByDepartment_DepartmentName() {
        List<Subject> found = subjectRepository.findByDepartment_DepartmentName("Departamento de Matematica y Computacion");
        assertFalse(found.isEmpty());
        assertEquals("Algebra Lineal", found.get(0).getName());
    }
}
