package com.iu3.usb4ch_util;

import com.iu3.usb4ch_util.configs.SRConfig;
import com.iu3.usb4ch_util.model.FileName;
import com.iu3.usb4ch_util.pipeline.PipelineLauncher;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class FXMLController implements Initializable {

    static Stage MAIN_STAGE;

    PipelineLauncher ppLauncher;
    SRConfig conf;

    File initialDirectory;

    public static void setmainStage(Stage mainStage) {
        FXMLController.MAIN_STAGE = mainStage;
    }

    @FXML
    private Label stationName;

    @FXML
    private Label chFile;
    
    @FXML
    private Label freqLabel;

    private void testTriggered() {

        conf.loadDefaults();
        conf.setValue("Bin2Asc", "OutputPrefix", SRConfig.toSRString(FileName.getFileName("MAGD")));

        ppLauncher.setSrDir(System.getProperty("user.dir"));
        ppLauncher.setTestConfig();
        ppLauncher.runPipeLine(true);
    }

    @FXML
    private void loadDefaults() {
        conf.loadDefaults();
    }

    @FXML
    private void renameStation() {
        TextInputDialog dialog = new TextInputDialog("MAGD");
        dialog.setTitle("Переименовать станцию");
        dialog.setHeaderText("Код станции");
        dialog.setContentText("Введите код станции:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> stationName.textProperty().set(name));
    }

    @FXML
    private void runConfig() {
        //todo
    }

    @FXML
    private void createFile() {
        if (checkDevice()) {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Записать файл");
            chooser.setInitialDirectory(initialDirectory);
            File selectedDirectory = chooser.showDialog(MAIN_STAGE);
            if (selectedDirectory != null) {
                initialDirectory = selectedDirectory;
                String fileName = FileName.getFileName(stationName.textProperty().get());
                fileName = fileName.replaceAll("\\s+", "");
                conf.setValue("Bin2Asc", "OutputPrefix", SRConfig.toSRString(fileName));

                ppLauncher.setSrDir(selectedDirectory.getAbsolutePath());
                ppLauncher.setTimeScope();
                ppLauncher.runPipeLine(true);

                chFile.textProperty().set(selectedDirectory.getAbsolutePath());
            }
        }
    }

    @FXML
    private void openFile() {

        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
        fileChooser.setTitle("Открыть файл");//Заголовок диалога
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All", "*"),
                new FileChooser.ExtensionFilter(".fw4", "*.fw4"),
                new FileChooser.ExtensionFilter(".fw8", "*.fw8"),
                new FileChooser.ExtensionFilter(".asc", "*.asc")
        );
        fileChooser.setInitialDirectory(initialDirectory);
        File file = fileChooser.showOpenDialog(MAIN_STAGE);
        if (file != null) {
            String path = file.getAbsolutePath();
            initialDirectory = file.getParentFile();
            String[] sp = path.split("\\.");
            String ext = sp[sp.length - 1];
            if (sp.length > 0) {
                if (ext.equals("asc") || ext.equals("fw4") || ext.equals("fw8")) {
                    conf.setValue("AsciiViewFw", "DataFileSuffix", SRConfig.toSRString(ext));
                    String name = file.getName().substring(0, (file.getName().length() - ext.length() - 1));
                    conf.setValue("AsciiViewFw", "DataFilePrefix", SRConfig.toSRString(name));

                    ppLauncher.setAsciiViewer();;
                    ppLauncher.setSrDir(file.getParent());
                    ppLauncher.runPipeLine(false);
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Тип файла не поддерживается");
                    alert.setContentText("Выберите другой файл");
                    alert.showAndWait();
                }

            }
        }

    }

    @FXML
    private void setDiscr() {
        List<String> choices = new ArrayList<>();
        for (int i = 1; i < 12; i++) {
            choices.add("s" + i);
        }

        TextInputDialog dialog = new TextInputDialog("120");
        dialog.setTitle("Установить частоту дискретизации");
        dialog.setHeaderText("Частота дискретизации");
        dialog.setContentText("Выберите частоту от 0 до 9000 Гц:");

        TextField tf = dialog.getEditor();

        ValidationSupport vs = new ValidationSupport();
        vs.registerValidator(tf, Validator.<String>createPredicateValidator(
                s -> {
                    try {
                        return 1 < Integer.valueOf(s) && Integer.valueOf(s) < 9000;
                    } catch (NumberFormatException ex) {
                        return false;
                    }
                },
                String.format("Неправильный формат частоты")));

        dialog.showAndWait().ifPresent(s -> {
            if (vs.isInvalid() == false) {
                conf.setValue("Interp", "OutputRate", s);
                freqLabel.textProperty().set(s+" Гц");
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initialDirectory = new File(System.getProperty("user.dir"));
        ppLauncher = new PipelineLauncher(System.getProperty("user.dir") + "/test.ini");
        conf = new SRConfig("test.ini");

        String stationName = conf.getValue("USB4CH_Util", "StationName");
        if (stationName == null) {
            conf.setValue("USB4CH_Util", "StationName", "MAGD");
            this.stationName.textProperty().set("MAGD");
        }
        this.stationName.textProperty().set(stationName);
        
        String freq = conf.getValue("Interp", "OutputRate");
        freqLabel.textProperty().set(freq+" Гц");

    }

    public boolean deviceConnected(String answer) throws IOException {

        String presence = PipelineLauncher.EXE_DIR + "/Presence.exe";
        Process proc = new ProcessBuilder(presence).start();
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            if (line.contains("Err 26")) {
                return false;
            }
            if (answer != null) {
                answer = answer + line + "\n\r";
            }
        }
        return true;
    }

    public boolean checkDevice() {
        String answer = new String();
        try {
            if (!deviceConnected(answer)) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Устройство недоступно");
                alert.setContentText(answer);
                alert.showAndWait();
                return false;
            }
            return true;

        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
