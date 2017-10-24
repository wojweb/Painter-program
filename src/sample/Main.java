package sample;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;

//Praktykuje obsluge gita, dodaje ten komentarz by miec zmiany xDDD.
//Jesli kiedys sie znajdzie ktos poza mna, kto to czyta to prosze nie smiej sie ze mnie :)

/**
 * Projekt: Kreator graficzny
 * Prosty kreator pozwalający na rysowanie podstawowych figur w różnych kolorach, zmienianie
 * ich położenia oraz wielkości. Stworzony obraz można eksportować do png.
 *
 *
 * Main.java
 *
 * @author Szymon Wojtaszek
 * @version Lepiej nie będzie
 */

public class Main extends Application {

    Stage window;
    Scene scene;
    BorderPane layout;

    public static void main(String[] args) {
        Application.launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Graphics editor - Szymon Wojtaszek");
        window.setResizable(true);
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });


        /**
         * Część kodu odpowiedzialna za wygląd
         */

        //Tworzymy Menu
        //FileMenu
        Menu fileMenu = new Menu("File");

        MenuItem newFile = new MenuItem("New file");
        MenuItem snapshotFile = new MenuItem("Save...");
        MenuItem exitFile = new MenuItem("Exit");

        fileMenu.getItems().addAll(newFile, snapshotFile, exitFile);

        //InfoMenu
        Menu infoMenu = new Menu("Info");

        MenuItem moreInfo = new MenuItem("More...");
        infoMenu.getItems().add(moreInfo);




        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, infoMenu);


        //Główny layout do rysowania
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setCursor(Cursor.CROSSHAIR);
        anchorPane.setId("board");


        //Stópka z obsługą programu
        GridPane footer = new GridPane();
        footer.setHgap(20);
        footer.setPadding(new Insets(10, 10, 10, 10));
        footer.setId("footer");


        //Wybór narzędzia
        Label toolLabel = new Label("Tool:");
        ComboBox<String> toolComboBox = new ComboBox<>();

        toolComboBox.getItems().addAll("Move", "Line", "Circle", "Rectangle", "Polygon");
        toolComboBox.setValue("Move");

        //Obramowanie
        Label strokeLabel = new Label("Stroke: ");
        ComboBox<Double> strokeComboBox = new ComboBox<>();
        strokeComboBox.getItems().addAll(0.0, 0.5, 1.0, 1.5, 2.0, 3.0, 4.0, 5.0, 7.0);
        strokeComboBox.setValue(1.0);

        //Edycja koloru
        Label colorLabel = new Label("Color:");
        ColorPicker colorPicker = new ColorPicker(Color.BLACK);

        //Określanie współrzędnych na ekranie
        Label coordinateX = new Label("X: ");
        coordinateX.setPrefWidth(60);
        Label coordinateY = new Label("Y: ");
        coordinateY.setPrefWidth(60);


        footer.addRow(0, toolLabel, toolComboBox, strokeLabel, strokeComboBox, colorLabel,colorPicker,coordinateX, coordinateY);
        footer.setAlignment(Pos.CENTER);

        //Podsumowanie layoutu
        layout = new BorderPane();
        layout.setCenter(anchorPane);
        layout.setTop(menuBar);
        layout.setBottom(footer);


        scene = new Scene(layout, 800, 500);
        scene.getStylesheets().add(getClass().getResource("MyStyle.css").toExternalForm());
        window.setScene(scene);
        window.show();


        //Wprawiamy wszystko w ruch

        //Najpier Menu
        newFile.setOnAction( e -> {
            anchorPane.getChildren().removeAll(anchorPane.getChildren());
        });

        snapshotFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();

            //Ustawienie rozszerzenie
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
            File file = fileChooser.showSaveDialog(window);

            if(file != null) {
                try {
                    //Pad the capture area
                    WritableImage writableImage = new WritableImage((int) anchorPane.getWidth() + 20,
                            (int) anchorPane.getHeight() + 20);
                    anchorPane.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    //Write the snapshot to the chosen file
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        exitFile.setOnAction(e -> {
            closeProgram();
        });

        moreInfo.setOnAction(e -> InfoClass.display());

        //Wybranie sposobu zachowywania się myszki
        toolComboBox.setOnAction( e -> {
           switch(toolComboBox.getValue()){
               case "Move":
                   makeMove(anchorPane, coordinateX, coordinateY);
                   break;
               case "Line":
                   makeLine(anchorPane, coordinateX, coordinateY, toolComboBox, strokeComboBox, colorPicker);
                   break;
               case "Circle":
                   makeCircle(anchorPane, coordinateX, coordinateY, toolComboBox, strokeComboBox, colorPicker);
                   break;
               case "Rectangle":
                   makeRectangle(anchorPane, coordinateX, coordinateY, toolComboBox, strokeComboBox, colorPicker);
                   break;
               case "Polygon":
                   makePolygon(anchorPane, coordinateX, coordinateY, toolComboBox, strokeComboBox, colorPicker);
                   break;
               default:
                   System.out.println("I don't know...");
           }

       });

        //Zachowanie myszki jeśli nie najeżdżamy na żadną z figur
        anchorPane.setOnMouseMoved( e -> {
            coordinateX.setText("X: " + e.getX());
            coordinateY.setText("Y: " + e.getY());
        });

        anchorPane.setOnMouseExited( e -> {
            coordinateX.setText("X: ");
            coordinateY.setText("Y: ");
        });



       }

    /**
     * Funkcja zmieniająca ustawienia myszki
     * tak by można było edytować figury na ekranie
     * @param node
     * @param coordinateX
     * @param coordinateY
     */
        void makeMove(Node node, Label coordinateX, Label coordinateY){
        node.setOnMousePressed( e-> {

        });

        node.setOnMouseDragged( e -> {
            coordinateX.setText("X: " + e.getX());
            coordinateY.setText("Y: " + e.getY());

        });

        node.setOnMouseReleased( e -> {

        });

        node.setOnMouseClicked(e -> {

        });

       }

    /**
     * Funkcja tworząca linie i dająca jej wszystkie podstawowe parametry
     * @param pane
     * @param coordinateX
     * @param coordinateY
     * @param comboBox
     * @param strokeComboBox
     * @param colorPicker
     */
        void makeLine(Pane pane, Label coordinateX, Label coordinateY, ComboBox<String> comboBox,ComboBox<Double> strokeComboBox ,ColorPicker colorPicker){
           MyLine line = new MyLine(comboBox, strokeComboBox, colorPicker);
            pane.getChildren().add(line);

            pane.setOnMousePressed( e -> {
                line.setStroke(colorPicker.getValue());
                line.setStrokeWidth(strokeComboBox.getValue());

               line.setStartX(e.getX());
               line.setStartY(e.getY());

               line.setEndX(e.getX());
               line.setEndY(e.getY());

           });

            pane.setOnMouseDragged( e -> {
               line.setEndX(e.getX());
               line.setEndY(e.getY());

               coordinateX.setText("X: " + e.getX());
               coordinateY.setText("Y: " + e.getY());
           });

            pane.setOnMouseReleased( e -> {
               line.setEndX(e.getX());
               line.setEndY(e.getY());
               comboBox.setValue("Move");
           });

           line.setOnMove(true);
           line.setOnScale(true);
           line.setContextMenu(true);
           line.setProperCursor(true);


        }

    /**
     * Funkcja tworząca koło i dająca mu wszystkie podstawowe parametry
     * @param pane
     * @param coordinateX
     * @param coordinateY
     * @param comboBox
     * @param strokeComboBox
     * @param colorPicker
     */
        void makeCircle(Pane pane, Label coordinateX, Label coordinateY, ComboBox<String> comboBox, ComboBox<Double> strokeComboBox, ColorPicker colorPicker){
            MyCircle circle = new MyCircle(comboBox, strokeComboBox, colorPicker);
            pane.getChildren().add(circle);

            pane.setOnMousePressed( e -> {
                circle.setFill(colorPicker.getValue());
                circle.setStrokeWidth(strokeComboBox.getValue());

                circle.setCenterX(e.getX());
                circle.setCenterY(e.getY());

                circle.setOldX(e.getX());
                circle.setOldY(e.getY());

                circle.setRadius(0);
            });

            pane.setOnMouseDragged( e -> {
                circle.setRadius(Math.sqrt(Math.pow(circle.getOldX() - e.getX(), 2) + Math.pow(circle.getOldY() - e.getY(), 2)));

                coordinateX.setText("X: " + e.getX());
                coordinateY.setText("Y: " + e.getY());
            });

            pane.setOnMouseReleased( e -> {
                comboBox.setValue("Move");
            });

            circle.setOnMove(true);
            circle.setOnScale(true);
            circle.setContextMenu(true);
            circle.setProperCursor(true);


        }

    /**
     * Funkcja tworząca prostokąt i dająca mu wszystkie podstawowe parametry
     * @param pane
     * @param coordinateX
     * @param coordinateY
     * @param comboBox
     * @param strokeComboBox
     * @param colorPicker
     */
        void makeRectangle(Pane pane, Label coordinateX, Label coordinateY, ComboBox<String> comboBox, ComboBox<Double> strokeComboBox, ColorPicker colorPicker){
            MyRectangle rectangle = new MyRectangle(comboBox, strokeComboBox, colorPicker);
            pane.getChildren().add(rectangle);

            pane.setOnMousePressed( e -> {
                rectangle.setFill(colorPicker.getValue());
                rectangle.setStrokeWidth(strokeComboBox.getValue());

                rectangle.setX(e.getX());
                rectangle.setY(e.getY());

                rectangle.setOldX(e.getX());
                rectangle.setOldY(e.getY());
                System.out.println(rectangle.getOldX());
            });

            pane.setOnMouseDragged( e -> {
                double d = e.getX() - rectangle.getOldX();

                if(d >= 0)
                    rectangle.setWidth(d);
                else{
                    rectangle.setX(e.getX());
                    rectangle.setWidth(-d);
                }

                d = e.getY() - rectangle.getOldY();
                if(d >= 0)
                    rectangle.setHeight(d);
                else{
                    rectangle.setY(e.getY());
                    rectangle.setHeight(-d);
                }

                coordinateX.setText("X: " + e.getX());
                coordinateY.setText("Y: " + e.getY());
            });

            pane.setOnMouseReleased( e -> {
                comboBox.setValue("Move");
            });

            rectangle.setOnMove(true);
            rectangle.setOnScale(true);
            rectangle.setContextMenu(true);
            rectangle.setProperCursor(true);
        }

    /**
     * Funkcja tworząca wielokąt i dająca mu wszystkie podstawowe parametry
     * @param pane
     * @param coordinateX
     * @param coordinateY
     * @param comboBox
     * @param strokeComboBox
     * @param colorPicker
     */
        void makePolygon(Pane pane, Label coordinateX, Label coordinateY, ComboBox<String> comboBox, ComboBox<Double> strokeComboBox, ColorPicker colorPicker){
        MyPolygon polygon = new MyPolygon(comboBox, strokeComboBox, colorPicker);
        pane.getChildren().add(polygon);

        pane.setOnMouseClicked( e -> {

            polygon.setFill(colorPicker.getValue());
            polygon.setStrokeWidth(strokeComboBox.getValue());


            polygon.getPoints().addAll(e.getX(), e.getY());

        });

        polygon.setOnMouseClicked(e -> {
            comboBox.setValue("Move");
            polygon.setOnMouseClicked(ex -> {});
        });


        polygon.setOnMove(true);
        polygon.setOnScale(true);
        polygon.setContextMenu(true);
        polygon.setProperCursor(true);

    }

    /**
     * Funkcja zamykająca program w bezpieczny sposów
     * tj. upewniając się, że użytkownik właśnie tego chciał.
     */
    void closeProgram(){
            boolean b = ConfirmBox.display();
            if(b){
                System.out.println(b);
                window.close();
            }
        }
}
