package cs211.project.services.datasource;

import cs211.project.models.*;
import cs211.project.models.collections.CommentActivitiesEventCollection;
import cs211.project.models.collections.CommentActivitiesTeamCollection;
import cs211.project.services.DatasourceInterface;
import cs211.project.utils.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class CommentActivitiesTeamFileListDatasource implements DatasourceInterface<CommentActivitiesTeamCollection> {
    private String basePath = "data/csv/";
    private String fileName = "commentsActivitiesTeam.csv";
    private FileIO fileIO;

    public CommentActivitiesTeamFileListDatasource() {
        this.fileIO = new FileIO(this.basePath + this.fileName);
    }

    @Override
    public CommentActivitiesTeamCollection readData() {
        BufferedReader buffer = this.fileIO.reader();

        String line = "";
        try {
            CommentActivitiesTeamCollection commentCollection = new CommentActivitiesTeamCollection();

            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;

                String[] data = line.split(",");

                String id = data[0].trim();
                String message = data[1].trim();
                String ownerId = data[2].trim();
                String activityId = data[3].trim();
                LocalDateTime timeStamps = LocalDateTime.parse(data[4].trim());

                UserFileListDatasource userFileListDatasource = new UserFileListDatasource();
                User owner = userFileListDatasource.readData().findById(ownerId);

                ActivitiesTeamFileListDatesource activitiesTeamFileListDatesource = new ActivitiesTeamFileListDatesource();
                ActivitiesTeam activitiesTeam = activitiesTeamFileListDatesource.readData().findById(activityId);

                CommentActivitiesTeam comment = new CommentActivitiesTeam(id, message, owner, activitiesTeam, timeStamps);

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
    public void writeData(CommentActivitiesTeamCollection data) {
        BufferedWriter buffer = this.fileIO.writer();
        try {
            for (CommentActivitiesTeam comment : data.getComments()) {
                String line = comment.getId() + "," + comment.getMessage() + "," + comment.getOwner().getId() + "," + comment.getActivitiesTeam().getId() + "," + comment.getTimeStamps().toString();
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
