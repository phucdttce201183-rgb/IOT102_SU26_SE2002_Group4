<%@page import="java.util.List"%>
<%@page import="model.Street"%>
<%@page import="model.WarningLog"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- Include Header có sẵn của em --%>
<%@include file="/WEB-INF/include/header.jsp" %>

<div class="container mt-5">
    <h2 class="mb-4 text-primary">Hệ thống Phân tích Giao thông Thông minh</h2>

    <div class="card mb-4">
        <div class="card-body">
            <form action="http://localhost:8080/Group-4-Project-SpeedTrap/analytics" method="GET" class="row g-3 align-items-center">
                
                <div class="col-md-5">
                    <label class="form-label fw-bold">Chọn tuyến đường:</label>
                    <select name="streetId" class="form-select" required>
                        <option value="">-- Vui lòng chọn tuyến đường --</option>
                        <% 
                            // Lấy danh sách tuyến đường từ Servlet gửi qua
                            List<Street> streets = (List<Street>) request.getAttribute("streets");
                            Object selectedStreetIdObj = request.getAttribute("selectedStreetId");
                            String selectedStrId = (selectedStreetIdObj != null) ? selectedStreetIdObj.toString() : "";

                            if (streets != null) {
                                for (Street s : streets) {
                                    // Giữ trạng thái con đường người dùng vừa chọn
                                    String selected = (String.valueOf(s.getId()).equals(selectedStrId)) ? "selected" : "";
                        %>
                                    <option value="<%= s.getId() %>" <%= selected %>>
                                        <%= s.getName() %> (Giới hạn: <%= s.getSpeedLimit() %> km/h)
                                    </option>
                        <% 
                                }
                            }
                        %>
                    </select>
                </div>

                <div class="col-md-4">
                    <label class="form-label fw-bold">Ngày tra cứu:</label>
                    <% 
                        Object selectedDateObj = request.getAttribute("selectedDate");
                        String selectedDate = (selectedDateObj != null) ? selectedDateObj.toString() : "";
                    %>
                    <input type="date" name="date" class="form-control" value="<%= selectedDate %>" required />
                </div>

                <div class="col-md-3 mt-5">
                    <button type="submit" class="btn btn-success w-100">Phân tích dữ liệu</button>
                </div>
            </form>
        </div>
    </div>

    <% 
        String peakTimeStat = (String) request.getAttribute("peakTimeStat");
        if (peakTimeStat != null && !peakTimeStat.isEmpty()) {
    %>
        <div class="alert alert-warning border-warning" role="alert">
            <h5 class="alert-heading">⚠️ Cảnh báo điểm nóng vi phạm!</h5>
            <p class="mb-0">Khung giờ có lưu lượng xe chạy quá tốc độ cao nhất là: <strong><%= peakTimeStat %></strong>.</p>
            <hr>
            <p class="mb-0 fs-6"><em>Đề xuất: Cần bố trí lực lượng CSGT tuần tra vào khung giờ này để đảm bảo an toàn.</em></p>
        </div>
    <% } %>

    <div class="card">
        <div class="card-header bg-dark text-white fw-bold">
            Chi tiết các phương tiện vi phạm tốc độ
        </div>
        <div class="card-body p-0">
            <table class="table table-striped table-hover mb-0">
                <thead class="table-light">
                    <tr>
                        <th>ID Cảnh báo</th>
                        <th>Tốc độ giới hạn</th>
                        <th>Tốc độ đo được</th>
                        <th>Thời gian ghi nhận</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        List<WarningLog> logs = (List<WarningLog>) request.getAttribute("violationLogs");
                        if (logs != null && !logs.isEmpty()) {
                            for (WarningLog log : logs) {
                    %>
                        <tr>
                            <td>#<%= log.getId() %></td>
                            <td><%= log.getSpeedLimit() %> km/h</td>
                            <td class="text-danger fw-bold"><%= log.getRecordedSpeed() %> km/h</td>
                            <td><%= log.getTimestamp() %></td>
                        </tr>
                    <% 
                            }
                        } else if (selectedDateObj != null) { 
                    %>
                        <tr>
                            <td colspan="4" class="text-center text-muted py-3">Không có dữ liệu vi phạm nào trong ngày này. Tuyệt vời!</td>
                        </tr>
                    <% } else { %>
                        <tr>
                            <td colspan="4" class="text-center text-muted py-3">Vui lòng chọn tuyến đường và ngày để xem dữ liệu.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%-- Include Footer có sẵn của em --%>
<%@include file="/WEB-INF/include/footer.jsp" %>