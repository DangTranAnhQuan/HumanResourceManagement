package nhom07.HumanResourceManagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Departments")
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DepartmentID")
    private Integer departmentID;

    @Column(name = "DepartmentName", nullable = false, length = 100)
    private String departmentName;

    @Column(name = "Description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @ManyToOne
    @JoinColumn(name = "ManagerID")
    @JsonIgnore
    private Employee manager; // Khóa ngoại trỏ đến Employee

    @Column(name = "Location", length = 255)
    private String location;

    @Column(name = "PhoneNumber", length = 20)
    private String phoneNumber;

    @Column(name = "Status", length = 50)
    private String status = "Đang hoạt động";

    @Column(name = "CreatedAt", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}