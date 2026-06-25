package DFP.collegeWithAI.repository;

import DFP.collegeWithAI.model.Department;
import DFP.collegeWithAI.model.Professor;
import DFP.collegeWithAI.model.Schedule;
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
public class ProfessorRepositoryTests {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department;
    private Professor professor;

    @BeforeEach
    public void setUp() {
        department = Department.builder()
                .departmentName("Departamento de Fisica Teorica")
                .build();
        department = departmentRepository.save(department);

        professor = Professor.builder()
                .name("Richard")
                .surname("Feynman")
                .dni("99999999F")
                .email("richard.feynman@theoretical.edu")
                .phone(699999999)
                .schedule(Schedule.AFTERNOON)
                .salary(4200.0)
                .department(department)
                .build();
        professor = professorRepository.save(professor);
    }

    @AfterEach
    public void tearDown() {
        professorRepository.delete(professor);
        departmentRepository.delete(department);
    }

    @Test
    public void testFindByName() {
        List<Professor> found = professorRepository.findByName("Richard");
        assertFalse(found.isEmpty());
        assertEquals("Feynman", found.get(0).getSurname());
    }

    @Test
    public void testFindBySurname() {
        List<Professor> found = professorRepository.findBySurname("Feynman");
        assertFalse(found.isEmpty());
        assertEquals("Richard", found.get(0).getName());
    }

    @Test
    public void testFindByDni_Success() {
        Optional<Professor> found = professorRepository.findByDni("99999999F");
        assertTrue(found.isPresent());
        assertEquals("Richard", found.get().getName());
    }

    @Test
    public void testFindByDni_NotFound() {
        Optional<Professor> found = professorRepository.findByDni("00000000X");
        assertFalse(found.isPresent());
    }

    @Test
    public void testFindByEmail_Success() {
        Optional<Professor> found = professorRepository.findByEmail("richard.feynman@theoretical.edu");
        assertTrue(found.isPresent());
        assertEquals("99999999F", found.get().getDni());
    }

    @Test
    public void testFindByPhone_Success() {
        Optional<Professor> found = professorRepository.findByPhone(699999999);
        assertTrue(found.isPresent());
    }

    @Test
    public void testFindBySchedule() {
        List<Professor> found = professorRepository.findBySchedule(Schedule.AFTERNOON);
        assertFalse(found.isEmpty());
    }

    @Test
    public void testFindBySalary() {
        List<Professor> found = professorRepository.findBySalary(4200.0);
        assertFalse(found.isEmpty());
    }

    @Test
    public void testFindByDepartment_DepartmentName() {
        List<Professor> found = professorRepository.findByDepartment_DepartmentName("Departamento de Fisica Teorica");
        assertFalse(found.isEmpty());
        assertEquals("Richard", found.get(0).getName());
    }
}
