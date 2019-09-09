import java.nio.file.*;



public class FileWatcher {

    boolean testEditingDetection(){
        boolean wasFileChanged = false;
        try{
            WatchService watchService = FileSystems.getDefault().newWatchService();

            Path path = Paths.get(System.getProperty("user.home"));//get the current properties of this file

            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);//watch if it has changed

            WatchKey key;
            if((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println(
                            "Event kind:" + event.kind()
                                    + ". File affected: " + event.context() + ".");
                }
                wasFileChanged = true;
                key.reset();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return wasFileChanged;
    }
    public static void main(String[] args) {

    }
}

