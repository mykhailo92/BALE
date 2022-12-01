@echo off
javaw -jar "%cd%\out\artifacts\bale_jar\bale.jar" "%cd%\Lerneinheit\example.html"

echo %errorlevel%