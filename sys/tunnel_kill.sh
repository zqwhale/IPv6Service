#!/bin/bash


RET=`ps -ef | grep "ssh" | grep "$1:$2:$3 $4@$5 -p $6" | grep -v "grep" | awk '{print $2}'` 
if [ "$RET" = "" ]; then
	echo "Nothing"
else
	echo "Find ssh"
	kill -9 $RET
fi

echo $RET


