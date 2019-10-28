package prototypes;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.input.KeyEvent;

import java.awt.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class decaf2 extends Application {
    private final String INITIAL_TEXT = "Lorem ipsum dolor sit "
            + "amet, consectetur adipiscing elit. Nam tortor felis, pulvinar "
            + "in scelerisque cursus, pulvinar at ante. Nulla consequat"
            + "congue lectus in sodales. Nullam eu est a felis ornare "
            + "bibendum et nec tellus. Vivamus non metus tempus augue auctor "
            + "ornare. Duis pulvinar justo ac purus adipiscing pulvinar. "
            + "Integer congue faucibus dapibus. Integer id nisl ut elit "
            + "aliquam sagittis gravida eu dolor. Etiam sit amet ipsum "
            + "sem.";

    String textUponOpen = "";
    String textBeforeExit = "";
    String path = "";
    String text = "";


    public static String getTextOnly(String html){
        String cleanText = "";
        Pattern p = Pattern.compile("<[^>]*>");
        Matcher m = p.matcher(html);
        final StringBuffer userText = new StringBuffer(html.length());

        while(m.find()){
            m.appendReplacement(userText, " ");
        }

        m.appendTail(userText);
        cleanText = userText.toString().trim();
        return cleanText;
    }

    public static void hideOriginalToolBar(final HTMLEditor h)
    {
        h.setVisible(false);
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                Node[] myNodes = h.lookupAll(".tool-bar").toArray(new Node[0]);
                for(Node node : myNodes)
                {
                    node.setVisible(false);
                    node.setManaged(false);
                }
                h.setVisible(true);
            }
        });
    }




    @Override
    public void start(Stage stage) {




        stage.setTitle("HTMLEditor Sample");
        stage.setWidth(500);
        stage.setHeight(500);
        Scene scene = new Scene(new Group());

        //this seems to be the main pop up
        VBox root = new VBox();
        root.setPadding(new Insets(8, 8, 8, 8));
        root.setSpacing(5);
        root.setAlignment(Pos.BOTTOM_LEFT);

        //HTMLEditor laid on top of the vbox (this is the top rectangle
        final HTMLEditor htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(245);
        htmlEditor.setHtmlText(INITIAL_TEXT);

        //this is the lower box
        final TextArea htmlCode = new TextArea();   // new TextArea object to display text
        htmlCode.setWrapText(true);

        String[] fileOptions = {"New", "Open", "Save", "Save As", "Remove File", "Exit"};
        ComboBox fileDropdown = new ComboBox(FXCollections.observableArrayList(fileOptions));
        fileDropdown.setPromptText("File");

        String[] editOptions = {"Cut", "Copy", "Paste"};
        ComboBox editDropdown = new ComboBox(FXCollections.observableArrayList(editOptions));
        editDropdown.setPromptText("Edit");

        String[] compileOptions = {"Build"};
        ComboBox compileDropdown = new ComboBox(FXCollections.observableArrayList(compileOptions));
        compileDropdown.setPromptText("Compile");

        Button exeBtn = new Button("Execute");

        //attempting grid-pane that'll hold the toolbar we actually want
        GridPane gp = new GridPane();
        gp.setMinSize(50,30);
        gp.setPadding(new Insets(10,10,10,10));
        gp.setHgap(5);
        gp.setAlignment(Pos.TOP_LEFT);

        gp.add(fileDropdown, 0, 0);
        gp.add(editDropdown, 1, 0);
        gp.add(compileDropdown, 2, 0);
        gp.add(exeBtn, 3, 0);


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setContent(htmlCode);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(180);



        fileDropdown.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            public void changed(ObservableValue<? extends String> ov,
                                final String oldValue, final String newValue){

                //method to do what you want done based on the dropdown choice
                //openFile();
            }
        });

        Scene myScene = new Scene(gp);
        stage.setScene(myScene);






        htmlEditor.setOnKeyReleased(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                if (isCharacterKeyReleased(event))
                {
//                    System.out.println(htmlEditor.getHtmlText());
                    System.out.printf(event.getText());
                }
            }

            private boolean isCharacterKeyReleased(KeyEvent event)
            {
                // Make custom changes here..
                switch (event.getCode())
                {
                    case ALT:
                    case COMMAND:
                    case CONTROL:
                    case SHIFT:
                    case CAPS:
                    case TAB:
                    case BACK_SPACE:
                    case DELETE:
                    case INSERT:
                    case NUM_LOCK:
                    case ESCAPE:
                        return false;
                    default:
                        return true;
                }
            }

        });




        root.getChildren().addAll(gp, htmlEditor, scrollPane);
        // Adds HTMLEditor object, Button object, and ScrollPane containing TextArea object to VBox object
        scene.setRoot(root);

        stage.setScene(scene);
        //stage.setScene(myScene);
        hideOriginalToolBar(htmlEditor);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
