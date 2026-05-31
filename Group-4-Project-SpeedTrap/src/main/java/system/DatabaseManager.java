package system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.TrafficTicket;

public class DatabaseManager {

    // Chuỗi kết nối đến SQL Server
    private final String connectionUrl
            = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=SpeedTrapDB;"
            + "encrypt=true;trustServerCertificate=true;";

    private final String user = "sa";
    private final String password = "123456";

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("[DB] Da tai thanh cong JDBC Driver.");
        } catch (ClassNotFoundException e) {
            System.err.println("[-] Loi thieu file JDBC Driver: " + e.getMessage());
        }
    }

    // Hàm thực hiện kết nối
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl, user, password);
    }

    /**
     * Hàm nhận đối tượng TrafficTicket và lưu xuống CSDL
     */
    public boolean insertTrafficTicket(TrafficTicket ticket) {
        // Câu lệnh SQL (Không cần INSERT TicketID và ViolationTime vì SQL Server tự tạo)
        String sql = "INSERT INTO Traffic_Ticket (LicensePlate, StationID, RecordedSpeed) VALUES (?, ?, ?)";

        // Sử dụng try-with-resources để tự động đóng Connection và PreparedStatement
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Gán các giá trị từ đối tượng Java vào các dấu chấm hỏi (?)
            pstmt.setString(1, ticket.getLicensePlate());
            pstmt.setString(2, ticket.getStationId());
            pstmt.setFloat(3, ticket.getRecordedSpeed());

            // Thực thi lệnh
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("[DB-SUCCESS] Da luu Giay Phat: " + ticket.getLicensePlate() + " | " + ticket.getRecordedSpeed() + " km/h");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("[-] LOI LUU DATABASE: " + e.getMessage());
        }
        return false;
    }

    public List<String> getAllTicketsForUI() {
        List<String> jsonList = new ArrayList<>();

        // Câu lệnh JOIN 3 bảng để lấy dữ liệu có ý nghĩa cho con người đọc
        String sql = "SELECT t.TicketID, v.LicensePlate, v.FullName, s.LocationName, "
                + "t.RecordedSpeed, t.ViolationTime, t.Status "
                + "FROM Traffic_Ticket t "
                + "JOIN Vehicle_Owner v ON t.LicensePlate = v.LicensePlate "
                + "JOIN Speed_Station s ON t.StationID = s.StationID "
                + "ORDER BY t.ViolationTime DESC"; // Sắp xếp cái mới nhất lên đầu

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Đóng gói thủ công thành chuỗi JSON (hoặc bạn có thể dùng thư viện Gson)
                String jsonObj = String.format(
                        "{\"ticketId\":%d, \"plate\":\"%s\", \"owner\":\"%s\", \"location\":\"%s\", \"speed\":%.1f, \"time\":\"%s\", \"status\":%d}",
                        rs.getInt("TicketID"),
                        rs.getString("LicensePlate"),
                        rs.getString("FullName"),
                        rs.getString("LocationName"),
                        rs.getFloat("RecordedSpeed"),
                        rs.getTimestamp("ViolationTime").toString(),
                        rs.getInt("Status")
                );
                jsonList.add(jsonObj);
            }
        } catch (SQLException e) {
            System.err.println("[-] Loi load danh sach: " + e.getMessage());
        }
        return jsonList;
    }
}
