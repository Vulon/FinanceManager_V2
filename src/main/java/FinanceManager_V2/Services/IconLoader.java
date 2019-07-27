package FinanceManager_V2.Services;


import FinanceManager_V2.MainApplication;
import javafx.scene.image.Image;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

@Service
public class IconLoader {
    private static int ESTIMATE_COUNT = 24;

    public ArrayList<Image> icons;

    public void loadIcons(){
        icons = new ArrayList<>(24);
        String template = "icons/%d.png";
        for(int i = 0; i < ESTIMATE_COUNT; i++){
            try {
                String filename = String.format(template, i);
                File file = new File(MainApplication.class.getClassLoader().getResource(filename).getFile());
                Image image = new Image(file.toURI().toString());

                icons.add(image);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
