package nhom07.HumanResourceManagement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleID")
    private Integer roleID;

    @Column(name = "RoleName", nullable = false, length = 50)
    private String roleName;

    @Column(name = "Description", length = 255)
    private String description;
}