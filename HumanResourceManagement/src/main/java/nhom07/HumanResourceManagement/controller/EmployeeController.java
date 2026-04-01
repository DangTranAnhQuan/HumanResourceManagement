package nhom07.HumanResourceManagement.controller;

import nhom07.HumanResourceManagement.entity.Employee;
import nhom07.HumanResourceManagement.entity.LeaveRequest;
import nhom07.HumanResourceManagement.entity.Payslip;
import nhom07.HumanResourceManagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        try {
            String email = authentication.getName();
            Employee employee = employeeService.getProfile(email);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // CẬP NHẬT: Nhận thêm Emergency Contact
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(Authentication authentication, @RequestBody Map<String, String> request) {
        try {
            String email = authentication.getName();
            String phoneNumber = request.get("phoneNumber");
            String currentAddress = request.get("currentAddress");
            String emergencyName = request.get("emergencyContactName");
            String emergencyPhone = request.get("emergencyContactPhone");

            Employee updatedEmployee = employeeService.updateContactInfo(email, phoneNumber, currentAddress, emergencyName, emergencyPhone);
            return ResponseEntity.ok(Map.of(
                    "message", "Cập nhật thông tin liên lạc thành công.",
                    "employee", updatedEmployee
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/leave-requests")
    public ResponseEntity<?> createLeaveRequest(Authentication authentication, @RequestBody Map<String, String> request) {
        try {
            String email = authentication.getName();
            String leaveType = request.get("leaveType");
            LocalDate startDate = LocalDate.parse(request.get("startDate"), DateTimeFormatter.ISO_DATE);
            LocalDate endDate = LocalDate.parse(request.get("endDate"), DateTimeFormatter.ISO_DATE);
            String reason = request.get("reason");

            LeaveRequest leaveRequest = employeeService.createLeaveRequest(email, leaveType, startDate, endDate, reason);
            return ResponseEntity.ok(Map.of(
                    "message", "Đã gửi đơn xin nghỉ phép thành công. Trạng thái: Chờ duyệt.",
                    "requestId", leaveRequest.getRequestID()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/leave-requests/{id}/cancel")
    public ResponseEntity<?> cancelLeaveRequest(Authentication authentication, @PathVariable("id") Integer requestId) {
        try {
            String email = authentication.getName();
            boolean isCancelled = employeeService.cancelLeaveRequest(email, requestId);
            if (isCancelled) {
                return ResponseEntity.ok(Map.of("message", "Đã hủy đơn xin nghỉ phép thành công."));
            }
            return ResponseEntity.badRequest().body(Map.of("error", "Không thể hủy đơn."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/payslips")
    public ResponseEntity<?> getMyPayslips(Authentication authentication) {
        try {
            String email = authentication.getName();
            List<Payslip> payslips = employeeService.getMyPayslips(email);
            return ResponseEntity.ok(payslips);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/leave-history")
    public ResponseEntity<?> getMyLeaveHistory(Authentication authentication) {
        try {
            String email = authentication.getName();
            List<LeaveRequest> history = employeeService.getMyLeaveHistory(email);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}