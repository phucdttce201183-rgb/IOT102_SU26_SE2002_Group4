CREATE DATABASE [SpeedTrapDB];
GO

USE [SpeedTrapDB];
GO

CREATE TABLE [violation_records] (
    [id] INT IDENTITY(1,1) PRIMARY KEY,
    [station_id] VARCHAR(50) NOT NULL,
    [recorded_speed] FLOAT NOT NULL,
    [violation_timestamp] DATETIME DEFAULT GETDATE()
);
GO
 ALTER TABLE [violation_records]
ALTER COLUMN [recorded_speed] DECIMAL(5,1) NOT NULL;

GO
-- 5. Truy vấn kiểm tra dữ liệu
SELECT * FROM [violation_records];
GO