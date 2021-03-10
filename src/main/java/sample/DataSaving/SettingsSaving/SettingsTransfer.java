package sample.DataSaving.SettingsSaving;


import com.google.gson.Gson;
import javafx.scene.paint.Color;
import sample.Controllers.StaticSettingsController;
import sample.DataSaving.SettingsSaving.DynamicSettings.CameraCustomizations;
import sample.DataSaving.SettingsSaving.DynamicSettings.GraphCustomizations;
import sample.DataSaving.SettingsSaving.DynamicSettings.SubstrateCustomizations;
import sample.DataSaving.SettingsSaving.StaticSettings.SetupCustomizations;
import sample.Program;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class SettingsTransfer {


    public static void getFullSettingsFromFile() throws IOException, URISyntaxException {
        Gson gson = new Gson();
        File jarPath = new File(Program.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI());
        System.out.println(jarPath.getPath());
        System.out.println(jarPath.getParentFile().getPath());
        FileInputStream inputStream = new FileInputStream(jarPath.getParentFile().getPath() + "\\Settings.json");
        //InputStream in = SettingsTransfer.class.getResourceAsStream("/Settings.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        // Reader reader = Files.newBufferedReader(Paths.get("/Settings.json"));
        SettingsFileObject settingsFileObject = gson.fromJson(reader, SettingsFileObject.class);
        if (settingsFileObject != null) {
            SettingsData.getInstance().setCameraId(settingsFileObject.getCameraId());
            SettingsData.getInstance().setFps(settingsFileObject.getFps());
            SettingsData.getInstance().setResolution(settingsFileObject.getResolution());
            SettingsData.getInstance().setHueMax(settingsFileObject.getHueMax());
            SettingsData.getInstance().setHueMin(settingsFileObject.getHueMin());
            SettingsData.getInstance().setSaturationMax(settingsFileObject.getSaturationMax());
            SettingsData.getInstance().setSaturationMin(settingsFileObject.getSaturationMin());
            SettingsData.getInstance().setValueMax(settingsFileObject.getValueMax());
            SettingsData.getInstance().setValueMin(settingsFileObject.getValueMin());
            SettingsData.getInstance().setBiaxialModulus(settingsFileObject.getBiaxialModulus());
            SettingsData.getInstance().setAdditionalBiaxialModule(settingsFileObject.getAdditionalBiaxialModule());
            SettingsData.getInstance().setThickness(settingsFileObject.getThickness());
            SettingsData.getInstance().setRotationTime(settingsFileObject.getRotationTime());
            SettingsData.getInstance().setType(settingsFileObject.getType());
            SettingsData.getInstance().setLine(Color.web(settingsFileObject.getLine().toString()));
            SettingsData.getInstance().setGrid(Color.web(settingsFileObject.getGrid().toString()));
            SettingsData.getInstance().setAngle(settingsFileObject.getAngle());
            SettingsData.getInstance().setDistance(settingsFileObject.getDistance());
            SettingsData.getInstance().setD0(settingsFileObject.getD0());
        }
        reader.close();
    }

    public static void writeToSettingsFile() throws IOException, URISyntaxException {
        Gson gson = new Gson();
        File jarPath = new File(Program.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI());
        FileOutputStream outputStream = new FileOutputStream(jarPath.getParentFile().getPath() + "\\Settings.json");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        // Writer writer = Files.newBufferedWriter(Paths.get("Settings.json"));
        gson.toJson(SettingsData.getInstance(), writer);
        writer.close();
    }


    public static void transferFromDynamicOptionsToFullSettings(
            CameraCustomizations cameraCustomizations,
            GraphCustomizations graphCustomizations,
            SubstrateCustomizations substrateCustomizations) {

        SettingsData.getInstance().setCameraId(cameraCustomizations.getCameraId());
        SettingsData.getInstance().setFps(cameraCustomizations.getFps());
        SettingsData.getInstance().setResolution(cameraCustomizations.getResolution());
        SettingsData.getInstance().setHueMax(cameraCustomizations.getHueMax());
        SettingsData.getInstance().setHueMin(cameraCustomizations.getHueMin());
        SettingsData.getInstance().setSaturationMax(cameraCustomizations.getSaturationMax());
        SettingsData.getInstance().setSaturationMin(cameraCustomizations.getSaturationMin());
        SettingsData.getInstance().setValueMax(cameraCustomizations.getValueMax());
        SettingsData.getInstance().setValueMin(cameraCustomizations.getValueMin());
        SettingsData.getInstance().setBiaxialModulus(substrateCustomizations.getBiaxialModulus());
        SettingsData.getInstance().setAdditionalBiaxialModule(substrateCustomizations.getAdditionalBiaxialModule());
        SettingsData.getInstance().setThickness(substrateCustomizations.getThickness());
        SettingsData.getInstance().setRotationTime(substrateCustomizations.getRotationTime());
        SettingsData.getInstance().setType(graphCustomizations.getType());
        SettingsData.getInstance().setLine(graphCustomizations.getLine());
        SettingsData.getInstance().setGrid(graphCustomizations.getGrid());
    }


    public static void transferFromFullSettingsToDynamicOptions(
            CameraCustomizations cameraCustomizations,
            GraphCustomizations graphCustomizations,
            SubstrateCustomizations substrateCustomizations) {

        cameraCustomizations.setCameraId(SettingsData.getInstance().getCameraId());
        cameraCustomizations.setFps(SettingsData.getInstance().getFps());
        cameraCustomizations.setResolution(SettingsData.getInstance().getResolution());
        cameraCustomizations.setHueMax(SettingsData.getInstance().getHueMax());
        cameraCustomizations.setHueMin(SettingsData.getInstance().getHueMin());
        cameraCustomizations.setSaturationMax(SettingsData.getInstance().getSaturationMax());
        cameraCustomizations.setSaturationMin(SettingsData.getInstance().getSaturationMin());
        cameraCustomizations.setValueMax(SettingsData.getInstance().getValueMax());
        cameraCustomizations.setValueMin(SettingsData.getInstance().getValueMin());
        substrateCustomizations.setAdditionalBiaxialModule(SettingsData.getInstance().getAdditionalBiaxialModule());
        substrateCustomizations.setBiaxialModulus(SettingsData.getInstance().getBiaxialModulus());
        substrateCustomizations.setRotationTime(SettingsData.getInstance().getRotationTime());
        substrateCustomizations.setThickness(SettingsData.getInstance().getThickness());
        graphCustomizations.setType(SettingsData.getInstance().getType());
        graphCustomizations.setLine(SettingsData.getInstance().getLine());
        graphCustomizations.setGrid(SettingsData.getInstance().getGrid());
    }


    public static void transferFromStaticOptionsToFullSettings( SetupCustomizations setupCustomizations) {
        SettingsData.getInstance().setAngle(setupCustomizations.getAngle());
        SettingsData.getInstance().setDistance(setupCustomizations.getDistance());
        SettingsData.getInstance().setD0(setupCustomizations.getD0());
    }

    public static void transferFromFullSettingsToStaticOptions(SetupCustomizations setupCustomizations) {
        setupCustomizations.setAngle(SettingsData.getInstance().getAngle());
        setupCustomizations.setDistance(SettingsData.getInstance().getDistance());
        setupCustomizations.setD0(SettingsData.getInstance().getD0());
    }


}
