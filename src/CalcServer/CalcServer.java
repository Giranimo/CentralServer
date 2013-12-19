package CalcServer;

import java.sql.ResultSet;

/**
 *
 * @author Mikael Kofod, Sune Heide, Kristian Jacobsen
 */
public class CalcServer {

    private static AlgorithmDB db;
    private static RouteCalc rCalc;
    private static PriceCalc pCalc;

    /**
     * @param args the command line arguments
     */
    public CalcServer() {
        db = new AlgorithmDB();
        rCalc = new RouteCalc(this);
        pCalc = new PriceCalc(this);
    }

    /**
     * @param args the command line arguments
     */
    public double getPrice(String start, String end) {
        double check = pCalc.getPrice(start, end);
        if (check == 5 || check == 6) {
            return check * 5 + 5.5;
        }
        return check * 5 + 5;
    }

    /**
     * @param args the command line arguments
     */
    public String getRoute(String start, String end) {
        System.out.println(start + end);
        if (rCalc == null) {
            return "Fejl";
        }

        return rCalc.getRoute(start, end);
    }

    /**
     * @param args the command line arguments
     */
    public ResultSet getStations() {
        return db.getStations();
    }

    /**
     * @param args the command line arguments
     */
    public ResultSet getRoutes() {
        return db.getRoutes();
    }
}
