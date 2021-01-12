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
        File jarPath=new File(Program.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI());
        System.out.println(jarPath.getPath());
        System.out.println(jarPath.getParentFile().getPath());
        FileInputStream inputStream = new FileInputStream(jarPath.getParentFile().getPath()+"\\Settings.json");
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
        }
        reader.close();
    }

    public static void writeToSettingsFile() throws IOException, URISyntaxException {
        Gson gson = new Gson();
        File jarPath=new File(Program.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI());
        FileOutputStream outputStream = new FileOutputStream(jarPath.getParentFile().getPath()+"\\Settings.json");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
       // Writer writer = Files.newBufferedWriter(Paths.get("Settings.json"));
        gson.toJson(SettingsData.getInstance(),writer);
        writer.close();
    }

    public static void transferFromDynamicOptionsToFullSettings() {

        SettingsData.getInstance().setCameraId(CameraCustomizations.getInstance().getCameraId());
        SettingsData.getInstance().setFps(CameraCustomizations.getInstance().getFps());
        SettingsData.getInstance().setResolution(CameraCustomizations.getInstance().getResolution());
        SettingsData.getInstance().setHueMax(CameraCustomizations.getInstance().getHueMax());
        SettingsData.getInstance().setHueMin(CameraCustomizations.getInstance().getHueMin());
        SettingsData.getInstance().setSaturationMax(CameraCustomizations.getInstance().getSaturationMax());
        SettingsData.getInstance().setSaturationMin(CameraCustomizations.getInstance().getSaturationMin());
        SettingsData.getInstance().setValueMax(CameraCustomizations.getInstance().getValueMax());
        SettingsData.getInstance().setValueMin(CameraCustomizations.getInstance().getValueMin());
        SettingsData.getInstance().setBiaxialModulus(SubstrateCustomizations.getInstance().getBiaxialModulus());
        SettingsData.getInstance().setAdditionalBiaxialModule(SubstrateCustomizations.getInstance().getAdditionalBiaxialModule());
        SettingsData.getInstance().setThickness(SubstrateCustomizations.getInstance().getThickness());
        SettingsData.getInstance().setRotationTime(SubstrateCustomizations.getInstance().getRotationTime());
        SettingsData.getInstance().setType(GraphCustomizations.getInstance().getType());
        SettingsData.getInstance().setLine(GraphCustomizations.getInstance().getLine());
        SettingsData.getInstance().setGrid(GraphCustomizations.getInstance().getGrid());
    }

    public static void transferFromFullSettingsToDynamicOptions()  {

        CameraCustomizations.getInstance().setCameraId(SettingsData.getInstance().getCameraId());
        CameraCustomizations.getInstance().setFps(SettingsData.getInstance().getFps());
        CameraCustomizations.getInstance().setResolution(SettingsData.getInstance().getResolution());
        CameraCustomizations.getInstance().setHueMax(SettingsData.getInstance().getHueMax());
        CameraCustomizations.getInstance().setHueMin(SettingsData.getInstance().getHueMin());
        CameraCustomizations.getInstance().setSaturationMax(SettingsData.getInstance().getSaturationMax());
        CameraCustomizations.getInstance().setSaturationMin(SettingsData.getInstance().getSaturationMin());
        CameraCustomizations.getInstance().setValueMax(SettingsData.getInstance().getValueMax());
        CameraCustomizations.getInstance().setValueMin(SettingsData.getInstance().getValueMin());
        SubstrateCustomizations.getInstance().setAdditionalBiaxialModule(SettingsData.getInstance().getAdditionalBiaxialModule());
        SubstrateCustomizations.getInstance().setBiaxialModulus(SettingsData.getInstance().getBiaxialModulus());
        SubstrateCustomizations.getInstance().setRotationTime(SettingsData.getInstance().getRotationTime());
        SubstrateCustomizations.getInstance().setThickness(SettingsData.getInstance().getThickness());
        GraphCustomizations.getInstance().setType(SettingsData.getInstance().getType());
        GraphCustomizations.getInstance().setLine(SettingsData.getInstance().getLine());
        GraphCustomizations.getInstance().setGrid(SettingsData.getInstance().getGrid());
    }

    public static void transferFromStaticOptionsToFullSettings() {
        SettingsData.getInstance().setAngle(SetupCustomizations.getInstance().getAngle());
        SettingsData.getInstance().setDistance(SetupCustomizations.getInstance().getDistance());
    }

    public static void transferFromFullSettingsToStaticOptions()  {
        SetupCustomizations.getInstance().setAngle(SettingsData.getInstance().getAngle());
        SetupCustomizations.getInstance().setDistance(SettingsData.getInstance().getDistance());
    }


}
