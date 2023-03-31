#!/bin/bash
cd ..
javac -d classes -cp lib/pddl4j-4.0.0.jar src/sat/Encoder.java

javac -d classes -cp classes:lib/pddl4j-4.0.0.jar:lib/sat4j-sat.jar src/sat/SAT.java