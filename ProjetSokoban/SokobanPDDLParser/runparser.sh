#!/bin/bash

program_argument=$1

# Executer le sokoban parser et rediriger la sortie
mvn exec:java -Dexec.args="$program_argument" | tee output.txt

# Récupérer les lignes contenant les actions push et move 
grep -E "push|move" output.txt > filtered_output.txt

# éxécuter l'interpréteur de résultat et sauvegarder le chemin
javac SokobanResultInterpreter.java
java SokobanResultInterpreter > chemin.txt

# afficher le chemin trouvé
echo "Un chemin a été trouvé : "
cat chemin.txt

