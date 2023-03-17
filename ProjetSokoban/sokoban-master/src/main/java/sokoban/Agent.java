package sokoban;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Agent {

    public static void main(String[] args) {
        String solution = "";
        String fileToPath = "../SokobanPDDLParser/chemin.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileToPath))) {
            solution= br.readLine();
        } catch(IOException e){
        }

        for (char c : solution.toCharArray()) {
            System.out.println(c);
        }
    }
}