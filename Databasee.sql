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
