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

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;

@WebServlet(urlPatterns = {"/passengertrips"})
public class PassengerTripsServlet extends HttpServlet {
        
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Trip> passengerTrips = new ArrayList<>();

        String sql = "SELECT * FROM trips WHERE trip_type = 0 ORDER BY start_date ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Trip trip = new Trip();
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
                passengerTrips.add(trip);
            }

        } catch (SQLException e) {
            request.setAttribute("error", "Erreur lors de la récupération des trajets.");
        }

        request.setAttribute("passengerTrips", passengerTrips);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/run/run-passengertrips.jsp");
        dispatcher.forward(request, response);
    }
}