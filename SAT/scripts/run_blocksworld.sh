#!/bin/bash
cd ..
path="resources/blocksworld_ipc2000_strips-typed/"
java -cp .:classes:lib/pddl4j-4.0.0.jar:lib/sat4j-sat.jar  sat.SAT $path"domain.pddl" $path"p001.pddl"
