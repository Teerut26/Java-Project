package cs211.project.utils;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ImageSaver {
    private String filename;
    private String folder;
    public FileChooser chooser;
    public File file;
    public Path target;
    public String resultPath;
    public String extention;

    public ImageSaver(String filename, String folder) {
        this.filename = filename;
        this.folder = folder;
        chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg"));
    }

    public void saveImage(ActionEvent event) {
        Node source = (Node) event.getSource();
        file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                File destDir = new File("data/images/"+folder);

                if (!destDir.exists()) destDir.mkdirs();

                String[] fileSplit = file.getName().split("\\.");
                this.extention = fileSplit[fileSplit.length-1];

                String filename = this.filename + "." +  this.extention;
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );
                resultPath = destDir.getAbsolutePath()+System.getProperty("file.separator")+filename;
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                this.target = target;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

