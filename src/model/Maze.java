package model;

/**
 * Représente le labyrinthe sous forme de matrice 2D de Cell.
 * Contient les positions de départ (S) et d'arrivée (E).
 */
public class Maze {

    private Cell[][] grid;   // La grille 2D
    private int rows;        // Nombre de lignes
    private int cols;        // Nombre de colonnes
    private Cell start;      // Case de départ
    private Cell end;        // Case d'arrivée

    /**
     * Construit un labyrinthe depuis un tableau de chaînes.
     * Chaque caractère de chaque ligne devient une Cell.
     *
     * @param lines tableau de chaînes représentant le labyrinthe
     */
    public Maze(String[] lines) {
        this.rows = lines.length;
        this.cols = lines[0].length();
        this.grid = new Cell[rows][cols];

        // Parcours de chaque caractère pour créer les cellules
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = lines[r].charAt(c);
                Cell.Type type;

                switch (ch) {
                    case '#': type = Cell.Type.WALL;    break;
                    case 'S': type = Cell.Type.START;   break;
                    case 'E': type = Cell.Type.END;     break;
                    default:  type = Cell.Type.PATH;    break; // '=' ou espace
                }

                grid[r][c] = new Cell(r, c, type);

                // On retient les cases spéciales
                if (type == Cell.Type.START) start = grid[r][c];
                if (type == Cell.Type.END)   end   = grid[r][c];
            }
        }
    }

    // --- Accès aux cellules ---

    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public Cell getStart() { return start; }
    public Cell getEnd()   { return end; }

    /**
     * Retourne les voisins franchissables d'une cellule
     * (haut, bas, gauche, droite — pas de diagonale).
     */
    public Cell[] getNeighbors(Cell cell) {
        int r = cell.getRow();
        int c = cell.getCol();

        // Directions : haut, bas, gauche, droite
        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
        java.util.List<Cell> neighbors = new java.util.ArrayList<>();

        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];
            // Vérification des bornes + franchissabilité
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                    && grid[nr][nc].isWalkable()) {
                neighbors.add(grid[nr][nc]);
            }
        }
        return neighbors.toArray(new Cell[0]);
    }

    /**
     * Affiche le labyrinthe dans la console.
     * Les types de cases sont convertis en caractères visuels.
     */
    public void print() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                switch (cell.getType()) {
                    case WALL:     System.out.print('#'); break;
                    case START:    System.out.print('S'); break;
                    case END:      System.out.print('E'); break;
                    case SOLUTION: System.out.print('+'); break;
                    case VISITED:  System.out.print('.'); break;
                    default:       System.out.print(' '); break;
                }
            }
            System.out.println(); // Nouvelle ligne après chaque rangée
        }
    }

    /**
     * Réinitialise toutes les cases visitées/solution
     * (pour pouvoir relancer un autre algorithme proprement).
     */
    public void reset() {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                Cell.Type t = grid[r][c].getType();
                if (t == Cell.Type.VISITED || t == Cell.Type.SOLUTION)
                    grid[r][c].setType(Cell.Type.PATH);
            }
    }
}