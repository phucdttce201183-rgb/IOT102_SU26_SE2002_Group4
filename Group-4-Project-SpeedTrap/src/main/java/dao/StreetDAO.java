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
import model.Street;

public class StreetDAO extends DBContext {

    /**
     * Lấy danh sách tất cả các tuyến đường
     */
    public List<Street> getAllStreets() {
        List<Street> list = new ArrayList<>();
        String sql = "SELECT street_id, street_name, speed_limit FROM Streets";

        try {
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Street street = new Street(
                        rs.getInt("street_id"),
                        rs.getString("street_name"),
                        rs.getInt("speed_limit")
                );
                list.add(street);
            }
        } catch (Exception e) {
            System.out.println("Lỗi getAllStreets (StreetDAO): " + e.getMessage());
        }
        return list;
    }
<<<<<<< HEAD
    /**
     * Lấy tốc độ giới hạn của một tuyến đường dựa vào ID
     */
    public int getSpeedLimitByStreetId(int streetId) {
        int speedLimit = 0;
        String sql = "SELECT speed_limit FROM Streets WHERE street_id = ?";
        
        try {
            java.sql.PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, streetId);
            java.sql.ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                speedLimit = rs.getInt("speed_limit");
            }
        } catch (Exception e) {
            System.out.println("Lỗi getSpeedLimitByStreetId: " + e.getMessage());
        }
        return speedLimit; // Trả về tốc độ cho phép
=======

    // Thêm vào class StreetDAO
    public int getSpeedLimitByStreetId(int streetId) {
        String sql = "SELECT speed_limit FROM Streets WHERE street_id = ?";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, streetId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("speed_limit");
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi tra cứu tốc độ: " + e.getMessage());
        }
        return 999; // Trả về con số an toàn mặc định nếu không tìm thấy đường
>>>>>>> 851a31d2b40f0bc01173700058a51a408c388102
    }
}
