package DigitalAssistant.utilities;

import javafx.scene.image.Image;

public class LoadImages {
    private final Image icon;

    public LoadImages(){
        icon = new Image(LoadImages.class.getResource("/DigitalAssistant/img/AI-icon.png").toString());
    }

    public Image getIcon(){
        return icon;
    }

}
