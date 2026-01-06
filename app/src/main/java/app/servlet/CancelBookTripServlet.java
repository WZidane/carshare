package app.servlet;

import app.model.User;
import app.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/cancelbooktrip")
public class CancelBookTripServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String tripIdStr = request.getParameter("trip_id");

        if (tripIdStr == null || tripIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        int tripId = Integer.parseInt(tripIdStr);
        int userId = user.getId();

        try (Connection conn = DBUtil.getConnection()) {

            String checkBookingSql = "SELECT COUNT(*) FROM bookings WHERE user_id = ? AND trip_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkBookingSql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, tripId);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next() || rs.getInt(1) == 0) {
                    session.setAttribute("error", "Vous n'avez pas de réservation sur ce trajet.");
                    response.sendRedirect(request.getContextPath() + "/tripdetails?id=" + tripId);
                    return;
                }
            }

            String deleteBookingSql = "DELETE FROM bookings WHERE user_id = ? AND trip_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteBookingSql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, tripId);
                stmt.executeUpdate();
            }

            String updateTripSql = "UPDATE trips SET nb_places = nb_places + 1 WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateTripSql)) {
                stmt.setInt(1, tripId);
                stmt.executeUpdate();
            }

            session.setAttribute("success", "La réservation a bien été annulé.");
            response.sendRedirect(request.getContextPath() + "/tripdetails?id=" + tripId);

        } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
    }
}