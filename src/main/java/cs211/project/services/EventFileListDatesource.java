package cs211.project.services;

import cs211.project.models.Comment;
import cs211.project.models.Event;
import cs211.project.models.ManyToMany;
import cs211.project.models.User;
import cs211.project.models.collections.CommentCollection;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.ManyToManyCollection;
import cs211.project.models.collections.UserCollection;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class EventFileListDatesource implements DatasourceInterface<EventCollection> {
    private String basePath = "data/csv/";
    private String fileName = "comments.csv";

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

    public EventFileListDatesource() {
        this.checkFileIsExisted();
    }

    @Override
    public EventCollection readData() {
        EventCollection eventCollection = new EventCollection();
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

                String eventID = data[0].trim();
                String nameEvent = data[1].trim();
                String imageEvent = data[2].trim();
                String descriptionEvent = data[3].trim();
                LocalDateTime startDate = LocalDateTime.parse(data[4].trim());
                LocalDateTime endDate = LocalDateTime.parse(data[5].trim());
                Integer quantityEvent = Integer.parseInt(data[6].trim());
                String ownerId = data[7].trim();

                UserFileListDatasource userFileListDatasource = new UserFileListDatasource();
                User owner = userFileListDatasource.readData().findById(ownerId);

                Event event = new Event(eventID, nameEvent, imageEvent, descriptionEvent, startDate, endDate, quantityEvent, owner);

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return eventCollection;
    }

    @Override
    public void writeData(EventCollection data) {
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
            for (Event event : data.getEvents()) {
                String line = event.getEventID() + "," + event.getNameEvent() + "," + event.getImageEvent() + "," + event.getDescriptionEvent() + "," + event.getStartDate().toString() + "," + event.getEndDate().toString() + "," + event.getQuantityEvent() + "," + event.getOwner().getId();
                buffer.append(line);
                buffer.append("\n");

                ManyToManyFileListDatasource manyToManyFileListDatasource = new ManyToManyFileListDatasource(ManyToManyFileListDatasource.MTM_EVENT_USER);

                ManyToManyCollection manyToManyCollection = new ManyToManyCollection();
                manyToManyCollection.setManyToManies(manyToManyFileListDatasource.readData().getManyToManies());

                event.getUserInEvent().getUsers().forEach((user) -> {
                    ManyToMany manyToMany = new ManyToMany(event.getEventID(), user.getId());
                    manyToManyCollection.add(manyToMany);
                });

                manyToManyFileListDatasource.writeData(manyToManyCollection);

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
