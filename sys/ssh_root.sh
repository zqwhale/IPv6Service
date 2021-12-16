#!/bin/bash

echo "Please input open/close:"
read -n1 -p "Do you want to permit root ssh access [Y/N]?" answer


if  [ "$answer"X = "Y"X ] || [ "$answer"X = "y"X ]; then
	echo "SSH root permition is open."
	sed -i "s/PermitRootLogin no/PermitRootLogin yes/g" /etc/ssh/sshd_config;
else
	echo "SSH root permition is close."
	sed -i "s/PermitRootLogin yes/PermitRootLogin no/g" /etc/ssh/sshd_config;
fi


service sshd restart

exit 0