package sample;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Node;

import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;

public class Controller {

    private StringProperty textUponOpen;
    private StringProperty textBeforeExit;
    Path path;
    public static int windowNum;
    private StringProperty textArea;

    private StringProperty currentText;

    @FXML
    private TabPane fileTabPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem newFilebtn;

    @FXML
    private MenuItem newProjbtn;

    @FXML
    private MenuItem openbtn;

    @FXML
    private MenuItem savebtn;

    @FXML
    private MenuItem saveAsbtn;

    @FXML
    private MenuItem closebtn;

    @FXML
    private MenuItem cutbtn;

    @FXML
    private MenuItem copybtn;

    @FXML
    private MenuItem pastebtn;

    @FXML
    private MenuItem buildbtn;

    @FXML
    private MenuItem runbtn;


    public void initialize() throws IOException{
        newTab();
    }

    @FXML
    private void newTab() throws IOException{
        Tab tab = new Tab("Untitled");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("textEditor.fxml"));
        tab.setContent(loader.load());
        EditorController controller = loader.getController();

        tab.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                currentText = controller.textProperty();
            }
        });

        fileTabPane.getTabs().add(tab);
        fileTabPane.getSelectionModel().select(tab);
    }

    @FXML
    void build(ActionEvent event) {

    }

    @FXML
    void close(ActionEvent event) {

    }

    @FXML
    void copy(ActionEvent event) {

    }

    @FXML
    void createProj(ActionEvent event) {

    }

    @FXML
    void cut(ActionEvent event) {

    }

    @FXML
    void open(ActionEvent event) {
        FileChooser jfc = new FileChooser();//directs user to home directory
        jfc.setTitle("Select a java file");//dialog for selecting file will say choose file
        jfc.setInitialDirectory(new File("."));
        jfc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Java (.java)", "*.java"));
        File returnValue = jfc.showOpenDialog(null);

        if (returnValue != null) {
            Path path = returnValue.toPath();//gets the path of the file
            try {
                currentText.set(String.join("\n", Files.readAllLines(path)));
            }
            catch(Exception evt) {
                Alert warning = new Alert(Alert.AlertType.ERROR);
                warning.setTitle("Load Error");
                warning.setContentText("Unable to load file " + path);
                warning.showAndWait();
            }
            fileTabPane.getSelectionModel().getSelectedItem().setText(path.getFileName().toString());
        }
        //textUponOpen = currentText;
    }

    @FXML
    void paste(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {
        Alert warning = new Alert(Alert.AlertType.ERROR);
        warning.setTitle("Load Error");
        warning.setContentText("Unable to load file " + path.toString());
        warning.showAndWait();
    }

    @FXML
    void saveAs(ActionEvent event) {

    }
}
