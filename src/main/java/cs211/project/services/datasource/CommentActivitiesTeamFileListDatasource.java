package cs211.project.services.datasource;

import cs211.project.models.*;
import cs211.project.models.collections.*;
import cs211.project.services.DatasourceInterface;
import cs211.project.utils.FileIO;
import cs211.project.utils.ReplaceComma;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class CommentActivitiesTeamFileListDatasource implements DatasourceInterface<CommentActivitiesTeamCollection> {
    private String basePath = "data/csv/";
    private String fileName = "commentsActivitiesTeam.csv";
    private FileIO fileIO;
    private ReplaceComma replaceComma;

    public CommentActivitiesTeamFileListDatasource() {
        this.replaceComma = new ReplaceComma();
        this.fileIO = new FileIO(this.basePath + this.fileName);
    }

    @Override
    public CommentActivitiesTeamCollection readData() {
        BufferedReader buffer = this.fileIO.reader();

        String line = "";
        try {
            CommentActivitiesTeamCollection commentCollection = new CommentActivitiesTeamCollection();
            UserCollection userCollection = new UserFileListDatasource().readData();
            ActivitiesTeamCollection activitiesTeamCollection = new ActivitiesTeamFileListDatesource().readData();

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
                ActivitiesTeam activitiesTeam = activitiesTeamCollection.findById(activityId);

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
                String line = comment.getId() + "," + this.replaceComma.replace(comment.getMessage()) + "," + comment.getOwner().getId() + "," + comment.getActivitiesTeam().getId() + "," + comment.getTimeStamps().toString();
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
