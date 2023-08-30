package cs211.project.services.datasource;

import cs211.project.models.Comment;
import cs211.project.models.User;
import cs211.project.models.collections.CommentCollection;
import cs211.project.services.DatasourceInterface;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class CommentFileListDatesource implements DatasourceInterface<CommentCollection> {
    private String basePath = "data/csv/";
    private String fileName = "comments.csv";

    private void checkFileIsExisted() {
        File file = new File(this.basePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = this.basePath + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public CommentFileListDatesource() {
        this.checkFileIsExisted();
    }

    @Override
    public CommentCollection readData() {
        CommentCollection commentCollection = new CommentCollection();
        String filePath = this.basePath + fileName;
        File file = new File(filePath);

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

        String line = "";
        try {
            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;

                String[] data = line.split(",");

                String id = data[0].trim();
                String message = data[1].trim();
                String ownerId = data[2].trim();
                LocalDateTime timeStamps = LocalDateTime.parse(data[3].trim());

                UserFileListDatasource userFileListDatasource = new UserFileListDatasource();
                User owner = userFileListDatasource.readData().findById(ownerId);

                Comment comment = new Comment(id, message, owner, timeStamps);

                commentCollection.add(comment);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return commentCollection;
    }

    @Override
    public void writeData(CommentCollection data) {
        String filePath = this.basePath + this.fileName;
        File file = new File(filePath);

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

        try {
            for (Comment comment : data.getComments()) {
                String line = comment.getId() + "," + comment.getMessage() + "," + comment.getOwner().getId() + "," + comment.getTimeStamps().toString();
                buffer.append(line);
                buffer.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.flush();
                buffer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
