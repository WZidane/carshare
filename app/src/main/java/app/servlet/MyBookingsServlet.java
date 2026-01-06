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

import java.util.List;
import java.util.ArrayList;

@WebServlet("/mybookings")
public class MyBookingsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        List<Trip> bookings = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT t.id, t.start_town, t.end_town, t.start_date, t.nb_places, t.price " +
                         "FROM bookings b JOIN trips t ON b.trip_id = t.id WHERE b.user_id = ? ORDER BY t.start_date ASC";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, user.getId());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Trip trip = new Trip();
                    trip.setId(rs.getInt("id"));
                    trip.setStartTown(rs.getString("start_town"));
                    trip.setEndTown(rs.getString("end_town"));
                    trip.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
                    trip.setNbPlaces(rs.getInt("nb_places"));
                    trip.setPrice(rs.getBigDecimal("price"));
                    bookings.add(trip);
                }
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur lors de la récupération des réservations.");
        }

        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("/run/run-mybookings.jsp").forward(request, response);
    }
}