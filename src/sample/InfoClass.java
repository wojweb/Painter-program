package sample;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Projekt: Kreator graficzny
 * InfoClass.java
 * Klasa wyświetlająca informacje o programie i autorze
 * @author Szymon Wojtaszek
 * @version Lepiej nie będzie
 */
public class InfoClass {
    /**
     * Jedyna funkcja wyświetlająca okienko z informacjami
     */
    public static void display(){
        Stage window = new Stage();
        window.setAlwaysOnTop(true);
        window.setTitle("Info Box");
        window.initModality(Modality.APPLICATION_MODAL);

        StackPane stackPane = new StackPane();

        Label infoLabel = new Label();
        infoLabel.setText("Welcome in Best-Drawer\n" +
            "When you want to draw any shape, you must just choose it\n" +
            "from toolbar on the bottom. You can also choose colour\n" +
            "or size of stroke, when you want to changes this parameters\n" +
            "on drawed shapes, you should just click this object rpm\n" +
            "and change anything what you want. You can also delete objects\n" +
            "by rpm. At the end for your drawing you can export you picture to jpg\n" +
            "and share with your friends\n\n" +
            "Szymon Wojtaszek 236592 - Wojweb Software Corporation");
        infoLabel.setTextAlignment(TextAlignment.CENTER);

        stackPane.getChildren().add(infoLabel);

        Scene scene = new Scene(stackPane, 500, 220);
        window.setScene(scene);
        window.show();

    }
}
