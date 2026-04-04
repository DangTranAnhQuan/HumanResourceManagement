package nhom07.HumanResourceManagement.service;

import nhom07.HumanResourceManagement.entity.*;
import nhom07.HumanResourceManagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class AdminEmployeeManagementService {

    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private PositionRepository positionRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SystemLogRepository systemLogRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // TÍNH NĂNG MỚI: Lấy 1 nhân viên
    public Employee getEmployeeById(Integer id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
    }

    // CẬP NHẬT: Lưu toàn bộ dữ liệu chi tiết
    @Transactional
    public Employee updateComprehensiveProfile(String adminEmail, Integer employeeId, Map<String, Object> payload) {
        Employee emp = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên."));

        // Cập nhật Phòng ban & Chức vụ
        if (payload.containsKey("department") && payload.get("department") != null) {
            Map<String, Object> deptMap = (Map<String, Object>) payload.get("department");
            Integer deptId = Integer.parseInt(deptMap.get("departmentID").toString());
            emp.setDepartment(departmentRepository.findById(deptId).orElse(null));
        }
        if (payload.containsKey("positionID") && payload.get("positionID") != null) {
            Integer posId = Integer.parseInt(payload.get("positionID").toString());
            emp.setPosition(positionRepository.findById(posId).orElse(null));
        }

        // Cập nhật các trường thông tin cá nhân cơ bản
        if (payload.containsKey("fullName")) emp.setFullName(payload.get("fullName").toString());
        if (payload.containsKey("gender")) emp.setGender(payload.get("gender").toString());
        if (payload.containsKey("birthDate") && !payload.get("birthDate").toString().isEmpty())
            emp.setDateOfBirth(LocalDate.parse(payload.get("birthDate").toString()));
        if (payload.containsKey("phoneNumber")) emp.setPhoneNumber(payload.get("phoneNumber").toString());
        if (payload.containsKey("address")) emp.setCurrentAddress(payload.get("address").toString());
        if (payload.containsKey("emergencyContactName")) emp.setEmergencyContactName(payload.get("emergencyContactName").toString());
        if (payload.containsKey("emergencyContactPhone")) emp.setEmergencyContactPhone(payload.get("emergencyContactPhone").toString());

        // Cập nhật Công việc & Hợp đồng
        if (payload.containsKey("qualification")) emp.setEducationLevel(payload.get("qualification").toString());
        if (payload.containsKey("contractType")) emp.setContractType(payload.get("contractType").toString());
        if (payload.containsKey("hireDate") && !payload.get("hireDate").toString().isEmpty())
            emp.setJoinDate(LocalDate.parse(payload.get("hireDate").toString()));
        if (payload.containsKey("leaveAllowance")) emp.setRemainingLeaveDays(Integer.parseInt(payload.get("leaveAllowance").toString()));

        // Cập nhật Ngân hàng & Thuế
        if (payload.containsKey("bankAccount")) emp.setBankAccount(payload.get("bankAccount").toString());
        if (payload.containsKey("bankName")) emp.setBankName(payload.get("bankName").toString());
        if (payload.containsKey("taxID")) emp.setTaxCode(payload.get("taxID").toString());
        if (payload.containsKey("socialSecurity")) emp.setSocialInsuranceNumber(payload.get("socialSecurity").toString());

        Employee saved = employeeRepository.save(emp);
        logAction(adminEmail, "Cập nhật hồ sơ chi tiết nhân viên ID: " + employeeId);
        return saved;
    }

    @Transactional
    public User toggleAccountStatus(String adminEmail, Integer userId, String status) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản."));
        user.setStatus(status);
        User saved = userRepository.save(user);
        logAction(adminEmail, "Thay đổi trạng thái tài khoản ID " + userId + " thành: " + status);
        return saved;
    }

    @Transactional
    public String resetUserPassword(String adminEmail, Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản."));
        Employee emp = employeeRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ nhân viên."));

        String cccd = emp.getCccd();
        String defaultPassword = "Hcmute@" + cccd.substring(Math.max(0, cccd.length() - 4));
        user.setPasswordHash(passwordEncoder.encode(defaultPassword));
        user.setIsFirstLogin(true);
        userRepository.save(user);

        logAction(adminEmail, "Reset mật khẩu mặc định cho tài khoản ID: " + userId);
        return defaultPassword;
    }

    private void logAction(String email, String action) {
        User admin = userRepository.findByEmail(email).orElse(null);
        if (admin != null) {
            SystemLog log = new SystemLog();
            log.setUser(admin);
            log.setAction(action);
            systemLogRepository.save(log);
        }
    }
}