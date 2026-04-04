package nhom07.HumanResourceManagement.controller;

import nhom07.HumanResourceManagement.entity.PayrollConfig;
import nhom07.HumanResourceManagement.entity.Payslip;
import nhom07.HumanResourceManagement.service.AdminPayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/payroll")
public class AdminPayrollController {

    @Autowired
    private AdminPayrollService payrollService;

    // TÍNH NĂNG MỚI: Lấy cấu hình lương hiện tại của nhân viên
    @GetMapping("/configs/{employeeId}")
    public ResponseEntity<?> getConfig(@PathVariable Integer employeeId) {
        try {
            PayrollConfig config = payrollService.getConfigByEmployeeId(employeeId);
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/configs/{employeeId}")
    public ResponseEntity<?> setupConfig(Authentication auth, @PathVariable Integer employeeId, @RequestBody PayrollConfig config) {
        try {
            PayrollConfig saved = payrollService.savePayrollConfig(auth.getName(), employeeId, config);
            return ResponseEntity.ok(Map.of("message", "Đã lưu cấu hình lương thành công.", "config", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/calculate/{employeeId}")
    public ResponseEntity<?> calculateSalary(Authentication auth, @PathVariable Integer employeeId, @RequestBody Map<String, String> request) {
        try {
            String monthYear = request.get("monthYear");
            BigDecimal standardDays = new BigDecimal(request.get("standardDays"));
            BigDecimal actualWorkingDays = new BigDecimal(request.get("actualWorkingDays"));

            Payslip payslip = payrollService.calculateSalary(auth.getName(), employeeId, monthYear, standardDays, actualWorkingDays);
            return ResponseEntity.ok(Map.of("message", "Tính lương và chốt bảng lương thành công.", "payslip", payslip));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/calculate-all")
    public ResponseEntity<?> calculateSalaryForAll(Authentication auth, @RequestBody Map<String, String> request) {
        try {
            String monthYear = request.get("monthYear");
            BigDecimal standardDays = new BigDecimal(request.get("standardDays"));

            payrollService.calculateSalaryForAll(auth.getName(), monthYear, standardDays);
            return ResponseEntity.ok(Map.of("message", "Đã chạy script tính lương tự động cho toàn bộ nhân sự."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}