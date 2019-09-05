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
  
   
    CodeEditor() 
    { 
        // Create the actual body of the gui
        title = new JFrame("Decaf");//creates the gui frame 
  
       
  
        
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
        JMenuItem exit = new JMenuItem("Exit");
       
      
  
        // Commands once a person clicks the menu item
        newB.addActionListener(this); 
        open.addActionListener(this); 
        save.addActionListener(this); 
        exit.addActionListener(this);//command to exit 
        file.add(newB);//adds small compenents to the menu bar 
        file.add(open); //adds 2nd menu bar to file when clicking to find open
        file.add(save); 
        file.add(exit);//adds small compenent exit under file
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
            title.setVisible(false); //exit the text
        } 
        else if (in.equals("Open")) 
        { 
        	JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());//directs user to home directory
    		jfc.setDialogTitle("Select a java file");//dialog for selecting file will say choose file
    		jfc.setAcceptAllFileFilterUsed(false);//Boolean to show that it wont accept any file 
    		FileNameExtensionFilter filter = new FileNameExtensionFilter("Java(.java)","java");//accept only .txt files
    		jfc.addChoosableFileFilter(filter);
    		int returnValue = jfc.showOpenDialog(null);
    		if (returnValue == JFileChooser.APPROVE_OPTION) 
    		{//input here a file read so that it can open and read the file
    			 File select = new File(jfc.getSelectedFile().getAbsolutePath()); //finds selected file so that it be open
    			 try//checks if the file is able to open and not invalid
    			 {
    				 String Line = "";
    				 FileReader read = new FileReader(select);//reads the file that is being open
    				 BufferedReader input = new BufferedReader(read);//shows the input of what the user used
    				 while ((Line = input.readLine()) != null) //reads the line
    				 {
    					 
    					
    					 text.append(Line);//appends the text
                         text.append("\n");//creates a new line
                    
    				 }
    				
    				 
    			 }
    			 catch(Exception evt)
    			 {
    				 JOptionPane.showMessageDialog(title, evt.getMessage()); //message appears if there is an error
    			 		}
    			 }
    			 else
    			 {
    				 JOptionPane.showMessageDialog(title, "You canceled to open a file"); //message appears when canceling 
    			 }
    	
    		
        }
    else if (in.equals("Save"))//saves only java files 
    { 
    	JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());//directs user to home directory
		jfc.setDialogTitle("Select a java file");//dialog for selecting file will say choose file
		jfc.setAcceptAllFileFilterUsed(false);//Boolean to show that it wont accept any file 
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Java(.java)","java");//accept only .txt files
		jfc.addChoosableFileFilter(filter);
		int returnValue = jfc.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) 
		{//input here a file read so that it can open and read the file
			 File select = new File(jfc.getSelectedFile().getAbsolutePath()); //finds selected file so that it be open
			 try//checks if the file is able to open and not invalid
			 {
				 
				  
				 File file = jfc.getSelectedFile();//gets the file name
				 String filePath = file.getPath();//gets the path of the file
				 if(!filePath.toLowerCase().endsWith(".java"))//if the file isn't a java script then create the file
				 {
				     file = new File(filePath + ".java");
				     BufferedWriter writeb = new BufferedWriter(new FileWriter(file)); //creates the file with a .java extension

		               
	                 writeb.write(text.getText()); //gets the text that is inputed

	                 writeb.flush(); 
	                 writeb.close(); //closes the file
				 }
                
				
				 
			 }
			 catch(Exception evt)
			 {
				 JOptionPane.showMessageDialog(title, evt.getMessage()); //message appears if there is an error
			 	 }
			 }
			 else
			 {
				 JOptionPane.showMessageDialog(title, "You canceled to save a file"); //message appears when canceling 
			 }
	
		}
    }
    // Main class 
    public static void main(String args[]) 
    { 
        CodeEditor e = new CodeEditor(); 
    } 
} 