@echo off
echo "">> "%cd%\bale.jar
xcopy /s /q /y /f "%cd%\out\artifacts\bale_jar\bale.jar" "%cd%\bale.jar"
javaw -jar "%cd%\bale.jar"

echo %errorlevel%