#!/bin/bash
cd ..
path="resources/logistics_ipc2000_strips-typed"
for file in $path/p*.pddl; do
  filename=$(basename -- "$file")
  java -cp .:classes:lib/pddl4j-4.0.0.jar:lib/sat4j-sat.jar  sat.SAT $path/"domain.pddl" $file > ./results/logistics/$filename"_solution".txt
done

