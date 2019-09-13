import java.awt.*;
import javax.swing.*; 
import java.io.*; 
import java.awt.event.*;
import javax.swing.plaf.metal.*;
import javax.swing.text.*; 
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;


class CodeEditor extends JFrame implements ActionListener { 
    //Creates a text box object
    JTextArea text; 
  
    //creates the frame gui object
    JFrame title;

    String textUponOpen = "";
    String textBeforeExit = "";
    String path = "";

    public static int windowNum = 1;

    CodeEditor() 
    { 
        // Create the actual body of the gui
        title = new JFrame("Decaf");//creates the gui frame 

        path = "";
        text = new JTextArea(); //adds huge text area when gui opens
  
        // Creates the top frame
        JMenuBar maintabs = new JMenuBar(); 
  //---------------------First menu tab File------------------------------------------//
        // Create a menu for menu 
        JMenu file = new JMenu("File"); 
  
        //Menu items when clicking file
        JMenuItem newB = new JMenuItem("New"); 
        JMenuItem open = new JMenuItem("Open"); 
        JMenuItem save = new JMenuItem("Save");
        JMenuItem saveAs = new JMenuItem("Save As");
        JMenuItem exit = new JMenuItem("Exit");
       
      
  
        // Commands once a person clicks the menu item
        newB.addActionListener(this); 
        open.addActionListener(this); 
        save.addActionListener(this);
        saveAs.addActionListener(this);
        exit.addActionListener(this);//command to exit 
        file.add(newB);//adds small components to the menu bar
        file.add(open); //adds 2nd menu bar to file when clicking to find open
        file.add(save);
        file.add(saveAs);
        file.add(exit);//adds small component exit under file
        //------------------------------2nd menu bar compile//-----------------------------------------//
        JMenu Compile = new JMenu("Compile");
        JMenuItem Build = new JMenuItem("Build");//Menu items adds tab under main Menu when clicking compile
        Compile.addActionListener(this);
        Build.addActionListener(this);
        Compile.add(Build);//adds the tab build under compile when clicking
        //---------------------------------------------------------------------------------------------//
        //----------------------------------3rd menu bar Execute---------------------------------------------//
        JMenu Execute = new JMenu("Execute");//top menu bar for execute
        Execute.addActionListener(this);//command to execute
        
        //----------------------------------------Edit tab---------------------------------------------------------------//
      
        JMenu Edit = new JMenu("Edit"); //Adds main tab to the top of the frame
        JMenuItem Cut = new JMenuItem("Cut"); //creates the menu tab when clicking edit for cut
        JMenuItem Copy = new JMenuItem("Copy"); //creates the menu tab when clicking edit for cut
        JMenuItem Paste = new JMenuItem("Paste"); //creates the menu tab when clicking edit for cut
  
      
        Cut.addActionListener(this); 
        Copy.addActionListener(this); 
        Paste.addActionListener(this); 
        
        Edit.add(Cut);//Adds the menu tab after clicking edit for cut
        Edit.add(Copy); //Adds the menu tab after clicking edit for copy
        Edit.add(Paste); //Adds the menu tab after clicking edit for paste
  //--------------------------------------------------------------------------------------------------------------------------//
   
  
        maintabs.add(file);//adds file tab on top  
        maintabs.add(Edit); //connects with mi6 paste
        maintabs.add(Compile);//adds the tab compile to the top
        maintabs.add(Execute);//adds execute bar

        
        title.setJMenuBar(maintabs); //creates the menu tabs on top
        title.add(text);//adds the text area when opening gui
        JScrollPane scroll = new JScrollPane (text);//creates a scroll panel object
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);//scroll bar to be vertical
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);//scroll bar to be horizontal
        title.add(scroll);//adds the scroll bar to the main gui frame
        title.setSize(800, 800); //sets the gui frame size
        title.show(); //gui frame will appear

        title.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                exitOptions();
            }
        });
    } 


    void saveFile(){
        if (!path.equals(""))
        {
            try//checks if the file is able to open and not invalid
            {
                File file = new File(path);

                BufferedWriter writeb = new BufferedWriter(new FileWriter(file)); //creates the file with a .java extension

                writeb.write(text.getText()); //gets the text that is inputed

                writeb.flush();
                writeb.close(); //closes the file
                textUponOpen = text.getText();
            }
            catch(Exception evt)
            {
                JOptionPane.showMessageDialog(title, evt.getMessage()); //message appears if there is an error
            }
        }

        else
        {
            saveAsFile();
        }

    }

    void saveAsFile(){
        //ToDo: Implement save so you can save changes to the same file repeatedly
        //Save currently only works as a "Save as"
        //If you type into a file and save it, then exit and come back, it definitely saves
        //But if you want to make changes to that already-saved file, you can't. You'll have to save it as a diff version.

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());//directs user to home directory
        jfc.setDialogTitle("Save as java file");//dialog for selecting file will say choose file
        jfc.setAcceptAllFileFilterUsed(false);//Boolean to show that it wont accept any file
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Java(.java)","java");//accept only .java files
        jfc.addChoosableFileFilter(filter);
        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {//input here a file read so that it can open and read the file
            File select = new File(jfc.getSelectedFile().getAbsolutePath()); //finds selected file so that it be open
            try//checks if the file is able to open and not invalid
            {


                File file = jfc.getSelectedFile();//gets the file name
                path = file.getPath();//gets the path of the file
                if(!path.toLowerCase().endsWith(".java"))//if the file isn't a java script then create the file
                {
                    file = new File(path + ".java");
                    BufferedWriter writeb = new BufferedWriter(new FileWriter(file)); //creates the file with a .java extension


                    writeb.write(text.getText()); //gets the text that is inputed

                    writeb.flush();
                    writeb.close(); //closes the file
                }
                textUponOpen = text.getText();



            }
            catch(Exception evt)
            {
                JOptionPane.showMessageDialog(title, evt.getMessage()); //message appears if there is an error
            }
        }
        //else {
        //    JOptionPane.showMessageDialog(title, "You canceled to save a file"); //message appears when canceling
        //}
    }

    public void exitOptions() {
        textBeforeExit = text.getText();
        if (textUponOpen.equals(textBeforeExit)) {//no changes have been made, don't need to prompt extra save
            title.setVisible(false); //exit the text
            title.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            //System.exit(0);
            windowNum--;

            //check if more than one window is open
        }
        else{

            //Changes have been made, prompt user to save
            int optionChosen = JOptionPane.showConfirmDialog(title, "You've changed your document. Would you like to save before exiting?");
            if (optionChosen == JOptionPane.YES_OPTION) {
                //reroute to save
                saveFile();

            } else if (optionChosen == JOptionPane.NO_OPTION) {
                title.setVisible(false);//Just exit the page. User doesn't want to save.
                //System.exit(0);
                windowNum--;
            }
            else
            {
                title.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            }
        }
        if(windowNum == 0){
            System.exit(0);
        }
    }

    // If a button is pressed 
    public void actionPerformed(ActionEvent e) 
    { 
        String in = e.getActionCommand(); 
  
        if (in.equals("Cut")) 
        { 
            text.cut(); //cuts the text
        }
        else if (in.equals("New")) {
            new CodeEditor();
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
        else if (in.equals("Exit")) 
        {
            exitOptions();
        }
        else if (in.equals("Open")) {
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());//directs user to home directory
            jfc.setDialogTitle("Select a java file");//dialog for selecting file will say choose file
            jfc.setAcceptAllFileFilterUsed(false);//Boolean to show that it wont accept any file
            jfc.setFileSelectionMode(jfc.FILES_AND_DIRECTORIES);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Java (.java)", "java");//accept only .txt files
            jfc.addChoosableFileFilter(filter);
            int returnValue = jfc.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {//input here a file read so that it can open and read the file

                File select = new File(jfc.getSelectedFile().getAbsolutePath()); //finds selected file so that it be open
                try//checks if the file is able to open and not invalid
                {
                    File file = jfc.getSelectedFile();//gets the file name
                    path = file.getPath();//gets the path of the file
                    String Line = "";
                    FileReader read = new FileReader(select);//reads the file that is being open
                    BufferedReader input = new BufferedReader(read);//shows the input of what the user used
                    text.setText("");
                    while ((Line = input.readLine()) != null) //reads the line
                    {


                        text.append(Line);//appends the text
                        text.append("\n");//creates a new line

                    }

                    input.close(); //closes the file

                } catch (Exception evt) {
                    JOptionPane.showMessageDialog(title, evt.getMessage()); //message appears if there is an error
                }
            } //else {
            //    JOptionPane.showMessageDialog(title, "You canceled to open a file"); //message appears when canceling
            //}
            textUponOpen = text.getText();


        } else if (in.equals("Save As"))//saves only java files
        {
            saveAsFile();
        }
        else if (in.equals("Save")){
            saveFile();
        }
}
    // Main class 
    public static void main(String [] args){
        CodeEditor e = new CodeEditor(); 
    } 
} 

/*TODO:
   - Click x or exit doesn't pull save
   - Click x or exit doesn't unload/end program
   - Don't pull edit option when double clicking when in file explorer
   - Remove message after clicking cancel button when saving - Done
   - Open file just adds to editor, should replace or create new window
   - change save to save as - Done
   - create save - Done

 */
