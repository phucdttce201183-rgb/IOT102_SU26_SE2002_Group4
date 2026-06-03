package controller;

import dao.StreetDAO;
import dao.WarningLogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SpeedCheckApiServlet", urlPatterns = {"/api/speedcheck"})
public class SpeedCheckApiServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 1. Đọc cục dữ liệu thô từ mạch ESP gửi lên (Ví dụ: "1|85")
        StringBuilder payload = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                payload.append(line);
            }
        }

        String data = payload.toString().trim();

        // 2. Tách dữ liệu an toàn bằng try-catch
        try {
            String[] parts = data.split("\\|");
            if (parts.length == 2) {
                int streetId = Integer.parseInt(parts[0].trim());
                int recordedSpeed = Integer.parseInt(parts[1].trim());

                // 3. Gọi DAO để lấy giới hạn tốc độ của con đường này
                StreetDAO streetDao = new StreetDAO();
                int speedLimit = streetDao.getSpeedLimitByStreetId(streetId);

                // 4. Xử lý Logic Phân tích
                if (recordedSpeed > speedLimit) {
                    // Vi phạm! Ghi vào cơ sở dữ liệu để phục vụ Analytics
                    WarningLogDAO logDao = new WarningLogDAO();
                    logDao.insertLog(streetId, recordedSpeed);

                    // Phản hồi khẩn cấp cho mạch ESP bật màn hình
                    out.print("WARNING");
                    System.out.println(">>> [BÁO ĐỘNG] Đường ID: " + streetId + " | Tốc độ đo: " + recordedSpeed + " km/h");
                } else {
                    // Xe chạy ngoan, không làm gì cả
                    out.print("SAFE");
                }
                response.setStatus(HttpServletResponse.SC_OK);

            } else {
                out.print("INVALID_FORMAT");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            out.print("DATA_CORRUPTED");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        out.flush();
    }
}
