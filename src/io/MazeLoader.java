package io;

import model.Maze;
import java.io.*;
import java.util.*;

/**
 * Charge un labyrinthe depuis un fichier texte.
 */
public class MazeLoader {

    public static Maze load(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty())
                    lines.add(line);
            }
        }

        if (lines.isEmpty())
            throw new IOException("Le fichier labyrinthe est vide : " + filePath);

        return new Maze(lines.toArray(new String[0]));
    }
}