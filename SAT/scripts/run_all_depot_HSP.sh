#!/bin/bash
cd ..
path="resources/depots_ipc2002_strips-automatic"
folder="depots_ipc2002_strips-automatic_HSP"
mkdir -p plans/$folder
mkdir -p results
mkdir -p output/$folder
mkdir -p validation/$folder
echo "Makespan,Total_time" > "results/"$folder"_results.txt"
for file in $path/p*.pddl; do
  filename=$(basename -- "$file" .pddl)
  echo "Processing $file"
  output_file="./output/$folder/"$filename"_output.txt"

  java -cp .:classes:lib/pddl4j-4.0.0.jar:lib/sat4j-sat.jar  sat.SAT $path/"domain.pddl" $file > $output_file

  # Récupérer les lignes contenant les actions push et move
  grep -E '^[0-9]+:'  $output_file > plans/"$folder"/"$filename"_filtered_output.txt

  makespan=$(grep -oP 'makespan : \K\d+\.\d+' "$output_file" | head -1)
  total_time=$(grep "total time" "$output_file" | grep -oP '\b\d+\.\d+\b')
  echo "$makespan,$total_time" >> "results/"$folder"_results.txt"

  ./../VAL/build/linux64/Release/bin/Validate $path/"domain.pddl" $file plans/"$folder"/"$filename"_filtered_output.txt > ./validation/$folder/"$filename"_validation.txt

  echo ""
done

