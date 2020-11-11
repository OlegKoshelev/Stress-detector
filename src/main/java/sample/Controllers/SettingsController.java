package sample.Controllers;

import sample.DataGetting.CvUtils;
import sample.DataSaving.SettingsSaving.*;
import sample.InitialDataSetting.Camera.CountCameras;
import sample.InitialDataSetting.Camera.Resolution;
import sample.InitialDataSetting.Graph.GraphType;
import sample.InitialDataSetting.Substrate.BiaxialModulus;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import sample.Utils.ImageUtils;


import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;


public class SettingsController implements Initializable {
    //FXML fields
    @FXML
    private MenuButton menuCameraButton;
    @FXML
    private MenuButton menuResolutionButton;
    @FXML
    private MenuButton menuBiaxialModulusButton;
    @FXML
    private MenuButton menuGraphTypeButton;
    @FXML
    private ImageView imageView;
    @FXML
    private CheckBox hsvMode;
    @FXML
    private CheckBox setYourBiaxialModule;
    @FXML
    private TextField fps;
    @FXML
    private TextField additionalBiaxialModule;
    @FXML
    private TextField thickness;
    @FXML
    private TextField rotationTime;
    //min and max TextFields
    @FXML
    private TextField hueMin;
    @FXML
    private TextField hueMax;
    @FXML
    private TextField saturationMin;
    @FXML
    private TextField saturationMax;
    @FXML
    private TextField valueMin;
    @FXML
    private TextField valueMax;
    @FXML
    private ColorPicker line;
    @FXML
    private ColorPicker grid;
    @FXML
    private Button apply;
    @FXML
    private Button ok;
    @FXML
    private Label countors;
    // Usual fields
    SettingsData settingsData = SettingsData.getInstance();
    private CameraCustomizations cameraCustomizations = CameraCustomizations.getInstance();
    private SubstrateCustomizations substrateCustomizations = SubstrateCustomizations.getInstance();
    private GraphCustomizations graphCustomizations = GraphCustomizations.getInstance();
    private TextField[] hsvFields = new TextField[10];
    private LinkedBlockingQueue<Mat> inputQueue = new LinkedBlockingQueue<>(); // images from cameras
    private CountCameras countCameras = new CountCameras(); // use for determine cameras count and turning on camera
    private Thread camerasThread; // use for display images from cameras
    //Boolean fields
    private boolean work = true; // while work is true camera is working
    private boolean hsvView = false; // display hsv fields

    @FXML
    public void checkHsvMode() {
        if (hsvMode.isSelected()) {
            for (TextField field : hsvFields) {
                field.setDisable(false);
            }
            hsvView = true;
        } else {
            for (TextField field : hsvFields) {
                field.setDisable(true);
            }
            hsvView = false;
        }
    }

    @FXML
    public void checkSetYourBiaxialModule() {
        if (setYourBiaxialModule.isSelected()) {
            menuBiaxialModulusButton.setDisable(true);
            menuBiaxialModulusButton.setText("Biaxial modulus (GPa)");
            substrateCustomizations.setBiaxialModulus(null);
            additionalBiaxialModule.setDisable(false);
        } else {
            additionalBiaxialModule.setDisable(true);
            additionalBiaxialModule.setText("");
            substrateCustomizations.setAdditionalBiaxialModule(0);
            menuBiaxialModulusButton.setDisable(false);
        }
    }

    @FXML
    public void applyAction() {
        SettingsTransfer.transferFromCustomizationsToSettings(settingsData,cameraCustomizations,substrateCustomizations,graphCustomizations);
    }

    @FXML
    public void okAction() throws IOException {
        Stage stage = (Stage) ok.getScene().getWindow();
        shutdown();
        stage.close();
        SettingsTransfer.writeToSettingsFile(settingsData);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SettingsTransfer.transferFromSettingsToCustomizations(settingsData,cameraCustomizations,substrateCustomizations,graphCustomizations);

        hsvFields = new TextField[]{hueMin, saturationMin, valueMin, hueMax, saturationMax, valueMax,
                fps, additionalBiaxialModule, thickness, rotationTime};

        for (TextField field : hsvFields) {
            setDisable(field);
            initializeNumberTextField(field);
        }
        setResolutionItems();
        setCameraItems();
        setBiaxialModulusItems();
        setMenuGraphTypeItems();
        setColorPickers();
    }

    private void setDisable(TextField field){
        if (field.equals(hueMin) || field.equals(hueMax) || field.equals(saturationMin) || field.equals(saturationMax) ||
                field.equals(valueMin) || field.equals(valueMax)) {
            field.setDisable(true);
        }
        if (field.equals(additionalBiaxialModule)){
            disableAdditionalBiaxialModule();
        }
    }


    private void initializeNumberTextField(TextField field) {
        setText(field);
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    field.setText(newValue.replaceAll("[^\\d]", ""));
                } else {
                    if (!newValue.isEmpty()) {
                        int magnitude = Integer.parseInt(newValue);
                        magnitude = checkThreshold(field, magnitude);
                        newValue = String.valueOf(magnitude);
                        setCustomizationField(field, magnitude);
                    }
                    field.setText(newValue);
                }
            }
        });
    }

    private void setResolutionItems() {
        menuResolutionButton.setText(cameraCustomizations.getResolution().getName());
        for (Resolution resolution : Resolution.values()) {
            MenuItem item = new MenuItem();
            item.setText(resolution.getName());
            menuResolutionButton.getItems().add(item);
            item.setOnAction(event -> {
                menuResolutionButton.setText(resolution.getName());
                cameraCustomizations.setResolution(resolution);
                countCameras.newResolution();
            });
        }
    }


    private void setCameraItems() {
        setCamerasThread();
        countCameras.setInputQueue(inputQueue);
        for (Map.Entry<MenuItem, Integer> pair : (countCameras.getItemsMap()).entrySet()) {
            MenuItem item = pair.getKey();
            int device = pair.getValue();
            menuCameraButton.getItems().add(item);
            item.setOnAction(event -> {
                if (countCameras.getCameraLauncher() == null) {
                    countCameras.start();
                }
                if (camerasThread.getState() != Thread.State.RUNNABLE)
                    camerasThread.start();
                menuCameraButton.setText(item.getText());
                countCameras.newCamStart();
                cameraCustomizations.setCameraId(device);
            });
        }
    }

    private void setBiaxialModulusItems() {
        for (BiaxialModulus module : BiaxialModulus.values()) {
            MenuItem item = new MenuItem();
            item.setText(module.getName());
            menuBiaxialModulusButton.getItems().add(item);
            item.setOnAction(event -> {
                menuBiaxialModulusButton.setText(module.getName());
                substrateCustomizations.setBiaxialModulus(module);
                substrateCustomizations.setAdditionalBiaxialModule(0);
                additionalBiaxialModule.setText("");
            });
        }
    }

    private void setMenuGraphTypeItems() {
        menuGraphTypeButton.setText(graphCustomizations.getType().getName());
        for (GraphType type : GraphType.values()) {
            MenuItem item = new MenuItem();
            item.setText(type.getName());
            menuGraphTypeButton.getItems().add(item);
            item.setOnAction(event -> {
                menuGraphTypeButton.setText(type.getName());
                graphCustomizations.setType(type);
            });
        }
    }

    private void setColorPickers() {
        line.setValue(graphCustomizations.getLine());
        grid.setValue(graphCustomizations.getGrid());
        line.setOnAction(event -> graphCustomizations.setLine(line.getValue()));
        grid.setOnAction(event -> graphCustomizations.setGrid(grid.getValue()));
    }

    private int checkThreshold(TextField field, int magnitude) {
        if (field.equals(fps)) {
            if (magnitude > 120)
                return 120;
            if (magnitude == 0)
                return 1;
        }
        if (field.equals(hueMin) || field.equals(hueMax) || field.equals(saturationMin) || field.equals(saturationMax) ||
                field.equals(valueMin) || field.equals(valueMax)) {
            if (magnitude > 255)
                return 255;
        }
        return magnitude;
    }

    private void setCustomizationField(TextField field, int magnitude) {
        // setting customizationCamera fields
        if (field.equals(hueMin))
            cameraCustomizations.setHueMin(magnitude);
        if (field.equals(saturationMin))
            cameraCustomizations.setSaturationMin(magnitude);
        if (field.equals(valueMin))
            cameraCustomizations.setValueMin(magnitude);
        if (field.equals(hueMax))
            cameraCustomizations.setHueMax(magnitude);
        if (field.equals(saturationMax))
            cameraCustomizations.setSaturationMax(magnitude);
        if (field.equals(valueMax))
            cameraCustomizations.setValueMax(magnitude);
        if (field.equals(fps))
            cameraCustomizations.setFps(magnitude);
        // setting substrateCustomizations fields
        if (field.equals(thickness))
            substrateCustomizations.setThickness(magnitude);
        if (field.equals(rotationTime))
            substrateCustomizations.setRotationTime(magnitude);
        if (field.equals(additionalBiaxialModule))
            substrateCustomizations.setAdditionalBiaxialModule(magnitude);
    }

    private void setCamerasThread() {
        camerasThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (work) {
                    Mat nextMat = inputQueue.poll();
                    if (nextMat == null) continue;
                    Image image = null;
                    if (hsvView){
                        Platform.runLater(() -> countors.setText("" + CvUtils.getContours(nextMat)));
                        image = ImageUtils.getHsvImage(nextMat, cameraCustomizations);
                    }

                    else
                        image = ImageUtils.getPrimaryImage(nextMat);
                    Image finalImage = image;
                    Platform.runLater(() -> imageView.setImage(finalImage));
                }
                System.out.println("work has done");
            }
        });
    }

    private void setText(TextField field) {
        if (field.equals(hueMin))
            field.setText(String.valueOf(cameraCustomizations.getHueMin()));
        if (field.equals(saturationMin))
            field.setText(String.valueOf(cameraCustomizations.getSaturationMin()));
        if (field.equals(valueMin))
            field.setText(String.valueOf(cameraCustomizations.getValueMin()));
        if (field.equals(hueMax))
            field.setText(String.valueOf(cameraCustomizations.getHueMax()));
        if (field.equals(saturationMax))
            field.setText(String.valueOf(cameraCustomizations.getSaturationMax()));
        if (field.equals(valueMax))
            field.setText(String.valueOf(cameraCustomizations.getValueMax()));
        if (field.equals(fps))
            field.setText(String.valueOf(cameraCustomizations.getFps()));
        if (field.equals(thickness))
            field.setText(String.valueOf(substrateCustomizations.getThickness()));
        if (field.equals(rotationTime))
            field.setText(String.valueOf(substrateCustomizations.getRotationTime()));
    }

    private void disableAdditionalBiaxialModule() {
        if (substrateCustomizations.getBiaxialModulus() == null) {
            menuBiaxialModulusButton.setDisable(true);
            additionalBiaxialModule.setText(String.valueOf(substrateCustomizations.getAdditionalBiaxialModule()));
            setYourBiaxialModule.setSelected(true);
        } else {
            additionalBiaxialModule.setDisable(true);
            menuBiaxialModulusButton.setText(substrateCustomizations.getBiaxialModulus().getName());
        }
    }

    public void shutdown() {
        countCameras.stop();
        System.out.println("close");
        work = false;
    }
}
