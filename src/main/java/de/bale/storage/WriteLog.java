package de.bale.storage;

import de.bale.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteLog extends Utils {

    public static BufferedWriter logWriter;

    static {
        try {
            logWriter = new BufferedWriter(new FileWriter(getSettingsDir() + "log_"+ java.time.LocalDate.now()+".txt", true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addLinetoAllLog(String newline) {
        try {
            logWriter.append(newline);
            logWriter.newLine();
            logWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
