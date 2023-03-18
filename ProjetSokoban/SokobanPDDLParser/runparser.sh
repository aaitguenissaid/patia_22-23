#!/bin/bash

program_argument=$1

# Executer le sokoban parser et rediriger la sortie
mvn -q exec:java -Dexec.mainClass=org.example.Main -Dexec.args="$program_argument" | tee "$program_argument"_output.txt

# Récupérer les lignes contenant les actions push et move 
grep -E "push|move" "$program_argument"_output.txt > "$program_argument"_filtered_output.txt

# éxécuter l'interpréteur de résultat et sauvegarder le chemin
mvn -q exec:java -Dexec.mainClass=org.example.SokobanResultInterpreter -Dexec.args="$program_argument" > "$program_argument"_path.txt

# afficher le chemin trouvé
echo "Un chemin a été trouvé : "
cat "$program_argument"_path.txt

