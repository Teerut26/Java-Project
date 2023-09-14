package cs211.project.services.datasource;

import cs211.project.models.Activities;
import cs211.project.models.User;
import cs211.project.models.collections.ActivitiesCollection;
import cs211.project.services.DatasourceInterface;
import cs211.project.utils.FileIO;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class ActivitiesFileListDatesource implements DatasourceInterface<ActivitiesCollection> {
    private String basePath = "data/csv/";
    private String fileName = "activities.csv";
    private FileIO fileIO;

    public ActivitiesFileListDatesource() {
        this.fileIO = new FileIO(this.basePath + this.fileName);
    }

    @Override
    public ActivitiesCollection readData() {
        BufferedReader buffer = this.fileIO.reader();

        String line = "";
        try {
            ActivitiesCollection activitiesCollection = new ActivitiesCollection();

            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;

                String[] data = line.split(",");

                String id = data[0].trim();
                String title = data[1].trim();
                String detail = data[2].trim();
                LocalDateTime startDate = LocalDateTime.parse(data[3].trim());
                LocalDateTime endDate = LocalDateTime.parse(data[4].trim());
                String ownerID = data[5].trim();

                UserFileListDatasource userFileListDatasource = new UserFileListDatasource();
                User owner = userFileListDatasource.readData().findById(ownerID);

                Activities activities = new Activities(id, title, detail, startDate, endDate, owner);

                activitiesCollection.add(activities);
            }

            return activitiesCollection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                buffer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }

    @Override
    public void writeData(ActivitiesCollection data) {
        BufferedWriter buffer = this.fileIO.writer();

        try {
            for (Activities activities : data.getActivitiesArrayList()) {
                String line = activities.getId() + "," + activities.getTitle() + "," + activities.getDetail() + "," + activities.getStartDate() + "," + activities.getEndDate();
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
