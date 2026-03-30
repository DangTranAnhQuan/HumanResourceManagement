package nhom07.HumanResourceManagement.controller;

import nhom07.HumanResourceManagement.repository.SystemLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/logs")
public class SystemLogController {

    @Autowired
    private SystemLogRepository systemLogRepository;

    @GetMapping
    public ResponseEntity<?> getAllLogs() {
        // Lấy tất cả log hệ thống để hiển thị lên bảng quản trị
        return ResponseEntity.ok(systemLogRepository.findAll());
    }
}