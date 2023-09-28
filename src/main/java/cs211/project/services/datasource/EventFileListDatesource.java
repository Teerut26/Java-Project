package cs211.project.services.datasource;

import cs211.project.models.Event;
import cs211.project.models.ManyToMany;
import cs211.project.models.User;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.ManyToManyCollection;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.DatasourceInterface;
import cs211.project.utils.FileIO;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class EventFileListDatesource implements DatasourceInterface<EventCollection> {
    private String basePath = "data/csv/";
    private String fileName = "events.csv";
    private FileIO fileIO;

    public EventFileListDatesource() {
        this.fileIO = new FileIO(this.basePath + this.fileName);
    }

    @Override
    public EventCollection readData() {

        BufferedReader buffer = this.fileIO.reader();
        String line = "";
        try {
            UserCollection userCollection = new UserFileListDatasource().readData();
            EventCollection eventCollection = new EventCollection();

            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;

                String[] data = line.split(",");

                String eventID = data[0].trim();
                String nameEvent = data[1].trim();
                String descriptionEvent = data[2].trim();
                String location = data[3].trim();
                String imageEvent = data[4].trim();
                LocalDateTime startDate = LocalDateTime.parse(data[5].trim());
                LocalDateTime endDate = LocalDateTime.parse(data[6].trim());
                Integer quantityEvent = Integer.parseInt(data[7].trim());
                boolean isPublic = Boolean.parseBoolean(data[8].trim());
                String ownerId = data[9].trim();

                User owner = userCollection.findById(ownerId);
                Event event = new Event(eventID, nameEvent, imageEvent, descriptionEvent, location, startDate, endDate, quantityEvent, isPublic, owner);

                eventCollection.add(event);
            }
            return eventCollection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @Override
    public void writeData(EventCollection data) {
        BufferedWriter buffer = this.fileIO.writer();

        try {
            for (Event event : data.getEvents()) {
                String line = event.getEventID() + "," + event.getNameEvent() + "," + event.getDescriptionEvent() + "," + event.getLocation() + "," + event.getImageEvent() + "," + event.getStartDate().toString() + "," + event.getEndDate().toString() + "," + event.getQuantityEvent() + "," + event.isPublic() + "," + event.getOwner().getId();
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
