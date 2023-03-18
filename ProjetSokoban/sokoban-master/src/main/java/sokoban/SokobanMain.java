package sokoban;

import com.codingame.gameengine.runner.SoloGameRunner;

public class SokobanMain {
    public static void main(String[] args) {
        String fileName = args[0];
        System.setProperty("fileName", fileName);
        SoloGameRunner gameRunner = new SoloGameRunner();
        gameRunner.setAgent(Agent.class);
        gameRunner.setTestCase(fileName + ".json");
        gameRunner.start();

    }
}
