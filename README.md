# BALE

## How to Build jar (Intellij)
 
```
Open "Project Structure" 
Under Menu Point "Project Settings" select "Artifacts"
In the Left Column, select "+"
Select JAR and then select "From modules with dependencies..."
Select Main class (de.bale.Main)
Under "Jar Files From Libraries" select "Extract to the target JAR"
In the Right Column that opens select the <PROJECTNAME> Folder, which contains Maven Libraries
Drag & Drop the Maven Libraries into the bale.jar in the left column
Click OK
In the Intellij Menu select "Build->Build Artifacts..." and choose Build if prompted
The JAR will be generated in the "out" Folder
```

## How to prepare Python Environment
```
This Projekt is intended to be installed using Anaconda.
If you conda is added to your System Path, run this in console:
    conda env create -f bale.yml
This will automatically create the conda virtual environment "bale" which is used for the Eyetracking   
```

## Compiled Folder Structure
In Order for the Programm to work correctly you need the following Folder Structure:
```
-- <any_name>
 |- bale
 |- python
 |- bale.jar
 |- run_bale.jar
```

## Local MySQL Server

In Order for this Programm to work with its current settings you need to have a local
MySQL Server such as XAMPP or MAMP with the following settings:
```
Database_Name: "bale"
Database_Host: "localhost"
Database_Port: "8889"
Database_User: "root"
Database_Password: "root"
```

## Using the Programm

In Order to start the Program, either start run_bale.bat or use
    java -jar "bale.jar"
to start the program. 
On the first Screen, next to the open button, a spinning wheel is shown. This wheel indicates, that the 
Eyetracker is currently not started. The first start-up of the Eyetracker may take up several Minutes, as 
Libraries are needed to be loaded into the memory.
Together with this code you should find two folder with example Learningunits. The "Lerneinheit" one 
contains a Learning Unit which does not utilize Eyetracking. The second one "EyeTrackingTest" uses 
Eyetracking to generate Data and Feedback. Due to the Eyetracking being not accurate enough, only the 
first chapter of the Learningunit possesses the required Tagadditions as well as Helptexts.
To add these Learningunits to your List click on the "New" Button in the starting screen.
```
