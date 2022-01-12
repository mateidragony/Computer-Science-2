package Challenge25Game;

import java.awt.*;

public class Achievement {

    private final Image myIcon;
    private final String name;
    private final String description;

    public Achievement(Image img, String name, String description){
        myIcon = img;
        this.name = name;
        this.description = description;
    }


    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Image getMyIcon() {
        return myIcon;
    }
}
