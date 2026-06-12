package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.SubjectDto;
import DFP.collegeWithAI.model.Subject;
import DFP.collegeWithAI.service.SubjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/subjects")
public class SubjectController {
    private final SubjectService subjectService;

    @PostMapping("")
    public ResponseEntity<SubjectDto> addSubject(@Valid @RequestBody SubjectDto subjectDto) {
        SubjectDto created = subjectService.addSubject(subjectDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("")
    public ResponseEntity<List<SubjectDto>> findAllSubjects() {
        List<SubjectDto> list = subjectService.findAllSubjects();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/full")
    public ResponseEntity<List<Subject>> findAllFull() {
        List<Subject> list = subjectService.findAllFull();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDto> findById(@PathVariable Long id) {
        SubjectDto found = subjectService.findById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SubjectDto> findByName(@PathVariable String name) {
        SubjectDto found = subjectService.findByName(name);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/internCode/{internCode}")
    public ResponseEntity<SubjectDto> findByInternCode(@PathVariable String internCode) {
        SubjectDto found = subjectService.findByInternCode(internCode);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/isElective/{isElective}")
    public ResponseEntity<List<SubjectDto>> findByIsElective(@PathVariable Boolean isElective) {
        List<SubjectDto> list = subjectService.findByIsElective(isElective);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/totalHours/{totalHours}")
    public ResponseEntity<List<SubjectDto>> findByTotalHours(@PathVariable Integer totalHours) {
        List<SubjectDto> list = subjectService.findByTotalHours(totalHours);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/department/{departmentName}")
    public ResponseEntity<List<SubjectDto>> findByDepartmentName(@PathVariable String departmentName) {
        List<SubjectDto> list = subjectService.findByDepartmentName(departmentName);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectDto> updateSubject(@PathVariable Long id, @Valid @RequestBody SubjectDto subjectDto) {
        SubjectDto updated = subjectService.updateSubject(id, subjectDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.ok("Materia eliminada exitosamente");
    }
}
