#!/bin/bash
cd ..
folders=("blocksworld_ipc2000_strips-typed" "depots_ipc2002_strips-automatic" "gripper_ipc1998_strips" "logistics_ipc2000_strips-typed")

for folder in ${folders[@]}; do
  path="resources/"$folder
  echo -e "\nProcessing $folder"

  mkdir -p plans/$folder
  mkdir -p results
  mkdir -p output/$folder
  mkdir -p validation/$folder
  results="results/"$folder"_results.csv"
  echo "Makespan,Total_time" > $results
  for file in $path/p*.pddl; do
    filename=$(basename -- "$file" .pddl)
    echo "Processing $file"
    output_file="./output/$folder/"$filename"_output.txt"
    plan_file="plans/"$folder"/"$filename"_filtered_output.txt"

    java -cp .:classes:lib/pddl4j-4.0.0.jar:lib/sat4j-sat.jar  sat.SAT $path/"domain.pddl" $file > $output_file

    # Récupérer les lignes contenant les actions du plan
    grep -E '^[0-9]+:'  $output_file > $plan_file

    makespan=$(grep -c ^ "$plan_file")
    total_time=$(grep "total time" "$output_file" | grep -oP '\b\d+\.\d+\b')
    echo "$makespan,$total_time" >> $results
    echo "Makespan: $makespan"
    echo "Total time: $total_time"

    ./../VAL/build/linux64/Release/bin/Validate $path/"domain.pddl" $file plans/"$folder"/"$filename"_filtered_output.txt > ./validation/$folder/"$filename"_validation.txt

    plan_valid=$(grep -E "Plan valid|Plan invalid|Plan failed to execute" ./validation/$folder/"$filename"_validation.txt)
    echo "$plan_valid!"
    echo ""
  done
done

