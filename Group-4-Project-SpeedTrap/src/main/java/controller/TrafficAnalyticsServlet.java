/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.StreetDAO;
import dao.WarningLogDAO;
import model.Street;
import model.WarningLog;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "TrafficAnalyticsServlet", urlPatterns = {"/analytics"})
public class TrafficAnalyticsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Khởi tạo các đối tượng DAO để tương tác với cơ sở dữ liệu
        StreetDAO streetDao = new StreetDAO();
        WarningLogDAO warningDao = new WarningLogDAO();

        // 1. Luôn luôn lấy danh sách các tuyến đường để hiển thị lên thanh cuộn (dropdown)
        List<Street> streetList = streetDao.getAllStreets();
        request.setAttribute("streets", streetList);

        // 2. Nhận tham số tìm kiếm từ người dùng (khi họ bấm nút submit form)
        String streetIdRaw = request.getParameter("streetId");
        String searchDate = request.getParameter("date");

        // 3. Xử lý logic tìm kiếm: Chỉ tìm khi người dùng đã chọn cả tuyến đường và ngày
        if (streetIdRaw != null && !streetIdRaw.isEmpty() && searchDate != null && !searchDate.isEmpty()) {
            try {
                int streetId = Integer.parseInt(streetIdRaw);

                // Lấy danh sách chi tiết các xe vi phạm
                List<WarningLog> logs = warningDao.getLogsByStreetAndDate(streetId, searchDate);
                request.setAttribute("violationLogs", logs);

                // Lấy câu thống kê khung giờ vi phạm nhiều nhất
                String peakTimeStat = warningDao.getPeakHourStatistic(streetId, searchDate);
                request.setAttribute("peakTimeStat", peakTimeStat);
                
                // Giữ lại các giá trị người dùng vừa nhập để form không bị reset trắng sau khi load lại
                request.setAttribute("selectedStreetId", streetId);
                request.setAttribute("selectedDate", searchDate);

            } catch (NumberFormatException e) {
                System.out.println("Lỗi định dạng ID tuyến đường: " + e.getMessage());
            }
        }

        // 4. Chuyển tiếp (forward) toàn bộ dữ liệu này sang trang JSP để vẽ giao diện
        // Lưu ý: Em cần tạo thư mục 'analytics' và file 'dashboard.jsp' bên trong WEB-INF của Web Pages
        request.getRequestDispatcher("/WEB-INF/analytics/dashboard.jsp").forward(request, response);
    }
}
