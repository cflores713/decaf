package com;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Node;

import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Controller {

//    private String textUponOpen;
//    private StringProperty textBeforeExit;
//    public static int windowNum;
//    private StringProperty textArea;
    public EditorController currentTab;

    @FXML
    public TreeView<Path> treeView;
    public TreeItem<Path> rootTreeItem;

    @FXML
    public TabPane fileTabPane;

    public void initialize() throws IOException{
        newFile();
    }


    @FXML
    public void newFile() throws IOException{
        Tab tab = new Tab("Untitled");
        newTab(tab);
    }

    public void newTab(Tab tab) throws IOException {
        FXMLLoader loader = new FXMLLoader(new File("./src/main/resources/textEditor.fxml").toURI().toURL());
        tab.setContent(loader.load());
        EditorController controller = loader.getController();

        tab.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                currentTab = controller.getTabModel();
                System.out.println(currentTab.path.toString());
            }
        });

        fileTabPane.getTabs().add(tab);
        fileTabPane.getSelectionModel().select(tab);
    }

    @FXML
    public void open(ActionEvent event) throws IOException {
        FileChooser jfc = new FileChooser();//directs user to home directory
        jfc.setTitle("Select a java file");//dialog for selecting file will say choose file
        jfc.setInitialDirectory(new File("."));
        jfc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Java (.java)", "*.java"));
        File returnValue = jfc.showOpenDialog(null);

        if (returnValue != null) {
            String tabName = fileTabPane.getSelectionModel().getSelectedItem().getText();
            System.out.println(tabName);
            Path path = returnValue.toPath();//gets the path of the file
            String parentFolder = returnValue.getParent();

            try {
                //tab is empty, replace with open file and its contents
                if (tabName.contains("Untitled") && currentTab.text.getLength() == 0){
                    String line = "";
                    FileReader read = new FileReader(returnValue);//reads the file that is being open
                    BufferedReader input = new BufferedReader(read);//shows the input of what the user used
                    currentTab.text.clear();
                    while ((line = input.readLine()) != null) //reads the line
                    {
                        currentTab.text.appendText(line);//appends the text
                        currentTab.text.appendText("\n");//creates a new line
                    }
                    input.close(); //closes the file
                    //currentText.set(String.join("\n", Files.readAllLines(path)));
                    currentTab.path = returnValue.toPath();
                }

            }
            catch(Exception evt) {
                Alert warning = new Alert(Alert.AlertType.ERROR);
                warning.setTitle("Load Error");
                warning.setContentText("Unable to load file " + path);
                warning.showAndWait();
            }
            fileTabPane.getSelectionModel().getSelectedItem().setText(path.getFileName().toString());


//            createTree();
//            treeView.setRoot(rootTreeItem);

        }
        //textUponOpen = currentText;
    }

    @FXML
    public void build(ActionEvent event) {
        JavaCompiler comp = ToolProvider.getSystemJavaCompiler();
        comp.run(System.in, System.out, System.err, currentTab.path.toString());
        System.out.println("Compilation complete");
    }

    @FXML
    public void run(ActionEvent event) {
        try {
            // TODO: replace hardcode for testoutput directory with something programmatic, grab the names for loadClass after build
            String path = currentTab.path.toString();
            if (path.length() > 0) {
                ClassLoader parentClassLoader = MyClassLoader.class.getClassLoader();
                MyClassLoader classLoader = new MyClassLoader(parentClassLoader);
                Class compiledClass = classLoader.loadClassByPath(path.replace(".java", ".class"));
                Method main = compiledClass.getDeclaredMethod("main", String[].class);
                String[] params = null; // init params accordingly
                main.invoke(null, (Object) params); // static method doesn't have an instance
            }
        } catch (Exception e) {
            System.out.println("Run failed.");
            e.printStackTrace();
        }
    }

    @FXML
    public void close(ActionEvent event) {
        Tab tab = fileTabPane.getSelectionModel().getSelectedItem();
        fileTabPane.getTabs().remove(tab);
    }

    @FXML
    public void createProj(ActionEvent event) {

    }

    @FXML
    public void cut(ActionEvent event) {
        currentTab.text.cut();
    }

    @FXML
    public void copy(ActionEvent event) {
        currentTab.text.copy();
    }

    @FXML
    public void paste(ActionEvent event) {
        currentTab.text.paste();
    }



    public void createTree() throws IOException {

        // create root
        rootTreeItem = new TreeItem<Path>(currentTab.path.getParent());
        rootTreeItem.setExpanded(true);

        // create tree structure recursively
        createTree(rootTreeItem);

    }

    public void createTree(TreeItem<Path> treeItem) throws IOException{
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(treeItem.getValue())) {

            for (Path path : directoryStream) {

                TreeItem<Path> newItem = new TreeItem<Path>(path);
                newItem.setExpanded(true);

                treeItem.getChildren().add(newItem);

                if (Files.isDirectory(path)) {
                    createTree(newItem);
                }
            }
        }
        // catch exceptions, e. g. java.nio.file.AccessDeniedException: c:\System Volume Information, c:\$RECYCLE.BIN
        catch( Exception ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    public void save(ActionEvent event) {
        if (!currentTab.path.equals(""))
        {
            try//checks if the file is able to open and not invalid
            {
                File file = new File(currentTab.path.toString());
                BufferedWriter writeb = new BufferedWriter(new FileWriter(file)); //creates the file with a .java extension

                writeb.write(currentTab.text.getText()); //gets the text that is inputed

                writeb.flush();
                writeb.close(); //closes the file
//                textUponOpen = currentTab.text.getText();
            }
            catch(Exception evt)
            {
                Alert warning = new Alert(Alert.AlertType.ERROR);
                warning.setTitle("Load Error");
                warning.setContentText("Unable to save file " );
                warning.showAndWait();
            }
        }

        else
        {
            saveAs();
        }
    }

    @FXML
    public void saveAs() {
        FileChooser jfc = new FileChooser();//directs user to home directory
        jfc.setTitle("Save as java file");//dialog for selecting file will say choose file
        jfc.setInitialDirectory(new File("."));
        jfc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Java (.java)", "*.java"));
        File returnValue = jfc.showSaveDialog(null);
        if (returnValue !=null)
        {//input here a file read so that it can open and read the file

            try//checks if the file is able to open and not invalid
            {
                File file = jfc.getInitialDirectory();//gets the file name
                currentTab.path = file.toPath();//gets the path of the file
                if(!currentTab.path.endsWith(".java"))//if the file isn't a java script then create the file
                {
//                    file = new File(path + ".java");
                    BufferedWriter writeb = new BufferedWriter(new FileWriter(returnValue)); //creates the file with a .java extension

                    writeb.write(currentTab.text.getText()); //gets the text that is inputed

                    writeb.flush();
                    writeb.close(); //closes the file
                }
//                textUponOpen = text.getText();
                fileTabPane.getSelectionModel().getSelectedItem().setText(currentTab.path.getFileName().toString());

            }
            catch(Exception evt)
            {
                Alert warning = new Alert(Alert.AlertType.ERROR);
                warning.setTitle("Decaf");
                warning.setHeaderText("File error");
                warning.setContentText("There is nothing in the file to save");

                warning.showAndWait();
            }
        }

    }
}
