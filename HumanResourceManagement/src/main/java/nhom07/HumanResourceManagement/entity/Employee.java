package nhom07.HumanResourceManagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "Employees")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeID")
    private Integer employeeID;

    @OneToOne
    @JoinColumn(name = "UserID", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "DepartmentID")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "PositionID")
    private Position position;

    @Column(name = "FullName", nullable = false, length = 100)
    private String fullName;

    @Column(name = "AvatarLink", length = 255)
    private String avatarLink;

    @Column(name = "DateOfBirth")
    private LocalDate dateOfBirth;

    @Column(name = "Gender", length = 10)
    private String gender;

    @Column(name = "CCCD", nullable = false, unique = true, length = 20)
    private String cccd;

    @Column(name = "TaxCode", length = 50)
    private String taxCode;

    @Column(name = "SocialInsuranceNumber", length = 50)
    private String socialInsuranceNumber;

    @Column(name = "EducationLevel", length = 100)
    private String educationLevel;

    @Column(name = "ContractType", length = 50)
    private String contractType;

    @Column(name = "ContractFileLink", length = 255)
    private String contractFileLink;

    @Column(name = "PhoneNumber", length = 20)
    private String phoneNumber;

    @Column(name = "CurrentAddress", length = 255)
    private String currentAddress;

    @Column(name = "EmergencyContactName", length = 100)
    private String emergencyContactName;

    @Column(name = "EmergencyContactPhone", length = 20)
    private String emergencyContactPhone;

    @Column(name = "JoinDate")
    private LocalDate joinDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @Column(name = "BankAccount", length = 50)
    private String bankAccount;

    @Column(name = "BankName", length = 100)
    private String bankName;

    @Column(name = "RemainingLeaveDays")
    private Integer remainingLeaveDays = 12;
}