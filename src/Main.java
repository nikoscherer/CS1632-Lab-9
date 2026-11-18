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

            System.out.print("List of nodes:");
            for (Node n : graph.nodes) {
                System.out.print(" " + n.getName());
            }
            System.out.println();

            System.out.print("Node to start at: ");
            Node start = graph.getNode(scanner.nextLine().trim());

            for (Map.Entry<Node, Double> entry : runDjikstra(graph, start).entrySet()) {
                System.out.println(start.getName()
                        + " -> " + entry.getKey().getName()
                        + "  " + "dist: " + entry.getValue());
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public static HashMap<Node, Double> runDjikstra(Graph graph, Node start) {
        HashMap<Node, Double> distance = new HashMap<>();
        PriorityQueue<NodeDistance> queue = new PriorityQueue<>();
        HashSet<Node> visited = new HashSet<>();

        for (Node n : graph.nodes) {
            distance.put(n, Double.MAX_VALUE); // node, infinity
        }

        distance.put(start, 0.0);
        queue.add(new NodeDistance(start, 0.0));

        while (!queue.isEmpty()) {
            NodeDistance currND = queue.poll();
            Node curr = currND.node;

            if (visited.contains(curr)) {
                continue;
            }

            visited.add(curr);

            System.out.println(curr.getName());

            HashMap<Node, Double> neighborDist = curr.getNeighbors();
            for (Map.Entry<Node, Double> entry : neighborDist.entrySet()) {
                Node neighbor = entry.getKey();
                double d = entry.getValue();

                if (visited.contains(neighbor)) {
                    continue;
                }

                double newDist = distance.get(curr) + d;
                if (newDist < distance.get(neighbor)) {
                    distance.put(neighbor, newDist);
                    queue.add(new NodeDistance(neighbor, newDist));
                }
            }
        }

        return distance;
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
