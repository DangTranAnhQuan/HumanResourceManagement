-- Xóa Database cũ nếu đang tồn tại
IF EXISTS (SELECT name FROM sys.databases WHERE name = N'QuanLyNhanSu')
BEGIN
    ALTER DATABASE QuanLyNhanSu SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE QuanLyNhanSu;
END
GO

CREATE DATABASE QuanLyNhanSu;
GO

USE QuanLyNhanSu;
GO

-- 1. Bảng Roles: Quản lý phân quyền
CREATE TABLE Roles (
    RoleID INT PRIMARY KEY IDENTITY(1,1),
    RoleName NVARCHAR(50) NOT NULL,
    Description NVARCHAR(255) -- MỚI: Mô tả quyền hạn
);
GO

-- 2. Bảng Users: Tài khoản đăng nhập
CREATE TABLE Users (
    UserID INT PRIMARY KEY IDENTITY(1,1),
    RoleID INT NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    PasswordHash VARCHAR(255) NOT NULL,
    Status NVARCHAR(20) DEFAULT N'Active', 
    IsFirstLogin BIT DEFAULT 1,
    CreatedAt DATETIME DEFAULT GETDATE(), 
    UpdatedAt DATETIME, -- MỚI: Lần đổi mật khẩu/cập nhật cuối
    LastLogin DATETIME, -- MỚI: Ghi nhận lần đăng nhập gần nhất
    FOREIGN KEY (RoleID) REFERENCES Roles(RoleID)
);
GO

-- 3. Bảng Departments: Phòng ban
CREATE TABLE Departments (
    DepartmentID INT PRIMARY KEY IDENTITY(1,1),
    DepartmentName NVARCHAR(100) NOT NULL,
    Description NVARCHAR(MAX),
    ManagerID INT, -- MỚI: Ai là trưởng phòng (Lưu ID của Employee)
    Location NVARCHAR(255), -- MỚI: Tòa nhà / Chi nhánh
    PhoneNumber VARCHAR(20), -- MỚI: SĐT nội bộ của phòng
    Status NVARCHAR(50) DEFAULT N'Đang hoạt động',
    CreatedAt DATETIME DEFAULT GETDATE()
);
GO

-- 4. Bảng Positions: Chức vụ 
CREATE TABLE Positions (
    PositionID INT PRIMARY KEY IDENTITY(1,1),
    PositionName NVARCHAR(100) NOT NULL,
    Description NVARCHAR(MAX), 
    JobLevel NVARCHAR(50), -- MỚI: Cấp bậc (Intern, Fresher, Junior, Senior, Manager, Director)
    Status NVARCHAR(50) DEFAULT N'Đang hoạt động'
);
GO

-- 5. Bảng Employees: Hồ sơ nhân viên (Đầy đủ như thực tế)
CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT UNIQUE NOT NULL,
    DepartmentID INT,
    PositionID INT, 
    FullName NVARCHAR(100) NOT NULL,
    AvatarLink NVARCHAR(255), -- MỚI: Link ảnh đại diện
    DateOfBirth DATE,
    Gender NVARCHAR(10),
    CCCD VARCHAR(20) UNIQUE NOT NULL,
    TaxCode VARCHAR(50), -- MỚI: Mã số thuế cá nhân
    SocialInsuranceNumber VARCHAR(50), -- MỚI: Số bảo hiểm xã hội
    EducationLevel NVARCHAR(100), -- MỚI: Trình độ học vấn (Đại học, Thạc sĩ...)
    ContractType NVARCHAR(50),
    ContractFileLink NVARCHAR(255),
    PhoneNumber VARCHAR(20), 
    CurrentAddress NVARCHAR(255), 
    EmergencyContactName NVARCHAR(100), -- MỚI: Tên người liên hệ khẩn cấp
    EmergencyContactPhone VARCHAR(20), -- MỚI: SĐT người liên hệ khẩn cấp
    JoinDate DATE, 
    EndDate DATE, -- MỚI: Ngày nghỉ việc (Nếu có)
    BankAccount VARCHAR(50), 
    BankName NVARCHAR(100), 
    RemainingLeaveDays INT DEFAULT 12,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID),
    FOREIGN KEY (PositionID) REFERENCES Positions(PositionID)
);
GO

-- (Thêm Khóa ngoại ManagerID cho bảng Departments tham chiếu ngược lại Employees)
ALTER TABLE Departments 
ADD CONSTRAINT FK_Departments_Employees 
FOREIGN KEY (ManagerID) REFERENCES Employees(EmployeeID);
GO

-- 6. Bảng Leave_Requests: Xin nghỉ phép
CREATE TABLE Leave_Requests (
    RequestID INT PRIMARY KEY IDENTITY(1,1),
    EmployeeID INT NOT NULL,
    LeaveType NVARCHAR(50) NOT NULL, 
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    Reason NVARCHAR(MAX),
    Status NVARCHAR(50) DEFAULT N'Chờ duyệt', 
    ApproverID INT, -- MỚI: ID của Admin/HR đã duyệt hoặc từ chối đơn này
    RejectReason NVARCHAR(MAX),
    CreatedAt DATETIME DEFAULT GETDATE(), 
    UpdatedAt DATETIME, -- MỚI: Thời gian được duyệt/từ chối
    FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID),
    FOREIGN KEY (ApproverID) REFERENCES Employees(EmployeeID)
);
GO

-- 7. Bảng Payroll_Config: Cấu hình lương
CREATE TABLE Payroll_Config (
    ConfigID INT PRIMARY KEY IDENTITY(1,1),
    EmployeeID INT UNIQUE NOT NULL,
    BasicSalary DECIMAL(18,2) NOT NULL, 
    LunchAllowance DECIMAL(18,2) DEFAULT 0,
    TravelAllowance DECIMAL(18,2) DEFAULT 0,
    OtherBonus DECIMAL(18,2) DEFAULT 0, -- MỚI: Phụ cấp/Thưởng khác
    InsuranceRate DECIMAL(5,2) DEFAULT 10.5, -- Mặc định đóng BHXH 10.5%
    DependentDeduction INT DEFAULT 0, -- MỚI: Số lượng người phụ thuộc (để giảm trừ gia cảnh)
    OtherDeduction DECIMAL(18,2) DEFAULT 0, -- MỚI: Các khoản trừ khác (Phạt, tạm ứng...)
    FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID)
);
GO

-- 8. Bảng Payslips: Bảng lương chi tiết
CREATE TABLE Payslips (
    PayslipID INT PRIMARY KEY IDENTITY(1,1),
    EmployeeID INT NOT NULL,
    MonthYear VARCHAR(7) NOT NULL,
    ActualWorkingDays DECIMAL(5,2) NOT NULL,
    BasicSalary DECIMAL(18,2) NOT NULL,
    TotalAllowance DECIMAL(18,2) NOT NULL,
    TotalDeduction DECIMAL(18,2) NOT NULL,
    TaxAmount DECIMAL(18,2) DEFAULT 0, -- MỚI: Tiền Thuế Thu nhập cá nhân phải nộp
    NetSalary DECIMAL(18,2) NOT NULL,
    PaymentStatus NVARCHAR(50) DEFAULT N'Chờ chuyển khoản', 
    PaymentDate DATETIME, -- MỚI: Ngày thực tế kế toán bấm chuyển khoản
    CreatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID)
);
GO

-- 9. Bảng System_Logs: Ghi nhận nhật ký hệ thống
CREATE TABLE System_Logs (
    LogID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    Action NVARCHAR(MAX) NOT NULL,
    IPAddress VARCHAR(50), -- MỚI: Lưu lại IP của máy tính thực hiện thao tác
    Timestamp DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO