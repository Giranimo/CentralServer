package CalcServer;

/**
 *
 * @author Mikael Kofod, Sune Heide, Kristian Jacobsen
 */
public class Edge {

    Vertex eTarget;
    int eWeight;

    /**
     * @param args the command line arguments
     */
    public Edge(Vertex target, int weight) {
        this.eTarget = target;
        this.eWeight = weight;
    }

    /**
     * @param args the command line arguments
     */
    @Override
    public String toString() {
        return eTarget.name + ":" + eWeight;
    }
}
