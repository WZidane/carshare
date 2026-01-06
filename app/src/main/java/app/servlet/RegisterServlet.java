package app.servlet;

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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

@WebServlet(urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session.getAttribute("user") != null) {
           
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/run/run-register.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session.getAttribute("user") != null) {
           
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (username == null || email == null || password == null ||
            username.isEmpty() || email.isEmpty() || password.isEmpty()) {

            request.setAttribute("error", "Tous les champs sont obligatoires.");
            doGet(request, response);
            return;
        }

        Argon2 argon2 = Argon2Factory.create();

        try (Connection conn = DBUtil.getConnection()) {

            // Vérifier que l'email n'existe pas déjà
            String checkEmailSql = "SELECT id FROM users WHERE email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkEmailSql)) {
                checkStmt.setString(1, email);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    request.setAttribute("error", "Un compte avec cet email existe déjà.");
                    request.getRequestDispatcher("/run/run-register.jsp").forward(request, response);
                    return;
                }
            } catch (SQLException e) {
                request.setAttribute("error", "Erreur serveur. Veuillez réessayer.");
                request.getRequestDispatcher("/run/run-register.jsp").forward(request, response);
            }

            String hashedPassword = argon2.hash(4, 65536, 1, password);

            String insertSql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, email);
                insertStmt.setString(3, hashedPassword);

                int affectedRows = insertStmt.executeUpdate();
                if (affectedRows == 0) {
                    request.setAttribute("error", "Erreur serveur. Veuillez réessayer.");
                    doGet(request, response);
                }
            } catch (SQLException e) {
                request.setAttribute("error", "Erreur serveur. Veuillez réessayer.");
                doGet(request, response);
            }

            request.setAttribute("success", "Inscription réussie ! Veuillez vous connecter.");
            doGet(request, response);

        } catch (SQLException e) {
            request.setAttribute("error", "Erreur serveur. Veuillez réessayer.");
            doGet(request, response);
        } finally {
            argon2.wipeArray(password.toCharArray());
        }
    }
}