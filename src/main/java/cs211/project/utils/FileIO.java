package cs211.project.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileIO {
    private String filePath;

    public FileIO(String filePath) {
        this.filePath = filePath;
        this.checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File file = new File(this.filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = this.filePath;
        file = new File(filePath);
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
