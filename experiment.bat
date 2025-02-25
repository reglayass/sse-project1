@echo off

:: Define the path to energibridge.exe
set ENERGIBRIDGE_PATH=..\energibridge.exe

:: Run Java
cd java
javac Experiment.java
"%ENERGIBRIDGE_PATH%" -o ..\output-java.csv --summary java -cp . Experiment
cd ..

:: Run python
cd python
"%ENERGIBRIDGE_PATH%" -o ..\output-python.csv --summary python experiment.py
cd ..

:: Run javascript
cd javascript
"%ENERGIBRIDGE_PATH%" -o ..\output-javascript.csv --summary node experiment.js
cd ..