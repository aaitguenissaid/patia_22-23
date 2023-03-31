#!/bin/bash
cd ..
java -cp .:classes:lib/pddl4j-4.0.0.jar:lib/sat4j-sat.jar  sat.SAT $1 $2
