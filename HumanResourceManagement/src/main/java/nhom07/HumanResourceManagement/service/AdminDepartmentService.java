package nhom07.HumanResourceManagement.service;

import nhom07.HumanResourceManagement.entity.Department;
import nhom07.HumanResourceManagement.entity.Employee;
import nhom07.HumanResourceManagement.entity.SystemLog;
import nhom07.HumanResourceManagement.entity.User;
import nhom07.HumanResourceManagement.repository.DepartmentRepository;
import nhom07.HumanResourceManagement.repository.EmployeeRepository;
import nhom07.HumanResourceManagement.repository.SystemLogRepository;
import nhom07.HumanResourceManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminDepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Transactional
    public Department createDepartment(String emailAdmin, Department department) {
        Department savedDept = departmentRepository.save(department);
        logAction(emailAdmin, "Thêm mới phòng ban: " + department.getDepartmentName());
        return savedDept;
    }

    @Transactional
    public Department updateDepartment(String emailAdmin, Integer id, Department details) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng ban."));

        dept.setDepartmentName(details.getDepartmentName());
        dept.setDescription(details.getDescription());
        dept.setStatus(details.getStatus());

        Department updatedDept = departmentRepository.save(dept);
        logAction(emailAdmin, "Cập nhật phòng ban ID " + id);
        return updatedDept;
    }

    @Transactional
    public void deleteDepartment(String emailAdmin, Integer id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng ban."));

        // Kiểm tra xem có nhân viên nào đang thuộc phòng ban này không (Quy tắc nghiệp vụ)
        // Nếu dùng Spring Data JPA chuẩn, ta có thể duyệt qua tất cả nhân viên để check tạm.
        // (Để tối ưu hơn, bạn có thể thêm hàm existsByDepartment vào EmployeeRepository sau)
        List<Employee> employees = employeeRepository.findAll();
        boolean hasEmployees = employees.stream()
                .anyMatch(emp -> emp.getDepartment() != null && emp.getDepartment().getDepartmentID().equals(id));

        if (hasEmployees) {
            throw new RuntimeException("Không thể xóa phòng ban đang có nhân viên trực thuộc.");
        }

        departmentRepository.delete(dept);
        logAction(emailAdmin, "Xóa phòng ban: " + dept.getDepartmentName());
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