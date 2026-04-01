package nhom07.HumanResourceManagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Payslips")
@Data
public class Payslip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PayslipID")
    private Integer payslipID;

    @ManyToOne
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employee;

    @Column(name = "MonthYear", nullable = false, length = 7)
    private String monthYear;

    @Column(name = "ActualWorkingDays", nullable = false, precision = 5, scale = 2)
    private BigDecimal actualWorkingDays;

    @Column(name = "BasicSalary", nullable = false, precision = 18, scale = 2)
    private BigDecimal basicSalary;

    @Column(name = "TotalAllowance", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAllowance;

    @Column(name = "TotalDeduction", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalDeduction;

    @Column(name = "TaxAmount", precision = 18, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "NetSalary", nullable = false, precision = 18, scale = 2)
    private BigDecimal netSalary;

    @Column(name = "PaymentStatus", length = 50)
    private String paymentStatus = "Chờ chuyển khoản";

    @Column(name = "PaymentDate")
    private LocalDateTime paymentDate;

    @Column(name = "CreatedAt", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}