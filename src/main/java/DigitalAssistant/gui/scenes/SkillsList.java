package DigitalAssistant.gui.scenes;

import DigitalAssistant.Utilities.Handler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class SkillsList {

    private String input;
    private Stage window;
    private Scene scene;
    private Handler handler;
    @FXML
    private TextArea textArea;

    public SkillsList(Handler handler){
        this.handler = handler;
    }

    public void display(){
        window = new Stage();
        window.getIcons().add(handler.getLoadImages().getIcon());
        window.setTitle("Current skills");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SkillsList.fxml"));
        loader.setController(this);

        try {
            this.scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        String directoryPath = System.getProperty("user.dir");
        String fileName = "grammar.txt";
        String filePath = directoryPath + "/" + fileName;
        File file = new File(filePath);

        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while(sc.hasNextLine()){
            this.textArea.appendText(sc.nextLine()+"\n");
        }

        textArea.setEditable(false);


        window.setScene(scene);
        window.show();

    }

}
