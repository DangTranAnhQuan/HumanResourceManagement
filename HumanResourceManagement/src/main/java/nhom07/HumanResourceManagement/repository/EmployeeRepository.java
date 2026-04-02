package nhom07.HumanResourceManagement.repository;

import nhom07.HumanResourceManagement.entity.Employee;
import nhom07.HumanResourceManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // Kiểm tra trùng lặp CCCD khi tạo mới nhân viên
    boolean existsByCccd(String cccd);

    // Tìm hồ sơ nhân viên dựa vào tài khoản User đăng nhập (Sửa lỗi gạch đỏ ở đây)
    Optional<Employee> findByUser(User user);
}