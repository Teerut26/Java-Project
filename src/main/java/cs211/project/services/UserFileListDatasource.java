package cs211.project.services;

import cs211.project.models.*;
import cs211.project.models.collections.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class UserFileListDatasource implements DatasourceInterface<UserCollection> {
    private String basePath = "data/csv/";
    private String fileName = "users.csv";

    public UserFileListDatasource() {
        this.checkFileIsExisted();
    }

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

    @Override
    public UserCollection readData() {
        UserCollection userCollection = new UserCollection();
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
                String nameUser = data[1].trim();
                String userName = data[2].trim();
                String email = data[3].trim();
                String password = data[4].trim();
                LocalDateTime lastLogin = LocalDateTime.parse(data[5].trim());

                // Read many to many event user file
                EventCollection MTM_EventUserCollection = new EventCollection();
                ManyToManyFileListDatasource MTM_EventUserFileListDatasource = new ManyToManyFileListDatasource(ManyToManyFileListDatasource.MTM_EVENT_USER);
                MTM_EventUserFileListDatasource.readData().findsByB(id).forEach((event) -> {
                    EventFileListDatesource eventFileListDatesource = new EventFileListDatesource();
                    Event event1 = eventFileListDatesource.readData().findById(event.getA());
                    MTM_EventUserCollection.add(event1);
                });

                // Read many to many team user file
                TeamCollection MTM_TeamUserCollection = new TeamCollection();
                ManyToManyFileListDatasource MTM_TeamUserFileListDatasource = new ManyToManyFileListDatasource(ManyToManyFileListDatasource.MTM_TEAM_USER);
                MTM_TeamUserFileListDatasource.readData().findsByB(id).forEach((team) -> {
                    TeamFileListDatasource teamFileListDatasource = new TeamFileListDatasource();
                    Team team1 = teamFileListDatasource.readData().findById(team.getA());
                    MTM_TeamUserCollection.add(team1);
                });

                User user = new User(id, nameUser, userName, email, password, lastLogin);

                user.setEventCollection(MTM_EventUserCollection);
                user.setTeamCollection(MTM_TeamUserCollection);

                userCollection.add(user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return userCollection;
    }

    @Override
    public void writeData(UserCollection data) {
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
            for (User user : data.getUsers()) {
                String line = user.getId() + "," + user.getNameUser() + "," + user.getUserName() + "," + user.getEmail() + "," + user.getPassword() + "," + user.getLastLogin().toString();

                // Write many to many event user file
                ManyToManyFileListDatasource MTM_EventUserFileListDatasource = new ManyToManyFileListDatasource(ManyToManyFileListDatasource.MTM_EVENT_USER);
                ManyToManyCollection MTM_EventUserCollection = new ManyToManyCollection();
                MTM_EventUserCollection.setManyToManies(MTM_EventUserFileListDatasource.readData().getManyToManies());
                user.getEventCollection().getEvents().forEach((event) -> {
                    ManyToMany manyToMany = new ManyToMany(event.getEventID(), user.getId());
                    MTM_EventUserCollection.add(manyToMany);
                });
                MTM_EventUserFileListDatasource.writeData(MTM_EventUserCollection);

                // Write many to many team user file
                ManyToManyFileListDatasource MTM_TeamUserFileListDatasource = new ManyToManyFileListDatasource(ManyToManyFileListDatasource.MTM_TEAM_USER);
                ManyToManyCollection MTM_TeamUserCollection = new ManyToManyCollection();
                MTM_TeamUserCollection.setManyToManies(MTM_TeamUserFileListDatasource.readData().getManyToManies());
                user.getTeamCollection().getTeams().forEach((team) -> {
                    ManyToMany manyToMany = new ManyToMany(team.getId(), user.getId());
                    MTM_TeamUserCollection.add(manyToMany);
                });
                MTM_TeamUserFileListDatasource.writeData(MTM_TeamUserCollection);

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
