package nhom07.HumanResourceManagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "Payroll_Config")
@Data
public class PayrollConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ConfigID")
    private Integer configID;

    @OneToOne
    @JoinColumn(name = "EmployeeID", nullable = false, unique = true)
    private Employee employee;

    @Column(name = "BasicSalary", nullable = false, precision = 18, scale = 2)
    private BigDecimal basicSalary;

    @Column(name = "LunchAllowance", precision = 18, scale = 2)
    private BigDecimal lunchAllowance = BigDecimal.ZERO;

    @Column(name = "TravelAllowance", precision = 18, scale = 2)
    private BigDecimal travelAllowance = BigDecimal.ZERO;

    @Column(name = "OtherBonus", precision = 18, scale = 2)
    private BigDecimal otherBonus = BigDecimal.ZERO;

    @Column(name = "InsuranceRate", precision = 5, scale = 2)
    private BigDecimal insuranceRate = new BigDecimal("10.5");

    @Column(name = "DependentDeduction")
    private Integer dependentDeduction = 0;

    @Column(name = "OtherDeduction", precision = 18, scale = 2)
    private BigDecimal otherDeduction = BigDecimal.ZERO;
}