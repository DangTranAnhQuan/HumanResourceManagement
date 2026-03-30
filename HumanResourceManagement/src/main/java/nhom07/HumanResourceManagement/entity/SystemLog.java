package nhom07.HumanResourceManagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "System_Logs")
@Data
public class SystemLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LogID")
    private Integer logID;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @Column(name = "Action", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String action;

    @Column(name = "IPAddress", length = 50)
    private String ipAddress;

    @Column(name = "Timestamp", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime timestamp = LocalDateTime.now();
}