package cs211.project.services.datasource;

import cs211.project.models.Activities;
import cs211.project.models.ActivitiesEvent;
import cs211.project.models.Event;
import cs211.project.models.collections.ActivitiesCollection;
import cs211.project.models.collections.ActivitiesEventCollection;
import cs211.project.models.collections.EventCollection;
import cs211.project.services.DatasourceInterface;
import cs211.project.utils.FileIO;
import cs211.project.utils.ReplaceComma;

import java.io.*;
import java.time.LocalDateTime;

public class ActivitiesEventFileListDatesource implements DatasourceInterface<ActivitiesEventCollection> {
    private String basePath = "data/csv/";
    private String fileName = "activitiesEvent.csv";
    private FileIO fileIO;
    private ReplaceComma replaceComma;

    public ActivitiesEventFileListDatesource() {
        this.fileIO = new FileIO(this.basePath + this.fileName);
        this.replaceComma = new ReplaceComma();
    }

    @Override
    public ActivitiesEventCollection readData() {
        BufferedReader buffer = this.fileIO.reader();

        String line = "";
        try {
            ActivitiesEventCollection activitiesEventCollection = new ActivitiesEventCollection();
            EventFileListDatesource eventFileListDatesource = new EventFileListDatesource();
            EventCollection eventCollection = eventFileListDatesource.readData();

            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;

                String[] data = line.split(",");

                String id = data[0].trim();
                String title = data[1].trim();
                String detail = data[2].trim();
                LocalDateTime dateStart = LocalDateTime.parse(data[3].trim());
                LocalDateTime dateEnd = LocalDateTime.parse(data[4].trim());
                String eventId = data[5].trim();

                Event event = eventCollection.findById(eventId);

                title = this.replaceComma.replaceBack(title);
                detail = this.replaceComma.replaceBack(detail);

                ActivitiesEvent activitiesEvent = new ActivitiesEvent(id, title, detail, dateStart, dateEnd, event);

                activitiesEventCollection.add(activitiesEvent);
            }

            return activitiesEventCollection;
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
    public void writeData(ActivitiesEventCollection data) {
        BufferedWriter buffer = this.fileIO.writer();

        try {
            for (ActivitiesEvent activities : data.getActivitiesArrayList()) {
                String line = activities.getId() + "," + this.replaceComma.replace(activities.getTitle()) + "," + this.replaceComma.replace(activities.getDetail()) + "," + activities.getDateStart() + "," + activities.getDateEnd() + "," + activities.getEvent().getEventID();
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
