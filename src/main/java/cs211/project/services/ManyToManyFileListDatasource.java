package cs211.project.services;

import cs211.project.models.ManyToMany;
import cs211.project.models.collections.ManyToManyCollection;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ManyToManyFileListDatasource implements DatasourceInterface<ManyToManyCollection> {
    private String basePath = "data/csv/mtm/";
    private String fileName;

    public static String MTM_EVENT_USER = "_eventToUser.csv";
    public static String MTM_TEAM_USER = "_teamToUser.csv";

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

    public ManyToManyFileListDatasource(String fileName) {
        this.fileName = fileName;
        this.checkFileIsExisted();
    }

    @Override
    public ManyToManyCollection readData() {
        ManyToManyCollection manyToManyCollection = new ManyToManyCollection();
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
