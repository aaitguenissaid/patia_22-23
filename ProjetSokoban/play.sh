#!/bin/bash

cd SokobanPDDLParser
./runparser.sh $1

cd ../sokoban-master
./runsokoban.sh $1


