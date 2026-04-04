package nhom07.HumanResourceManagement.repository;

import nhom07.HumanResourceManagement.entity.Employee;
import nhom07.HumanResourceManagement.entity.PayrollConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayrollConfigRepository extends JpaRepository<PayrollConfig, Integer> {
    Optional<PayrollConfig> findByEmployee(Employee employee);
}