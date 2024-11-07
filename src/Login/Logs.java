package Login;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logs {
    private BufferedWriter writer;
    private PrintStream originalOut;

    // Constructor to initialize the writer with the current date and time as filename
    public Logs() {
        try {
            String fileName ="C:/Users/Mehmet Emin/Desktop/comp132 project/uno game/src/data/Logs/" + getCurrentDateTime() + ".txt";
            writer = new BufferedWriter(new FileWriter(fileName, true));
            originalOut = System.out;
            System.setOut(new PrintStream(new LogOutputStream(), true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to get current date and time formatted as yyyy-MM-dd_HH-mm-ss
    private String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }

    // Custom OutputStream to intercept terminal output
    private class LogOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {
            writer.write(b);
            writer.flush();
            originalOut.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            writer.write(new String(b, off, len));
            writer.flush();
            originalOut.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            writer.flush();
            originalOut.flush();
        }

        @Override
        public void close() throws IOException {
            writer.close();
            originalOut.close();
        }
    }

    // Method to close the writer and restore original System.out
    public void close() {
        try {
            System.setOut(originalOut);
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
