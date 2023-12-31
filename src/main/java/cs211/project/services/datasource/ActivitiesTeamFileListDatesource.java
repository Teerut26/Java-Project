package cs211.project.services.datasource;

import cs211.project.models.ActivitiesEvent;
import cs211.project.models.ActivitiesTeam;
import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.models.collections.ActivitiesEventCollection;
import cs211.project.models.collections.ActivitiesTeamCollection;
import cs211.project.models.collections.EventCollection;
import cs211.project.models.collections.TeamCollection;
import cs211.project.services.DatasourceInterface;
import cs211.project.utils.FileIO;
import cs211.project.utils.ReplaceComma;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class ActivitiesTeamFileListDatesource implements DatasourceInterface<ActivitiesTeamCollection> {
    private String basePath = "data/csv/";
    private String fileName = "activitiesTeam.csv";
    private FileIO fileIO;
    private ReplaceComma replaceComma;


    public ActivitiesTeamFileListDatesource() {
        this.replaceComma = new ReplaceComma();
        this.fileIO = new FileIO(this.basePath + this.fileName);
    }

    @Override
    public ActivitiesTeamCollection readData() {
        BufferedReader buffer = this.fileIO.reader();

        String line = "";
        try {
            ActivitiesTeamCollection activitiesTeamCollection = new ActivitiesTeamCollection();
            TeamFileListDatasource teamFileListDatasource = new TeamFileListDatasource();
            TeamCollection teamCollection = teamFileListDatasource.readData();

            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;

                String[] data = line.split(",");

                String id = data[0].trim();
                String title = data[1].trim();
                String detail = data[2].trim();
                LocalDateTime dateStart = LocalDateTime.parse(data[3].trim());
                LocalDateTime dateEnd = LocalDateTime.parse(data[4].trim());
                String teamId = data[5].trim();

                title = this.replaceComma.replaceBack(title);
                detail = this.replaceComma.replaceBack(detail);

                Team team = teamCollection.findById(teamId);

                ActivitiesTeam activitiesTeam = new ActivitiesTeam(id, title, detail, dateStart, dateEnd, team);

                activitiesTeamCollection.add(activitiesTeam);
            }

            return activitiesTeamCollection;
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
    public void writeData(ActivitiesTeamCollection data) {
        BufferedWriter buffer = this.fileIO.writer();

        try {
            for (ActivitiesTeam activities : data.getActivitiesArrayList()) {
                String line = activities.getId() + "," + this.replaceComma.replace(activities.getTitle()) + "," + this.replaceComma.replace(activities.getDetail()) + "," + activities.getDateStart() + "," + activities.getDateEnd() + "," + activities.getTeam().getId();
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
