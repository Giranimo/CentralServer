package CalcServer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author Mikael Kofod, Sune Heide, Kristian Jacobsen
 */
public class RouteCalc {

    private CalcServer serv;
    private HashMap stations;

    public RouteCalc(CalcServer cServ) {
        serv = cServ;
        stations = new HashMap();
        getRoutes();
    }

    /**
     * @param args the command line arguments
     */
    public String getRoute(String start, String end) {
        Vertex szone = (Vertex) stations.get(start);
        Vertex ezone = (Vertex) stations.get(end);
        findPaths(szone);
        ArrayList<Vertex> path = getPath(ezone);
        String line, sRoute;
        line = sRoute = "";
        int next = 1;
        for (Vertex n : path) {
            boolean f = false;
            String k = null;
            sRoute = sRoute + n.name + ":";

            for (Map.Entry<String, Edge> entry : (Set<Map.Entry<String, Edge>>) n.adjacencies.entrySet()) {
                if (next < path.size()) {
                    if (path.get(next).equals(entry.getValue().eTarget)) {
                        k = Character.toString(entry.getKey().charAt(0));
                        for (Map.Entry<String, Edge> e : (Set<Map.Entry<String, Edge>>) n.adjacencies.entrySet()) {
                            String ek = Character.toString(entry.getKey().charAt(0));
                            if (ek.equals(line) && path.get(next).equals(e.getValue().eTarget)) {
                                f = true;
                            }
                        }
                    }
                }
            }
            if (!f && next < path.size() || line.isEmpty()) {
                line = k;
                sRoute = sRoute + "CHANGE," + line + ":";
            }
            next++;
        }

        return sRoute;
    }

    /**
     * @param args the command line arguments
     */
    public void getRoutes() {
        try {
            ResultSet rset = serv.getRoutes();
            while (rset.next()) {
                Vertex station = new Vertex(rset.getString(2));
                stations.put(station.name, station);
            }
            rset.beforeFirst();
            for (Map.Entry<String, Vertex> entry : (Set<Map.Entry<String, Vertex>>) stations.entrySet()) {
                Vertex station = entry.getValue();
                while (rset.next()) {

                    if (rset.getString(2).equals(station.name)) {
                        if (!rset.getString(3).isEmpty() || !station.adjacencies.toString().contains(rset.getString(3))) {
                            station.adjacencies.put(Character.toString(rset.getString(6).charAt(0)) + "1", new Edge((Vertex) stations.get(rset.getString(3)), rset.getInt(4)));
                        }
                        if (!rset.getString(1).isEmpty() || !station.adjacencies.toString().contains(rset.getString(1))) {
                            station.adjacencies.put(Character.toString(rset.getString(6).charAt(0)) + "0", new Edge((Vertex) stations.get(rset.getString(1)), rset.getInt(5)));
                        }
                    }
                }
                rset.beforeFirst();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public void findPaths(Vertex nStart) {
        PriorityQueue<Vertex> PrioQueue = new PriorityQueue<>();
        nStart.minDist = 0;
        PrioQueue.add(nStart);

        while (!PrioQueue.isEmpty()) {
            Vertex currentN = PrioQueue.poll();

            for (Map.Entry<String, Edge> entry : (Set<Map.Entry<String, Edge>>) currentN.adjacencies.entrySet()) {
                Vertex nextN = entry.getValue().eTarget;
                double newDistCost = currentN.minDist + entry.getValue().eWeight;

                if (newDistCost < nextN.minDist) {
                    PrioQueue.remove(nextN);

                    nextN.minDist = newDistCost;
                    nextN.previous = currentN;
                    PrioQueue.add(nextN);
                    stations.put(nextN.name, nextN);
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public ArrayList<Vertex> getPath(Vertex t) {
        ArrayList<Vertex> fsPath = new ArrayList<>();
        for (Vertex n = t; n != null; n = n.previous) {
            fsPath.add(n);
        }
        Collections.reverse(fsPath);
        return fsPath;
    }
}
