package nhom07.HumanResourceManagement.service;

import nhom07.HumanResourceManagement.entity.*;
import nhom07.HumanResourceManagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    @Autowired private UserRepository userRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private PositionRepository positionRepository;
    @Autowired private SystemLogRepository systemLogRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Transactional
    public Employee createNewEmployee(String adminEmail, String email, String fullName, String cccd, Integer roleId, Integer departmentId, Integer positionId) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã tồn tại trong hệ thống.");
        }
        if (employeeRepository.existsByCccd(cccd)) {
            throw new RuntimeException("CCCD đã tồn tại.");
        }

        User user = new User();
        user.setEmail(email);

        String defaultPassword = "Hcmute@" + cccd.substring(Math.max(0, cccd.length() - 4));
        user.setPasswordHash(passwordEncoder.encode(defaultPassword));
        user.setIsFirstLogin(true);
        user.setStatus("Active");

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Không tìm thấy Role ID: " + roleId));
        user.setRole(role);

        User savedUser = userRepository.save(user);

        Employee employee = new Employee();
        employee.setUser(savedUser);
        employee.setFullName(fullName);
        employee.setCccd(cccd);
        employee.setRemainingLeaveDays(12); // Mặc định 12 ngày phép

        if (departmentId != null) {
            Department dept = departmentRepository.findById(departmentId).orElse(null);
            employee.setDepartment(dept);
        }

        if (positionId != null) {
            Position pos = positionRepository.findById(positionId).orElse(null);
            employee.setPosition(pos);
        }

        Employee savedEmployee = employeeRepository.save(employee);

        logSystemAction(adminEmail, "Thêm mới hồ sơ nhân viên và cấp tài khoản: " + email);

        return savedEmployee;
    }

    private void logSystemAction(String email, String action) {
        User admin = userRepository.findByEmail(email).orElse(null);
        if (admin != null) {
            SystemLog log = new SystemLog();
            log.setUser(admin);
            log.setAction(action);
            systemLogRepository.save(log);
        }
    }
}