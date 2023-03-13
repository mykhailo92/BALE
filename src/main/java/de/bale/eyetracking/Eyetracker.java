package de.bale.eyetracking;

import de.bale.logger.Logger;
import de.bale.messages.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Eyetracker {

    Process process;
    Thread consoleThread;
    ProcessBuilder processBuilder;
    boolean running = false;

    public Eyetracker() {
        Logger.getInstance().post(new InitMessage("CREATING PROCESS BUILDER"));
        processBuilder = new ProcessBuilder("conda", "run", "--no-capture-output", "-n", "bale", "python", "src/main/resources/de/bale/eyetracking/overfit_solution.py", "--image_count=50");
        processBuilder.redirectErrorStream(true);
        consoleThread = new Thread(() -> {
            try {
                startListeningToOutput();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void startRunning() {
        if (!running) {
            running = true;
            try {
                process = processBuilder.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            consoleThread.start();
        } else {
            Logger.getInstance().post(new ErrorMessage("Trying to start Eyetracking but Eyetracking is already running"));
        }
    }

    public void answerToPython(String text) {
        OutputStream os = process.getOutputStream();
        PrintWriter writer = new PrintWriter(os);
        writer.write(text + "\n");
        writer.flush();
        writer.close();
    }

    public void destroyProcess() {
        if (running) {
            running = false;
            consoleThread.interrupt();
            process.descendants().forEach(ProcessHandle::destroy);
            process.destroy();
        } else {
            Logger.getInstance().post(new ErrorMessage("Trying to stop Eyetracking but Eyetracking is not running"));
        }
    }

    public void startListeningToOutput() throws IOException, InterruptedException {
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String line;
        while (process.isAlive()) {
            line = reader.readLine();
            if (line != null) {
                String[] splittedLine = line.split("::");
                switch (splittedLine[0]) {
                    case "EYETRACKING_CALLIBRATION":
                        Logger.getInstance().post(new EyetrackingCallibrationMessage());
                        break;
                    case "EYETRACKING_START":
                        Logger.getInstance().post(new EyetrackingStartMessage());
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
                }
            }
        }
    }
}
