package sokoban;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Agent {

    public static void main(String[] args) {
        String fileName = System.getProperty("fileName");
        String solution = "";
        String fileToPath = "/home/agsa/Documents/Studies/M1/PATIA/patia_22-23/ProjetSokoban/SokobanPDDLParser/" + fileName + "_path.txt";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileToPath));
            solution = br.readLine();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (char c : solution.toCharArray()) {
            System.out.println(c);
        }
    }
}