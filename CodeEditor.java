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
    JTabbedPane tabbedPane = new JTabbedPane();
    JPanel workSpacePane = new JPanel();
    JScrollPane explorerScrollPane = new JScrollPane();
    public static int windowNum = 1;

    CodeEditor()
    {
        // Create the actual body of the gui
        this.title = new JFrame("Decaf");//creates the gui frame

        this.path = "";

        // Creates the top frame
        JMenuBar maintabs = new JMenuBar();
    //---------------------Zero menu tab Project------------------------------------------//
        // TODO: add actionListeners and actions for project menuitems
        JMenu project = new JMenu("Project");
        //Menu items when clicking file
        JMenuItem newProj = new JMenuItem("New Project");
        JMenuItem openProj = new JMenuItem("Open Project");
        JMenuItem saveProj = new JMenuItem("Save Project");
        JMenuItem saveProjAs = new JMenuItem("Save Project As");
        JMenuItem closeProj = new JMenuItem("Close Project");
        JMenuItem exitProj = new JMenuItem("Exit");
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

    // ------------------------------2nd menu bar compile//-----------------------------------------//
        JMenu Compile = new JMenu("Compile");
        JMenuItem Build = new JMenuItem("Build");//Menu items adds tab under main Menu when clicking compile
        Compile.addActionListener(this);
        Build.addActionListener(this);
        Compile.add(Build);//adds the tab build under compile when clicking

    //----------------------------------3rd menu bar Execute---------------------------------------------//
        JMenu Execute = new JMenu("Execute");//top menu bar for execute
        Execute.addActionListener(this);//command to execute

    //--------------------------------4th menu bar Edit tab---------------------------------------------------------------//

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
        maintabs.add(project); // adds project tab
        maintabs.add(file);//adds file tab on top
        maintabs.add(Edit); //connects with mi6 paste
        maintabs.add(Compile);//adds the tab compile to the top
        maintabs.add(Execute);//adds execute bar

    //--------------------------------------------------------------------------------------------------//

        this.title.setJMenuBar(maintabs); //creates the menu tabs on top

    //----------------------Create default new workspace as a tab---------------------------------------//
        text = new JTextArea();
        title.add(text);//adds the text area when opening gui

        //this.title.add(scroll);//adds the scroll bar to the main gui frame
        // NEW

//        JPanel workspace = new Workspace();
//        JScrollPane scroll = new JScrollPane (workspace);//creates a scroll panel object
//        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);//scroll bar to be vertical
//        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);//scroll bar to be horizontal
//        text = new JTextArea();
//        JPanel pane = new JPanel();
//        pane.add(text);
//        JScrollPane scroll = new JScrollPane (text);//creates a scroll panel object
//        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);//scroll bar to be vertical
//        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);//scroll bar to be horizontal

        this.tabbedPane.addTab("new", new Workspace());
        this.workSpacePane.add(this.tabbedPane);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, explorerScrollPane, this.workSpacePane);
        //splitPane.setOneTouchExpandable(true);
        //splitPane.setDividerLocation(150);
        this.title.add(splitPane);
        this.title.setSize(800, 800); //sets the gui frame size
        this.title.pack();
        this.title.setVisible(true);

        this.title.show(); //gui frame will appear

        this.title.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                exitOptions();
            }
        });

    }


    void saveFile(){
        Workspace currentTab = (Workspace) tabbedPane.getSelectedComponent();
        try//checks if the file is able to open and not invalid
        {
            currentTab.saveFile();
        }
        catch(Exception evt)
        {
            JOptionPane.showMessageDialog(title, evt.getMessage()); //message appears if there is an error
        }
    }

    void saveAsFile(){
        Workspace currentTab = (Workspace) tabbedPane.getSelectedComponent();
        try//checks if the file is able to open and not invalid
        {
            currentTab.saveAsFile();
        }
        catch(Exception evt)
        {
            JOptionPane.showMessageDialog(title, evt.getMessage()); //message appears if there is an error
        }
    }

    public void exitOptions() {
//        Workspace currentTab = (Workspace) tabbedPane.getSelectedComponent();
//        try//checks if the file is able to open and not invalid
//        {
//            currentTab.exitOptions();
//        }
//        catch(Exception evt)
//        {
//            JOptionPane.showMessageDialog(title, evt.getMessage()); //message appears if there is an error
//        }



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
    public void addPanel() {
        JPanel workspace = new Workspace();
        this.tabbedPane.addTab("new", workspace);
    }
    public void open() {
        Workspace workspace = new Workspace();
        workspace.open();
        this.tabbedPane.addTab(workspace.getFileName(), workspace);
    }
    // If a button is pressed
    public void actionPerformed(ActionEvent e)
    {
        String in = e.getActionCommand();

        if (in.equals("Cut"))
        {
            text.cut(); //cuts the text
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
        else if (in.equals("New")) {
            this.addPanel();
            windowNum++;
        }
        else if (in.equals("Open")) {
            this.open();
        }
        else if (in.equals("Save As"))//saves only java files
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
