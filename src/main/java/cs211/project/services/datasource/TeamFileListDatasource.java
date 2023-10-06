package cs211.project.services.datasource;

import cs211.project.models.Event;
import cs211.project.models.ManyToMany;
import cs211.project.models.Team;
import cs211.project.models.User;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.ManyToManyCollection;
import cs211.project.models.collections.TeamCollection;
import cs211.project.models.collections.UserCollection;
import cs211.project.services.DatasourceInterface;
import cs211.project.utils.FileIO;
import cs211.project.utils.ReplaceComma;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class TeamFileListDatasource implements DatasourceInterface<TeamCollection> {
    private String basePath = "data/csv/";
    private String fileName = "team.csv";
    private FileIO fileIO;
    private ReplaceComma replaceComma;

    public TeamFileListDatasource() {
        this.replaceComma = new ReplaceComma();
        this.fileIO = new FileIO(this.basePath + this.fileName);
    }

    @Override
    public TeamCollection readData() {
        BufferedReader buffer = this.fileIO.reader();

        String line = "";
        try {
            EventCollection eventCollection = new EventFileListDatesource().readData();
            UserCollection userCollection = new UserFileListDatasource().readData();

            TeamCollection teamCollection = new TeamCollection();
            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;

                String[] data = line.split(",");

                String id = data[0].trim();
                String name = data[1].trim();
                Integer quantity = Integer.parseInt(data[2].trim());
                LocalDateTime startRecruitDate = LocalDateTime.parse(data[3].trim());
                LocalDateTime endRecruitDate = LocalDateTime.parse(data[4].trim());
                String ownerID = data[5].trim();
                String eventID = data[6].trim();

                name = this.replaceComma.replaceBack(name);

                Event event = eventCollection.findById(eventID);
                User user = userCollection.findById(ownerID);

                Team team = new Team(id, name, quantity, startRecruitDate, endRecruitDate, user, event);

                teamCollection.add(team);
            }
            return teamCollection;
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
    public void writeData(TeamCollection data) {
        BufferedWriter buffer = fileIO.writer();

        try {
            for (Team team : data.getTeams()) {
                String line = team.getId() + "," +
                        this.replaceComma.replace(team.getName()) + "," +
                        team.getQuantity() + "," +
                        team.getStartRecruitDate() + "," +
                        team.getEndRecruitDate() + "," +
                        team.getOwner().getId() + "," +
                        team.getEvent().getEventID();
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
