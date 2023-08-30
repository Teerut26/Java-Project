package cs211.project.services.datasource;

import cs211.project.models.Comment;
import cs211.project.models.User;
import cs211.project.models.collections.CommentCollection;
import cs211.project.services.DatasourceInterface;
import cs211.project.utils.FileIO;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class CommentFileListDatesource implements DatasourceInterface<CommentCollection> {
    private String basePath = "data/csv/";
    private String fileName = "comments.csv";
    private FileIO fileIO;

    public CommentFileListDatesource() {
        this.fileIO = new FileIO(this.basePath + this.fileName);
    }

    @Override
    public CommentCollection readData() {
        CommentCollection commentCollection = new CommentCollection();
        BufferedReader buffer = this.fileIO.reader();

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
        BufferedWriter buffer = this.fileIO.writer();

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
