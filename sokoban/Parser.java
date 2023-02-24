package sokoban;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Parser {

    char wall = '#';
    char box = '$';
    char destination = '.';
    char boxOnStoragePlace = '*';
    char guard = '@';
    char guardOnStoragePlace = '+';
    char floor = ' ';

    String domain = "SOKOBAN";


    public Parser(String pathToJSON) {


    }

    public void Parse() {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get("data.json")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject json = new JSONObject(content);
        String title = json.getString("title");
        String testIn = json.getInt("testIn");

    }

}