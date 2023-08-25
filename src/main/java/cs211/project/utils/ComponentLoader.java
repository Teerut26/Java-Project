package cs211.project.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ComponentLoader {
    private String basePath = "/cs211/project/components/";
    private final FXMLLoader fxmlLoader;

    public ComponentLoader(String pathSourceComponent) {
        this.fxmlLoader = new FXMLLoader();
        this.fxmlLoader.setLocation(getClass().getResource(basePath + pathSourceComponent));
    }

    public FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }
}
