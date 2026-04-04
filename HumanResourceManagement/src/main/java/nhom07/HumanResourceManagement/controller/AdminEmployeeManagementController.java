package nhom07.HumanResourceManagement.controller;

import nhom07.HumanResourceManagement.entity.Employee;
import nhom07.HumanResourceManagement.service.AdminEmployeeManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/employees-management")
public class AdminEmployeeManagementController {

    @Autowired
    private AdminEmployeeManagementService employeeManagementService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(employeeManagementService.getAllEmployees());
    }

    // TÍNH NĂNG MỚI: Lấy chi tiết 1 hồ sơ nhân viên để đưa lên Form UI
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeDetail(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(employeeManagementService.getEmployeeById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // CẬP NHẬT: Hứng toàn bộ dữ liệu từ form UI mới
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(Authentication auth, @PathVariable Integer id, @RequestBody Map<String, Object> payload) {
        try {
            Employee updated = employeeManagementService.updateComprehensiveProfile(auth.getName(), id, payload);
            return ResponseEntity.ok(Map.of("message", "Cập nhật hồ sơ thành công.", "employee", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<?> changeAccountStatus(Authentication auth, @PathVariable Integer userId, @RequestBody Map<String, String> body) {
        try {
            String newStatus = body.get("status");
            employeeManagementService.toggleAccountStatus(auth.getName(), userId, newStatus);
            return ResponseEntity.ok(Map.of("message", "Đã cập nhật trạng thái tài khoản thành: " + newStatus));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/users/{userId}/reset-password")
    public ResponseEntity<?> resetPassword(Authentication auth, @PathVariable Integer userId) {
        try {
            String defaultPwd = employeeManagementService.resetUserPassword(auth.getName(), userId);
            return ResponseEntity.ok(Map.of(
                    "message", "Đã reset mật khẩu thành công.",
                    "defaultPassword", defaultPwd
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}