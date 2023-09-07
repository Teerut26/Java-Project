package cs211.project.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileIO {
    private String filePath;

    public FileIO(String filePath) {
        this.filePath = filePath;
        this.checkFileIsExisted();
    }

    public FileIO() {
        this.filePath = filePath;
        this.checkFileIsExisted();
    }

    public void checkFileIsExisted() {
        File file = new File(this.filePath);
        File parentDirectory = file.getParentFile();
        if (!parentDirectory.exists()) {
            if (!parentDirectory.mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + parentDirectory.getAbsolutePath());
            }
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void checkFileIsExisted(String filePath) {
        File file = new File(filePath);
        File parentDirectory = file.getParentFile();
        if (!parentDirectory.exists()) {
            if (!parentDirectory.mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + parentDirectory.getAbsolutePath());
            }
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public BufferedReader reader() {
        File file = new File(this.filePath);

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        return buffer;
    }

    public BufferedWriter writer() {
        File file = new File(this.filePath);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                fileOutputStream,
                StandardCharsets.UTF_8
        );
        BufferedWriter buffer = new BufferedWriter(outputStreamWriter);

        return buffer;
    }
}
