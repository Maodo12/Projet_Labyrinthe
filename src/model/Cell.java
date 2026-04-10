package model;

/**
 * Représente une cellule (case) du labyrinthe.
 * Chaque case a une position (ligne, colonne) et un type.
 */
public class Cell {

    // Types possibles d'une case
    public enum Type {
        WALL,    // Mur (#)
        PATH,    // Passage (=)
        START,   // Point de départ (S)
        END,     // Point d'arrivée (E)
        VISITED, // Case visitée lors de la résolution
        SOLUTION // Case faisant partie du chemin solution (+)
    }

    private int row;    // Numéro de ligne
    private int col;    // Numéro de colonne
    private Type type;  // Type de cette case

    public Cell(int row, int col, Type type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    // Getters / Setters
    public int getRow()       { return row; }
    public int getCol()       { return col; }
    public Type getType()     { return type; }
    public void setType(Type type) { this.type = type; }

    /**
     * Une case est franchissable si elle n'est pas un mur.
     */
    public boolean isWalkable() {
        return type != Type.WALL;
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}