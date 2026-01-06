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

@WebServlet("/booktrip")
public class BookTripServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String tripIdStr = request.getParameter("trip_id");

        if (tripIdStr == null || tripIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        int tripId = Integer.parseInt(tripIdStr);
        int userId = user.getId();

        try (Connection conn = DBUtil.getConnection()) {

            String checkTripSql = "SELECT user_id, nb_places FROM trips WHERE id = ?";
            int OwnerId;
            int nbPlaces;

            try (PreparedStatement stmt = conn.prepareStatement(checkTripSql)) {
                stmt.setInt(1, tripId);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    request.setAttribute("error", "Le trajet n'existe pas.");
                    request.getRequestDispatcher("/run/run-home.jsp").forward(request, response);
                    return;
                }

                OwnerId = rs.getInt("user_id");
                nbPlaces = rs.getInt("nb_places");
            }

            if (OwnerId == userId) {
                session.setAttribute("error", "Vous ne pouvez pas réserver un trajet dont vous êtes le créateur.");
                response.sendRedirect(request.getContextPath() + "/tripdetails?id=" + tripId);
                return;
            }

            if (nbPlaces <= 0) {
                session.setAttribute("error", "Il n'y a plus de places disponibles pour ce trajet.");
                response.sendRedirect(request.getContextPath() + "/tripdetails?id=" + tripId);
                return;
            }

            String checkBookingSql = "SELECT COUNT(*) FROM bookings WHERE user_id = ? AND trip_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkBookingSql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, tripId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    session.setAttribute("error", "Vous avez déjà une réservation sur ce trajet.");
                    response.sendRedirect(request.getContextPath() + "/tripdetails?id=" + tripId);
                    return;
                }
            }

            String insertBookingSql = "INSERT INTO bookings (user_id, trip_id) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertBookingSql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, tripId);
                stmt.executeUpdate();
            }

            String updateTripSql = "UPDATE trips SET nb_places = nb_places - 1 WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateTripSql)) {
                stmt.setInt(1, tripId);
                stmt.executeUpdate();
            }

            session.setAttribute("success", "Le trajet a bien été réservé.");
            response.sendRedirect(request.getContextPath() + "/tripdetails?id=" + tripId);

        } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
    }
}