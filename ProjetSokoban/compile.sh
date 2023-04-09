#!/bin/bash

cd SokobanPDDLParser || exit

echo -e "\n\e[1;32m Cleaning SokobanPDDLParser \e[0m\n"
mvn -q clean

echo -e "\n\e[1;32m Compiling SokobanPDDLParser \e[0m\n"
mvn package



cd ../sokoban-master || exit
echo -e "\n\e[1;32m Cleaning sokoban-master \e[0m\n"
mvn -q clean

echo -e "\n\e[1;32m Compiling sokoban-master \e[0m\n"
mvn package


