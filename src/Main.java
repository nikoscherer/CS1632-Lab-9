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

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while (br.ready()) {
                String line = br.readLine().trim();
                String[] connection = line.split(" ");

                if (!graph.contains(connection[0])) {
                    graph.addNode(new Node(connection[0]));
                }
                if (!graph.contains(connection[1])) {
                    graph.addNode(new Node(connection[1]));
                }

                graph.addConnection(connection[0], connection[1], Double.parseDouble(connection[2]));
            }

            System.out.print("List of nodes:");
            for (Node n : graph.nodes) {
                System.out.print(" " + n.getName());
            }
            System.out.println();

            System.out.print("Node to start at: ");
            Node start = graph.getNode(scanner.nextLine().trim());

            HashMap<Node, Double> distances = new HashMap<>();
            HashMap<Node, Node> previous = new HashMap<>();

            runDijkstra(graph, start, distances, previous);

            // Print distances and paths
            for (Node n : graph.nodes) {
                double dist = distances.get(n);
                if (dist == Double.MAX_VALUE) {
                    System.out.println(n.getName() + " is unreachable.");
                } else {
                    printPath(start, n, previous, dist);
                }
            }

        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public static void runDijkstra(Graph graph, Node start, HashMap<Node, Double> distances, HashMap<Node, Node> previous) {
        PriorityQueue<NodeDistance> queue = new PriorityQueue<>();
        HashSet<Node> visited = new HashSet<>();

        for (Node n : graph.nodes) {
            distances.put(n, Double.MAX_VALUE);
        }

        distances.put(start, 0.0);
        queue.add(new NodeDistance(start, 0.0));

        while (!queue.isEmpty()) {
            NodeDistance currND = queue.poll();
            Node curr = currND.node;

            if (visited.contains(curr)) {
                continue;
            }
            visited.add(curr);

            HashMap<Node, Double> neighborDist = curr.getNeighbors();
            for (Map.Entry<Node, Double> entry : neighborDist.entrySet()) {
                Node neighbor = entry.getKey();
                double weight = entry.getValue();

                if (visited.contains(neighbor)) {
                    continue;
                }

                double newDist = distances.get(curr) + weight;
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, curr); // Save the path history
                    queue.add(new NodeDistance(neighbor, newDist));
                }
            }
        }
    }

    // Helper to print the path like: B -> A -> C  dist: 5.0
    public static void printPath(Node start, Node target, HashMap<Node, Node> previous, double dist) {
        List<String> path = new ArrayList<>();
        Node curr = target;

        while (curr != null) {
            path.add(0, curr.getName());
            if (curr.equals(start)) {
                break;
            }
            curr = previous.get(curr);
        }

        System.out.println(String.join(" -> ", path) + "  dist: " + dist);
    }
}

// Required wrapper class because Java's PriorityQueue doesn't support decreaseKey()
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