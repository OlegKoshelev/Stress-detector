package sample.AdditionalUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.DataSaving.SettingsSaving.StaticSettings.SetupCustomizations;

public class StaticSettingsControllerUtils {


    public static void  textFieldsCustomization (TextField angle, TextField distance){
        angle.setText(String.valueOf(SetupCustomizations.getInstance().getAngle()));
        distance.setText(String.valueOf(SetupCustomizations.getInstance().getDistance()));
        StaticSettingsControllerUtils.addListenerToAngleTextField(angle);
        StaticSettingsControllerUtils.addListenerToDistanceTextField(distance);
    }

     public static void uploadImage(ImageView imageView){
        Image image = new Image(StaticSettingsControllerUtils.class.getResourceAsStream("/MOSS.png"));
        imageView.setImage(image);
    }

    public static void addListenerToAngleTextField (TextField field) {
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    field.setText(newValue.replaceAll("[^\\d]", ""));
                } else {
                    if (newValue.isEmpty()){
                        newValue = "0";
                    }
                    field.setText(newValue);
                    pushAngleTextField(field);
                }
            }
        });
    }

    public static void addListenerToDistanceTextField(TextField field) {
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("[1-9]+\\.*\\d+|0{1}\\.{1}\\d+")) {
                    String subString = newValue;
                    subString =  subString.replaceAll("[^\\d\\.]", "");
                    subString = subString.replaceFirst("\\.", ",");
                    subString = subString.replaceAll("\\.", "");
                    subString = subString.replaceFirst(",", "\\.");
                    subString = subString.replaceAll("^\\.","0.");
                    field.setText(subString);
                } else {
                    if (newValue.isEmpty()){
                        newValue = "0.0";
                    }
                    field.setText(newValue);
                    pushDistanceTextField(field);
                }
            }
        });
    }

    public static void pushDistanceTextField(TextField field) {
        double result = Double.parseDouble(field.getText());
        SetupCustomizations.getInstance().setDistance(result);
    }

    public static void pushAngleTextField(TextField field) {
        int result = Integer.parseInt(field.getText());
        SetupCustomizations.getInstance().setAngle(result);
    }

}
