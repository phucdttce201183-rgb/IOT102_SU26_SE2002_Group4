package controller; 

import dao.WarningLogDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(name = "ViolationApiServlet", urlPatterns = {"/api/nhan-du-lieu"})
public class ViolationApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Cấu hình trả về text đơn giản cho ESP32 đọc
        response.setContentType("text/plain;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            
            // 1. LẤY DỮ LIỆU TỪ ESP32
            // Đã đổi tên tham số thành streetId cho khớp với DB mới
            String streetIdStr = request.getParameter("streetId"); 
            String speedStr = request.getParameter("speed");
            
            if (streetIdStr != null && speedStr != null) {
                // 2. CHUYỂN ĐỔI KIỂU DỮ LIỆU
                int streetId = Integer.parseInt(streetIdStr);
                
                // Vì database thiết kế recorded_speed là INT, ta ép kiểu Float về Int (hoặc em có thể đổi DB thành Float tùy ý)
                float speedFloat = Float.parseFloat(speedStr);
                int speedValue = Math.round(speedFloat); 
                
                // 3. GỌI DAO ĐỂ LƯU VÀO DATABASE
                WarningLogDAO dao = new WarningLogDAO();
                boolean isSuccess = dao.insertWarningLog(streetId, speedValue);
                
                // 4. PHẢN HỒI LẠI CHO ESP32
                if (isSuccess) {
                    out.println("SUCCESS"); 
                    System.out.println(">>> [WIFI API] Nhan du lieu - Duong ID: " + streetId + " - Toc do: " + speedValue + " km/h");
                } else {
                    out.println("ERROR: Khong the luu vao DB");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                
            } else {
                out.println("ERROR: Thieu tham so streetId hoac speed");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("ERROR: Sai dinh dang so (NumberFormatException)");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("ERROR: " + e.getMessage());
        }
    }
}