package de.bale.eyetracking;

import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

import java.io.IOException;

public class Eyetracker {

    Process process;
    Thread consoleThread;
    ProcessBuilder processBuilder;
    boolean running = false;

    public Eyetracker(WebEngine webEngine) {
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) ->
        {
            JSObject window = (JSObject) webEngine.executeScript("window");
            webEngine.executeScript("console.log = function(message)\n" +
                    "{\n" +
                    "    window.javaBridge.log(message);\n" +
                    "};");
        });
//        //this.getClass().getResource("prediction.py").getFile()
//        Logger.getInstance().post(new InitMessage("CREATING PROCESS BUILDER"));
//        processBuilder = new ProcessBuilder("conda", "run", "--no-capture-output", "-n", "bale", "python", "src/main/resources/de/bale/eyetracking/prediction.py");
//        processBuilder.redirectErrorStream(true);
//        consoleThread = new Thread(() -> {
//            try {
//                startListeningToOutput();
//            } catch (IOException | InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
    }

    public void startRunning() {
//        if (!running) {
//            running = true;
//            try {
//                process = processBuilder.start();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            consoleThread.start();
//        } else {
//            Logger.getInstance().post(new ErrorMessage("Trying to start Eyetracking but Eyetracking is already running"));
//        }
    }

    public void stopRunning() {
//        if (running) {
//            running = false;
//            consoleThread.interrupt();
//            process.destroy();
//        } else {
//            Logger.getInstance().post(new ErrorMessage("Trying to stop Eyetracking but Eyetracking is not running"));
//        }
    }

    public void startListeningToOutput() throws IOException, InterruptedException {
//        InputStream is = process.getInputStream();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
//        String line;
//        while (process.isAlive()) {
//            line = reader.readLine();
//            if (line != null) {
//                String[] splittedLine = line.split("::");
//                switch (splittedLine[0]) {
//                    case "PLAIN":
//                        if (splittedLine.length >= 2) {
//                            Logger.getInstance().post(new PythonAnswerMessage(splittedLine[1]));
//                        }
//                        break;
//                    case "EYETRACKING":
//                        if (splittedLine.length >= 2) {
//                            String[] coordinates = splittedLine[1].split(" ");
//                            int xCoordinate = Integer.parseInt(coordinates[0].substring(2));
//                            int yCoordinate = Integer.parseInt(coordinates[1].substring(2));
//                            Logger.getInstance().post(new EyeTrackingDataMessage(xCoordinate,yCoordinate));
//                        }
//                        break;
//                }
//            }
//        }
    }
}
