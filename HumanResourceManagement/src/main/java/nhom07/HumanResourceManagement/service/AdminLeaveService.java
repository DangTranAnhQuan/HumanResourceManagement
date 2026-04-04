package nhom07.HumanResourceManagement.service;

import nhom07.HumanResourceManagement.entity.Employee;
import nhom07.HumanResourceManagement.entity.LeaveRequest;
import nhom07.HumanResourceManagement.entity.SystemLog;
import nhom07.HumanResourceManagement.entity.User;
import nhom07.HumanResourceManagement.repository.EmployeeRepository;
import nhom07.HumanResourceManagement.repository.LeaveRequestRepository;
import nhom07.HumanResourceManagement.repository.SystemLogRepository;
import nhom07.HumanResourceManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AdminLeaveService {

    @Autowired private LeaveRequestRepository leaveRequestRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private SystemLogRepository systemLogRepository;
    @Autowired private UserRepository userRepository;

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    // Logic duyệt và trừ phép
    @Transactional
    public LeaveRequest approveRequest(String emailAdmin, Integer requestId) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn xin nghỉ phép."));

        if (!"Chờ duyệt".equals(request.getStatus())) {
            throw new RuntimeException("Chỉ có thể duyệt đơn đang ở trạng thái 'Chờ duyệt'.");
        }

        // Trừ ngày phép nếu là "Nghỉ phép năm"
        if ("Nghỉ phép năm".equalsIgnoreCase(request.getLeaveType())) {
            Employee employee = request.getEmployee();
            long daysToDeduct = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;

            int remaining = employee.getRemainingLeaveDays() - (int) daysToDeduct;
            if (remaining < 0) {
                throw new RuntimeException("Nhân viên không đủ số ngày phép để duyệt đơn này.");
            }
            employee.setRemainingLeaveDays(remaining);
            employeeRepository.save(employee); // Cập nhật lại hồ sơ
        }

        request.setStatus("Đã duyệt");
        LeaveRequest saved = leaveRequestRepository.save(request);
        logAction(emailAdmin, "Phê duyệt đơn nghỉ phép ID " + requestId + ". Đã trừ quỹ phép nếu áp dụng.");
        return saved;
    }

    @Transactional
    public LeaveRequest rejectRequest(String emailAdmin, Integer requestId, String rejectReason) {
        LeaveRequest request = leaveRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Không tìm thấy đơn."));
        if (!"Chờ duyệt".equals(request.getStatus())) {
            throw new RuntimeException("Chỉ có thể từ chối đơn đang chờ duyệt.");
        }
        request.setStatus("Bị từ chối");
        request.setRejectReason(rejectReason);
        LeaveRequest saved = leaveRequestRepository.save(request);
        logAction(emailAdmin, "Từ chối đơn nghỉ phép ID " + requestId + ". Lý do: " + rejectReason);
        return saved;
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