#!/bin/bash
cd ..
path="resources/gripper_ipc1998_strips/"
java -cp .:classes:lib/pddl4j-4.0.0.jar:lib/sat4j-sat.jar  sat.SAT $path"domain.pddl" $path"p01.pddl"
