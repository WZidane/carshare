package app.servlet;

import app.model.Trip;
import app.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;

@WebServlet("/searchtrips")
public class SearchTripsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String depart = request.getParameter("depart");
        String destination = request.getParameter("destination");
        String dateStr = request.getParameter("date");

        List<Trip> trips = new ArrayList<>();

        if (depart != null && destination != null && dateStr != null) {
            try (Connection conn = DBUtil.getConnection()) {
                String sql = "SELECT * FROM trips WHERE start_town = ? AND end_town = ? AND DATE(start_date) = ? ORDER BY start_date ASC";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, depart);
                    stmt.setString(2, destination);
                    stmt.setString(3, dateStr);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        Trip trip = new Trip();
                        trip.setId(rs.getInt("id"));
                        trip.setStartTown(rs.getString("start_town"));
                        trip.setEndTown(rs.getString("end_town"));
                        trip.setStartDate(rs.getTimestamp("start_date").toLocalDateTime());
                        trip.setNbPlaces(rs.getInt("nb_places"));
                        trip.setPrice(rs.getBigDecimal("price"));
                        trip.setDescription(rs.getString("description"));
                        trips.add(trip);
                    }
                }
            } catch (SQLException e) {
                request.setAttribute("error", "Erreur lors de la récupération des trajets.");
            }
        } else {
            request.setAttribute("error", "Il manque un ou plusieurs champs.");
        }

        List<String> startTowns = new ArrayList<>();
        List<String> endTowns = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT start_town FROM trips");
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                startTowns.add(rs.getString("start_town"));
            }
        } catch (Exception e) {
           request.setAttribute("error", "Erreur lors de la récupération des villes de départ.");
        }

         try (Connection conn = DBUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT end_town FROM trips");
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                endTowns.add(rs.getString("end_town"));
            }
        } catch (Exception e) {
            request.setAttribute("error", "Erreur lors de la récupération des villes d'arrivée.");
        }

        request.setAttribute("startTowns", startTowns);
        request.setAttribute("endTowns", endTowns);
        request.setAttribute("depart", depart);
        request.setAttribute("destination", destination);
        request.setAttribute("date", dateStr);
        request.setAttribute("trips", trips);

        request.getRequestDispatcher("/run/run-searchtrips.jsp").forward(request, response);
    }
}