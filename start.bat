@echo off
start javaw -jar "%cd%\bale.jar" "%cd%\Lerneinheit\example.html"
echo %errorlevel%
exit