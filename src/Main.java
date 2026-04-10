import algorithm.BFSSolver;
import algorithm.DFSSolver;
import algorithm.Solver;
import io.MazeGenerator;
import io.MazeLoader;
import model.Cell;
import model.Maze;
import ui.MazeGUI;

import javax.swing.SwingUtilities;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║   RESOLUTION DE LABYRINTHE       ║");
        System.out.println("║   ESP/UCAD - Master 1 GLSI/SRT   ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. Mode Console                 ║");
        System.out.println("║  2. Mode Graphique (Swing)       ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.print("Votre choix : ");

        int choice = sc.nextInt();

        if (choice == 2) {
            SwingUtilities.invokeLater(MazeGUI::new);
        } else {
            System.out.println("\n1. Charger depuis fichier");
            System.out.println("2. Generer aleatoirement");
            System.out.print("Choix : ");
            int src = sc.nextInt();

            Maze maze;
            if (src == 1) {
                System.out.print("Chemin du fichier : ");
                maze = MazeLoader.load(sc.next());
            } else {
                System.out.print("Lignes (impair, ex: 11) : ");
                int rows = sc.nextInt();
                System.out.print("Colonnes (impair, ex: 21) : ");
                int cols = sc.nextInt();
                maze = MazeGenerator.generate(rows, cols);
            }

            System.out.println("\n=== Labyrinthe initial ===");
            maze.print();

            Solver dfs = new DFSSolver();
            List<Cell> dfsPath = dfs.solve(maze);
            System.out.println("\n=== Resolution DFS ===");
            maze.print();
            printStats("DFS", dfsPath, dfs);

            maze.reset();

            Solver bfs = new BFSSolver();
            List<Cell> bfsPath = bfs.solve(maze);
            System.out.println("\n=== Resolution BFS ===");
            maze.print();
            printStats("BFS", bfsPath, bfs);

            System.out.println("\n=== COMPARAISON DFS vs BFS ===");
            System.out.printf("%-15s %-10s %-10s%n", "Critere", "DFS", "BFS");
            System.out.printf("%-15s %-10d %-10d%n", "Etapes",      dfs.getStepCount(),      bfs.getStepCount());
            System.out.printf("%-15s %-10d %-10d%n", "Long.chemin", dfsPath.size(),           bfsPath.size());
            System.out.printf("%-15s %-10d %-10d%n", "Temps(ms)",   dfs.getExecutionTime(),   bfs.getExecutionTime());
        }
    }

    private static void printStats(String name, List<Cell> path, Solver solver) {
        if (path.isEmpty()) {
            System.out.println(name + " : Aucun chemin trouve !");
        } else {
            System.out.printf("%s : chemin=%d cases | etapes=%d | temps=%d ms%n",
                name, path.size(), solver.getStepCount(), solver.getExecutionTime());
        }
    }
}