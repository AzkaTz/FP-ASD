import java.util.*;

class Graph {
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;
    private int[][] adjacencyMatrix;
    private String[] label;
    private ArrayList<Edge> shortestPathEdges; // Menyimpan edges dari path terpendek

    public Graph(int[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.shortestPathEdges = new ArrayList<>();
        initializeGraph();
    }

    public Graph(int[][] adjacencyMatrix, String[] l) {
        this.adjacencyMatrix = adjacencyMatrix;
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.label = l;
        this.shortestPathEdges = new ArrayList<>();
        initializeGraph();
    }

    private void initializeGraph() {
        int n = adjacencyMatrix.length;

        // Create nodes in circular layout
        int centerX = 400;
        int centerY = 300;
        int radius = 200;

        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            int x = centerX + (int)(radius * Math.cos(angle));
            int y = centerY + (int)(radius * Math.sin(angle));
            nodes.add(new Node(i, x, y));
        }

        // Create edges from adjacency matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (adjacencyMatrix[i][j] != 0) {
                    edges.add(new Edge(nodes.get(i), nodes.get(j), adjacencyMatrix[i][j]));
                }
            }
        }
    }

    // Implementasi Algoritma Dijkstra
    public DijkstraResult dijkstra(int sourceId, int targetId) {
        int n = adjacencyMatrix.length;
        int[] distances = new int[n];
        int[] previous = new int[n];
        boolean[] visited = new boolean[n];

        // Inisialisasi
        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(previous, -1);
        distances[sourceId] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.offer(new int[]{sourceId, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int currentNode = current[0];

            if (visited[currentNode]) continue;
            visited[currentNode] = true;

            // Periksa semua tetangga
            for (int neighbor = 0; neighbor < n; neighbor++) {
                if (adjacencyMatrix[currentNode][neighbor] != 0 && !visited[neighbor]) {
                    int newDist = distances[currentNode] + adjacencyMatrix[currentNode][neighbor];

                    if (newDist < distances[neighbor]) {
                        distances[neighbor] = newDist;
                        previous[neighbor] = currentNode;
                        pq.offer(new int[]{neighbor, newDist});
                    }
                }
            }
        }

        // Rekonstruksi path
        ArrayList<Integer> path = new ArrayList<>();
        if (distances[targetId] != Integer.MAX_VALUE) {
            int current = targetId;
            while (current != -1) {
                path.add(0, current);
                current = previous[current];
            }
        }

        // Update shortestPathEdges
        updateShortestPathEdges(path);

        return new DijkstraResult(distances[targetId], path);
    }

    private void updateShortestPathEdges(ArrayList<Integer> path) {
        shortestPathEdges.clear();

        for (int i = 0; i < path.size() - 1; i++) {
            int from = path.get(i);
            int to = path.get(i + 1);

            // Cari edge yang sesuai
            for (Edge edge : edges) {
                if (edge.getSource().getId() == from && edge.getTarget().getId() == to) {
                    shortestPathEdges.add(edge);
                    break;
                }
            }
        }
    }

    public ArrayList<Node> getNodes() { return nodes; }
    public ArrayList<Edge> getEdges() { return edges; }
    public int[][] getAdjacencyMatrix() { return adjacencyMatrix; }
    public String[] getLabel() { return label; }
    public ArrayList<Edge> getShortestPathEdges() { return shortestPathEdges; }

    // Inner class untuk hasil Dijkstra
    public static class DijkstraResult {
        private int distance;
        private ArrayList<Integer> path;

        public DijkstraResult(int distance, ArrayList<Integer> path) {
            this.distance = distance;
            this.path = path;
        }

        public int getDistance() { return distance; }
        public ArrayList<Integer> getPath() { return path; }

        public String getPathString(String[] labels) {
            if (path.isEmpty()) return "No path found";

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < path.size(); i++) {
                if (labels != null) {
                    sb.append(labels[path.get(i)]);
                } else {
                    sb.append(path.get(i));
                }
                if (i < path.size() - 1) sb.append(" -> ");
            }
            return sb.toString();
        }
    }
}