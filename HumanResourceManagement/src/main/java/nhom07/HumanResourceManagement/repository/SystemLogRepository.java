package nhom07.HumanResourceManagement.repository;

import nhom07.HumanResourceManagement.entity.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Integer> {
}