import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GraphPanel extends JPanel {
    private Graph graph;
    private double[] nodeX;
    private double[] nodeY;
    private static final int NODE_RADIUS = 30;
    private int draggedNode = -1;

    public GraphPanel(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }
        this.graph = graph;
        setBackground(new Color(20, 30, 48));
        setPreferredSize(new Dimension(1000, 700));

        int numNodes = graph.getNumNodes();
        nodeX = new double[numNodes];
        nodeY = new double[numNodes];

        calculateNodePositions();

        // Mouse listener untuk drag
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (int i = 0; i < graph.getNumNodes(); i++) {
                    double dist = Math.hypot(e.getX() - nodeX[i], e.getY() - nodeY[i]);
                    if (dist <= NODE_RADIUS) {
                        draggedNode = i;
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggedNode = -1;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedNode != -1) {
                    nodeX[draggedNode] = e.getX();
                    nodeY[draggedNode] = e.getY();
                    repaint();
                }
            }
        });
    }

    private void calculateNodePositions() {
        int width = getWidth();
        int height = getHeight();

        if (width <= 1 || height <= 1) {
            width = 1000;
            height = 700;
        }

        double centerX = width / 2.0;
        double centerY = height / 2.0;
        double radius = Math.min(width, height) / 2.0 - 100;

        for (int i = 0; i < graph.getNumNodes(); i++) {
            double angle = 2 * Math.PI * i / graph.getNumNodes() - Math.PI / 2;
            nodeX[i] = centerX + radius * Math.cos(angle);
            nodeY[i] = centerY + radius * Math.sin(angle);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        calculateNodePositions();

        drawEdges(g2d);
        drawNodes(g2d);
    }

    private void drawEdges(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (Edge edge : graph.getEdges()) {
            int source = edge.getSource();
            int dest = edge.getDestination();
            int weight = edge.getWeight();

            // Cek apakah edge ini bagian dari shortest path
            boolean isInPath = isEdgeInPath(source, dest);

            // Warna edge
            Color edgeColor;
            if (isInPath) {
                edgeColor = new Color(255, 0, 0); // Merah untuk shortest path
                g2d.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            } else {
                float hue = (10 - weight) / 10.0f;
                edgeColor = Color.getHSBColor(hue * 0.6f, 0.8f, 0.9f);
                g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            }

            g2d.setColor(edgeColor);
            g2d.drawLine(
                    (int) nodeX[source],
                    (int) nodeY[source],
                    (int) nodeX[dest],
                    (int) nodeY[dest]
            );

            // Label weight
            double midX = (nodeX[source] + nodeX[dest]) / 2;
            double midY = (nodeY[source] + nodeY[dest]) / 2;

            g2d.setColor(new Color(20, 30, 48, 200));
            g2d.fillRect((int) midX - 15, (int) midY - 12, 30, 24);

            g2d.setColor(edgeColor);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRect((int) midX - 15, (int) midY - 12, 30, 24);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            FontMetrics fm = g2d.getFontMetrics();
            String weightStr = String.valueOf(weight);
            int labelX = (int) (midX - fm.stringWidth(weightStr) / 2);
            int labelY = (int) (midY + fm.getAscent() / 2 - 2);
            g2d.drawString(weightStr, labelX, labelY);
        }
    }

    private void drawNodes(Graphics2D g2d) {
        for (int i = 0; i < graph.getNumNodes(); i++) {
            // Gradient fill
            GradientPaint gradient = new GradientPaint(
                    (float) (nodeX[i] - NODE_RADIUS), (float) (nodeY[i] - NODE_RADIUS),
                    new Color(100, 180, 255),
                    (float) (nodeX[i] + NODE_RADIUS), (float) (nodeY[i] + NODE_RADIUS),
                    new Color(50, 120, 200)
            );
            g2d.setPaint(gradient);
            g2d.fillOval(
                    (int) (nodeX[i] - NODE_RADIUS),
                    (int) (nodeY[i] - NODE_RADIUS),
                    NODE_RADIUS * 2,
                    NODE_RADIUS * 2
            );

            // Border
            g2d.setColor(new Color(200, 220, 255));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawOval(
                    (int) (nodeX[i] - NODE_RADIUS),
                    (int) (nodeY[i] - NODE_RADIUS),
                    NODE_RADIUS * 2,
                    NODE_RADIUS * 2
            );

            // Label - Letakkan di bawah node
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 13));
            FontMetrics fm = g2d.getFontMetrics();
            String label = graph.getLabels()[i];
            int labelX = (int) (nodeX[i] - fm.stringWidth(label) / 2);
            int labelY = (int) (nodeY[i] + NODE_RADIUS + 20);
            g2d.drawString(label, labelX, labelY);
        }
    }

    private boolean isEdgeInPath(int source, int dest) {
        List<Integer> path = graph.getShortestPath();
        for (int i = 0; i < path.size() - 1; i++) {
            int a = path.get(i);
            int b = path.get(i + 1);
            if ((a == source && b == dest) || (a == dest && b == source)) {
                return true;
            }
        }
        return false;
    }
}