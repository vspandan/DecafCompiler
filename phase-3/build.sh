dirr=bin
if [ ! -d $dirr ]; then
	mkdir bin
fi
javac -cp jllvm.jar -d bin/ src/*.java
