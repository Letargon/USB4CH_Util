package com.iu3.usb4ch_util;

import com.iu3.usb4ch_util.configs.SRConfig;
import com.iu3.usb4ch_util.pipeline.PipelineLauncher;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ConverterWinController implements Initializable {

    public static PipelineLauncher PLL;
    public static SRConfig CONF;
    public static File INIT_DIR;

    public static Stage MAIN_STAGE;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    TextField filePath;

    @FXML
    TextField dirPath;

    @FXML
    RadioButton fw4Radio;

    @FXML
    RadioButton mseedRadio;

    @FXML
    private void openFile() {
        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
        fileChooser.setTitle("Открыть файл");//Заголовок диалога
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(".bin", "*.bin")
        );
        fileChooser.setInitialDirectory(INIT_DIR);
        File file = fileChooser.showOpenDialog(MAIN_STAGE);

        if (file != null) {
            INIT_DIR = file.getParentFile();
            filePath.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void openDir() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Выбрать папку:");
        chooser.setInitialDirectory(INIT_DIR);
        File dir = chooser.showDialog(MAIN_STAGE);

        if (dir != null) {
            dirPath.setText(dir.getAbsolutePath());
        }
    }

    @FXML
    private void convertAction() {
        File file = new File(filePath.getText());
        File dir = new File(dirPath.getText());

        if (file.isFile() && dir.isDirectory()) {
            String[] sp = file.getAbsolutePath().split("\\.");
            String ext = sp[sp.length - 1];
            if (sp.length > 0 && ext.equals("bin")) {

                CONF.setValue("BinFread", "InputExt", SRConfig.toSRString(ext));

                String name = file.getName().substring(0, (file.getName().length() - ext.length() - 1));
                //Path fileP = Paths.get(file.getParent()); 
                //Path dirP = Paths.get(dir.getAbsolutePath());              
                //CONF.setValue("BinFread", "InputPrefix", SRConfig.toSRString(".\\"+dirP.relativize(fileP).toString()+"\\"+name));
                CONF.setValue("BinFread", "InputPrefix", SRConfig.toSRString(file.getParent() + "\\" + name));

                try {
                    if (fw4Radio.isSelected()) {
                        CONF.setValue("Bin2Asc", "OutputPrefix", name);
                        PLL.setFw4Converter();
                    } else if (mseedRadio.isSelected()) {
                        PLL.setMseedConverter();
                    } else {
                        throw new IOException();
                    }
                    PLL.setSrDir(dir.getAbsolutePath());
                    PLL.runPipeLine(true);
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Не выбран формат для конвертации");
                    alert.setContentText("Выберите формат для конвертации");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Тип файла не поддерживается");
                alert.setContentText("Выберите другой файл");
                alert.showAndWait();
            }
        }
    }

}
