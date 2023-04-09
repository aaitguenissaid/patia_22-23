#!/bin/bash

cd SokxobanPDDLParser || exit
for i in {1..30}
do
   ./runparser.sh  test$i
done

