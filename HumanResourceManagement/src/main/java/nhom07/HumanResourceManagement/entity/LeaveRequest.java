package nhom07.HumanResourceManagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Leave_Requests")
@Data
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequestID")
    private Integer requestID;

    @ManyToOne
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employee;

    @Column(name = "LeaveType", nullable = false, length = 50)
    private String leaveType;

    @Column(name = "StartDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "EndDate", nullable = false)
    private LocalDate endDate;

    @Column(name = "Reason", columnDefinition = "NVARCHAR(MAX)")
    private String reason;

    @Column(name = "Status", length = 50)
    private String status = "Chờ duyệt";

    @ManyToOne
    @JoinColumn(name = "ApproverID")
    private Employee approver; // Người duyệt đơn

    @Column(name = "RejectReason", columnDefinition = "NVARCHAR(MAX)")
    private String rejectReason;

    @Column(name = "CreatedAt", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;
}