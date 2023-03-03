package org.example;

public class Main {
    public static void main(String[] args) {
        SokobanDomainParser sokobanDomainParser = new SokobanDomainParser();
        sokobanDomainParser.parse("src/main/resources/test00.json");
        System.out.println("Done");
    }
}