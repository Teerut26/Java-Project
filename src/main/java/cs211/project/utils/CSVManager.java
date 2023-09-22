package cs211.project.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVManager {
    private String filePath;

    public CSVManager(String filePath) {
        this.filePath = filePath;
    }

    public List<CSVRecord> loadData() throws IOException {
        CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT);
        return parser.getRecords();
    }

    public void addData(List<String> rowData) throws IOException {
        FileWriter writer = new FileWriter(filePath, true);
        writer.append(String.join(",", rowData));
        writer.append("\n");
        writer.close();
    }

    public void removeData(int rowToRemove) throws IOException {
        List<CSVRecord> records = loadData();
        records.remove(rowToRemove);

        FileWriter writer = new FileWriter(filePath, false);
        for (CSVRecord record : records) {
            writer.append(String.join(",", record));
            writer.append("\n");
        }
        writer.close();
    }

    // Implement querying and table relationship methods as needed.
}
