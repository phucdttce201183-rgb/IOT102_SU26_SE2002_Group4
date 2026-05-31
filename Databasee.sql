CREATE TABLE Vehicle_Owner (
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