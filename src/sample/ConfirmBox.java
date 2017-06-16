package sample;


import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

/**
 * Projekt: Kreator graficzny
 * ConfirmBox.java
 * Klasa usprawniająa poprawne zamykanie aplikacji
 * @author Szymon Wojtaszek
 * @version Lepiej nie będzie
 */
public class ConfirmBox {


    static boolean answer;

    /**
     * Funkcja wyświetlająca okienko prawidłowego zamknięcia
     * @return odpowiedz logiczna
     */
    public static boolean display() {
        Stage window = new Stage();
        window.setAlwaysOnTop(true);

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Closing");
        window.setMinWidth(250);
        Label label = new Label();
        label.setText("Do you really want to close this program?");

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(20);
        HBox hBox = new HBox(yesButton, noButton);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);

        layout.getChildren().addAll(label, hBox);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 300, 150);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }

}