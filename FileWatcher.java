import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.nio.file.*;
import javax.swing.plaf.metal.*;
import javax.swing.text.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;


public class FileWatcher {
    public static void main(String[] args) {
        try{
            System.out.println("Testing if we get into this at all...");
            WatchService watchService = FileSystems.getDefault().newWatchService();

            Path path = Paths.get(System.getProperty("user.home/newTest.java"));

            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println(
                            "Event kind:" + event.kind()
                                    + ". File affected: " + event.context() + ".");
                }
                key.reset();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}

