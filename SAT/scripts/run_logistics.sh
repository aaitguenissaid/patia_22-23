#!/bin/bash
cd ..
path="resources/logistics_ipc2000_strips-typed/"
java -cp .:classes:lib/pddl4j-4.0.0.jar:lib/sat4j-sat.jar  sat.SAT $path"domain.pddl" $path"p12.pddl"
