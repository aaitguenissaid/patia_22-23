#!/bin/bash
cd ..
path="exercice5_Pursuit-Evasion/"
java -cp SAT/lib/pddl4j-4.0.0.jar fr.uga.pddl4j.planners.statespace.HSP $path"domain.pddl" $path"p01.pddl"

