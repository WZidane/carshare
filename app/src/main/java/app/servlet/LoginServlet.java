package app.servlet;

import app.util.DBUtil;
import app.model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;   

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
           
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/run/run-login.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
           
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }


        String email = request.getParameter("email");
        String password = request.getParameter("password");

         if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "Tous les champs sont obligatoires.");
            doGet(request, response);
            return;
        }

        try {
            Argon2 argon2 = Argon2Factory.create();

            Connection conn = DBUtil.getConnection();

            String sql = "SELECT * FROM users WHERE BINARY email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
            
                if (argon2.verify(hashedPassword, password)) {

                    HttpSession session_create = request.getSession();
                    User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("avatar_url"),
                        rs.getTimestamp("created_at")
                    );

                    session_create.setAttribute("user", user);

                    response.sendRedirect(request.getContextPath() + "/profile");
                } else {
                    request.setAttribute("error", "Adresse e-mail ou mot de passe incorrect.");
                    doGet(request, response);
                }
            } else {
                request.setAttribute("error", "Indentifiants incorrects.");
                doGet(request, response);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            request.setAttribute("error", "Erreur serveur : " + e.getMessage());
            doGet(request, response);
        }
    }
}