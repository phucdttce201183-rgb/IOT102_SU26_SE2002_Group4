/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DBContext;
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
}
