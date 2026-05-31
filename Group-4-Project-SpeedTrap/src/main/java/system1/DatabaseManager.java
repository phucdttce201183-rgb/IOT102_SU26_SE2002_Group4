package system1;

import model1.SpeedViolation;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DatabaseManager {
    // Chuỗi kết nối đến SQL Server
    private final String connectionUrl = 
            "jdbc:sqlserver://localhost:1433;" +
            "databaseName=SpeedTrapDB;" +
            "encrypt=true;trustServerCertificate=true;";
    
    private final String user = "sa"; 
    private final String password = "123456"; 

    // Phương thức lưu bản ghi vi phạm vào bảng
    public void forwardDataDataBase(SpeedViolation violation) {
        String sqlQuery = "INSERT INTO violation_records (station_id, recorded_speed) VALUES (?, ?)";
        
        try {
            // --- DÒNG LỆNH BẮT BUỘC THÊM VÀO CHO WEB SERVER ---
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            try (Connection conn = DriverManager.getConnection(connectionUrl, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
                
                // Gán dữ liệu vào các dấu chấm hỏi (?)
                pstmt.setString(1, violation.getStationId());
                pstmt.setDouble(2, violation.getSpeed());
                
                // Thực thi câu lệnh
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                   System.out.println("[DB_SUCCESS] Da luu thanh cong: Tram "
                           + violation.getStationId() + " | Toc do: "
                           + violation.getSpeed() + " km/h");
                }
            }
        } catch (Exception e) {
            System.out.println("-> [DATABASE ERROR]: Ket noi that bai! Chi tiet: " + e.getMessage());
        }
    }
}