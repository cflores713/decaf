import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button; 
import javafx.scene.layout.*; 
import javafx.event.ActionEvent; 
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.event.EventHandler; 
import javafx.scene.control.*; 
import javafx.scene.control.Alert.AlertType;
import java.io.BufferedReader;
import java.io.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import javax.tools.*;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
public class Main extends Application implements  EventHandler<ActionEvent>{
	String textUponOpen;
    String textBeforeExit;
    String path;
    public static int windowNum;
  
	TextArea text;
	public void start(Stage s) {
	
		
			 //Creates a text box object
		    //JTextArea text; 
		  
		    //creates the frame gui object
		    s.setTitle("Decaf Code Editor");
		
	  
		  
		    text = new TextArea();
		    windowNum = 1;
		    textUponOpen = "";
		    textBeforeExit = "";
		    path = "";
		    
		  //-----------------First menu tab file-------------------//
		    Menu file = new Menu("File");
		    MenuItem newB = new MenuItem("New"); 
		    MenuItem NewP= new MenuItem("New Project");
		    MenuItem open = new MenuItem("Open"); 
		    MenuItem save = new MenuItem("Save");
		    MenuItem saveAs = new MenuItem("Save As");
		    MenuItem Remove = new MenuItem("Remove File");
		    MenuItem exit = new MenuItem("Exit");
		    
		    file.getItems().add(newB); 
		    file.getItems().add(NewP);
		    file.getItems().add(open); 
		    file.getItems().add(save);
		    file.getItems().add(saveAs);
		    file.getItems().add(Remove);
		    file.getItems().add(exit);
		    
		    newB.setOnAction(this); //action listeners
		    NewP.setOnAction(this);
	        open.setOnAction(this); 
	        save.setOnAction(this);
	        saveAs.setOnAction(this); 
	        Remove.setOnAction(this);
	        exit.setOnAction(this); 
		    //-----------------------------------------------------------//
			  //-----------------First menu tab file-------------------//
		    Menu Edit = new Menu("Edit");
		    MenuItem Cut = new MenuItem("Cut"); 
		    MenuItem Copy = new MenuItem("Copy"); 
		    MenuItem Paste = new MenuItem("Paste");
		   
		    Edit.getItems().add(Cut);
		    Edit.getItems().add(Copy);
		    Edit.getItems().add(Paste);
		    
		    Cut.setOnAction(this); //action listeners
	        Copy.setOnAction(this); 
	        Paste.setOnAction(this);
		
		    //-----------------------------------------------------------//
		    //----------------- Compile tab-------------------//
		    Menu Compile = new Menu("Compile");
		    MenuItem Build = new MenuItem("Build");
		    Compile.getItems().add(Build);
		    Compile.setOnAction(this);
		    //-----------------------------------------------------------//
		  //----------------- Execute tab-------------------//
		    Menu Execute = new Menu("Execute");
		    MenuItem Run = new MenuItem("Run");
		    Execute.getItems().add(Run);
		    Execute.setOnAction(this);

		    //-----------------------------------------------------------//

			Menu Stats = new Menu("Statistics");
			MenuItem showStats = new MenuItem("Show Code Statistics");
			Stats.getItems().add(showStats);
			Stats.setOnAction(this);

		    MenuBar mb = new MenuBar(); 
	
		    // add menu to menubar 
		    mb.getMenus().add(file); 
		    mb.getMenus().add(Edit);
		    mb.getMenus().add(Compile); 
		    mb.getMenus().add(Execute);
		    mb.getMenus().add(Stats);
		    // create a VBox 
		    VBox vb = new VBox(mb); 
		  
		    text.setPrefHeight(1000); 
		    text.setPrefWidth(1000);  
		    vb.getChildren().add(text);
		    Scene sc = new Scene(vb, 500,500); 
		    // set the scene 
		    s.setScene(sc); 
		    
		    s.show(); 
		    
		    
		
		
	}
	
	        

	void throwMyException(Exception evt)
    {
        Alert warning = new Alert(AlertType.ERROR);
        warning.setTitle("Decaf");
        warning.setHeaderText("File error");
        warning.setContentText("There is nothing in the file to save");

        warning.showAndWait();
    }

	void saveFile()
	{
        if (!path.equals(""))
        {
            try//checks if the file is able to open and not invalid
            {
                File file = new File(path);
//                path = file.getPath();//gets the path of the file
				System.out.println(file.getName());
                BufferedWriter writeb = new BufferedWriter(new FileWriter(file)); //creates the file with a .java extension

                writeb.write(text.getText()); //gets the text that is inputed

                writeb.flush();
                writeb.close(); //closes the file
                textUponOpen = text.getText(); // Reset textUponOpen after saving changes
            }
            catch(Exception evt)
            {
                throwMyException(evt);
            }
        }

        else
        {
            saveAsFile();
        }

    }
	
	 public void exitOptions() {//Need to fix thie exit command because it doesn't close the window
		
	        textBeforeExit = text.getText();
	      
	        if (textUponOpen.equals(textBeforeExit)) {//no changes have been made, don't need to prompt extra save
	        	
	            //System.exit(0);
	        	Stage newStage = new Stage();
	        	newStage.close();
	            windowNum--;

	            //check if more than one window is open
	        }
	        else{
	        		
	            //Changes have been made, prompt user to save
	            Alert warning = new Alert(AlertType.CONFIRMATION);
	            warning.setTitle("Confirmation Dialog");
	            warning.setHeaderText("Memory loss warning");
	            warning.setContentText("You've changed your document. Would you like to save before exiting?");
	            Optional<ButtonType> entry = warning.showAndWait();
	            if (entry.get() == ButtonType.YES) {
	                //reroute to save
	            	
	                saveFile();


	            } else if (entry.get() == ButtonType.NO) {
	            	
	            	
	                //System.exit(0);
	            	
	                windowNum--;
	            }
	            else
	            {
	            	 Stage newStage = new Stage();
				     newStage.close();
	            }
	        }
	        if(windowNum == 0){
	            System.exit(0);
	        }
	 }
	 public void RemoveFile()
	 {
		 FileChooser jfc = new FileChooser();//directs user to home directory
         jfc.setTitle("Select a java file");//dialog for selecting file will say choose file
         jfc.setInitialDirectory(new File("."));
         jfc.getExtensionFilters().addAll(new ExtensionFilter("Java (.java)", "*.java"));
         File returnValue = jfc.showOpenDialog(null);
       
         if (returnValue != null) 
         {
        	  Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
              warning.setTitle("File Warning");
              warning.setHeaderText("The file you selected "+returnValue.getName()+" will be deleted");
              warning.setContentText("Are you sure you want to delete " +returnValue.getName());
              Optional<ButtonType> entry = warning.showAndWait();
              
              if (entry.get() == ButtonType.OK) 
              {
                
                      returnValue.delete();//deletes the file
                      Alert warning2 = new Alert(AlertType.INFORMATION);
                      warning2.setTitle("Confirmation");
                      warning2.setHeaderText("The file you selected "+returnValue.getName()+" has been deleted ");
                      warning2.setContentText("Please hit the Ok button to close the dialog");
                      warning2.showAndWait();
              }
          } 
         else 
         {
        	Alert warning = new Alert(AlertType.INFORMATION);
     		warning.setTitle("Decaf");
     		warning.setHeaderText("File cancelation");
     		warning.setContentText("No file was selected to delete");
     		warning.showAndWait();
          }
      
	}
	 void saveAsFile(){
	        //ToDo: Implement save so you can save changes to the same file repeatedly
	        //Save currently only works as a "Save as"
	        //If you type into a file and save it, then exit and come back, it definitely saves
	        //But if you want to make changes to that already-saved file, you can't. You'll have to save it as a diff version.
	        FileChooser jfc = new FileChooser();//directs user to home directory
	        jfc.setTitle("Save as java file");//dialog for selecting file will say choose file
	        jfc.setInitialDirectory(new File("."));
	        jfc.getExtensionFilters().addAll(new ExtensionFilter("Java (.java)", "*.java"));
	        File returnValue = jfc.showSaveDialog(null);	// returnValue is either what the user selects or types

	        if (returnValue !=null)
	        {//input here a file read so that it can open and read the file
	            try//checks if the file is able to open and not invalid
	            {
	            	// Need to update path variable

					// if file does not exist, create a new file and return the path
					// if path does not end in .java, add .java to name and path
	                if(!returnValue.exists())//if the file isn't a java script then create the file
	                {
						path = returnValue.getPath();//gets the path of the file
						if(!path.toLowerCase().endsWith(".java"))
							path += ".java";
	                    File file = new File(path);
	                    System.out.println("Created new file");
	                    BufferedWriter writeb = new BufferedWriter(new FileWriter(file)); //creates the file with a .java extension
	                    writeb.write(text.getText()); //gets the text that is input
	                    writeb.flush();
	                    writeb.close(); //closes the file
	                }
	                else {
	                	saveFile();
					}
	                textUponOpen = text.getText();
	            }
	            catch(Exception evt)
	            {
                    throwMyException(evt);
	            }
	        }
	    }
	@Override
	 public void handle(ActionEvent event) 
    { 
        String in = ((MenuItem)event.getSource()).getText();
        // Below are variables needed for build and run
		JavaCompiler comp = ToolProvider.getSystemJavaCompiler();
		//StandardJavaFileManager fileManager = comp.getStandardFileManager(null, null, null);

		if(in.equals("Open"))
        {
            FileChooser jfc = new FileChooser();//directs user to home directory
            jfc.setTitle("Select a java file");//dialog for selecting file will say choose file
            jfc.setInitialDirectory(new File("."));
            jfc.getExtensionFilters().addAll(new ExtensionFilter("Java (.java)", "*.java"));
            File returnValue = jfc.showOpenDialog(null);
            if (returnValue != null) 
            {
            	    //File select = new File(jfc.getInitialDirectory().getAbsolutePath()); //finds selected file so that it be open
                	try
                	{
                        path = returnValue.getAbsolutePath();//gets the path of the file
                        String Line = "";
                        FileReader read = new FileReader(returnValue);//reads the file that is being open
                        BufferedReader input = new BufferedReader(read);//shows the input of what the user used
                        text.setText("");
                        
                        while ((Line = input.readLine()) != null) //reads the line
                        {
                            text.appendText(Line);//appends the text
                            text.appendText("\n");//creates a new line
                        }
                       
                        input.close(); //closes the file
                	}
                	catch(Exception evt)
                    {
                		Alert warning = new Alert(AlertType.ERROR);
                		warning.setTitle("Decaf");
                		warning.setHeaderText("File error");
                		warning.setContentText("No such file exists to open");

                		warning.showAndWait();
                    }
                }
            	textUponOpen = text.getText();
        }
        if(in.equals("New Project"))//needs to be worked on
        {
        	 
        	//What do we want to go here as opposed to just opening new?
                
	    }
        else if(in.equals("Remove File"))//Removes a file
        {
        	RemoveFile();
        }
        else if (in.equals("Save As"))//saves only java files
        {
            saveAsFile();
        }
        else if (in.equals("Save")){

            saveFile();
        }
        else if (in.equals("Exit")) 
        {
            exitOptions();
        }
        else if (in.equals("Cut")) 
        { 
            text.cut(); //cuts the text
        }
        else if (in.equals("New")) 
        {
        	Stage s= new Stage();
        	start(s);
        	windowNum++;
        }
        else if (in.equals("Copy")) 
        { 
            text.copy(); //copies text
        } 
        else if (in.equals("Paste")) 
        { 
            text.paste(); //paste the text
        }
        else if(in.equals("Compile"))
        {
        	// TODO: get this working so we can compile multiple files, need to figure out cleanup of testoutput directory
//        	// Creates a directory called testoutput that contains the .class files for a compiled .java file
//			String[] options = new String[] { "-d", "testoutput" };
//			File[] javaFiles = new File[] { new File(path) };
//			JavaCompiler.CompilationTask compilationTask = comp.getTask(null, null, null,
//					Arrays.asList(options),
//					null,
//					fileManager.getJavaFileObjects(javaFiles)
//			);
//			compilationTask.call();
			comp.run(System.in, System.out, System.err, path);
            System.out.println("Compilation complete");
        }
        else if(in.equals("Execute"))
        {
        	// TODO: Compile if not already compile
            try
            {
            	// TODO: replace hardcode for testoutput directory with something programmatic, grab the names for loadClass after build
				if (path.length() > 0) {
					ClassLoader parentClassLoader = MyClassLoader.class.getClassLoader();
					MyClassLoader classLoader = new MyClassLoader(parentClassLoader);
					Class compiledClass = classLoader.loadClassByPath(path.replace(".java", ".class"));
					Method main = compiledClass.getDeclaredMethod("main", String[].class);
					String[] params = null; // init params accordingly
					main.invoke(null, (Object) params); // static method doesn't have an instance
				}
            }
            catch(Exception e)
            {
            	System.out.println("Run failed.");
            	e.printStackTrace();
            }
        }
        else if(in.equals("Statistics"))
		{
			//ToDo: Figure out where to have this result show up inside the application, not just in console
			int charNum = 0;
			int lineNum = 0;//requires we delimit by... \n?
			String currentCode = text.getText();
			for(int i = 0; i < currentCode.length(); i++)
			{
				char letter = currentCode.charAt(i);
				if(letter == '\n') {
					//if we hit a new line, increment lineNum
					lineNum++;
				}
				charNum++;

			}
			System.out.println("Number of characters: " + charNum);
			System.out.println("Number of lines: " + lineNum);
		}
    }

	public static void main(String[] args) {
		launch(args);
	}
}
