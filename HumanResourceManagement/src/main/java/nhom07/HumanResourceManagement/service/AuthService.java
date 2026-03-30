package nhom07.HumanResourceManagement.service;

import nhom07.HumanResourceManagement.entity.User;
import nhom07.HumanResourceManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return "Sai email hoặc mật khẩu!";
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            return "Sai email hoặc mật khẩu!";
        }

        if ("Khóa".equalsIgnoreCase(user.getStatus())) {
            return "Tài khoản đã bị vô hiệu hóa!";
        }

        // Yêu cầu đổi mật khẩu ở lần đăng nhập đầu tiên
        if (Boolean.TRUE.equals(user.getIsFirstLogin())) {
            return "REQUIRE_PASSWORD_CHANGE";
        }

        return "Đăng nhập thành công với quyền " + user.getRole().getRoleName();
    }

    @Transactional
    public boolean changeFirstPassword(String email, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (Boolean.TRUE.equals(user.getIsFirstLogin())) {
                user.setPasswordHash(passwordEncoder.encode(newPassword));
                user.setIsFirstLogin(false); // Cập nhật trạng thái sau khi đổi
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}