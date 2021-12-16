#!/bin/bash


RET=`ps -ef | grep "ffmpeg" | grep "$1" | grep "$2" | grep -v "grep" | awk '{print $2}'` 
if [ "$RET" = "" ]; then
	echo "Nothing"
else
	echo "Find " + $RET
	kill -9 $RET
fi

echo $RET


