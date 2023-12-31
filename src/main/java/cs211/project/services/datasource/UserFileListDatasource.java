package cs211.project.services.datasource;

import cs211.project.models.*;
import cs211.project.models.collections.*;
import cs211.project.services.DatasourceInterface;
import cs211.project.utils.FileIO;
import cs211.project.utils.ReplaceComma;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class UserFileListDatasource implements DatasourceInterface<UserCollection> {
    private String basePath = "data/csv/";
    private String fileName = "users.csv";
    private FileIO fileIO;
    private ReplaceComma replaceComma;

    public UserFileListDatasource() {
        this.replaceComma = new ReplaceComma();
        this.fileIO = new FileIO(this.basePath + this.fileName);
    }

    @Override
    public UserCollection readData() {
        UserCollection userCollection = new UserCollection();
        BufferedReader buffer = this.fileIO.reader();

        String line = "";
        try {
            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;

                String[] data = line.split(",");

                String id = data[0].trim();
                String nameUser = data[1].trim();
                String userName = data[2].trim();
                String password = data[3].trim();
                String role = data[4].trim();
                String imagePath = data[5].trim();
                LocalDateTime lastLogin = LocalDateTime.parse(data[6].trim());
                String teamMode = data[7].trim();
                String font = data[8].trim();

                nameUser = this.replaceComma.replaceBack(nameUser);
                userName = this.replaceComma.replaceBack(userName);


                User user = new User(id, nameUser, userName, password, role, imagePath, lastLogin , teamMode , font);

                userCollection.add(user);
            }
            return userCollection;
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
    public void writeData(UserCollection data) {
        BufferedWriter buffer = this.fileIO.writer();

        try {
            for (User user : data.getUsers()) {
                String line = user.getId() + "," + this.replaceComma.replace(user.getNameUser()) + "," + this.replaceComma.replace(user.getUserName()) + "," + user.getPassword() + "," + user.getRole() + "," + user.getImageProfile() + "," + user.getLastLogin().toString() + "," + user.getThemeMode() + "," + user.getFont();
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
