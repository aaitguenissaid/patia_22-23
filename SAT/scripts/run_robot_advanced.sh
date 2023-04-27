#!/bin/bash
cd ..
path="resources/robot_advanced/"
java -cp .:classes:lib/pddl4j-4.0.0.jar:lib/sat4j-sat.jar  sat.SAT $path"domain.pddl" $path"p05.pddl"
