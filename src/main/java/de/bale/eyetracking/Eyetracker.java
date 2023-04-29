package de.bale.eyetracking;

import de.bale.logger.Logger;
import de.bale.messages.ErrorMessage;
import de.bale.messages.eyetracking.EyetrackingFitDoneMessage;
import de.bale.messages.InitMessage;
import de.bale.messages.PythonAnswerMessage;
import de.bale.messages.eyetracking.EyeTrackingDataMessage;
import de.bale.messages.eyetracking.EyetrackingInformationMessage;
import de.bale.messages.eyetracking.EyetrackingRunningMessage;
import de.bale.messages.eyetracking.EyetrackingStoppingMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Eyetracker {

    Process process;
    Thread consoleThread;
    ProcessBuilder processBuilder;
    boolean running = false;
    private EyetrackerPythonWriter eyetrackerPythonWriter;

    public Eyetracker() {
        Logger.getInstance().post(new InitMessage("CREATING PROCESS BUILDER"));
        long pid = ProcessHandle.current().pid(); // Get Process ID to Monitor in Python
        processBuilder = new ProcessBuilder("conda", "run", "--no-capture-output", "-n", "bale", "python",
                "python/overfit_solution.py");
        processBuilder.redirectErrorStream(true);
        consoleThread = new Thread(() -> {
            try {
                int exitValue = startListeningToOutput();
                if (exitValue != 0) {
                    Logger.getInstance().post(new ErrorMessage("Eyetracker Error!"));
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }


    public void startRunning() {
        if (!running) {
            try {
                process = processBuilder.start();
                eyetrackerPythonWriter = new EyetrackerPythonWriter(process);
                Logger.getInstance().register(eyetrackerPythonWriter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            consoleThread.start();
        } else {
            Logger.getInstance().post(new ErrorMessage("Trying to start Eyetracking but Eyetracking is already running"));
        }
    }

    public void destroyProcess() {
        consoleThread.interrupt();
        process.descendants().forEach(ProcessHandle::destroy);
        process.destroy();
    }

    public int startListeningToOutput() throws IOException, InterruptedException {
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String line;
        while (process.isAlive()) {
            line = reader.readLine();
            if (line != null) {
                String[] splittedLine = line.split("::");
                switch (splittedLine[0]) {
                    case "EYETRACKING_INFO":
                        if (splittedLine.length >= 2) {
                            Logger.getInstance().post(new EyetrackingInformationMessage(splittedLine[1]));
                        }
                        break;
                    case "EYETRACKING_RUNNING":
                        running = true;
                        Logger.getInstance().post(new EyetrackingRunningMessage("Eyetracker is running"));
                        break;
                    case "EYETRACKING_STOP":
                        Logger.getInstance().post(new EyetrackingStoppingMessage("Eyetracker is stopping"));
                        break;
                    case "EYETRACKING_FIT_START":
                    case "PLAIN":
                        if (splittedLine.length >= 2) {
                            Logger.getInstance().post(new PythonAnswerMessage(splittedLine[1]));
                        }
                        break;
                    case "EYETRACKING":
                        if (splittedLine.length >= 2) {
                            String[] coordinates = splittedLine[1].split(" ");
                            int xCoordinate = (int) Float.parseFloat(coordinates[0]);
                            int yCoordinate = (int) Float.parseFloat(coordinates[1]);
                            Logger.getInstance().post(new EyeTrackingDataMessage(xCoordinate, yCoordinate));
                        }
                        break;
                    case "EYETRACKING_FIT_STOP":
                        Logger.getInstance().post(new EyetrackingFitDoneMessage("Eyetracking is now ready"));
                }
            }
        }
        return process.exitValue();
    }

    public boolean isRunning() {
        return running;
    }
}
