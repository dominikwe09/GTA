@echo off
echo Kompiliere...
if not exist out mkdir out
javac -cp "lib\GLOOP.jar;lib\gluegen-rt.jar;lib\jogl-all.jar" -d out src\*.java
if errorlevel 1 (echo Fehler beim Kompilieren & pause & exit /b 1)

echo Erstelle JAR...
jar -cf GTA.jar -C out .
echo Fertig! Starte mit start.bat
pause
