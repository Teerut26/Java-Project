package cs211.project.services.datasource;

import cs211.project.models.Event;
import cs211.project.models.ManyToMany;
import cs211.project.models.Team;
import cs211.project.models.User;
import cs211.project.models.collections.ManyToManyCollection;
import cs211.project.models.collections.TeamCollection;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.DatasourceInterface;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class TeamFileListDatasource implements DatasourceInterface<TeamCollection> {
    private String basePath = "data/csv/";
    private String fileName = "team.csv";

    public TeamFileListDatasource() {
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
    public TeamCollection readData() {
        TeamCollection teamCollection = new TeamCollection();
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
                String name = data[1].trim();
                String quantity = data[2].trim();
                LocalDateTime startRecruitDate = LocalDateTime.parse(data[3].trim());
                LocalDateTime endRecruitDate = LocalDateTime.parse(data[4].trim());
                String ownerID = data[5].trim();
                String eventID = data[6].trim();

                EventFileListDatesource eventFileListDatesource = new EventFileListDatesource();
                Event event = eventFileListDatesource.readData().findById(eventID);

                UserFileListDatasource userFileListDatasource = new UserFileListDatasource();
                User user = userFileListDatasource.readData().findById(ownerID);

                Team team = new Team(id, name, quantity, startRecruitDate, endRecruitDate, user, event);

                // Read many to many team user file
                UserCollection MTM_TeamUserCollection = new UserCollection();
                ManyToManyFileListDatasource MTM_EventUserFileListDatasource = new ManyToManyFileListDatasource(ManyToManyFileListDatasource.MTM_TEAM_USER);
                MTM_EventUserFileListDatasource.readData().findsByA(id).forEach((userMTM) -> {
                    UserFileListDatasource userFileListDatasource1 = new UserFileListDatasource();
                    User user1 = userFileListDatasource1.readData().findById(userMTM.getB());
                    MTM_TeamUserCollection.add(user1);
                });

                team.setUserInTeam(MTM_TeamUserCollection);
                teamCollection.add(team);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return teamCollection;
    }

    @Override
    public void writeData(TeamCollection data) {
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
            for (Team team : data.getTeams()) {
                String line = team.getId() + "," +
                        team.getName() + "," +
                        team.getQuantity() + "," +
                        team.getStartRecruitDate() + "," +
                        team.getEndRecruitDate() + "," +
                        team.getOwner().getId() + "," +
                        team.getEvent().getEventID();

                // Write many to many event user file
                if (team.getUserInTeam() != null) {
                    ManyToManyFileListDatasource manyToManyFileListDatasource = new ManyToManyFileListDatasource(ManyToManyFileListDatasource.MTM_TEAM_USER);
                    ManyToManyCollection manyToManyCollection = new ManyToManyCollection();
                    manyToManyCollection.setManyToManies(manyToManyFileListDatasource.readData().getManyToManies());
                    team.getUserInTeam().getUsers().forEach((user) -> {
                        ManyToMany manyToMany = new ManyToMany(team.getId(), user.getId());
                        manyToManyCollection.add(manyToMany);
                    });
                    manyToManyFileListDatasource.writeData(manyToManyCollection);
                }


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
