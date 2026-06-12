package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.ClassroomDto;
import DFP.collegeWithAI.model.Classroom;
import DFP.collegeWithAI.service.ClassroomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/classrooms")
public class ClassroomController {

    private final ClassroomService classroomService;

    @PostMapping("")
    public ResponseEntity<Classroom> addClassroom(
            @RequestParam String studentEmail,
            @RequestParam String professorEmail,
            @RequestParam String subjectInternCode,
            @RequestParam(required = false) Double grade) {
        Classroom created = classroomService.addClassroom(studentEmail, professorEmail, subjectInternCode, grade);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("")
    public ResponseEntity<List<ClassroomDto>> findAllClassrooms() {
        List<ClassroomDto> list = classroomService.findAllClassrooms();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomDto> findById(@PathVariable Long id) {
        ClassroomDto found = classroomService.findById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/student/{email}")
    public ResponseEntity<List<ClassroomDto>> findByStudentEmail(@PathVariable String email) {
        List<ClassroomDto> list = classroomService.findByStudentEmail(email);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/subject/{internCode}")
    public ResponseEntity<List<ClassroomDto>> findBySubjectInternCode(@PathVariable String internCode) {
        List<ClassroomDto> list = classroomService.findBySubjectInternCode(internCode);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/professor/{email}")
    public ResponseEntity<List<ClassroomDto>> findByProfessorEmail(@PathVariable String email) {
        List<ClassroomDto> list = classroomService.findByProfessorEmail(email);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}/grade")
    public ResponseEntity<ClassroomDto> updateGrade(@PathVariable Long id, @RequestParam Double grade) {
        ClassroomDto updated = classroomService.updateGrade(id, grade);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClassroom(@PathVariable Long id) {
        classroomService.deleteClassroom(id);
        return ResponseEntity.ok("Clase/Matricula eliminada exitosamente");
    }
}
