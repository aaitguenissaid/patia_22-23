package org.example;

public class Main {
    public static void main(String[] args) {

        String domain = "src/main/resources/domain.pddl";

        SokobanDomainParser sokobanDomainParser = new SokobanDomainParser();
        sokobanDomainParser.parse("../sokoban-master/config/"+args[0]+".json");

        SokobanSolutionSolver sokobanSolutionSolver = new SokobanSolutionSolver();
        sokobanSolutionSolver.solve(domain,sokobanDomainParser.title + ".pddl");
        System.out.println("Done");

    }
}