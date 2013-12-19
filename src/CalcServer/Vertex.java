package CalcServer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Mikael Kofod, Sune Heide, Kristian Jacobsen
 */
public class Vertex implements Comparable<Vertex> {

    String name;
    double minDist = Double.POSITIVE_INFINITY;
    HashMap adjacencies;
    ArrayList<String> stations;
    Vertex previous;

    /**
     * @param args the command line arguments
     */
    public Vertex(String name) {
        this.name = name;
        stations = new ArrayList<>();
        adjacencies = new HashMap();
    }

    /**
     * @param args the command line arguments
     */
    @Override
    public int compareTo(Vertex o) {
        return Double.compare(minDist, o.minDist);
    }

    /**
     * @param args the command line arguments
     */
    @Override
    public String toString() {
        return name;
    }
}
