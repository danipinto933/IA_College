package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.StudentDto;
import DFP.collegeWithAI.model.Student;
import DFP.collegeWithAI.service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Date;
import DFP.collegeWithAI.model.Schedule;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    @PostMapping("")
    public ResponseEntity<StudentDto> addStudent(@Valid @RequestBody StudentDto studentDto) {
        StudentDto created = studentService.addStudent(studentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("")
    public ResponseEntity<List<StudentDto>> findAllStudents() {
        List<StudentDto> list = studentService.findAllStudents();
        return ResponseEntity.ok(list);
    }
    
    @GetMapping("/full")
    public ResponseEntity<List<Student>> findAllFull() {
        List<Student> list = studentService.findAllFull();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> findById(@PathVariable Long id) {
        StudentDto found = studentService.findById(id);
        return ResponseEntity.ok(found);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDto studentDto) {
        StudentDto updated = studentService.updateStudent(id, studentDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Estudiante eliminado exitosamente");
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<StudentDto>> findByName(@PathVariable String name) {
        List<StudentDto> list = studentService.findByName(name);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/surname/{surname}")
    public ResponseEntity<List<StudentDto>> findBySurname(@PathVariable String surname) {
        List<StudentDto> list = studentService.findBySurname(surname);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<StudentDto> findByDni(@PathVariable String dni) {
        StudentDto found = studentService.findByDni(dni);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<StudentDto> findByEmail(@PathVariable String email) {
        StudentDto found = studentService.findByEmail(email);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<StudentDto> findByPhone(@PathVariable Integer phone) {
        StudentDto found = studentService.findByPhone(phone);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/schedule/{schedule}")
    public ResponseEntity<List<StudentDto>> findBySchedule(@PathVariable Schedule schedule) {
        List<StudentDto> list = studentService.findBySchedule(schedule);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/dateJoining/{dateJoining}")
    public ResponseEntity<List<StudentDto>> findByDateJoining(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateJoining) {
        List<StudentDto> list = studentService.findByDateJoining(dateJoining);
        return ResponseEntity.ok(list);
    }
}
