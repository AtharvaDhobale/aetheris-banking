@echo off
REM Setup script for installing Java 17 and Maven on Windows

echo ========================================
echo Accounting System - Setup Script
echo ========================================

REM Check if Java is installed
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Java not found. Installing Java 17 OpenJDK...
    REM Download Java 17 (you need to have curl or PowerShell)
    powershell -Command "Invoke-WebRequest -Uri 'https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.8%2B7/OpenJDK17U-jdk_x64_windows_hotspot_17.0.8_7.msi' -OutFile 'java-installer.msi'" 2>nul
    if exist java-installer.msi (
        echo Running Java installer...
        msiexec /i java-installer.msi /quiet /norestart
        del java-installer.msi
        echo Java installed successfully!
    ) else (
        echo Please install Java 17 manually from: https://adoptium.net/
        pause
        exit /b 1
    )
) else (
    java -version
    echo Java is already installed!
)

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Maven not found. Installing Maven...
    REM Download Maven
    powershell -Command "Invoke-WebRequest -Uri 'https://dlcdn.apache.org/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.zip' -OutFile 'maven.zip'" 2>nul
    if exist maven.zip (
        echo Extracting Maven...
        powershell -Command "Expand-Archive -Path 'maven.zip' -DestinationPath 'C:\'"
        del maven.zip
        echo Setting Maven path...
        setx PATH "%PATH%;C:\apache-maven-3.9.5\bin"
        echo Maven installed successfully!
        echo Please restart your terminal or run: set PATH=%PATH%;C:\apache-maven-3.9.5\bin
    ) else (
        echo Please install Maven manually from: https://maven.apache.org/download.cgi
        pause
        exit /b 1
    )
) else (
    mvn -version
    echo Maven is already installed!
)

echo ========================================
echo Installation complete!
echo ========================================
echo Next steps:
echo 1. Close this terminal
echo 2. Open a new terminal
echo 3. Run: cd "c:\Users\athar\OneDrive\Desktop\AM"
echo 4. Run: mvn clean package -DskipTests
echo 5. Run: mvn spring-boot:run
echo ========================================
pause
