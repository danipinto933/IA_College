package DFP.collegeWithAI.repository;

import DFP.collegeWithAI.model.Schedule;
import DFP.collegeWithAI.model.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentRepositoryTests {

    @Autowired
    private StudentRepository studentRepository;

    private Student student;
    private Date testDate;

    @BeforeEach
    public void setUp() {
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.JANUARY, 1);
        testDate = cal.getTime();

        student = Student.builder()
                .name("John")
                .surname("Doe")
                .dni("11111111A")
                .email("john.doe@student.edu")
                .phone(611111111)
                .schedule(Schedule.MORNING)
                .dateJoining(testDate)
                .build();
        student = studentRepository.save(student);
    }

    @AfterEach
    public void tearDown() {
        studentRepository.delete(student);
    }

    @Test
    public void testFindByDni_Success() {
        Optional<Student> found = studentRepository.findByDni("11111111A");
        assertTrue(found.isPresent());
        assertEquals("John", found.get().getName());
    }

    @Test
    public void testFindByDni_NotFound() {
        Optional<Student> found = studentRepository.findByDni("00000000X");
        assertFalse(found.isPresent());
    }

    @Test
    public void testFindByName() {
        List<Student> found = studentRepository.findByName("John");
        assertFalse(found.isEmpty());
        assertEquals("Doe", found.get(0).getSurname());
    }

    @Test
    public void testFindBySurname() {
        List<Student> found = studentRepository.findBySurname("Doe");
        assertFalse(found.isEmpty());
        assertEquals("John", found.get(0).getName());
    }

    @Test
    public void testFindByEmail_Success() {
        Optional<Student> found = studentRepository.findByEmail("john.doe@student.edu");
        assertTrue(found.isPresent());
    }

    @Test
    public void testFindByPhone_Success() {
        Optional<Student> found = studentRepository.findByPhone(611111111);
        assertTrue(found.isPresent());
    }

    @Test
    public void testFindBySchedule() {
        List<Student> found = studentRepository.findBySchedule(Schedule.MORNING);
        assertFalse(found.isEmpty());
    }

    @Test
    public void testFindByDateJoiningLessThanEqual() {
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.JANUARY, 2);
        List<Student> found = studentRepository.findByDateJoiningLessThanEqual(cal.getTime());
        assertFalse(found.isEmpty());

        cal.set(2024, Calendar.DECEMBER, 31);
        List<Student> empty = studentRepository.findByDateJoiningLessThanEqual(cal.getTime());
        assertTrue(empty.stream().noneMatch(s -> s.getDni().equals("11111111A")));
    }
}
