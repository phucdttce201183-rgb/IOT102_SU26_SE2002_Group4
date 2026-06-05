package controller;

import dao.WarningLogDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ViolationApiServlet", urlPatterns = {"/api/nhan-du-lieu"})
public class ViolationApiServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Cấu hình trả về text đơn giản cho ESP đọc
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 2. Đọc cục dữ liệu thô từ mạch ESP gửi lên (Ví dụ: "1|85.5")
        StringBuilder payload = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                payload.append(line);
            }
        }

        String data = payload.toString().trim();

        // 3. Tách và xử lý dữ liệu an toàn
        if (!data.isEmpty()) {
            try {
                String[] parts = data.split("\\|");

                if (parts.length == 2) {
                    int streetId = Integer.parseInt(parts[0].trim());

                    // Chuyển Float sang Int cho khớp DB
                    float speedFloat = Float.parseFloat(parts[1].trim());
                    int speedValue = Math.round(speedFloat);

                    // 4. GỌI DAO ĐỂ LƯU VÀO DATABASE
                    WarningLogDAO dao = new WarningLogDAO();
                    boolean isSuccess = dao.insertWarningLog(streetId, speedValue);

                    // 5. PHẢN HỒI LẠI CHO ESP
                    if (isSuccess) {
                        out.print("SUCCESS");
                        System.out.println(">>> [WIFI API] Nhan du lieu - Duong ID: " + streetId + " - Toc do: " + speedValue + " km/h");
                        response.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        out.print("DB_ERROR");
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                } else {
                    out.print("INVALID_FORMAT");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }

            } catch (NumberFormatException e) {
                out.print("DATA_CORRUPTED");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (Exception e) {
                out.print("SERVER_ERROR");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            out.print("EMPTY_PAYLOAD");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        out.flush();
    }
}
