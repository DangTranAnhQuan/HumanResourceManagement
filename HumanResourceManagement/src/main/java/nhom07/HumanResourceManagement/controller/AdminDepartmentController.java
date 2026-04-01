package nhom07.HumanResourceManagement.controller;

import nhom07.HumanResourceManagement.entity.Department;
import nhom07.HumanResourceManagement.service.AdminDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/departments")
public class AdminDepartmentController {

    @Autowired
    private AdminDepartmentService departmentService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PostMapping
    public ResponseEntity<?> create(Authentication auth, @RequestBody Department department) {
        try {
            Department created = departmentService.createDepartment(auth.getName(), department);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(Authentication auth, @PathVariable Integer id, @RequestBody Department department) {
        try {
            Department updated = departmentService.updateDepartment(auth.getName(), id, department);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(Authentication auth, @PathVariable Integer id) {
        try {
            departmentService.deleteDepartment(auth.getName(), id);
            return ResponseEntity.ok(Map.of("message", "Đã xóa phòng ban thành công."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}