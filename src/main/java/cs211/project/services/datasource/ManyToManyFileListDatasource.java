package cs211.project.services.datasource;

import cs211.project.models.ManyToMany;
import cs211.project.models.collections.ManyToManyCollection;
import cs211.project.services.DatasourceInterface;
import cs211.project.utils.FileIO;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ManyToManyFileListDatasource implements DatasourceInterface<ManyToManyCollection> {
    private String basePath = "data/csv/mtm/";
    private String fileName;
    public static String MTM_EVENT_USER = "_eventToUser.csv";
    public static String MTM_TEAM_USER = "_teamToUser.csv";
    private FileIO fileIO;

    public ManyToManyFileListDatasource(String fileName) {
        this.fileName = fileName;
        this.fileIO = new FileIO(this.basePath + this.fileName);
    }

    @Override
    public ManyToManyCollection readData() {
        ManyToManyCollection manyToManyCollection = new ManyToManyCollection();
        BufferedReader buffer = this.fileIO.reader();

        String line = "";
        try {
            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;

                String[] data = line.split(",");

                String primary = data[0].trim();
                String secondary = data[1].trim();

                ManyToMany manyToMany = new ManyToMany(primary, secondary);

                manyToManyCollection.add(manyToMany);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return manyToManyCollection;
    }

    @Override
    public void writeData(ManyToManyCollection data) {
        BufferedWriter buffer = this.fileIO.writer();

        try {
            for (ManyToMany manyToMany : data.getManyToManies()) {
                String line = manyToMany.getA() + "," + manyToMany.getB();
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
