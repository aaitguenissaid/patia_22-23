#!/bin/bash
cd ..
path="resources/depots_ipc2002_strips-automatic/"
java -cp lib/pddl4j-4.0.0.jar fr.uga.pddl4j.planners.statespace.HSP $path"domain.pddl" $path"p01.pddl"

