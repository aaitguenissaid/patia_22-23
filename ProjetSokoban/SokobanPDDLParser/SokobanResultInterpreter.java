
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SokobanResultInterpreter {
    public static void main(String[] args) throws IOException {
        String filename = "filtered_output.txt";
        String[] input = parseInputFile(filename);
        String[] actions = input[0].split("\n");
        String chemin = getPath(actions);
        System.out.println(chemin);
    }

    private static String[] parseInputFile(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String actions = "";
            String line;
            while ((line = br.readLine()) != null) {
                actions += line + "\n";
            }
            return new String[]{actions};
        }
    }

    private static String getDirection(String pos1, String pos2) {
        int y1 = Integer.parseInt(String.valueOf(pos1.charAt(1)));
        int x1 = Integer.parseInt(String.valueOf(pos1.charAt(2)));
        int y2 = Integer.parseInt(String.valueOf(pos2.charAt(1)));
        int x2 = Integer.parseInt(String.valueOf(pos2.charAt(2)));

        //différence en x et en y
        int dx = x2 - x1;
        int dy = y2 - y1;

        if (dx == 1) {
            return "D";
        } else if (dx == -1) {
            return "U";
        } else if (dy == 1) {
            return "R";
        } else if (dy == -1) {
            return "L";
        }

        return null;
    }

    public static String[] cleanInputString(String moveString) {
        moveString = moveString.substring(3);
        String[] parts = moveString.split("\\s+");
        parts[parts.length - 1] = parts[parts.length - 1].substring(0, parts[parts.length - 1].length() - 1);
        return parts;
    }

    private static String getPath(String[] actions) {
        StringBuilder chemin = new StringBuilder();
        for (String action : actions) {
            String[] parts = cleanInputString(action);
	        //System.out.println(parts[2]);
	
            //si action move
            if ( parts[1].replaceAll("[()\\s]", "").equals("move")) {
                String fromPos = parts[3].replaceAll("[()\\s]", "");
                String toPos = parts[4].replaceAll("[()\\s]", "");
                String pas = getDirection(fromPos, toPos);
                chemin.append(pas);
            }

            //si action move
            if ( parts[2].replaceAll("[()\\s]", "").equals("move")) {
                String fromPos = parts[4].replaceAll("[()\\s]", "");
                String toPos = parts[5].replaceAll("[()\\s]", "");
                String pas = getDirection(fromPos, toPos);
                chemin.append(pas);
            }

            //si action push
            else if (parts[1].replaceAll("[()\\s]", "").equals("push")){
                String fromPos = parts[5].replaceAll("[()\\s]", "");
                String toPos = parts[6].replaceAll("[()\\s]", "");
                String pas = getDirection(fromPos, toPos);
                chemin.append(pas);
            }

            //si action push
            else if ( parts[0].replaceAll("[()\\s]", "").equals("push")) {
                String fromPos = parts[4].replaceAll("[()\\s]", "");
                String toPos = parts[5].replaceAll("[()\\s]", "");
                String pas = getDirection(fromPos, toPos);
                chemin.append(pas);
            }



        }
        return chemin.toString();
    }

}


