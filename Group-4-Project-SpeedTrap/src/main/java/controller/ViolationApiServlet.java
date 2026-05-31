package controller; // Nhớ đổi tên package nếu em tạo ở chỗ khác

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import model.TrafficTicket;
import system.DatabaseManager;

// Đây chính là "địa chỉ nhà hàng" để mạch ESP32 tìm đến
@WebServlet(name = "ViolationApiServlet", urlPatterns = {"/api/vipham"})
public class ViolationApiServlet extends HttpServlet {

    // 1. MẢNG DỮ LIỆU GIẢ LẬP (MOCK DATA)
    // Cảnh báo: Bắt buộc phải khớp 100% với các biển số đã INSERT trong SQL Server (Bước 1)
    private final String[] VALID_LICENSE_PLATES = {
        "65A-111.11",
        "65A-222.22",
        "65A-333.33",
        "65A-444.44",
        "65A-555.55"
    };

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Thiết lập header trả về cho ESP8266
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 2. Đọc dữ liệu (Payload) gửi lên từ mạch ESP
        StringBuilder payload = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                payload.append(line);
            }
        }

        String data = payload.toString().trim();

        // 3. Phân tích chuỗi và bảo vệ luồng (Validation)
        if (data.startsWith("VI_PHAM|")) {
            try {
                String[] parts = data.split("\\|");

                // Đảm bảo chuỗi gửi lên có đúng 3 phần
                if (parts.length == 3) {
                    String stationId = parts[1].trim();
                    float speedValue = Float.parseFloat(parts[2].trim()); // Có thể văng lỗi nếu gửi sai số

                    // 4. Giả lập Camera đọc biển số (Bốc random 1 biển hợp lệ)
                    Random rand = new Random();
                    String randomPlate = VALID_LICENSE_PLATES[rand.nextInt(VALID_LICENSE_PLATES.length)];

                    // 5. Đóng gói OOP
                    TrafficTicket ticket = new TrafficTicket(randomPlate, stationId, speedValue);

                    // 6. Khởi tạo DatabaseManager cục bộ (Bảo vệ an toàn đa luồng)
                    DatabaseManager dbManager = new DatabaseManager();
                    boolean isSaved = dbManager.insertTrafficTicket(ticket);

                    if (isSaved) {
                        System.out.println(">>> [API] Da xu ly va luu thanh cong giay phat cho xe: " + randomPlate);
                        out.print("OK"); // Trả HTTP 200 kèm chữ OK để mạch ESP biết
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
                System.err.println("[-] [API] Loi ep kieu toc do: " + e.getMessage());
                out.print("DATA_CORRUPTED");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            out.print("UNKNOWN_COMMAND");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        out.flush();
    }
}
