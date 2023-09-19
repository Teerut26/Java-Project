package cs211.project.services.datasource;

import cs211.project.models.CommentActivitiesEvent;
import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.models.User;
import cs211.project.models.collections.CommentActivitiesEventCollection;
import cs211.project.services.DatasourceInterface;
import cs211.project.utils.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class CommentActivitiesEventFileListDatasource implements DatasourceInterface<CommentActivitiesEventCollection> {
    private String basePath = "data/csv/";
    private String fileName = "commentsActivitiesEvent.csv";
    private FileIO fileIO;

    public CommentActivitiesEventFileListDatasource() {
        this.fileIO = new FileIO(this.basePath + this.fileName);
    }

    @Override
    public CommentActivitiesEventCollection readData() {
        BufferedReader buffer = this.fileIO.reader();

        String line = "";
        try {
            CommentActivitiesEventCollection commentCollection = new CommentActivitiesEventCollection();

            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;

                String[] data = line.split(",");

                String id = data[0].trim();
                String message = data[1].trim();
                String ownerId = data[2].trim();
                String eventId = data[3].trim();
                LocalDateTime timeStamps = LocalDateTime.parse(data[4].trim());

                UserFileListDatasource userFileListDatasource = new UserFileListDatasource();
                User owner = userFileListDatasource.readData().findById(ownerId);

                EventFileListDatesource eventFileListDatesource = new EventFileListDatesource();
                Event event = eventFileListDatesource.readData().findById(eventId);

                CommentActivitiesEvent comment = new CommentActivitiesEvent(id, message, owner, event, timeStamps);

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
                String line = comment.getId() + "," + comment.getMessage() + "," + comment.getOwner().getId() + "," + comment.getEvent().getEventID() + "," + comment.getTimeStamps().toString();
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
