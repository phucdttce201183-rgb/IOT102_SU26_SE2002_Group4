CREATE DATABASE [SmartTrafficMonitor];
GO

USE [SmartTrafficMonitor];
GO


-- Bảng quản lý tuyến đường
CREATE TABLE Streets (
    street_id INT PRIMARY KEY,
    street_name NVARCHAR(255),
    speed_limit INT
);

-- Bảng lưu lịch sử cảnh báo (Đã bỏ cột license_plate)
CREATE TABLE Warning_Logs (
    log_id INT IDENTITY(1,1) PRIMARY KEY, -- ID tự tăng
    street_id INT FOREIGN KEY REFERENCES Streets(street_id),
    recorded_speed INT,
    timestamp DATETIME -- Thời gian vi phạm
);
<<<<<<< HEAD

UPDATE Streets SET speed_limit = 2 WHERE street_id = 1;

-- 2. Thêm dữ liệu các tuyến đường (Bảng cha)
INSERT INTO Streets (street_id, street_name, speed_limit) VALUES
(1, N'Phạm Văn Đồng', 60),
(2, N'Nguyễn Văn Linh', 50),
(3, N'Quốc Lộ 1A', 80),
(4, N'Võ Văn Kiệt', 60);
GO

-- 3. Thêm dữ liệu các lượt vi phạm (Bảng con)
INSERT INTO Warning_Logs (street_id, recorded_speed, timestamp) VALUES
-- Đường Phạm Văn Đồng (ID=1)
(1, 65, '2026-06-04 08:15:00'),
(1, 72, '2026-06-04 14:05:00'),
(1, 80, '2026-06-04 14:20:00'),
(1, 68, '2026-06-04 14:45:00'),
(1, 62, '2026-06-04 19:30:00'),

-- Đường Nguyễn Văn Linh (ID=2)
(2, 55, '2026-06-04 07:30:00'),
(2, 60, '2026-06-04 18:45:00'),

-- Dữ liệu ngày hôm qua (để test bộ lọc)
(1, 70, '2026-06-03 10:00:00'),
(3, 85, '2026-06-03 11:30:00'),
(4, 65, '2026-06-03 22:15:00');
GO

SELECT * FROM Warning_Logs;
=======
=======
﻿CREATE TABLE Vehicle_Owner (
	LicensePlate VARCHAR(20) PRIMARY KEY,
	FullName NVARCHAR(100) NOT NULL,
	CitizenID VARCHAr(20) UNIQUE NOT NULL,
	[Address] NVARCHAR(255)
);

CREATE TABLE Speed_Station(
	StationID VARCHAR(50) PRIMARY KEY,
	LocationName NVARCHAR(255) NOT NULL,
	SpeedLimit FLOAT NOT NULL
);

CREATE TABLE Traffic_Ticket(
	TicketID INT IDENTITY(1,1) PRIMARY KEY,

	LicensePlate VARCHAR(20) FOREIGN KEY REFERENCES Vehicle_Owner(LicensePlate),
	StationID VARCHAR(50) FOREIGN KEY REFERENCES Speed_Station(StationID),

	RecordedSpeed FLOAT NOT NULL,
	ViolationTime DATETIME DEFAULT GETDATE(),
	Status INT DEFAULT 0
)

-- Thêm 1 trạm đo tốc độ giả lập (Giống với ID khai báo trong mạch C++)
INSERT INTO Speed_Station (StationID, LocationName, SpeedLimit)
VALUES ('TRAM_WIFI_01', N'QL1A - Đường nội bộ', 5.0);

-- Thêm 5 phương tiện mẫu vào hệ thống
INSERT INTO Vehicle_Owner (LicensePlate, FullName, CitizenID, Address)
VALUES 
('65A-111.11', N'Nguyễn Văn An', '087203001234', N'Ninh Kiều, Cần Thơ'),
('65A-222.22', N'Trần Thị Bích', '087203001235', N'Bình Thủy, Cần Thơ'),
('65A-333.33', N'Lê Hoàng Cường', '087203001236', N'Cái Răng, Cần Thơ'),
('65A-444.44', N'Phạm Đại Dương', '087203001237', N'Phong Điền, Cần Thơ'),
('65A-555.55', N'Võ Tường E', '087203001238', N'Ô Môn, Cần Thơ');

-- Thêm thử 1 giấy phạt thủ công để xem các bảng liên kết ra sao
INSERT INTO Traffic_Ticket (LicensePlate, StationID, RecordedSpeed)
VALUES ('65A-111.11', 'TRAM_WIFI_01', 8.5);
>>>>>>> 851a31d2b40f0bc01173700058a51a408c388102
