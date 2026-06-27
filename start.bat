@echo off
java -Djava.library.path=lib\natives\windows-amd64 -cp "GTA.jar;lib\GLOOP.jar;lib\gluegen-rt.jar;lib\jogl-all.jar" GTA
pause
