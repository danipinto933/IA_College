package DFP.collegeWithAI.controller;

import DFP.collegeWithAI.dto.response.DepartmentDto;
import DFP.collegeWithAI.model.Department;
import DFP.collegeWithAI.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {
    
    private final DepartmentService departmentService;

    @PostMapping("")
    public ResponseEntity<DepartmentDto> addDepartment(@Valid @RequestBody DepartmentDto departmentDto) {
        DepartmentDto created = departmentService.addDepartment(departmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("")
    public ResponseEntity<List<DepartmentDto>> findAllDepartments() {
        List<DepartmentDto> list = departmentService.findAllDepartments();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/full")
    public ResponseEntity<List<Department>> findAllFull() {
        List<Department> list = departmentService.findAllFull();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> findById(@PathVariable Long id) {
        DepartmentDto found = departmentService.findById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/name/{departmentName}")
    public ResponseEntity<DepartmentDto> findByDepartmentName(@PathVariable String departmentName) {
        DepartmentDto found = departmentService.findByDepartmentName(departmentName);
        return ResponseEntity.ok(found);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Long id,
                                                            @Valid @RequestBody DepartmentDto departmentDto) {
        DepartmentDto updated = departmentService.updateDepartment(id, departmentDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok("Departmento eliminado exitosamente");
    }
}
