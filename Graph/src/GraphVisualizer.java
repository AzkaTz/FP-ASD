import javax.swing.*;
import java.awt.*;

public class GraphVisualizer extends JFrame {
    private Graph graph;
    private GraphPanel graphPanel;
    private JComboBox<String> sourceCombo;
    private JComboBox<String> targetCombo;
    private JLabel resultLabel;

    public GraphVisualizer(int[][] adjacencyMatrix) {
        setTitle("Graph Visualizer with Dijkstra Algorithm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] label = {"MKS", "SUB", "BDG", "CGK", "DPS", "MLG", "DHS", "YOG", "PDG", "BTM"};
        graph = new Graph(adjacencyMatrix, label);
        graphPanel = new GraphPanel(graph);

        setLayout(new BorderLayout());
        add(graphPanel, BorderLayout.CENTER);

        // Panel kontrol untuk Dijkstra
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        controlPanel.add(new JLabel("Source:"));
        sourceCombo = new JComboBox<>(label);
        controlPanel.add(sourceCombo);

        controlPanel.add(new JLabel("Target:"));
        targetCombo = new JComboBox<>(label);
        targetCombo.setSelectedIndex(label.length - 1); // Default ke node terakhir
        controlPanel.add(targetCombo);

        JButton calculateButton = new JButton("Find Shortest Path");
        calculateButton.addActionListener(e -> calculateShortestPath());
        controlPanel.add(calculateButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetPath());
        controlPanel.add(resetButton);

        add(controlPanel, BorderLayout.NORTH);

        // Panel info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        resultLabel = new JLabel("Select source and target, then click 'Find Shortest Path'");
        resultLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        infoPanel.add(resultLabel, BorderLayout.CENTER);

        JLabel dragLabel = new JLabel("Drag nodes to rearrange | Green edges = shortest path");
        dragLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        infoPanel.add(dragLabel, BorderLayout.SOUTH);

        add(infoPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void calculateShortestPath() {
        int sourceIndex = sourceCombo.getSelectedIndex();
        int targetIndex = targetCombo.getSelectedIndex();

        if (sourceIndex == targetIndex) {
            resultLabel.setText("Source and target are the same!");
            return;
        }

        Graph.DijkstraResult result = graph.dijkstra(sourceIndex, targetIndex);

        if (result.getPath().isEmpty()) {
            resultLabel.setText("No path found from " + sourceCombo.getSelectedItem() +
                    " to " + targetCombo.getSelectedItem());
        } else {
            String pathStr = result.getPathString(graph.getLabel());
            resultLabel.setText("<html>Shortest Path: <b>" + pathStr +
                    "</b> | Total Distance: <b>" + result.getDistance() + "</b></html>");
        }

        graphPanel.repaint();
    }

    private void resetPath() {
        graph.getShortestPathEdges().clear();
        resultLabel.setText("Select source and target, then click 'Find Shortest Path'");
        graphPanel.repaint();
    }

    public static void main(String[] args) {
        // Example adjacency matrix (weighted directed graph)
        int[][] adjacencyMatrix = {
                {0, 2, 3, 0, 8, 0, 0, 0, 0, 0},
                {2, 0, 0, 3, 1, 0, 0, 0, 0, 0},
                {3, 0, 0, 4, 0, 2, 0, 0, 0, 0},
                {0, 3, 4, 0, 0, 6, 4, 0, 0, 0},
                {8, 1, 0, 0, 0, 0, 2, 3, 0, 10},
                {0, 0, 2, 6, 0, 0, 8, 0, 4, 0},
                {0, 0, 0, 4, 2, 8, 0, 0, 0, 3},
                {0, 0, 0, 0, 3, 0, 0, 0, 0, 4},
                {0, 0, 0, 0, 0, 4, 0, 0, 0, 3},
                {0, 0, 0, 0, 10, 0, 3, 4, 3, 0},
        };

        SwingUtilities.invokeLater(() -> {
            GraphVisualizer visualizer = new GraphVisualizer(adjacencyMatrix);
            visualizer.setVisible(true);
        });
    }
}