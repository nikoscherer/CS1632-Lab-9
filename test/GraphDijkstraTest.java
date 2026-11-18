import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

public class GraphDijkstraTest {
    private Graph graph;
    private Node a, b, c, d;

    @BeforeEach
    public void setUp() {
        graph = new Graph();
        a = new Node("A");
        b = new Node("B");
        c = new Node("C");
        d = new Node("D");

        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);

        // Build a simple graph
        graph.addConnection("A", "B", 2.0);
        graph.addConnection("A", "C", 5.0);
        graph.addConnection("B", "C", 1.0);
        graph.addConnection("B", "D", 4.0);
        graph.addConnection("C", "D", 2.0);
    }

    @Test
    public void testGraphAdditionsAndConnections() {
        assertTrue(graph.contains("A"));
        assertTrue(graph.contains("B"));
        assertNotNull(graph.getNode("A"));
        assertEquals(2.0, graph.getNode("A").getNeighbors().get(graph.getNode("B")), 0.001);
    }

    @Test
    public void testDijkstraShortestPaths() {
        HashMap<Node, Double> distances = new HashMap<>();
        HashMap<Node, Node> previous = new HashMap<>();

        Main.runDijkstra(graph, a, distances, previous);

        // Verify shortest distances from A
        assertEquals(0.0, distances.get(a), 0.001);
        assertEquals(2.0, distances.get(b), 0.001);
        assertEquals(3.0, distances.get(c), 0.001); // Path A -> B -> C (2.0 + 1.0 = 3.0)
        assertEquals(5.0, distances.get(d), 0.001); // Path A -> B -> C -> D (2.0 + 1.0 + 2.0 = 5.0)
    }

    @Test
    public void testUnreachableNode() {
        Graph isolatedGraph = new Graph();
        Node x = new Node("X");
        Node y = new Node("Y");
        isolatedGraph.addNode(x);
        isolatedGraph.addNode(y);
        // No connection added between X and Y

        HashMap<Node, Double> distances = new HashMap<>();
        HashMap<Node, Node> previous = new HashMap<>();

        Main.runDijkstra(isolatedGraph, x, distances, previous);

        assertEquals(0.0, distances.get(x), 0.001);
        assertEquals(Double.MAX_VALUE, distances.get(y), 0.001);
    }
}