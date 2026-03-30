USE QuanLyNhanSu;
GO

-- 1. Thêm Roles
INSERT INTO Roles (RoleName, Description) VALUES 
(N'Admin', N'Quản trị viên toàn quyền hệ thống'), 
(N'Employee', N'Nhân viên thông thường');
GO

-- 2. Thêm Users (Tất cả pass là 123456: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi)
INSERT INTO Users (RoleID, Email, PasswordHash, Status, IsFirstLogin) VALUES
(1, 'quandta@congty.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', N'Active', 0),
(2, 'phunt@congty.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', N'Active', 1),
(2, 'dieptq@congty.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', N'Active', 1),
(2, 'thienta@congty.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', N'Active', 1),
(2, 'ketoan@congty.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', N'Active', 0);
GO

-- 3. Thêm Positions
INSERT INTO Positions (PositionName, Description, JobLevel, Status) VALUES
(N'Giám đốc điều hành', N'Điều hành toàn bộ công ty', N'C-Level', N'Đang hoạt động'),
(N'Trưởng phòng IT', N'Quản lý team công nghệ', N'Manager', N'Đang hoạt động'),
(N'Lập trình viên Backend', N'Code Spring Boot / Java', N'Senior', N'Đang hoạt động'),
(N'Lập trình viên Frontend', N'Code React / HTML CSS', N'Junior', N'Đang hoạt động'),
(N'Kế toán tổng hợp', N'Tính lương, khai thuế', N'Senior', N'Đang hoạt động');
GO

-- 4. Thêm Departments (Tạm thời chưa gán ManagerID vì nhân viên chưa được tạo)
INSERT INTO Departments (DepartmentName, Description, Location, PhoneNumber, Status) VALUES
(N'Ban Giám Đốc', N'Bộ máy lãnh đạo', N'Tầng 10, Tòa nhà A', '028.111.2222', N'Đang hoạt động'),
(N'Phòng Công Nghệ', N'Phát triển phần mềm', N'Tầng 9, Tòa nhà A', '028.111.3333', N'Đang hoạt động'),
(N'Phòng Kế Toán', N'Tài chính & C&B', N'Tầng 8, Tòa nhà A', '028.111.4444', N'Đang hoạt động');
GO

-- 5. Thêm Employees (Hồ sơ siêu đầy đủ)
INSERT INTO Employees (UserID, DepartmentID, PositionID, FullName, DateOfBirth, Gender, CCCD, TaxCode, SocialInsuranceNumber, EducationLevel, ContractType, PhoneNumber, CurrentAddress, EmergencyContactName, EmergencyContactPhone, JoinDate, BankAccount, BankName, RemainingLeaveDays) VALUES
(1, 1, 1, N'Đặng Trần Anh Quân', '2003-01-15', N'Nam', '079203000001', '8123456789', 'BH99887766', N'Thạc sĩ', N'Vô thời hạn', '0901234567', N'Thủ Đức, TP.HCM', N'Trần Thị B', '0912333444', '2023-01-01', '19033334444', N'Techcombank', 15),
(2, 2, 2, N'Nguyễn Thuận Phú', '2003-05-20', N'Nam', '079203000002', '8123456790', 'BH99887767', N'Đại học', N'Vô thời hạn', '0987654321', N'Bình Thạnh, TP.HCM', N'Nguyễn Văn C', '0988777666', '2024-05-10', '0123456789', N'Vietcombank', 12),
(3, 2, 3, N'Trương Quang Điệp', '2003-08-10', N'Nam', '079203000003', '8123456791', 'BH99887768', N'Đại học', N'Chính thức 3 năm', '0911222333', N'Quận 9, TP.HCM', N'Trương Văn D', '0911222111', '2024-06-15', '9876543210', N'MB Bank', 12),
(4, 2, 4, N'Trần An Thiên', '2003-11-25', N'Nam', '079203000004', '8123456792', 'BH99887769', N'Đại học', N'Chính thức 1 năm', '0944555666', N'Dĩ An, Bình Dương', N'Trần Văn E', '0944555444', '2025-01-01', '5556667778', N'ACB', 12),
(5, 3, 5, N'Nguyễn Kế Toán', '1995-10-10', N'Nữ', '079195000005', '8123456793', 'BH99887770', N'Đại học', N'Vô thời hạn', '0922333444', N'Quận 3, TP.HCM', N'Lê Thị F', '0922333222', '2022-02-01', '1112223334', N'Vietinbank', 14);
GO

-- Cập nhật lại ManagerID cho các phòng ban sau khi đã có Employee
UPDATE Departments SET ManagerID = 1 WHERE DepartmentID = 1;
UPDATE Departments SET ManagerID = 2 WHERE DepartmentID = 2;
UPDATE Departments SET ManagerID = 5 WHERE DepartmentID = 3;
GO

-- 6. Thêm Leave_Requests
INSERT INTO Leave_Requests (EmployeeID, LeaveType, StartDate, EndDate, Reason, Status, ApproverID) VALUES
(3, N'Nghỉ phép năm', '2026-04-10', '2026-04-11', N'Về quê thăm gia đình', N'Đã duyệt', 1),
(4, N'Nghỉ ốm', '2026-04-12', '2026-04-12', N'Sốt xuất huyết', N'Chờ duyệt', NULL);
GO

-- 7. Thêm Payroll_Config (Bổ sung người phụ thuộc & Thưởng)
INSERT INTO Payroll_Config (EmployeeID, BasicSalary, LunchAllowance, TravelAllowance, OtherBonus, InsuranceRate, DependentDeduction) VALUES
(1, 60000000.00, 2000000.00, 2000000.00, 5000000.00, 10.5, 2), -- 2 người phụ thuộc
(2, 40000000.00, 1500000.00, 1000000.00, 2000000.00, 10.5, 1),
(3, 25000000.00, 1000000.00, 500000.00, 1000000.00, 10.5, 0),
(4, 15000000.00, 1000000.00, 500000.00, 500000.00, 10.5, 0),
(5, 20000000.00, 1000000.00, 500000.00, 1000000.00, 10.5, 1);
GO

-- 8. Thêm Payslips (Bảng lương có tính Thuế TNCN)
INSERT INTO Payslips (EmployeeID, MonthYear, ActualWorkingDays, BasicSalary, TotalAllowance, TotalDeduction, TaxAmount, NetSalary, PaymentStatus, PaymentDate) VALUES
(2, '03/2026', 22.00, 40000000.00, 4500000.00, 4200000.00, 2500000.00, 37800000.00, N'Đã chuyển khoản', '2026-04-05'),
(3, '03/2026', 22.00, 25000000.00, 2500000.00, 2625000.00, 800000.00, 24075000.00, N'Đã chuyển khoản', '2026-04-05');
GO

-- 9. Thêm System_Logs
INSERT INTO System_Logs (UserID, Action, IPAddress) VALUES
(1, N'Đăng nhập vào hệ thống', '192.168.1.100'),
(1, N'Duyệt đơn xin phép của Trương Quang Điệp', '192.168.1.100');
GO