#!/bin/bash
cd ..

mkdir -p results

javac -d classes src/sat/ActionFormula.java
javac -d classes -cp classes:lib/pddl4j-4.0.0.jar:lib/sat4j-sat.jar src/sat/Encoder.java

javac -d classes -cp classes:lib/pddl4j-4.0.0.jar:lib/sat4j-sat.jar src/sat/SAT.java