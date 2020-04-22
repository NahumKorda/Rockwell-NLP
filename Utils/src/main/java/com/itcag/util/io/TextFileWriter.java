package com.itcag.util.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>This class creates a text file, and provides writing access to it. If the file exists, it is replaced by a new one.</p>
 */
public final class TextFileWriter {

    private final PrintWriter out;

    /**
     * @param filePath String holding a local path where the new file will be created. It must contain the file name.
     * @throws Exception if anything goes wrong.
     */
    public TextFileWriter(String filePath) throws Exception {

        File file = new File(filePath);
        if (file.exists()) file.delete();
        file.createNewFile();

        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        out = new PrintWriter(bufferedWriter);

    }

    /**
     * @param line String that will be inserted as a new line at the end of the file.
     * @throws IOException if writing fails.
     */
    public final void write(String line) throws IOException {
        out.println(line);
        out.flush();
    }
    
    /**
     * Flushes the local buffer and closes the file.
     */
    public final void close() {
        out.flush();
        out.close();
    }

}
