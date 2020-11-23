package sample.AdditionalUtils;

import javafx.scene.control.CheckMenuItem;

public class MainControllerUtils {
    public static boolean setAutoRanging(CheckMenuItem autoRanging){
        autoRanging.setSelected(true);
        return true;
    }
    public  static boolean removeAutoRanging (CheckMenuItem autoRanging){
        autoRanging.setSelected(false);
        return false;
    }


}
