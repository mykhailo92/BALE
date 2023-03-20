package de.bale.eyetracking;

import com.google.common.eventbus.Subscribe;
import de.bale.messages.eyetracking.EyetrackingCallibrationDoneMessage;
import de.bale.messages.eyetracking.WriteToPythonMessage;

import java.io.OutputStream;
import java.io.PrintWriter;

public class EyetrackerPythonWriter {

    private Process process;

    EyetrackerPythonWriter(Process process) {
        this.process = process;
    }

    @Subscribe
    public void gotWriteToPythonMessage(WriteToPythonMessage message) {
        answerToPython(message.getMessage());
    }

    @Subscribe
    public void gotCallibrationDoneMessage(EyetrackingCallibrationDoneMessage message) {
        answerToPython("end");
    }

    public void answerToPython(String text) {
//        System.out.println("PRINTING TO PYTHON: " + text);
        OutputStream os = process.getOutputStream();
        PrintWriter writer = new PrintWriter(os);
        writer.write(text + "\n");
        writer.flush();
    }

}
