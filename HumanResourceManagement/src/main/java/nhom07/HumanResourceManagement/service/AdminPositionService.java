package nhom07.HumanResourceManagement.service;

import nhom07.HumanResourceManagement.entity.Employee;
import nhom07.HumanResourceManagement.entity.Position;
import nhom07.HumanResourceManagement.entity.SystemLog;
import nhom07.HumanResourceManagement.entity.User;
import nhom07.HumanResourceManagement.repository.EmployeeRepository;
import nhom07.HumanResourceManagement.repository.PositionRepository;
import nhom07.HumanResourceManagement.repository.SystemLogRepository;
import nhom07.HumanResourceManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminPositionService {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    @Transactional
    public Position createPosition(String emailAdmin, Position position) {
        Position saved = positionRepository.save(position);
        logAction(emailAdmin, "Thêm mới chức vụ: " + position.getPositionName());
        return saved;
    }

    @Transactional
    public Position updatePosition(String emailAdmin, Integer id, Position details) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chức vụ."));

        position.setPositionName(details.getPositionName());
        position.setStatus(details.getStatus());

        Position updated = positionRepository.save(position);
        logAction(emailAdmin, "Cập nhật chức vụ ID: " + id);
        return updated;
    }

    @Transactional
    public void deletePosition(String emailAdmin, Integer id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chức vụ."));

        // Ràng buộc: Không xóa nếu đang có nhân viên giữ chức vụ này
        List<Employee> employees = employeeRepository.findAll();
        boolean hasEmployees = employees.stream()
                .anyMatch(emp -> emp.getPosition() != null && emp.getPosition().getPositionID().equals(id));

        if (hasEmployees) {
            throw new RuntimeException("Không thể xóa chức vụ đang được gán cho nhân viên.");
        }

        positionRepository.delete(position);
        logAction(emailAdmin, "Xóa chức vụ: " + position.getPositionName());
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