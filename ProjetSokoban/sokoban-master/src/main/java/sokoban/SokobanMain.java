package sokoban;

import com.codingame.gameengine.runner.SoloGameRunner;

public class SokobanMain {
    public static void main(String[] args) {
        SoloGameRunner gameRunner = new SoloGameRunner();
        gameRunner.setAgent(Agent.class);
        gameRunner.setTestCase(args[0] + ".json");
        gameRunner.start();
    }
}
