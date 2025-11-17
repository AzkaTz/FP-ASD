import javax.swing.*;
import java.awt.*;

public class GraphVisualizer extends JFrame {
    private Graph graph;
    private GraphPanel graphPanel;

    public GraphVisualizer(int[][] adjacencyMatrix) {
        setTitle("Graph Visualizer - Dijkstra Shortest Path");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] label = {"SUB", "MKS", "BDG", "CGK", "MLG", "DPS", "DHS", "YOG", "BTM", "PDG"};
        graph = new Graph(adjacencyMatrix, label);
        graphPanel = new GraphPanel(graph);

        add(graphPanel, BorderLayout.CENTER);

        // Add info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout());
        JLabel infoLabel = new JLabel("Drag nodes to rearrange. Red edges show shortest path from SUB to PDG.");
        infoPanel.add(infoLabel);
        add(infoPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        // Example adjacency matrix (weighted undirected graph)
        int[][] adjacencyMatrix = {
                //     SUB  MKS  BDG  CGK  MLG  DPS  DHS  YOG  BTM  PDG
                {0, 2, 3, 0, 8, 0, 0, 0, 0, 0},      // SUB
                {2, 0, 0, 3, 1, 0, 0, 0, 0, 0},      // MKS
                {3, 0, 0, 4, 0, 2, 0, 0, 0, 0},      // BDG
                {0, 3, 4, 0, 0, 6, 4, 0, 0, 0},      // CGK
                {8, 1, 0, 0, 0, 0, 2, 3, 0, 10},     // MLG
                {0, 0, 2, 6, 0, 0, 8, 0, 4, 0},      // DPS
                {0, 0, 0, 4, 2, 8, 0, 0, 0, 3},      // DHS
                {0, 0, 0, 0, 3, 0, 0, 0, 0, 4},      // YOG
                {0, 0, 0, 0, 0, 4, 0, 0, 0, 3},      // BTM
                {0, 0, 0, 0, 10, 0, 3, 4, 3, 0}      // PDG
        };

        SwingUtilities.invokeLater(() -> {
            GraphVisualizer visualizer = new GraphVisualizer(adjacencyMatrix);

            // Run Dijkstra algorithm
            visualizer.graph.setShortestPath(visualizer.graph.dijkstra(0, 9));

            visualizer.setVisible(true);
        });
    }
}