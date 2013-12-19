package RMI;

import java.sql.*;

/**
 *
 *
 * @author Sune Heide
 *
 */
public final class DBManager {

    private static DBManager _instance = null;
    private Connection _con = null;

    /**
     * @param args the command line arguments
     */
    public DBManager() {
        _con = getSQLServerConnection();
    }

    //Thread safe instatiate method
    public static synchronized DBManager getInstance() {
        if (_instance == null) {
            _instance = new DBManager();
        }
        return _instance;
    }

    /**
     * @param args the command line arguments
     */
    public Connection getConnection() {
        return _con;
    }

    /**
     * @param args the command line arguments
     */
    private static Connection getSQLServerConnection() {
        Connection con = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            String URL = "jdbc:mysql://thelizard6.eitlab.ihk-edu.dk/mikael3e13";
            con = DriverManager.getConnection(URL, "mikael3e13", "121661");
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }
}