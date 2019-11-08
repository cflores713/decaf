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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        newFile();//start with a blank file
    }

    //Creates new blank tab in editor
    @FXML
    public void newFile() throws IOException{
        newTab(new Tab("Untitled"));//create new tab
    }

    //create tab and sets as current or inFocus tab
    public void newTab(Tab tab) throws IOException {
        FXMLLoader loader = new FXMLLoader(new File("./src/main/resources/textEditor.fxml").toURI().toURL());
        tab.setContent(loader.load());
        EditorController controller = loader.getController();

        //Code area listener, assigns text to code area based on tab path
        tab.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                currentTab = controller.getTabModel();
//                System.out.println(currentTab.path.toString());
            }
        });

        fileTabPane.getTabs().add(tab);//add new tab to tabPane
        fileTabPane.getSelectionModel().select(tab);//set new tab to focus
    }

    //Uses file explorer to open a file or project
    //TODO: implement open to also open a project, currently just a file
    @FXML
    public void open(ActionEvent event) throws IOException {
        FileChooser jfc = new FileChooser();//directs user to home directory
        jfc.setTitle("Select a java file");//dialog for selecting file will say choose file
        jfc.setInitialDirectory(new File("."));
        jfc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Java (.java)", "*.java"));
        File returnValue = jfc.showOpenDialog(null);

        //check if file exists, then either create tab or replace tab
        if (returnValue != null) {

            Path path = returnValue.toPath();//gets the path of the file
            String parentFolder = returnValue.getParent();

            try {
                newTab(new Tab(path.toString())); //

                //fill code area
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
                currentTab.path = returnValue.toPath(); //set current tab path

            }
            catch(Exception evt) {
                Alert warning = new Alert(Alert.AlertType.ERROR);
                warning.setTitle("Load Error");
                warning.setContentText("Unable to load file " + path);
                warning.showAndWait();
            }
            String pathName = path.getFileName().toString();
            fileTabPane.getSelectionModel().getSelectedItem().setText(pathName);//set tab title to file name

            //TODO: create tree based on path of file
//            createTree();
//            treeView.setRoot(rootTreeItem);

        }
        //textUponOpen = currentText;
    }
    
    //create tree structure
    public void createTree() throws IOException {

        // create root
        rootTreeItem = new TreeItem<Path>(currentTab.path.getParent());
        rootTreeItem.setExpanded(true);

        // create tree structure recursively
        createTree(rootTreeItem);

    }

    //iterate through directory structure and create file tree
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

    //Compile system
    @FXML
    public void build(ActionEvent event) {
        JavaCompiler comp = ToolProvider.getSystemJavaCompiler();
        comp.run(System.in, System.out, System.err, currentTab.path.toString());
        System.out.println("Compilation complete");
    }

    @FXML
    public StringBuilder printClassNameList(List<String> classNames){
        StringBuilder forPopUp = new StringBuilder();
        for(String name: classNames){
            forPopUp.append(name);
            forPopUp.append("\n");
        }
        return forPopUp;
    }
    //Compute stats of file
    /* TODO: add new feature 1 here
     */
    @FXML
    public void computeStats(ActionEvent event) {
        int charNum = 0;
        int lineNum = 0;
        int keyNum = 0;
        List<String> classNames = new ArrayList<>();
        String currentCode = currentTab.text.getText();
        String lineSplit[] = currentCode.split("\\n");//delimit by new line character
        for(int i = 0; i < lineSplit.length; i++)
        {
            if(!lineSplit[i].isEmpty()){//keeps us from counting blank lines towards lineNum
                lineNum++;
            }
            if(lineSplit[i].contains("class")){
                String classDeclaration[] = lineSplit[i].split(" ");//delimit by space
                for(int index = 0; index < classDeclaration.length; index++){
                    if(classDeclaration[index].equals("class")){
                        classNames.add(classDeclaration[index+1]);
                    }
                }
            }
            for(int j = 0; j < lineSplit[i].length(); j++)
            {
                charNum++;//read through each line and count characters
            }
        }
        String firstSplit[] = currentCode.split("[;]");
        for(int i = 0; i < firstSplit.length; i++)
        {
            //code string splits each LINE from firstSplit
            String codeStr[] = firstSplit[i].trim().split("[\\s\\n({})]");
            for(int j = 0; j < codeStr.length; j++)
            {
                if(codeStr[j].equals("if") || codeStr[j].equals("else") || codeStr[j].equals("for") || codeStr[j].equals("while"))
                {
                    keyNum++;
                }
            }
        }
        Alert stats = new Alert(Alert.AlertType.INFORMATION);
        stats.setHeaderText("Code Statistics");
        stats.setContentText("Number of characters: " + charNum + "\n"
                + "Number of lines: " + lineNum + "\n"
                + "Keywords in use (if, else, while, for): " + keyNum + "\n"
                + "Classes:" + "\n"
                + printClassNameList(classNames)
        );
        printClassNameList(classNames);
        stats.showAndWait();
    }


    //Execute main program
    /* TODO: replace hardcode for testoutput directory with something programmatic, grab the names for loadClass after build
                    - currently only executes file in focus, needs to run based on project
     */
    @FXML
    public void run(ActionEvent event) {
        try {
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

    //close tab/file in focus
    /*TODO: check if text changed and signal if user wants to save
          *idea: have variable in editorController and check that
     */
    @FXML
    public void close(ActionEvent event) {
        Tab tab = fileTabPane.getSelectionModel().getSelectedItem();
        fileTabPane.getTabs().remove(tab);
    }


    @FXML
    public void createProj(ActionEvent event) {

    }

    //basic utility editing functions for text
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



    //Replace contents of file in directory with contents in editor
    @FXML
    public void save(ActionEvent event) {
        if (!currentTab.path.equals(""))//checks if current tab has a path
        {
            try//checks if the file is able to open and not invalid
            {
                File file = new File(currentTab.path.toString());
                BufferedWriter writeb = new BufferedWriter(new FileWriter(file)); //creates the file with a .java extension

                writeb.write(currentTab.text.getText()); //gets the text that is inputted

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
        //there is no path, call saveAs function
        else
        {
            saveAs();
        }
    }

    //Create file in directory structure to save contents in editor
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
