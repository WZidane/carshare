package app.servlet;

import app.model.Trip;
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

@WebServlet("/tripdetails")
public class TripDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String idStr = request.getParameter("id");
        int tripId = -1;

        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        try {
            tripId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Trip trip = null;

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT t.*, u.username AS user_name FROM trips t JOIN users u ON t.user_id = u.id WHERE t.id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, tripId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    trip = new Trip();
                    trip.setId(rs.getInt("id"));
                    trip.setUserId(rs.getInt("user_id"));
                    trip.setStartTown(rs.getString("start_town"));
                    trip.setEndTown(rs.getString("end_town"));
                    trip.setStartAddress(rs.getString("start_address"));
                    trip.setEndAddress(rs.getString("end_address"));
                    trip.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
                    trip.setNbPlaces(rs.getInt("nb_places"));
                    trip.setPrice(rs.getBigDecimal("price"));
                    trip.setDescription(rs.getString("description"));
                    trip.setVehicule(rs.getString("vehicule"));
                    trip.setTripType(rs.getInt("trip_type"));
                    trip.setUsername(rs.getString("user_name"));
                }
            }
        } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        if (trip == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        } else {
            request.setAttribute("trip", trip);
        }

        boolean alreadyBooked = false;

        try (Connection conn = DBUtil.getConnection()) {
            String checkSql = "SELECT COUNT(*) FROM bookings WHERE user_id = ? AND trip_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                stmt.setInt(1, user.getId());
                stmt.setInt(2, tripId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    alreadyBooked = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur lors de la récupération de la réservation.");
        }

        request.setAttribute("alreadyBooked", alreadyBooked);

        request.getRequestDispatcher("/run/run-tripdetails.jsp").forward(request, response);
    }
}