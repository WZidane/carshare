package app.servlet;

import app.model.User;
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

@WebServlet("/deletetrip")
public class DeleteTripServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String tripIdStr = request.getParameter("trip_id");
        if (tripIdStr == null || tripIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        int tripId = Integer.parseInt(tripIdStr);
        int userId = user.getId();

        try (Connection conn = DBUtil.getConnection()) {

            String checkSql = "SELECT user_id FROM trips WHERE id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, tripId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        int ownerId = rs.getInt("user_id");
                        if (ownerId != userId) {
                            request.setAttribute("error", "Vous ne pouvez pas supprimer un trajet dont vous n'êtes pas le créateur.");
                            request.getRequestDispatcher("/run/run-home.jsp").forward(request, response);
                            return;
                        }
                    } else {
                        response.sendRedirect(request.getContextPath() + "/");
                        return;
                    }
                }
            }


            String deleteSql = "DELETE FROM trips WHERE id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, tripId);
                deleteStmt.executeUpdate();
            }

        } catch (SQLException e) {
            request.setAttribute("error", "Erreur lors de la suppression du trajet.");
        }

        request.setAttribute("success", "Le trajet a bien été supprimé.");
        request.getRequestDispatcher("/run/run-home.jsp").forward(request, response);
    }
}