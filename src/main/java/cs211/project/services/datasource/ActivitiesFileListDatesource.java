package cs211.project.services.datasource;

import cs211.project.models.Activities;
import cs211.project.models.User;
import cs211.project.models.collections.ActivitiesCollection;
import cs211.project.services.DatasourceInterface;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class ActivitiesFileListDatesource implements DatasourceInterface<ActivitiesCollection> {
    private String basePath = "data/csv/";
    private String fileName = "activities.csv";

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

    public ActivitiesFileListDatesource() {
        this.checkFileIsExisted();
    }

    @Override
    public ActivitiesCollection readData() {
        ActivitiesCollection activitiesCollection = new ActivitiesCollection();
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return activitiesCollection;
    }

    @Override
    public void writeData(ActivitiesCollection data) {
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
