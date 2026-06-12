package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.ProfessorDto;
import DFP.collegeWithAI.model.Professor;
import DFP.collegeWithAI.model.Schedule;
import DFP.collegeWithAI.service.ProfessorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/professors")
public class ProfessorController {
    private final ProfessorService professorService;

    @PostMapping("")
    public ResponseEntity<Professor> addProfessor(@Valid @RequestBody ProfessorDto professorDto) {
        Professor created = professorService.addProfessor(professorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("")
    public ResponseEntity<List<ProfessorDto>> findAllProfessors() {
        List<ProfessorDto> list = professorService.findAllProfessors();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDto> findById(@PathVariable Long id) {
        ProfessorDto found = professorService.findById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<ProfessorDto>> findByName(@PathVariable String name) {
        List<ProfessorDto> list = professorService.findByName(name);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/surname/{surname}")
    public ResponseEntity<List<ProfessorDto>> findBySurname(@PathVariable String surname) {
        List<ProfessorDto> list = professorService.findBySurname(surname);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<ProfessorDto> findByDni(@PathVariable String dni) {
        ProfessorDto found = professorService.findByDni(dni);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ProfessorDto> findByEmail(@PathVariable String email) {
        ProfessorDto found = professorService.findByEmail(email);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<ProfessorDto> findByPhone(@PathVariable Integer phone) {
        ProfessorDto found = professorService.findByPhone(phone);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/schedule/{schedule}")
    public ResponseEntity<List<ProfessorDto>> findBySchedule(@PathVariable Schedule schedule) {
        List<ProfessorDto> list = professorService.findBySchedule(schedule);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/salary/{salary}")
    public ResponseEntity<List<ProfessorDto>> findBySalary(@PathVariable Double salary) {
        List<ProfessorDto> list = professorService.findBySalary(salary);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/department/{departmentName}")
    public ResponseEntity<List<ProfessorDto>> findByDepartmentName(@PathVariable String departmentName) {
        List<ProfessorDto> list = professorService.findByDepartmentName(departmentName);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorDto> updateProfessor(@PathVariable Long id,
            @Valid @RequestBody ProfessorDto professorDto) {
        ProfessorDto updated = professorService.updateProfessor(id, professorDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfessor(@PathVariable Long id) {
        professorService.deleteProfessor(id);
        return ResponseEntity.ok("Profesor eliminado exitosamente");
    }

    @PostMapping("/{professorId}/students/{studentId}")
    public ResponseEntity<ProfessorDto> assignStudentToProfessor(@PathVariable Long professorId,
            @PathVariable Long studentId) {
        ProfessorDto updated = professorService.assignStudentToProfessor(professorId, studentId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{professorId}/students/{studentId}")
    public ResponseEntity<ProfessorDto> removeStudentFromProfessor(@PathVariable Long professorId,
            @PathVariable Long studentId) {
        ProfessorDto updated = professorService.removeStudentFromProfessor(professorId, studentId);
        return ResponseEntity.ok(updated);
    }
}
