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
        EventCollection eventCollection = new EventCollection();
        BufferedReader buffer = this.fileIO.reader();

        String line = "";
        try {
            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;

                String[] data = line.split(",");

                String eventID = data[0].trim();
                String nameEvent = data[1].trim();
                String descriptionEvent = data[2].trim();
                String imageEvent = data[3].trim();
                LocalDateTime startDate = LocalDateTime.parse(data[4].trim());
                LocalDateTime endDate = LocalDateTime.parse(data[5].trim());
                Integer quantityEvent = Integer.parseInt(data[6].trim());
                boolean isPublic = Boolean.parseBoolean(data[7].trim());
                String ownerId = data[8].trim();

                UserFileListDatasource userFileListDatasource = new UserFileListDatasource();
                User owner = userFileListDatasource.readData().findById(ownerId);

                Event event = new Event(eventID, nameEvent, imageEvent, descriptionEvent, startDate, endDate, quantityEvent, isPublic, owner);

                // Read many to many event user file
                UserCollection userCollection = new UserCollection();
                ManyToManyFileListDatasource manyToManyFileListDatasource = new ManyToManyFileListDatasource(ManyToManyFileListDatasource.MTM_EVENT_USER);
                manyToManyFileListDatasource.readData().findsByA(eventID).forEach((userID) -> {
                    User user = userFileListDatasource.readData().findById(userID.getB());
                    userCollection.add(user);
                });

                event.setUserInEvent(userCollection);
                eventCollection.add(event);
            }
            buffer.close();
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
        return eventCollection;
    }

    @Override
    public void writeData(EventCollection data) {
        BufferedWriter buffer = this.fileIO.writer();

        try {
            for (Event event : data.getEvents()) {
                String line = event.getEventID() + "," + event.getNameEvent() + "," + event.getDescriptionEvent() + "," + event.getImageEvent() + "," + event.getStartDate().toString() + "," + event.getEndDate().toString() + "," + event.getQuantityEvent() + "," + event.isPublic() + "," + event.getOwner().getId();
                buffer.append(line);
                buffer.append("\n");

                if (event.getUserInEvent() != null) {
                    ManyToManyFileListDatasource manyToManyFileListDatasource = new ManyToManyFileListDatasource(ManyToManyFileListDatasource.MTM_EVENT_USER);
                    ManyToManyCollection manyToManyCollection = new ManyToManyCollection();
                    manyToManyCollection.setManyToManies(manyToManyFileListDatasource.readData().getManyToManies());
                    event.getUserInEvent().getUsers().forEach((user) -> {
                        ManyToMany manyToMany = new ManyToMany(event.getEventID(), user.getId());
                        manyToManyCollection.add(manyToMany);
                    });
                    manyToManyFileListDatasource.writeData(manyToManyCollection);
                }

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
