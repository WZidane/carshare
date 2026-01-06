package app.servlet;

import app.util.DBUtil;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/index.html"})
public class HomeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
        RequestDispatcher dispatcher = request.getRequestDispatcher("/run/run-home.jsp");
        dispatcher.forward(request, response);
    }
}