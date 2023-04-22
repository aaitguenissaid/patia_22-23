#!/bin/bash
cd ..
path="resources/depots_ipc2002_strips-automatic/"
java -cp .:classes:lib/pddl4j-4.0.0.jar:lib/sat4j-sat.jar  sat.SAT $path"domain.pddl" $path"p01.pddl"
