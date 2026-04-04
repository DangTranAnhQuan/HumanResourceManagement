package nhom07.HumanResourceManagement.service;

import nhom07.HumanResourceManagement.entity.*;
import nhom07.HumanResourceManagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class AdminPayrollService {

    @Autowired private PayrollConfigRepository payrollConfigRepository;
    @Autowired private PayslipRepository payslipRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private SystemLogRepository systemLogRepository;
    @Autowired private UserRepository userRepository;

    // TÍNH NĂNG MỚI: Truy xuất cấu hình lương
    public PayrollConfig getConfigByEmployeeId(Integer employeeId) {
        Employee emp = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên."));
        return payrollConfigRepository.findByEmployee(emp).orElseThrow(() -> new RuntimeException("Chưa có cấu hình lương."));
    }

    @Transactional
    public PayrollConfig savePayrollConfig(String adminEmail, Integer employeeId, PayrollConfig configData) {
        Employee emp = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên."));

        PayrollConfig config = payrollConfigRepository.findByEmployee(emp).orElse(new PayrollConfig());
        config.setEmployee(emp);
        config.setBasicSalary(configData.getBasicSalary());
        config.setLunchAllowance(configData.getLunchAllowance());
        config.setTravelAllowance(configData.getTravelAllowance());
        config.setInsuranceRate(configData.getInsuranceRate());
        config.setDependentDeduction(configData.getDependentDeduction());

        PayrollConfig saved = payrollConfigRepository.save(config);
        logAction(adminEmail, "Cập nhật cấu hình lương cho nhân viên ID: " + employeeId);
        return saved;
    }

    @Transactional
    public Payslip calculateSalary(String adminEmail, Integer employeeId, String monthYear, BigDecimal standardDays, BigDecimal actualWorkingDays) {
        Employee emp = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên."));

        PayrollConfig config = payrollConfigRepository.findByEmployee(emp)
                .orElseThrow(() -> new RuntimeException("Nhân viên " + emp.getFullName() + " chưa được cấu hình lương."));

        BigDecimal dailyRate = config.getBasicSalary().divide(standardDays, 2, RoundingMode.HALF_UP);
        BigDecimal workingSalary = dailyRate.multiply(actualWorkingDays);
        BigDecimal totalAllowance = config.getLunchAllowance().add(config.getTravelAllowance());
        BigDecimal deductionRate = config.getInsuranceRate().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal totalDeduction = config.getBasicSalary().multiply(deductionRate);
        BigDecimal netSalary = workingSalary.add(totalAllowance).subtract(totalDeduction);

        Payslip payslip = new Payslip();
        payslip.setEmployee(emp);
        payslip.setMonthYear(monthYear);
        payslip.setActualWorkingDays(actualWorkingDays);
        payslip.setBasicSalary(config.getBasicSalary());
        payslip.setTotalAllowance(totalAllowance);
        payslip.setTotalDeduction(totalDeduction);
        payslip.setNetSalary(netSalary);

        Payslip saved = payslipRepository.save(payslip);
        logAction(adminEmail, "Chốt bảng lương tháng " + monthYear + " cho nhân viên ID: " + employeeId);
        return saved;
    }

    @Transactional
    public void calculateSalaryForAll(String adminEmail, String monthYear, BigDecimal standardDays) {
        List<Employee> allEmployees = employeeRepository.findAll();
        int count = 0;

        for (Employee emp : allEmployees) {
            if ("Active".equalsIgnoreCase(emp.getUser().getStatus())) {
                try {
                    calculateSalary(adminEmail, emp.getEmployeeID(), monthYear, standardDays, standardDays);
                    count++;
                } catch (Exception e) {
                    System.out.println("Bỏ qua NV ID " + emp.getEmployeeID() + ": " + e.getMessage());
                }
            }
        }
        logAction(adminEmail, "Chạy script tính lương tự động hàng loạt cho " + count + " nhân viên, kỳ " + monthYear);
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