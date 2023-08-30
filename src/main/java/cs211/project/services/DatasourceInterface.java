package cs211.project.services;

public interface DatasourceInterface <T>{
    T readData();
    void writeData(T data);
}
