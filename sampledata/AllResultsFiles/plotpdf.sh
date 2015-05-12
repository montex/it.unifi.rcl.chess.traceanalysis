#!/bin/bash
gnuplot -e "filename='$1';n=$2" distribution.plt 

