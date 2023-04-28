#!/bin/bash
cd ..
path="resources/logistics_ipc2000_strips-typed"
folder="logistics_ipc2000_strips-typed"
mkdir -p plans/$folder
mkdir -p results/$folder

for file in $path/p*.pddl; do
  filename=$(basename -- "$file" .pddl)
  echo "Processing $file"
  ./../VAL/build/linux64/Release/bin/Validate  $path/"domain.pddl" $file plans/"$folder"/"$filename"_filtered_output.txt

done

