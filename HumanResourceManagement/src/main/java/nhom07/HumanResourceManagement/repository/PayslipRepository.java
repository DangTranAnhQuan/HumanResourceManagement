package nhom07.HumanResourceManagement.repository;

import nhom07.HumanResourceManagement.entity.Employee;
import nhom07.HumanResourceManagement.entity.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PayslipRepository extends JpaRepository<Payslip, Integer> {
    List<Payslip> findByEmployee(Employee employee);
}