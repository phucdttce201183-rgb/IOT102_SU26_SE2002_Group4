/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.WarningLog;

public class WarningLogDAO extends DBContext {

    /**
     * Tìm các lượt vi phạm theo tên đường và ngày
     */
    public List<WarningLog> getLogsByStreetAndDate(int streetId, String date) {
        List<WarningLog> list = new ArrayList<>();
        String sql = "SELECT w.log_id, s.speed_limit, w.recorded_speed, w.timestamp "
                + "FROM Warning_Logs w "
                + "JOIN Streets s ON w.street_id = s.street_id "
                + "WHERE w.street_id = ? AND CAST(w.timestamp AS DATE) = ? "
                + "ORDER BY w.timestamp DESC";

        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, streetId);
            st.setString(2, date);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                WarningLog log = new WarningLog(
                        rs.getInt("log_id"),
                        rs.getInt("speed_limit"),
                        rs.getInt("recorded_speed"),
                        rs.getTimestamp("timestamp")
                );
                list.add(log);
            }
        } catch (Exception e) {
            System.out.println("Lỗi getLogsByStreetAndDate (WarningLogDAO): " + e.getMessage());
        }
        return list;
    }

    /**
     * Thống kê: Tìm khung giờ có nhiều lượt vi phạm nhất trong ngày
     */
    public String getPeakHourStatistic(int streetId, String date) {
        String result = null;
        String sql = "SELECT TOP 1 DATEPART(HOUR, timestamp) AS PeakHour, COUNT(*) AS TotalViolations "
                + "FROM Warning_Logs "
                + "WHERE street_id = ? AND CAST(timestamp AS DATE) = ? "
                + "GROUP BY DATEPART(HOUR, timestamp) "
                + "ORDER BY TotalViolations DESC";

        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, streetId);
            st.setString(2, date);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int peakHour = rs.getInt("PeakHour");
                int total = rs.getInt("TotalViolations");
                result = String.format("%02d:00 - %02d:00 (%d lượt vi phạm)", peakHour, peakHour + 1, total);
            }
        } catch (Exception e) {
            System.out.println("Lỗi getPeakHourStatistic (WarningLogDAO): " + e.getMessage());
        }
        return result;
    }

    public boolean insertWarningLog(int streetId, int recordedSpeed) {
        // Dùng GETDATE() của SQL Server để tự động lấy giờ hiện tại
        String sql = "INSERT INTO Warning_Logs (street_id, recorded_speed, timestamp) VALUES (?, ?, GETDATE())";

        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, streetId);
            st.setInt(2, recordedSpeed);

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("[DB_SUCCESS] Da luu vi pham tai duong ID: " + streetId + " | Toc do: " + recordedSpeed + " km/h");
                return true;
            }
        } catch (Exception e) {
            System.out.println("-> [DAO ERROR] Loi insertWarningLog: " + e.getMessage());
        }
        return false;
    }

    // Thêm vào class WarningLogDAO
    public void insertLog(int streetId, int recordedSpeed) {
        // Lưu ý: Cột timestamp trong SQL cấu hình mặc định là GETDATE()
        String sql = "INSERT INTO Warning_Logs (street_id, recorded_speed, timestamp) VALUES (?, ?, GETDATE())";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, streetId);
            ps.setInt(2, recordedSpeed);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi ghi log vi phạm: " + e.getMessage());
        }
    }
}
