#!/bin/bash
mkdir -p out
javac -d out src/com/warehouse/*.java
java -cp out com.warehouse.Main
