package nhom07.HumanResourceManagement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Positions")
@Data
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PositionID")
    private Integer positionID;

    @Column(name = "PositionName", nullable = false, length = 100)
    private String positionName;

    @Column(name = "Description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "JobLevel", length = 50)
    private String jobLevel;

    @Column(name = "Status", length = 50)
    private String status = "Đang hoạt động";
}