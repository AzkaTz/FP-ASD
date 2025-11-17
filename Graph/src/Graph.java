import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Graph {
    private int numNodes;
    private int[][] adjacencyMatrix;
    private List<Edge> edges;
    private List<Integer> shortestPath;

    public Graph(int numNodes) {
        this.numNodes = numNodes;
        this.adjacencyMatrix = new int[numNodes][numNodes];
        this.edges = new ArrayList<>();
        this.shortestPath = new ArrayList<>();
    }

    public void addEdge(int source, int destination, int weight) {
        adjacencyMatrix[source][destination] = weight;
        adjacencyMatrix[destination][source] = weight; // Undirected graph
        edges.add(new Edge(source, destination, weight));
    }

    public void setShortestPath(List<Integer> path) {
        this.shortestPath = path;
    }

    public List<Integer> getShortestPath() {
        return shortestPath;
    }

    public int getNumNodes() {
        return numNodes;
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    // Dijkstra Algorithm Method
    public List<Integer> dijkstra(int source, int destination) {
        int[] distance = new int[numNodes];
        int[] parent = new int[numNodes];
        boolean[] visited = new boolean[numNodes];

        // Inisialisasi
        for (int i = 0; i < numNodes; i++) {
            distance[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }
        distance[source] = 0;

        // Dijkstra algorithm
        for (int count = 0; count < numNodes - 1; count++) {
            int u = findMinDistance(distance, visited);

            if (u == -1) break;

            visited[u] = true;

            // Update distance untuk semua neighbor
            for (int v = 0; v < numNodes; v++) {
                if (!visited[v] && adjacencyMatrix[u][v] != 0) {
                    int newDist = distance[u] + adjacencyMatrix[u][v];
                    if (newDist < distance[v]) {
                        distance[v] = newDist;
                        parent[v] = u;
                    }
                }
            }
        }

        // Reconstruct path
        List<Integer> path = new ArrayList<>();
        int current = destination;

        while (current != -1) {
            path.add(current);
            current = parent[current];
        }

        Collections.reverse(path);

        // Print hasil
        if (distance[destination] == Integer.MAX_VALUE) {
            System.out.println("\n❌ Tidak ada path dari N" + source + " ke N" + destination);
        } else {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║     DIJKSTRA SHORTEST PATH         ║");
            System.out.println("╚════════════════════════════════════╝\n");
            System.out.println("📍 Source: N" + source);
            System.out.println("🎯 Destination: N" + destination);
            System.out.println("📏 Total Distance: " + distance[destination]);

            System.out.println("\n🛣️  Path: ");
            for (int i = 0; i < path.size(); i++) {
                System.out.print("N" + path.get(i));
                if (i < path.size() - 1) {
                    System.out.print(" → ");
                }
            }
            System.out.println("\n");

            System.out.println("📋 Nodes in order:");
            for (int i = 0; i < path.size(); i++) {
                System.out.println("   " + (i + 1) + ". N" + path.get(i));
            }
            System.out.println();
        }

        return path;
    }

    private int findMinDistance(int[] distance, boolean[] visited) {
        int min = Integer.MAX_VALUE;
        int minNode = -1;

        for (int i = 0; i < numNodes; i++) {
            if (!visited[i] && distance[i] < min) {
                min = distance[i];
                minNode = i;
            }
        }

        return minNode;
    }

    public void printAdjacencyMatrix() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║   WEIGHTED ADJACENCY MATRIX        ║");
        System.out.println("╚════════════════════════════════════╝\n");

        System.out.print("     ");
        for (int i = 0; i < numNodes; i++) {
            System.out.print(String.format("N%-2d ", i));
        }
        System.out.println();

        for (int i = 0; i < numNodes; i++) {
            System.out.print(String.format("N%-2d  ", i));
            for (int j = 0; j < numNodes; j++) {
                System.out.print(String.format("%-3d ", adjacencyMatrix[i][j]));
            }
            System.out.println();
        }

        System.out.println("\n🔍 Edge Information:");
        for (Edge edge : edges) {
            System.out.println("   N" + edge.getSource() + " ↔ N" + edge.getDestination() +
                    " (Weight: " + edge.getWeight() + ")");
        }
        System.out.println();
    }
}