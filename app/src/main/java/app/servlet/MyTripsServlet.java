package app.servlet;

import app.util.DBUtil;
import app.model.User;
import app.model.Trip;

import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/mytrips"})
public class MyTripsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = (int) user.getId();

        List<Trip> passengerTrips = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM trips WHERE user_id = ? AND trip_type = 0 ORDER BY start_date ASC";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Trip trip = new Trip();
                    trip.setId(rs.getInt("id"));
                    trip.setStartTown(rs.getString("start_town"));
                    trip.setEndTown(rs.getString("end_town"));
                    trip.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
                    trip.setNbPlaces(rs.getInt("nb_places"));
                    trip.setPrice(rs.getBigDecimal("price"));
                    passengerTrips.add(trip);
                }
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur lors de la récupération des trajets.");
        }

        List<Trip> driverTrips = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM trips WHERE user_id = ? AND trip_type = 1 ORDER BY start_date ASC";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Trip trip = new Trip();
                    trip.setId(rs.getInt("id"));
                    trip.setStartTown(rs.getString("start_town"));
                    trip.setEndTown(rs.getString("end_town"));
                    trip.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
                    trip.setNbPlaces(rs.getInt("nb_places"));
                    trip.setPrice(rs.getBigDecimal("price"));
                    driverTrips.add(trip);
                }
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur lors de la récupération des trajets.");
        }

        request.setAttribute("passengerTrips", passengerTrips);
        request.setAttribute("driverTrips", driverTrips);
        request.getRequestDispatcher("/run/run-mytrips.jsp").forward(request, response);
    }
}