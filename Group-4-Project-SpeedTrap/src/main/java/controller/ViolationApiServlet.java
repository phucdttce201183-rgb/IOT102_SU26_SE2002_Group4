package controller; // Nhớ đổi tên package nếu em tạo ở chỗ khác

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model1.SpeedViolation;
import system1.DatabaseManager; // Import DatabaseManager của em vào

// Đây chính là "địa chỉ nhà hàng" để mạch ESP32 tìm đến
@WebServlet(name = "ViolationApiServlet", urlPatterns = {"/api/nhan-du-lieu"})
public class ViolationApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Cấu hình trả về định dạng chữ đơn giản (Text) cho ESP32 dễ đọc
        response.setContentType("text/plain;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            
            // 1. NHẬN DỮ LIỆU TỪ ESP32 GỬI LÊN (Khách gọi món)
            // Lấy thông số từ URL (ví dụ: ?station=STATION_CE201665&speed=75.5)
            String stationId = request.getParameter("station");
            String speedStr = request.getParameter("speed");
            
            // 2. KIỂM TRA DỮ LIỆU CÓ HỢP LỆ KHÔNG
            if (stationId != null && speedStr != null) {
                // Đổi tốc độ từ chuỗi chữ (String) sang số thập phân (Float)
                float speedValue = Float.parseFloat(speedStr);
                
                // 3. GỌI DATABASE MANAGER ĐỂ LƯU VÀO SQL (Mang vào bếp)
                DatabaseManager dbManager = new DatabaseManager();
                SpeedViolation sv = new SpeedViolation(stationId, speedValue);
                dbManager.forwardDataDataBase(sv);
                
                // 4. TRẢ LỜI CHO ESP32 BIẾT LÀ ĐÃ NHẬN THÀNH CÔNG (Mang hóa đơn ra)
                out.println("SUCCESS"); 
                
                // In ra màn hình Output của NetBeans để em dễ theo dõi
                System.out.println(">>> [WIFI API] Nhan du lieu tu: " + stationId + " - Toc do: " + speedValue + " km/h");
                
            } else {
                // Báo lỗi nếu ESP32 gửi thiếu trạm hoặc tốc độ
                out.println("ERROR: Thieu tham so station hoac speed");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            
        } catch (Exception e) {
            // Báo lỗi nếu tốc độ gửi lên không phải là số hoặc lỗi database
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("ERROR: " + e.getMessage());
        }
    }
}