import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class ColoredText {


    void testing(Scene sc, Stage s){
        sc.getStylesheets().add(ColoredText.class.getResource("java-keywords.css").toExternalForm());
        s.setScene(sc);
    }

}