package nhom07.HumanResourceManagement.controller;

import nhom07.HumanResourceManagement.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Đăng nhập hệ thống
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        String result = authService.login(email, password);

        if ("REQUIRE_PASSWORD_CHANGE".equals(result)) {
            // Cờ IsFirstLogin = true -> Trả về mã lỗi đặc biệt để Front-end ép chuyển trang đổi mật khẩu
            return ResponseEntity.status(403).body(Map.of("message", "Vui lòng đổi mật khẩu ở lần đăng nhập đầu tiên.", "status", "REQUIRE_PASSWORD_CHANGE"));
        }

        if (result.contains("thành công")) {
            return ResponseEntity.ok(Map.of("message", result));
        }

        return ResponseEntity.badRequest().body(Map.of("error", result));
    }

    // Đặt lại mật khẩu lần đầu
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        boolean success = authService.changeFirstPassword(email, newPassword);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "Đổi mật khẩu thành công. Bạn có thể sử dụng hệ thống."));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Đổi mật khẩu thất bại hoặc tài khoản đã được đổi mật khẩu."));
    }
}