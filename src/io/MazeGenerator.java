package io;

import model.Maze;
import java.util.*;

/**
 * Génère aléatoirement un labyrinthe avec l'algorithme de Prim modifié.
 * Garantit toujours l'existence d'un chemin entre S et E.
 */
public class MazeGenerator {

    private static final Random rand = new Random();

    /**
     * Génère un labyrinthe de dimensions rows x cols.
     * Les dimensions doivent être impaires pour un meilleur résultat.
     *
     * @param rows nombre de lignes (recommandé : impair, ex. 11)
     * @param cols nombre de colonnes (recommandé : impair, ex. 21)
     * @return le Maze généré
     */
    public static Maze generate(int rows, int cols) {
        // S'assurer que les dimensions sont impaires
        if (rows % 2 == 0) rows++;
        if (cols % 2 == 0) cols++;

        // 1. Tout remplir de murs
        char[][] grid = new char[rows][cols];
        for (char[] row : grid) Arrays.fill(row, '#');

        // 2. Cellule de départ au centre-haut
        int startR = 1, startC = 1;
        grid[startR][startC] = '=';

        // 3. Liste des murs à considérer (frontières de la zone déjà creusée)
        List<int[]> walls = new ArrayList<>();
        addWalls(grid, startR, startC, walls, rows, cols);

        // 4. Algorithme de Prim : on "casse" les murs un par un
        while (!walls.isEmpty()) {
            // Choisir un mur aléatoire dans la liste
            int idx = rand.nextInt(walls.size());
            int[] wall = walls.get(idx);
            walls.remove(idx);

            int wr = wall[0], wc = wall[1];

            // Compter combien de voisins sont déjà des passages
            List<int[]> passages = getPassageNeighbors(grid, wr, wc, rows, cols);

            if (passages.size() == 1) {
                // Ce mur est sur la frontière : on le creuse
                grid[wr][wc] = '=';
                // On cherche la cellule de l'autre côté du mur
                int[] p = passages.get(0);
                int nr = 2*wr - p[0];
                int nc = 2*wc - p[1];

                if (nr > 0 && nr < rows-1 && nc > 0 && nc < cols-1 && grid[nr][nc] == '#') {
                    grid[nr][nc] = '=';
                    addWalls(grid, nr, nc, walls, rows, cols);
                }
            }
        }

        // 5. Placer S (départ) et E (arrivée) aux coins
        grid[1][1] = 'S';
        // Trouver un coin bas-droit accessible
        int er = rows - 2, ec = cols - 2;
        grid[er][ec] = 'E';

        // 6. Convertir en tableau de String pour Maze
        String[] lines = new String[rows];
        for (int r = 0; r < rows; r++) lines[r] = new String(grid[r]);

        return new Maze(lines);
    }

    /** Ajoute les murs voisins d'une cellule dans la liste */
    private static void addWalls(char[][] grid, int r, int c,
                                  List<int[]> walls, int rows, int cols) {
        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr > 0 && nr < rows-1 && nc > 0 && nc < cols-1 && grid[nr][nc] == '#')
                walls.add(new int[]{nr, nc});
        }
    }

    /** Retourne les voisins qui sont déjà des passages (à distance 1) */
    private static List<int[]> getPassageNeighbors(char[][] grid, int r, int c,
                                                    int rows, int cols) {
        List<int[]> result = new ArrayList<>();
        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] != '#')
                result.add(new int[]{nr, nc});
        }
        return result;
    }
}