
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
            int longestLength = 0;

            for (String ligne : lignes) {
                if (ligne.length() > longestLength) {
                    longestLength = ligne.length();
                }
            }

            System.out.println(Arrays.toString(lignes));
            int nbRows = lignes.length;
            this.level = new char[nbRows][];

            for (int i=0; i<lignes.length; i++) {
                level[i] = new char[lignes[i].length()];
            }

            

            // Objets
            String _positions = "";
            String _guard = "";
            String _boxes = "";

            // Etat initial
            String _initialState = "";

            // Objectif
            String _finalState = "";

            for (int i = 0; i < nbRows; i++) {
                int nbCols = lignes[i].length();
                for (int j = 0; j < nbCols; j++) {
                    this.level[i][j] = lignes[i].charAt(j);

                    if (this.level[i][j] == box || this.level[i][j] == boxOnStoragePlace) {
                        _boxes += "B" + i + j + " ";
                        _positions += "P" + i + j + " ";
                        _initialState += "(boxOn " + "P" + i + j + ")\n";
                        _initialState = checkForNeighbours(_initialState, i, j, nbRows, nbCols);

                        if (this.level[i][j] == boxOnStoragePlace) {
                            _finalState += "(boxOn " + "P" + i + j + ")\n";
                        }
                    }

                    if (this.level[i][j] == floor || this.level[i][j] == destination) {
                        _positions += "P" + i + j + " ";
                        _initialState += "(isFree " + "P" + i + j + ")\n";
                        _initialState = checkForNeighbours(_initialState, i, j, nbRows, nbCols);

                        if (this.level[i][j] == destination) {
                            _finalState += "(boxOn " + "P" + i + j + ")\n";   
                        }

                    } else if (this.level[i][j] == guard || this.level[i][j] == guardOnStoragePlace) {
                        _guard += "g" + i + j + " ";
                        _positions += "P" + i + j + " ";
                        _initialState += "(guardOn " + "P" + i + j + " " + "G" + i + j + ")\n";
                        _initialState = checkForNeighbours(_initialState, i, j, nbRows, nbCols);

                        if (this.level[i][j] == guardOnStoragePlace) {
                            _finalState += "(boxOn " + "P" + i + j + ")\n";
                        }
                    }
                }

                System.out.println(this.level[i]);
            }

            _positions += " - position";
            _guard += " - guard ";
            _boxes += " - box";

            String _directions = " left right up down - direction ";

            // Création du fichier
            File file = new File(this.title + ".pddl");
            FileWriter writer = new FileWriter(file);
            writer.write("(define (problem " + this.title + ")\n");
            writer.write("(:domain " + this.domain + ")\n");
            writer.write("(:objects " + _positions +" \n "+_guard +" \n "+_boxes + " \n "+ _directions + ")\n");
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
        int up = i - 1;
        int down = i + 1;
        int left = j - 1; 
        int right = j + 1;

        if (i > 0
                && (this.level[up][j] == floor
                || this.level[up][j] == destination
                || this.level[up][j] == box
                || this.level[up][j] == boxOnStoragePlace
                || this.level[up][j] == guard
                || this.level[up][j] == guardOnStoragePlace
        )) {
            _initialState += "(isNeighbor" + " P" + i +""+ j + " P" + up +""+ j + " up )\n";
            _initialState += "(isNeighbor" + " P" + up +""+ j + " P" + i +""+ j + " down )\n";
        }
         if (i < nbCols - 1
                && (this.level[down][j] == floor
                || this.level[down][j] == destination
                || this.level[down][j] == box
                || this.level[down][j] == boxOnStoragePlace
                || this.level[down][j] == guard
                || this.level[down][j] == guardOnStoragePlace
        )) {
            _initialState += "(isNeighbor" + " P" + i +""+ j + " P" + down +""+ j + " down )\n";
            _initialState += "(isNeighbor" + " P" + down +""+ j + " P" + i +""+ j + " up )\n";
        }
        if (j > 0
                && (this.level[i][left] == floor
                || this.level[i][left] == destination
                || this.level[i][left] == box
                || this.level[i][left] == boxOnStoragePlace
                || this.level[i][left] == guard
                || this.level[i][left] == guardOnStoragePlace
        )) {
            _initialState += "(isNeighbor" + " P" + i +""+ j + " P" + i +""+ left + " left )\n";
            _initialState += "(isNeighbor" + " P" + i +""+ left + " P" + i +""+ j + " right )\n";
        
        }
        if (j < nbRows - 1
                && (this.level[i][right] == floor
                || this.level[i][right] == destination
                || this.level[i][right] == box
                || this.level[i][right] == boxOnStoragePlace
                || this.level[i][right] == guard
                || this.level[i][right] == guardOnStoragePlace
        )) {
            _initialState += "(isNeighbor" + " P" + i +""+ j + " P" + i +""+ right + " right )\n";
            _initialState += "(isNeighbor" + " P" + i +""+ right + " P" + i +""+ j + " left)\n";
        }
        return _initialState;
    }

}
