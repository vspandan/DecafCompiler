#!/bin/bash
llvmir=0
option=-llvm-ir
output1=decaf.out
output2=decaf.s

cd bin

if [ -s $output2 ] 
then
	rm $output2
fi

if [ "$#" -ne 0 ]
then
	if [ $1 = $option ]
   	then
		llvmir=1
		if [ "$#" -eq 2  ]
		then
			java -cp ".:../jllvm.jar" Parser ../"$2"
		else
			java -cp ".:../jllvm.jar" Parser
		fi
	else
		java -cp ".:../jllvm.jar" Parser ../"$1"
	fi
else
	java -cp ".:../jllvm.jar" Parser
fi

if [ -s $output1 ]
then
	llvm-dis -f decaf.out -o decaf.s

	if [ $llvmir -eq  1 ];
	then
		cat decaf.s
		echo "Press any key to continue ... " 
		read tmp
	fi
	lli decaf.s
fi
cd ..
