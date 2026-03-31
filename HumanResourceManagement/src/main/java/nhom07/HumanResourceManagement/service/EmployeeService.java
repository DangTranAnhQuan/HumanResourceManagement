package nhom07.HumanResourceManagement.service;

import nhom07.HumanResourceManagement.entity.Employee;
import nhom07.HumanResourceManagement.entity.LeaveRequest;
import nhom07.HumanResourceManagement.entity.Payslip;
import nhom07.HumanResourceManagement.entity.User;
import nhom07.HumanResourceManagement.repository.EmployeeRepository;
import nhom07.HumanResourceManagement.repository.LeaveRequestRepository;
import nhom07.HumanResourceManagement.repository.PayslipRepository;
import nhom07.HumanResourceManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired private UserRepository userRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private LeaveRequestRepository leaveRequestRepository;
    @Autowired private PayslipRepository payslipRepository;

    public Employee getProfile(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản."));
        return employeeRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ."));
    }

    // CẬP NHẬT: Thêm tham số liên hệ khẩn cấp
    @Transactional
    public Employee updateContactInfo(String email, String phoneNumber, String currentAddress, String emergencyName, String emergencyPhone) {
        Employee employee = getProfile(email);
        if (phoneNumber != null) employee.setPhoneNumber(phoneNumber);
        if (currentAddress != null) employee.setCurrentAddress(currentAddress);
        if (emergencyName != null) employee.setEmergencyContactName(emergencyName);
        if (emergencyPhone != null) employee.setEmergencyContactPhone(emergencyPhone);
        return employeeRepository.save(employee);
    }

    @Transactional
    public LeaveRequest createLeaveRequest(String email, String leaveType, LocalDate startDate, LocalDate endDate, String reason) {
        Employee employee = getProfile(email);

        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("Ngày bắt đầu không thể lớn hơn ngày kết thúc.");
        }

        long requestedDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if ("Nghỉ phép năm".equalsIgnoreCase(leaveType)) {
            if (employee.getRemainingLeaveDays() < requestedDays) {
                throw new RuntimeException("Quỹ phép năm của bạn không đủ. Bạn chỉ còn " + employee.getRemainingLeaveDays() + " ngày.");
            }
        }

        LeaveRequest request = new LeaveRequest();
        request.setEmployee(employee);
        request.setLeaveType(leaveType);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setReason(reason);
        request.setStatus("Chờ duyệt");

        return leaveRequestRepository.save(request);
    }

    @Transactional
    public boolean cancelLeaveRequest(String email, Integer requestId) {
        Employee employee = getProfile(email);
        LeaveRequest request = leaveRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Không tìm thấy đơn."));
        if (!request.getEmployee().getEmployeeID().equals(employee.getEmployeeID())) {
            throw new RuntimeException("Không có quyền.");
        }
        if (!"Chờ duyệt".equals(request.getStatus())) {
            throw new RuntimeException("Chỉ được hủy đơn đang chờ duyệt.");
        }
        request.setStatus("Đã hủy");
        leaveRequestRepository.save(request);
        return true;
    }

    public List<Payslip> getMyPayslips(String email) { return payslipRepository.findByEmployee(getProfile(email)); }
    public List<LeaveRequest> getMyLeaveHistory(String email) { return leaveRequestRepository.findByEmployee(getProfile(email)); }
}