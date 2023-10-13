package cs211.project.services.datasource;

import cs211.project.models.*;
import cs211.project.models.collections.ActivitiesEventCollection;
import cs211.project.models.collections.CommentActivitiesEventCollection;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.DatasourceInterface;
import cs211.project.utils.FileIO;
import cs211.project.utils.ReplaceComma;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class CommentActivitiesEventFileListDatasource implements DatasourceInterface<CommentActivitiesEventCollection> {
    private String basePath = "data/csv/";
    private String fileName = "commentsActivitiesEvent.csv";
    private FileIO fileIO;
    private ReplaceComma replaceComma;

    public CommentActivitiesEventFileListDatasource() {
        this.replaceComma = new ReplaceComma();
        this.fileIO = new FileIO(this.basePath + this.fileName);
    }

    @Override
    public CommentActivitiesEventCollection readData() {
        BufferedReader buffer = this.fileIO.reader();

        String line = "";
        try {
            CommentActivitiesEventCollection commentCollection = new CommentActivitiesEventCollection();
            UserCollection userCollection = new UserFileListDatasource().readData();
            ActivitiesEventCollection activitiesEventCollection = new ActivitiesEventFileListDatesource().readData();

            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;

                String[] data = line.split(",");

                String id = data[0].trim();
                String message = data[1].trim();
                String ownerId = data[2].trim();
                String activityId = data[3].trim();
                LocalDateTime timeStamps = LocalDateTime.parse(data[4].trim());

                message = this.replaceComma.replaceBack(message);

                User owner = userCollection.findById(ownerId);
                ActivitiesEvent activitiesEvent = activitiesEventCollection.findById(activityId);

                CommentActivitiesEvent comment = new CommentActivitiesEvent(id, message, owner, activitiesEvent, timeStamps);

                commentCollection.add(comment);
            }

            return commentCollection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void writeData(CommentActivitiesEventCollection data) {
        BufferedWriter buffer = this.fileIO.writer();
        try {
            for (CommentActivitiesEvent comment : data.getComments()) {
                String line = comment.getId() + "," + this.replaceComma.replace(comment.getMessage()) + "," + comment.getOwner().getId() + "," + comment.getActivitiesEvent().getId() + "," + comment.getTimeStamps().toString();
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
