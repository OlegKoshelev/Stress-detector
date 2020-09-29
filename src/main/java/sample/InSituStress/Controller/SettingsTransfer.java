package sample.InSituStress.Controller;


import InitialDataSetting.Camera.CameraCustomizations;
import com.google.gson.Gson;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;


public class SettingsTransfer {

    public static void readFromSettingsFile(SettingsData settingsData) throws IOException {
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get("Settings.json"));
        SettingsFileObject settingsFileObject = gson.fromJson(reader, SettingsFileObject.class);
        if (settingsFileObject != null) {
            settingsData.setCameraId(settingsFileObject.getCameraId());
            settingsData.setFps(settingsFileObject.getFps());
            settingsData.setResolution(settingsFileObject.getResolution());
            settingsData.setHueMax(settingsFileObject.getHueMax());
            settingsData.setHueMin(settingsFileObject.getHueMin());
            settingsData.setSaturationMax(settingsFileObject.getSaturationMax());
            settingsData.setSaturationMin(settingsFileObject.getSaturationMin());
            settingsData.setValueMax(settingsFileObject.getValueMax());
            settingsData.setValueMin(settingsFileObject.getValueMin());
            settingsData.setBiaxialModulus(settingsFileObject.getBiaxialModulus());
            settingsData.setAdditionalBiaxialModule(settingsFileObject.getAdditionalBiaxialModule());
            settingsData.setThickness(settingsFileObject.getThickness());
            settingsData.setRotationTime(settingsFileObject.getRotationTime());
            settingsData.setType(settingsFileObject.getType());
            settingsData.setLine(Color.web(settingsFileObject.getLine().toString()));
            settingsData.setGrid(Color.web(settingsFileObject.getGrid().toString()));
        }
        reader.close();
    }

    public static void writeToSettingsFile(SettingsData settingsData) throws IOException {
        Gson gson = new Gson();
        Writer writer = Files.newBufferedWriter(Paths.get("Settings.json"));
        gson.toJson(settingsData,writer);
        writer.close();
    }

    public static void transferFromCustomizationsToSettings(SettingsData settingsData,
                                                            CameraCustomizations cameraCustomizations,
                                                            SubstrateCustomizations substrateCustomizations,
                                                            GraphCustomizations graphCustomizations) {

        settingsData.setCameraId(cameraCustomizations.getCameraId());
        settingsData.setFps(cameraCustomizations.getFps());
        settingsData.setResolution(cameraCustomizations.getResolution());
        settingsData.setHueMax(cameraCustomizations.getHueMax());
        settingsData.setHueMin(cameraCustomizations.getHueMin());
        settingsData.setSaturationMax(cameraCustomizations.getSaturationMax());
        settingsData.setSaturationMin(cameraCustomizations.getSaturationMin());
        settingsData.setValueMax(cameraCustomizations.getValueMax());
        settingsData.setValueMin(cameraCustomizations.getValueMin());
        settingsData.setBiaxialModulus(substrateCustomizations.getBiaxialModulus());
        settingsData.setAdditionalBiaxialModule(substrateCustomizations.getAdditionalBiaxialModule());
        settingsData.setThickness(substrateCustomizations.getThickness());
        settingsData.setRotationTime(substrateCustomizations.getRotationTime());
        settingsData.setType(graphCustomizations.getType());
        settingsData.setLine(graphCustomizations.getLine());
        settingsData.setGrid(graphCustomizations.getGrid());
    }

    public static void transferFromSettingsToCustomizations(SettingsData settingsData,
                                                            CameraCustomizations cameraCustomizations,
                                                            SubstrateCustomizations substrateCustomizations,
                                                            GraphCustomizations graphCustomizations)  {

        cameraCustomizations.setCameraId(settingsData.getCameraId());
        cameraCustomizations.setFps(settingsData.getFps());
        cameraCustomizations.setResolution(settingsData.getResolution());
        cameraCustomizations.setHueMax(settingsData.getHueMax());
        cameraCustomizations.setHueMin(settingsData.getHueMin());
        cameraCustomizations.setSaturationMax(settingsData.getSaturationMax());
        cameraCustomizations.setSaturationMin(settingsData.getSaturationMin());
        cameraCustomizations.setValueMax(settingsData.getValueMax());
        cameraCustomizations.setValueMin(settingsData.getValueMin());
        substrateCustomizations.setAdditionalBiaxialModule(settingsData.getAdditionalBiaxialModule());
        substrateCustomizations.setBiaxialModulus(settingsData.getBiaxialModulus());
        substrateCustomizations.setRotationTime(settingsData.getRotationTime());
        substrateCustomizations.setThickness(settingsData.getThickness());
        graphCustomizations.setType(settingsData.getType());
        graphCustomizations.setLine(settingsData.getLine());
        graphCustomizations.setGrid(settingsData.getGrid());
    }
}
