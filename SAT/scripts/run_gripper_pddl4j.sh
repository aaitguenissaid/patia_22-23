#!/bin/bash
cd ..
path="resources/gripper_ipc1998_strips/"
java -cp lib/pddl4j-4.0.0.jar fr.uga.pddl4j.planners.statespace.HSP $path"domain.pddl" $path"p01.pddl"

