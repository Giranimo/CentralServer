package CalcServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mikael Kofod, Sune Heide, Kristian Jacobsen
 */
public class AlgorithmDB {

    private Connection con;
    private Statement stmt;

    public AlgorithmDB() {
        connection();
    }

    /**
     * @param args the command line arguments
     */
    public void connection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://goonhilly6.eitlab.ihk-edu.dk:3306/mikael3e13";
            String userName = "mikael3e13";
            String password = "121661";

            con = DriverManager.getConnection(url, userName, password);
            stmt = con.createStatement();

        } catch (Exception e) {
            Logger.getLogger(AlgorithmDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * @param args the command line arguments
     */
    public ResultSet getStations() {
        try {
            ResultSet rset = stmt.executeQuery("SELECT Current, Zone FROM stations");
            return rset;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param args the command line arguments
     */
    public ResultSet getRoutes() {
        try {
            ResultSet rset = stmt.executeQuery("SELECT Last, Current, Next, nTime, lTime, stopID FROM stations");
            return rset;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
