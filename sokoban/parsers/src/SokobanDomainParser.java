
package org.example;

/*
INCLURE la dépendance MAVEN pour "pom.xml" :

<dependency>
	<groupId>org.json</groupId>
        <artifactId>json</artifactId>
        <version>20220924</version>
</dependency>
*/

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class SokobanDomainParser {

    char wall = '#';
    char box = '$';
    char destination = '.';
    char boxOnStoragePlace = '*';
    char guard = '@';
    char guardOnStoragePlace = '+';
    char floor = ' ';

    // Domaine
    String domain = "SOKOBAN";

    // Nom du problème
    String title;

    // Contenu du niveau
    String testIn;
    char[][] level;

    public SokobanDomainParser() {

    }

    public void parse(String pathToJSON) {
        try {
            String content = Files.readString(Paths.get(pathToJSON));

            JSONObject jsonObject = new JSONObject(content);
            this.title = jsonObject.getJSONObject("title").getString("2");
            this.title = this.title.replace(" ","");
            this.testIn = jsonObject.getString("testIn");

            // Niveau
            String[] lignes = this.testIn.split("\n");
            System.out.println(Arrays.toString(lignes));
            int nbRows = lignes.length;
            int nbCols = lignes[0].length();
            
            this.level = new char[nbRows][nbCols];

            // Objets
            String _positions = "";
            String _guard = "";
            String _boxes = "";

            // Etat initial
            String _initialState = "";

            // Objectif
            String _finalState = "";
            ArrayList<String> toBindBox = new ArrayList<>(); // boites à lier à une destination
            ArrayList<String> toBindDest = new ArrayList<>(); // destinations à lier à une boite

            for (int i = 0; i < nbRows; i++) {
                for (int j = 0; j < nbCols; j++) {
                    this.level[i][j] = lignes[i].charAt(j);

                    if (this.level[i][j] == box || this.level[i][j] == boxOnStoragePlace) {
                        _boxes += "B" + i + j + " ";
                        _positions += "P" + i + j + " ";
                        _initialState += "(boxOn " + "P" + i + j + " " + "B" + i + j + ")\n";
                        _initialState = checkForNeighbours(_initialState, i, j, nbRows, nbCols);

                        if (this.level[i][j] == box) {
                            toBindBox.add("B" + i + j);
                        }

                        if (this.level[i][j] == boxOnStoragePlace) {
                            _finalState += "(boxOn " + "P" + i + j + " " + "B" + i + j + ")\n";
                        }

                    }

                    if (this.level[i][j] == floor || this.level[i][j] == destination) {
                        _positions += "P" + i + j + " ";
                        _initialState += "(isFree " + "P" + i + j + ")\n";
                        _initialState = checkForNeighbours(_initialState, i, j, nbRows, nbCols);

                        if (this.level[i][j] == destination) {
                            toBindDest.add("P" + i + j);
                        }

                    } else if (this.level[i][j] == guard || this.level[i][j] == guardOnStoragePlace) {
                        _guard += "G" + i + j + " ";
                        _positions += "P" + i + j + " ";
                        _initialState += "(guardOn " + "P" + i + j + " " + "G" + i + j + ")\n";
                        _initialState = checkForNeighbours(_initialState, i, j, nbRows, nbCols);

                        if (this.level[i][j] == guardOnStoragePlace) {
                            toBindDest.add("P" + i + j);
                        }
                    }

                    if (!toBindBox.isEmpty() && !toBindDest.isEmpty()) {
                        _finalState += "(boxOn " + toBindDest.remove(toBindDest.size() - 1) + " " + toBindBox.remove(toBindBox.size() - 1) + ")\n";
                    }
                }

                System.out.println(this.level[i]);
            }

            _positions += " - position";
            _guard += " - guard ";
            _boxes += " - box";

            // Création du fichier
            File file = new File(this.title + ".pddl");
            FileWriter writer = new FileWriter(file);
            writer.write("(define (problem " + this.title + ")\n");
            writer.write("(:domain " + this.domain + ")\n");
            writer.write("(:objects " + _positions +" \n "+_guard +" \n "+_boxes + ")\n");
            writer.write("(:init " + _initialState + ")\n");
            writer.write("(:goal (and " + _finalState + "))\n");
            writer.write(")");
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // vérifier l'existence de voisins
    public String checkForNeighbours(String _initialState, int i, int j, int nbCols, int nbRows) {

        if (i > 0
                && (this.level[i - 1][j] == floor
                || this.level[i - 1][j] == destination
                || this.level[i - 1][j] == box
                || this.level[i - 1][j] == boxOnStoragePlace
                || this.level[i - 1][j] == guard
                || this.level[i - 1][j] == guardOnStoragePlace
        )) {
            _initialState += "(isNeighbor" + " P" + i + j + " P" + (i - 1) + j + ")\n";
            _initialState += "(isNeighbor" + " P" + (i - 1) + j + " P" + i + j + ")\n";
        }
        if (i < nbCols - 1
                && (this.level[i + 1][j] == floor
                || this.level[i + 1][j] == destination
                || this.level[i + 1][j] == box
                || this.level[i + 1][j] == boxOnStoragePlace
                || this.level[i + 1][j] == guard
                || this.level[i + 1][j] == guardOnStoragePlace
        )) {
            _initialState += "(isNeighbor" + " P" + i + j + " P" + (i + 1) + j + ")\n";
            _initialState += "(isNeighbor" + " P" + (i + 1) + j + " P" + i + j + ")\n";
        }
        if (j > 0
                && (this.level[i][j - 1] == floor
                || this.level[i][j - 1] == destination
                || this.level[i][j - 1] == box
                || this.level[i][j - 1] == boxOnStoragePlace
                || this.level[i][j - 1] == guard
                || this.level[i][j - 1] == guardOnStoragePlace
        )) {
            _initialState += "(isNeighbor" + " P" + i + j + " P" + i + (j - 1) + ")\n";
            _initialState += "(isNeighbor" + " P" + i + (j - 1) + " P" + i + j + ")\n";
        }
        if (j < nbRows - 1
                && (this.level[i][j + 1] == floor
                || this.level[i][j + 1] == destination
                || this.level[i][j + 1] == box
                || this.level[i][j + 1] == boxOnStoragePlace
                || this.level[i][j + 1] == guard
                || this.level[i][j + 1] == guardOnStoragePlace
        )) {
            _initialState += "(isNeighbor" + " P" + i + j + " P" + i + (j + 1) + ")\n";
            _initialState += "(isNeighbor" + " P" + i + (j + 1) + " P" + i + j + ")\n";
        }

        return _initialState;
    }

}
