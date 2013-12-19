package CalcServer;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author Mikael Kofod, Sune Heide, Kristian Jacobsen
 */
public class PriceCalc {

    private CalcServer serv;
    private HashMap zones;
    private int mHeight = 4;
    private int mWidth = 5;

    /**
     * @param args the command line arguments
     */
    public PriceCalc(CalcServer cServ) {
        serv = cServ;
        zones = new HashMap();
        createMatrix();
        getStations();
    }

    /**
     * @param args the command line arguments
     */
    public double getPrice(String start, String end) {
        Vertex zone, szone, ezone;
        zone = szone = ezone = null;
        for (Entry<String, Vertex> entry : (Set<Entry<String, Vertex>>) zones.entrySet()) {
            zone = entry.getValue();
            if (!zone.stations.isEmpty()) {
                for (String s : zone.stations) {
                    if (s.equals(start)) {
                        szone = zone;
                    }
                    for (String se : zone.stations) {
                        if (se.equals(end)) {
                            ezone = zone;
                        }
                    }
                }
            }
        }
        findPaths(szone);
        ArrayList path = getPath(ezone);
        return path.size();
    }

    /**
     * @param args the command line arguments
     */
    public void createMatrix() {
        int w = 0;
        int h = 2;
        zones.put("1", new Vertex("1"));
        while (zones.size() < ((mHeight * mWidth) + 1)) {
            if (w < mWidth) {
                zones.put(h + "" + w + "", new Vertex(h + "" + w + ""));
                w++;
            } else {
                w = 0;
                h++;
            }
        }
        Vertex zone, tZone;
        zone = (Vertex) zones.get("1");
        for (int w1 = 0; w1 < mWidth; w1++) {
            tZone = (Vertex) zones.get("2" + w1);
            zone.adjacencies.put(tZone, new Edge(tZone, 1));
        }
        zones.put(zone.name, zone);

        w = 0;
        h = 2;
        while (h < mHeight + 2) {
            zone = (Vertex) zones.get("" + h + "" + w);
            int nh, nw;
            if (h == 2) {
                zone.adjacencies.put("1", new Edge((Vertex) zones.get("1"), 1));
                nh = h;
            } else {
                nh = h - 1;
            }
            if (w == 0) {
                nw = w;
            } else {
                nw = w - 1;
            }

            for (int hi = nh; hi <= h + 1; hi++) {
                if (hi > 5) {
                    break;
                }
                for (int wi = nw; wi <= w + 1; wi++) {
                    if (wi > 4) {
                        break;
                    } else {
                        zone.adjacencies.put(hi + "" + wi + "", (new Edge((Vertex) zones.get(hi + "" + wi + ""), 1)));
                    }
                }
            }
            zones.put(h + "" + w + "", zone);
            if (w < mWidth - 1) {
                w++;
            } else {
                w = 0;
                h++;
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public void getStations() {
        try {
            ResultSet rset = serv.getStations();
            while (rset.next()) {
                Vertex zone = (Vertex) zones.get(rset.getString(2));
                if (!zone.stations.contains(rset.getString(1))) {
                    zone.stations.add(rset.getString(1));
                }
                zones.put(zone.name, zone);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                    zones.put(nextN.name, nextN);
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
