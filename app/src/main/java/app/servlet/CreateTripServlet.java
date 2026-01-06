package app.servlet;

import app.util.DBUtil;
import app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@WebServlet(urlPatterns = {"/createtrip"})
public class CreateTripServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/run/run-createtrip.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session.getAttribute("user") == null ) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = (int) user.getId();

        // Récupération des paramètres
        String startTown = request.getParameter("start_town");
        String endTown = request.getParameter("end_town");
        String startAddress = request.getParameter("start_address");
        String endAddress = request.getParameter("end_address");
        String startDateStr = request.getParameter("start_date");
        String nbPlacesStr = request.getParameter("nb_places");
        String priceStr = request.getParameter("price");
        String description = request.getParameter("description");
        String vehicule = request.getParameter("vehicule");
        String tripTypeStr = request.getParameter("trip_type");
        
        if (startTown == null || startTown.isEmpty() ||
            endTown == null || endTown.isEmpty() ||
            startDateStr == null || startDateStr.isEmpty() ||
            nbPlacesStr == null || nbPlacesStr.isEmpty() ||
            priceStr == null || priceStr.isEmpty() ||
            tripTypeStr == null || tripTypeStr.isEmpty()) {

            request.setAttribute("error", "Champs obligatoires manquants.");
            doGet(request, response);
            return;
        }

        try {
            LocalDateTime startDate = LocalDateTime.parse(startDateStr);
            Timestamp sqlDate = Timestamp.valueOf(startDate);
            int nbPlaces = Integer.parseInt(nbPlacesStr);
            BigDecimal price = new BigDecimal(priceStr);
            int tripType = Integer.parseInt(tripTypeStr);

            if (startDate.isBefore(LocalDateTime.now())) {
                request.setAttribute("error", "La date de départ ne peut pas être antérieure à la date actuelle.");
                doGet(request, response);
                return;
            }


            try (Connection conn = DBUtil.getConnection()) {
                String sql = "INSERT INTO trips (user_id, start_town, end_town, start_address, end_address, " +
                            "start_date, nb_places, price, description, vehicule, trip_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, userId);
                    stmt.setString(2, startTown);
                    stmt.setString(3, endTown);
                    stmt.setString(4, startAddress);
                    stmt.setString(5, endAddress);
                    stmt.setTimestamp(6, sqlDate);
                    stmt.setInt(7, nbPlaces);
                    stmt.setBigDecimal(8, price);
                    stmt.setString(9, description);
                    stmt.setString(10, vehicule);
                    stmt.setInt(11, tripType);

                    stmt.executeUpdate();
                    request.setAttribute("success", "Trajet créé avec succès !");
                }
            }

        } catch (DateTimeParseException | NumberFormatException e) {
            request.setAttribute("error", "Veuillez vérifier les champs numériques et la date.");
            doGet(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Erreur lors de la création du trajet.");
            doGet(request, response);
        }

        doGet(request, response);
    }
}