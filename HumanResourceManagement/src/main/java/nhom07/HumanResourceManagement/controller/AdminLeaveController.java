package nhom07.HumanResourceManagement.controller;

import nhom07.HumanResourceManagement.entity.LeaveRequest;
import nhom07.HumanResourceManagement.service.AdminLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/leave-requests")
public class AdminLeaveController {

    @Autowired
    private AdminLeaveService adminLeaveService;

    // Lấy toàn bộ đơn xin nghỉ phép của công ty
    @GetMapping
    public ResponseEntity<?> getAllRequests() {
        return ResponseEntity.ok(adminLeaveService.getAllLeaveRequests());
    }

    // Phê duyệt đơn
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approve(Authentication auth, @PathVariable Integer id) {
        try {
            LeaveRequest approved = adminLeaveService.approveRequest(auth.getName(), id);
            return ResponseEntity.ok(Map.of("message", "Đã phê duyệt đơn thành công.", "status", approved.getStatus()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Từ chối đơn (kèm lý do)
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> reject(Authentication auth, @PathVariable Integer id, @RequestBody Map<String, String> body) {
        try {
            String rejectReason = body.getOrDefault("rejectReason", "Không có lý do");
            LeaveRequest rejected = adminLeaveService.rejectRequest(auth.getName(), id, rejectReason);
            return ResponseEntity.ok(Map.of("message", "Đã từ chối đơn.", "status", rejected.getStatus()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}