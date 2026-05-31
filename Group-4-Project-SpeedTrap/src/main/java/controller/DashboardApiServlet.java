package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "DashboardApiServlet", urlPatterns = {"/api/dashboard"})
public class DashboardApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Trả về định dạng JSON
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        DatabaseManager db = new DatabaseManager();
        List<String> tickets = db.getAllTicketsForUI();

        // Nối mảng JSON thủ công để gửi về Front-end
        out.print("[");
        out.print(String.join(",", tickets));
        out.print("]");

        out.flush();
    }
}
