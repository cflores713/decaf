package sample;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.nio.file.Path;

public class EditorController {

    @FXML
    private TextArea textArea;

    public StringProperty textProperty() {
        return textArea.textProperty();
    }

}
