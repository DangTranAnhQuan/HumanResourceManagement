package nhom07.HumanResourceManagement.controller;

import nhom07.HumanResourceManagement.entity.Employee;
import nhom07.HumanResourceManagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/employees")
    public ResponseEntity<?> createEmployeeAccount(Authentication auth, @RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String fullName = request.get("fullName");
            String cccd = request.get("cccd");
            Integer roleId = Integer.parseInt(request.get("roleId"));

            Integer departmentId = request.containsKey("departmentId") ? Integer.parseInt(request.get("departmentId")) : null;
            Integer positionId = request.containsKey("positionId") ? Integer.parseInt(request.get("positionId")) : null;

            Employee newEmp = adminService.createNewEmployee(auth.getName(), email, fullName, cccd, roleId, departmentId, positionId);

            return ResponseEntity.ok(Map.of(
                    "message", "Tạo tài khoản và hồ sơ thành công.",
                    "employeeID", newEmp.getEmployeeID(),
                    "email", newEmp.getUser().getEmail()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}