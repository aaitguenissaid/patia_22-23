#!/bin/bash
cd ..
path="resources/logistics_ipc2000_strips-typed/"
java -cp lib/pddl4j-4.0.0.jar fr.uga.pddl4j.planners.statespace.HSP $path"domain.pddl" $path"p01.pddl"

