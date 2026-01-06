package app.servlet;

import app.util.DBUtil;
import app.model.User;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

@WebServlet(urlPatterns = {"/profile"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class ProfileServlet extends HttpServlet {

    private String uploadPath = "/usr/local/tomcat/webapps/carshare-app/uploads";
    private String uploadPathName = "/uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/run/run-profile.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        int userId = (int) user.getId();

        Argon2 argon2 = Argon2Factory.create();
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Part avatar = request.getPart("avatar");

        String avatarUrl = null;
        String hashedPassword = null;

        boolean hasUsername = username != null && !username.isEmpty();
        boolean hasEmail = email != null && !email.isEmpty();
        boolean hasPassword = password != null && !password.isEmpty();
        boolean hasAvatar = avatar != null && avatar.getSize() > 0;

        if (!hasUsername || !hasEmail) {
            request.setAttribute("error", "Nom d'utilisateur et email sont requis.");
            return;
        }

        try {
            if (hasAvatar) {
                avatarUrl = avatar.getSubmittedFileName();
                avatar.write(uploadPath + File.separator + avatarUrl);
            }

            if (hasPassword) {
                hashedPassword = argon2.hash(4, 65536, 1, password);
            }

            // Construction dynamique de la requête SQL
            StringBuilder sql = new StringBuilder("UPDATE users SET username = ?, email = ?");
            List<Object> params = new ArrayList<>(List.of(username, email));

            if (hashedPassword != null) {
                sql.append(", password = ?");
                params.add(hashedPassword);
            }

            if (avatarUrl != null) {
                sql.append(", avatar_url = ?");
                params.add(uploadPathName + File.separator + avatarUrl);
            }

            sql.append(" WHERE id = ?");
            params.add(userId);

            try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }

                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    request.setAttribute("error", "Erreur serveur. Veuillez réessayer.");
                    return;
                }

                // Mise à jour de l'objet en session
                user.setUsername(username);
                user.setEmail(email);

                if (avatarUrl != null) {
                    user.setAvatar(request.getContextPath() + uploadPathName + File.separator + avatarUrl);
                }

                request.setAttribute("success", "Les informations ont bien été modifiées !");
                request.setAttribute("user", user);

            } catch (SQLException e) {
                request.setAttribute("error", "Erreur serveur. Veuillez réessayer.");
            }

        } catch (Exception e) {
            request.setAttribute("error", "Erreur serveur inattendue.");
        } finally {
            if (hasPassword) {
                argon2.wipeArray(password.toCharArray());
            }
        }

        doGet(request, response);
    }
}