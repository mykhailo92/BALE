package de.bale.eyetracking;

import de.bale.logger.Logger;
import de.bale.messages.PythonAnswerMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Eyetracker {

    boolean running = false;
    Process process;
    Thread consoleThread;
    ProcessBuilder processBuilder;

    public Eyetracker() {
        processBuilder = new ProcessBuilder("python", "src/main/resources/de/bale/eyetracking/EyeTracker.py");
        processBuilder.redirectErrorStream(true);
        consoleThread = new Thread(() -> {
            try {
                startListeningToOutput();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void startEyetracking() {
        running = true;
        consoleThread.start();
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopRunning() {
        running = false;
        consoleThread.interrupt();
        process.destroy();
    }

    public void startListeningToOutput() throws IOException, InterruptedException {
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String line;
        while (process.isAlive()) {
            line = reader.readLine();
            if (line != null) {
                Logger.getInstance().post(new PythonAnswerMessage(line));
            }
        }
    }
}
