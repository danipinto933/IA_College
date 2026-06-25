package DFP.collegeWithAI.repository;

import DFP.collegeWithAI.model.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DepartmentRepositoryTests {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    public void testSaveAndFindById() {
        // Arrange
        Department department = Department.builder()
                .departmentName("Ciencia de los Materiales")
                .build();

        // Act
        Department saved = departmentRepository.save(department);
        Optional<Department> found = departmentRepository.findById(saved.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Ciencia de los Materiales", found.get().getDepartmentName());

        // Clean up
        departmentRepository.delete(saved);
    }

    @Test
    public void testFindByDepartmentName_Success() {
        // Arrange
        Department department = Department.builder()
                .departmentName("Ingenieria de Software")
                .build();
        Department saved = departmentRepository.save(department);

        // Act
        Department found = departmentRepository.findByDepartmentName("Ingenieria de Software");

        // Assert
        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals("Ingenieria de Software", found.getDepartmentName());

        // Clean up
        departmentRepository.delete(saved);
    }

    @Test
    public void testFindByDepartmentName_NotFound() {
        // Act
        Department found = departmentRepository.findByDepartmentName("Departamento Fantasma");

        // Assert
        assertNull(found);
    }

    @Test
    public void testDelete() {
        // Arrange
        Department department = Department.builder()
                .departmentName("Temporal")
                .build();
        Department saved = departmentRepository.save(department);

        // Act
        departmentRepository.delete(saved);
        Optional<Department> found = departmentRepository.findById(saved.getId());

        // Assert
        assertFalse(found.isPresent());
    }
}
