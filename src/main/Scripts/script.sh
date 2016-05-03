#!/bin/bash 
inputFile=/home/gman/Desktop/Consumer_Complaints.csv
while read line;
	do	
		#randRange=$( shuf -i 1-9 )
		#delay=${randRange:0:1}
		#sleep $delay
		echo $line | netcat localhost 6666
		echo $line
	done <$inputFile
echo "***************DONE**********************"
