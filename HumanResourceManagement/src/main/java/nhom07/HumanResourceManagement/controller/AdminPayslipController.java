package nhom07.HumanResourceManagement.controller;

import nhom07.HumanResourceManagement.repository.PayslipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminPayslipController {

    @Autowired
    private PayslipRepository payslipRepository;

    // API Lấy toàn bộ lịch sử lương công ty để hiển thị vào bảng
    @GetMapping("/payslips")
    public ResponseEntity<?> getAllPayslipsHistory() {
        return ResponseEntity.ok(payslipRepository.findAll());
    }
}