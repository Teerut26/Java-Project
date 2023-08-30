package cs211.project.services;

import cs211.project.utils.FileIO;
import org.json.JSONObject;

import cs211.project.models.collections.EventCollection;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class EventFileListDatesourceJson implements DatasourceInterface<EventCollection> {
    private String basePath = "data/json/";
    private String fileName = "events.json";

    @Override
    public EventCollection readData() {
//        FileIO fileIO = new FileIO(basePath + fileName);
//        JSONObject jsonObject = new JSONObject(fileIO.reader().toString());
        return null;
    }

    @Override
    public void writeData(EventCollection data) {
//        FileIO fileIO = new FileIO(basePath + fileName);
//        BufferedWriter buffer = fileIO.writer();
//        buffer.append()
//        try {
//            buffer.flush();
//            buffer.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
