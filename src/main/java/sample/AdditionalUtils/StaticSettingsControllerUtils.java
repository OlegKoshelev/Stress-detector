package sample.AdditionalUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.DataSaving.SettingsSaving.StaticSettings.SetupCustomizations;
import sample.Enums.SetupCustomizationsEnum;

public class StaticSettingsControllerUtils {


    public static void textFieldsCustomization(TextField angle,
                                               TextField distance, TextField d0,
                                               SetupCustomizations setupCustomizations) {
        angle.setText(String.valueOf(setupCustomizations.getAngle()));
        distance.setText(String.valueOf(setupCustomizations.getDistance()));
        d0.setText(String.valueOf(setupCustomizations.getD0()));
        StaticSettingsControllerUtils.addListenerToTextField(angle, setupCustomizations,SetupCustomizationsEnum.angle);
        StaticSettingsControllerUtils.addListenerToTextField(distance, setupCustomizations,SetupCustomizationsEnum.distance);
        StaticSettingsControllerUtils.addListenerToTextField(d0, setupCustomizations,SetupCustomizationsEnum.d0);
    }

    public static void uploadImage(ImageView imageView) {
        Image image = new Image(StaticSettingsControllerUtils.class.getResourceAsStream("/MOSS.png"));
        imageView.setImage(image);
    }


    public static void addListenerToTextField(TextField field, SetupCustomizations setupCustomizations, SetupCustomizationsEnum fieldType) {
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                getChoice(field,newValue,setupCustomizations,fieldType);
            }
        });
    }

    private static void getChoice(TextField field, String newValue,
                                  SetupCustomizations setupCustomizations,
                                  SetupCustomizationsEnum fieldType) {
        if (fieldType == SetupCustomizationsEnum.d0 || fieldType == SetupCustomizationsEnum.distance) {
            if (!newValue.matches("[1-9]+0*\\.*\\d+|0{1}\\.{1}\\d+|0")) {
                String subString = newValue;
                subString = subString.replaceAll("[^\\d\\.]", "");
                subString = subString.replaceAll("^0+|^0[1-9]+", "0");
                subString = subString.replaceAll("^0[1-9]+", "0");
                subString = subString.replaceFirst("\\.", ",");
                subString = subString.replaceAll("\\.", "");
                subString = subString.replaceFirst(",", "\\.");
                subString = subString.replaceAll("^\\.", "0.");
                field.setText(subString);
            } else {
                if (newValue.isEmpty()) {
                    newValue = "0.0";
                }
                field.setText(newValue);
                pushValueToTextField(field, setupCustomizations,fieldType);
            }
        }

        if (fieldType == SetupCustomizationsEnum.angle) {
            if (!newValue.matches("\\d*")) {
                field.setText(newValue.replaceAll("[^\\d]", ""));
            } else {
                if (newValue.isEmpty()) {
                    newValue = "0";
                }
                field.setText(newValue);
                pushValueToTextField(field, setupCustomizations,fieldType);
            }
        }
    }


    public static void pushValueToTextField(TextField field,
                                           SetupCustomizations setupCustomizations,
                                           SetupCustomizationsEnum setupCustomizationsEnum) {
        switch (setupCustomizationsEnum) {
            case d0:
                setupCustomizations.setD0(Double.parseDouble(field.getText()));
                break;
            case distance:
                setupCustomizations.setDistance(Double.parseDouble(field.getText()));
                break;
            case angle:
                setupCustomizations.setAngle(Integer.parseInt(field.getText()));
                break;
        }
    }
}
