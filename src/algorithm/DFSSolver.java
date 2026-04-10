package algorithm;

import model.Cell;
import model.Maze;
import java.util.*;

/**
 * Résolution par DFS (Depth-First Search = Recherche en Profondeur).
 *
 * PRINCIPE : On explore une direction jusqu'au bout avant de revenir en arrière.
 * STRUCTURE UTILISÉE : une Pile (Stack) — LIFO (Last In, First Out).
 *
 * AVANTAGE  : Faible consommation mémoire.
 * INCONVÉNIENT : Ne garantit PAS le chemin le plus court.
 */
public class DFSSolver implements Solver {

    private int stepCount;       // Nombre de cases explorées
    private long executionTime;  // Durée en millisecondes

    @Override
    public List<Cell> solve(Maze maze) {
        stepCount = 0;
        long startTime = System.currentTimeMillis();

        Cell start = maze.getStart();
        Cell end   = maze.getEnd();

        // Pile DFS : chaque élément est le chemin complet jusqu'à cette case
        // (utile pour reconstruire la solution directement)
        Deque<List<Cell>> stack = new ArrayDeque<>();

        // Ensemble des cases déjà visitées (évite les cycles)
        Set<Cell> visited = new HashSet<>();

        // Initialisation : on part de la case départ
        List<Cell> initialPath = new ArrayList<>();
        initialPath.add(start);
        stack.push(initialPath);

        while (!stack.isEmpty()) {
            // On dépile le chemin courant (dernier entré = premier sorti)
            List<Cell> currentPath = stack.pop();
            Cell current = currentPath.get(currentPath.size() - 1);

            // Si déjà visité, on passe
            if (visited.contains(current)) continue;
            visited.add(current);
            stepCount++;

            // Marquer comme visité dans la grille (pour affichage)
            if (current.getType() == Cell.Type.PATH)
                current.setType(Cell.Type.VISITED);

            // OBJECTIF ATTEINT ?
            if (current == end) {
                executionTime = System.currentTimeMillis() - startTime;
                markSolution(currentPath); // Marquer le chemin final
                return currentPath;
            }

            // Explorer les voisins franchissables non encore visités
            for (Cell neighbor : maze.getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    // Créer un nouveau chemin = chemin actuel + ce voisin
                    List<Cell> newPath = new ArrayList<>(currentPath);
                    newPath.add(neighbor);
                    stack.push(newPath); // Empiler pour exploration future
                }
            }
        }

        // Aucun chemin trouvé
        executionTime = System.currentTimeMillis() - startTime;
        return new ArrayList<>();
    }

    /** Marque les cellules du chemin solution avec le type SOLUTION */
    private void markSolution(List<Cell> path) {
        for (Cell c : path) {
            if (c.getType() == Cell.Type.VISITED || c.getType() == Cell.Type.PATH)
                c.setType(Cell.Type.SOLUTION);
        }
    }

    @Override public int getStepCount()      { return stepCount; }
    @Override public long getExecutionTime() { return executionTime; }
}