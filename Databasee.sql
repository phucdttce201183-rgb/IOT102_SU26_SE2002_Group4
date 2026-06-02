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
