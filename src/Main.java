import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Graph graph = new Graph();

        System.out.print("Graph file to read from: ");
        String fileName = "data/" + scanner.nextLine().trim();

        try (BufferedReader br =
                     new BufferedReader(new FileReader(fileName))) {
            while(br.ready()) {
                String line = br.readLine().trim();
                String[] connection = line.split(" ");

                // Add nodes if they don't exist
                if (!graph.contains(connection[0])) {
                    Node n = new Node(connection[0]);
                    graph.addNode(n);
                }
                if (!graph.contains(connection[1])) {
                    Node n = new Node(connection[1]);
                    graph.addNode(n);
                }

                graph.addConnection(connection[0], connection[1],
                        Double.parseDouble(connection[2]));
            }

            System.out.print("Starting Node: ");
            Node start = graph.getNode(scanner.nextLine().trim());

            runDjikstra(graph, start);
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public static void runDjikstra(Graph graph, Node start) {
        HashMap<Node, Double> distance = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>();
        HashSet<Node> visited = new HashSet<>();

        distance.put(start, 0.0);
        for (Node n : graph.nodes) {
            distance.put(n, Double.MAX_VALUE); // node, infinity
        }

        queue.add(start);
        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            HashMap<Node, Double> neighborDist = curr.getNeighbors();

            for (Node n : neighborDist.keySet()) {
                double dist =
                        neighborDist.get(n) + distance.get(curr);
                if (dist < distance.get(n)) {
                    distance.put(n, dist);
                }

                if (!visited.contains(curr)) {
                    queue.add(curr);
                }
            }

            System.out.println(curr.getName());



            visited.add(curr);
        }
    }
}

// Small wrapper class for distance from start node
class NodeDistance implements Comparable<NodeDistance> {
    Node node;
    double dist;

    public NodeDistance(Node node, double dist) {
        this.node = node;
        this.dist = dist;
    }

    @Override
    public int compareTo(NodeDistance other) {
        return Double.compare(this.dist, other.dist);
    }
}
