import javafx.scene.control.skin.TextAreaSkin;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;
import javax.swing.text.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;


public class Workspace extends JPanel {


    JTextArea textArea; //adds huge text area when gui opens
    String textUponOpen = "";
    String textBeforeExit = "";
    String path = "";   // Name of the file
    String fileName = "";

    private static final int ROWS = 40; // rows in the JTextArea
    private static final int COLS = 60; // columns in the JTextArea


    Workspace() {
        this.textArea = new JTextArea(ROWS, COLS);

        JScrollPane scroll = new JScrollPane (this.textArea);//creates a scroll panel object
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);//scroll bar to be vertical
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);//scroll bar to be horizontal
        this.add(scroll);

    }
    public JTextArea getTextArea() {
        return textArea;
    }
    void saveFile(){
        if (!path.equals(""))
        {
            try//checks if the file is able to open and not invalid
            {
                File file = new File(path);

                BufferedWriter writeb = new BufferedWriter(new FileWriter(file)); //creates the file with a .java extension

                writeb.write(textArea.getText()); //gets the text that is inputed

                writeb.flush();
                writeb.close(); //closes the file
                textUponOpen = textArea.getText();
            }
            catch(Exception evt)
            {
                System.out.println(evt.getMessage());
                //JOptionPane.showMessageDialog(title, evt.getMessage()); //message appears if there is an error
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


                    writeb.write(textArea.getText()); //gets the text that is inputed

                    writeb.flush();
                    writeb.close(); //closes the file
                }
                textUponOpen = textArea.getText();
            }
            catch(Exception evt)
            {
                System.out.println(evt.getMessage());
                //JOptionPane.showMessageDialog(title, evt.getMessage()); //message appears if there is an error
            }
        }
    }

    public void open() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());//directs user to home directory
        jfc.setDialogTitle("Select a java file");//dialog for selecting file will say choose file
        jfc.setAcceptAllFileFilterUsed(false);//Boolean to show that it wont accept any file
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Java (.java)", "java");//accept only .txt files
        jfc.addChoosableFileFilter(filter);
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {//input here a file read so that it can open and read the file

            File select = new File(jfc.getSelectedFile().getAbsolutePath()); //finds selected file so that it be open
            try//checks if the file is able to open and not invalid
            {
                File file = jfc.getSelectedFile();//gets the file name
                fileName = file.getName();
                path = file.getPath();//gets the path of the file
                String Line = "";
                FileReader read = new FileReader(select);//reads the file that is being open
                BufferedReader input = new BufferedReader(read);//shows the input of what the user used
                textArea.setText("");
                while ((Line = input.readLine()) != null) //reads the line
                {
                    textArea.append(Line);//appends the text
                    textArea.append("\n");//creates a new line
                }
                input.close(); //closes the file

            } catch (Exception evt) {
                System.out.println(evt.getMessage());
                //JOptionPane.showMessageDialog(title, evt.getMessage()); //message appears if there is an error
            }
        }
        textUponOpen = textArea.getText();
    }
    public String getFileName() {
        return fileName;
    }

//    public void exitFile() {
//        textBeforeExit = textArea.getText();
//        if (textUponOpen.equals(textBeforeExit)) {//no changes have been made, don't need to prompt extra save
//            title.setVisible(false); //exit the text
//            title.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//            //System.exit(0);
//            windowNum--;
//
//            //check if more than one window is open
//        }
//        else{
//
//            //Changes have been made, prompt user to save
//            int optionChosen = JOptionPane.showConfirmDialog(title, "You've changed your document. Would you like to save before exiting?");
//            if (optionChosen == JOptionPane.YES_OPTION) {
//                //reroute to save
//                saveFile();
//
//            } else if (optionChosen == JOptionPane.NO_OPTION) {
//                title.setVisible(false);//Just exit the page. User doesn't want to save.
//                //System.exit(0);
//                windowNum--;
//            }
//            else
//            {
//                title.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
//            }
//        }
//        if(windowNum == 0){
//            System.exit(0);
//        }
//    }

}
