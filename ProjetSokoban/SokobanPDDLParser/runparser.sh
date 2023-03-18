#!/bin/bash

program_argument=$1
path="results/$program_argument/$program_argument"

mkdir -p results/$1
mkdir -p results/PDDLproblems/

# Executer le sokoban parser et rediriger la sortie
mvn -q exec:java -Dexec.mainClass=org.example.Main -Dexec.args="$program_argument" | tee "$path"_output.txt

# Récupérer les lignes contenant les actions push et move 
grep -E "push|move" "$path"_output.txt > "$path"_filtered_output.txt

# éxécuter l'interpréteur de résultat et sauvegarder le chemin
mvn -q exec:java -Dexec.mainClass=org.example.SokobanResultInterpreter -Dexec.args="$program_argument" > "$path"_path.txt

# afficher le chemin trouvé
echo "Un chemin a été trouvé : "
cat "$path"_path.txt

