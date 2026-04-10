package algorithm;

import model.Cell;
import model.Maze;
import java.util.List;

/**
 * Interface que tous les algorithmes de résolution doivent implémenter.
 * Permet de les utiliser de manière interchangeable (polymorphisme).
 */
public interface Solver {

    /**
     * Résout le labyrinthe donné.
     *
     * @param maze le labyrinthe à résoudre
     * @return la liste des cellules formant le chemin solution,
     *         ou une liste vide si aucun chemin n'existe
     */
    List<Cell> solve(Maze maze);

    /**
     * Retourne le nombre d'étapes explorées (pour comparaison des perf).
     */
    int getStepCount();

    /**
     * Retourne le temps d'exécution en millisecondes.
     */
    long getExecutionTime();
}