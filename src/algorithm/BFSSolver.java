package algorithm;

import model.Cell;
import model.Maze;
import java.util.*;

/**
 * Résolution par BFS (Breadth-First Search = Recherche en Largeur).
 *
 * PRINCIPE : On explore toutes les cases à distance 1, puis 2, puis 3...
 * STRUCTURE UTILISÉE : une File (Queue) — FIFO (First In, First Out).
 *
 * AVANTAGE  : Garantit le chemin le PLUS COURT.
 * INCONVÉNIENT : Consomme plus de mémoire (stocke plus de chemins).
 */
public class BFSSolver implements Solver {

    private int stepCount;
    private long executionTime;

    @Override
    public List<Cell> solve(Maze maze) {
        stepCount = 0;
        long startTime = System.currentTimeMillis();

        Cell start = maze.getStart();
        Cell end   = maze.getEnd();

        // File BFS : chaque élément est un chemin complet
        Queue<List<Cell>> queue = new LinkedList<>();

        // Ensemble des cases déjà visitées
        Set<Cell> visited = new HashSet<>();

        // Initialisation
        List<Cell> initialPath = new ArrayList<>();
        initialPath.add(start);
        queue.add(initialPath);
        visited.add(start);

        while (!queue.isEmpty()) {
            // On défile le chemin courant (premier entré = premier sorti)
            List<Cell> currentPath = queue.poll();
            Cell current = currentPath.get(currentPath.size() - 1);
            stepCount++;

            // Marquer visuellement
            if (current.getType() == Cell.Type.PATH)
                current.setType(Cell.Type.VISITED);

            // OBJECTIF ATTEINT ?
            if (current == end) {
                executionTime = System.currentTimeMillis() - startTime;
                markSolution(currentPath);
                return currentPath;
            }

            // Explorer tous les voisins non visités
            for (Cell neighbor : maze.getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    List<Cell> newPath = new ArrayList<>(currentPath);
                    newPath.add(neighbor);
                    queue.add(newPath); // Enfiler pour exploration future
                }
            }
        }

        executionTime = System.currentTimeMillis() - startTime;
        return new ArrayList<>();
    }

    private void markSolution(List<Cell> path) {
        for (Cell c : path) {
            if (c.getType() == Cell.Type.VISITED || c.getType() == Cell.Type.PATH)
                c.setType(Cell.Type.SOLUTION);
        }
    }

    @Override public int getStepCount()      { return stepCount; }
    @Override public long getExecutionTime() { return executionTime; }
}