package ui;

import algorithm.BFSSolver;
import algorithm.DFSSolver;
import algorithm.Solver;
import io.MazeGenerator;
import io.MazeLoader;
import model.Cell;
import model.Maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

/**
 * Interface graphique Swing pour visualiser la résolution du labyrinthe.
 * Affiche la grille, permet de choisir l'algorithme, et anime la solution.
 */
public class MazeGUI extends JFrame {

    // Taille d'une case en pixels
    private static final int CELL_SIZE = 40;

    private Maze maze;           // Le labyrinthe courant
    private MazePanel panel;     // Panneau de dessin

    // Couleurs des types de cases
    private static final Color COLOR_WALL     = new Color(40, 40, 40);
    private static final Color COLOR_PATH     = new Color(240, 240, 240);
    private static final Color COLOR_START    = new Color(50, 200, 50);
    private static final Color COLOR_END      = new Color(200, 50, 50);
    private static final Color COLOR_VISITED  = new Color(173, 216, 230);
    private static final Color COLOR_SOLUTION = new Color(255, 215, 0);

    public MazeGUI() {
        super("Résolution de Labyrinthe — ESP/UCAD");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Panneau de contrôle en bas ---
        JPanel controls = new JPanel();

        JButton btnLoad    = new JButton("📂 Charger fichier");
        JButton btnGenerate = new JButton("🎲 Générer");
        JButton btnDFS     = new JButton("▶ DFS");
        JButton btnBFS     = new JButton("▶ BFS");
        JButton btnReset   = new JButton("🔄 Reset");
        JLabel  lblStatus  = new JLabel("  Chargez ou générez un labyrinthe.");

        controls.add(btnLoad);
        controls.add(btnGenerate);
        controls.add(btnDFS);
        controls.add(btnBFS);
        controls.add(btnReset);
        controls.add(lblStatus);

        add(controls, BorderLayout.SOUTH);

        // --- Panneau de dessin ---
        panel = new MazePanel();
        JScrollPane scroll = new JScrollPane(panel);
        add(scroll, BorderLayout.CENTER);

        // --- Actions des boutons ---

        btnGenerate.addActionListener(e -> {
            maze = MazeGenerator.generate(15, 21);
            panel.setMaze(maze);
            lblStatus.setText("  Labyrinthe généré.");
        });

        btnLoad.addActionListener(e -> {
            JFileChooser fc = new JFileChooser("mazes/");
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    maze = MazeLoader.load(fc.getSelectedFile().getAbsolutePath());
                    panel.setMaze(maze);
                    lblStatus.setText("  Fichier chargé : " + fc.getSelectedFile().getName());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
                }
            }
        });

        btnDFS.addActionListener(e -> runSolver(new DFSSolver(), "DFS", lblStatus));
        btnBFS.addActionListener(e -> runSolver(new BFSSolver(), "BFS", lblStatus));

        btnReset.addActionListener(e -> {
            if (maze != null) {
                maze.reset();
                panel.repaint();
                lblStatus.setText("  Réinitialisé.");
            }
        });

        pack();
        setLocationRelativeTo(null); // Centrer la fenêtre
        setVisible(true);
    }

    /**
     * Lance un algorithme de résolution et affiche les résultats.
     */
    private void runSolver(Solver solver, String name, JLabel lblStatus) {
        if (maze == null) {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord charger ou générer un labyrinthe.");
            return;
        }
        maze.reset();

        List<Cell> solution = solver.solve(maze);
        panel.repaint();

        if (solution.isEmpty()) {
            lblStatus.setText("  " + name + " : aucun chemin trouvé !");
        } else {
            lblStatus.setText(String.format(
                "  %s : chemin=%d cases | étapes=%d | temps=%d ms",
                name,
                solution.size(),
                solver.getStepCount(),
                solver.getExecutionTime()
            ));
        }
    }

    // -------------------------------------------------------
    /** Panneau interne qui dessine le labyrinthe case par case */
    private class MazePanel extends JPanel {

        private Maze currentMaze;

        public void setMaze(Maze m) {
            this.currentMaze = m;
            // Ajuster la taille du panneau selon le labyrinthe
            setPreferredSize(new Dimension(
                m.getCols() * CELL_SIZE,
                m.getRows() * CELL_SIZE
            ));
            revalidate();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (currentMaze == null) return;

            for (int r = 0; r < currentMaze.getRows(); r++) {
                for (int c = 0; c < currentMaze.getCols(); c++) {
                    Cell cell = currentMaze.getCell(r, c);

                    // Choisir la couleur selon le type
                    Color color;
                    switch (cell.getType()) {
                        case WALL:     color = COLOR_WALL;     break;
                        case START:    color = COLOR_START;    break;
                        case END:      color = COLOR_END;      break;
                        case VISITED:  color = COLOR_VISITED;  break;
                        case SOLUTION: color = COLOR_SOLUTION; break;
                        default:       color = COLOR_PATH;     break;
                    }

                    // Dessiner la case
                    g.setColor(color);
                    g.fillRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                    // Dessiner le contour
                    g.setColor(Color.GRAY);
                    g.drawRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                    // Dessiner les lettres S et E
                    if (cell.getType() == Cell.Type.START || cell.getType() == Cell.Type.END) {
                        g.setColor(Color.WHITE);
                        g.setFont(new Font("Arial", Font.BOLD, CELL_SIZE / 2));
                        String letter = cell.getType() == Cell.Type.START ? "S" : "E";
                        g.drawString(letter,
                            c * CELL_SIZE + CELL_SIZE/3,
                            r * CELL_SIZE + 2*CELL_SIZE/3);
                    }
                }
            }
        }
    }
}