package app.dao;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import app.model.User;

public class UserDAO {

    private DataSource dataSource;

    public UserDAO() {
        try {
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/carshare");
        } catch (NamingException e) {
            e.getMessage();
        }
    }
}