package sample;

import javafx.scene.Cursor;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 * Projekt: Kreator graficzny
 * MyCircle.java
 * Klasa tworząca koła dostosowane do mojego programu
 *
 * @author Szymon Wojtaszek
 * @version Lepiej nie będzie
 */


public class MyCircle extends Circle {
    private double oldX, oldY;
    private double oldTranslateX, oldTranslateY;
    private ComboBox<String> toolComboBox;
    private ComboBox<Double> strokeComboBox;
    private ColorPicker colorPicker;

    /**
     * Funkcja dostosowująca kursos do sytuacji w programie
     * @param b
     */
    public void setProperCursor(boolean b){
        if(b){
            setOnMouseEntered(e -> {
                if(toolComboBox.getValue() == "Move")
                    setCursor(Cursor.HAND);
                else
                    setCursor(Cursor.CROSSHAIR);
            });
        }else{
            setOnMouseEntered(e -> {});
        }
    }

    /**
     * Funkcja włączająca ruch obiektów za pomocą myszy
     * @param b
     */
    public void setOnMove(boolean b){
        if(b) {
            setOnMousePressed(e -> {
                if(e.getButton() == MouseButton.PRIMARY && toolComboBox.getValue() == "Move"){
                    setOldX(e.getSceneX());
                    setOldY(e.getSceneY());
                    setOldTranslateX(getTranslateX());
                    setOldTranslateY(getTranslateY());

                    setCursor(Cursor.MOVE);
                }
            });
            setOnMouseDragged(e -> {
                if(e.getButton() == MouseButton.PRIMARY && toolComboBox.getValue() == "Move"){
                    double newTranslateX = getOldTranslateX() + e.getSceneX() - getOldX();
                    double newTranslateY = getOldTranslateY() + e.getSceneY() - getOldY();

                    setTranslateX(newTranslateX);
                    setTranslateY(newTranslateY);
                }
            });
            setOnMouseReleased(e -> {
                setCursor(Cursor.HAND);
            });
        } else {
            setOnMousePressed(e -> {});
            setOnMouseDragged(e -> {});
            setOnMouseReleased(e -> {});
        }

    }

    /**
     * Funkcja włączająca powiekszanie obiektów za pomocą scrolla
     * @param b
     */
    public void setOnScale(boolean b){
        if(b){
            setOnScroll( e -> {
                double zoomFactor = 1.05;
                if(0 > e.getDeltaY()) {
                    zoomFactor = 2 - zoomFactor;
                }
                setScaleX(getScaleX() * zoomFactor);
                setScaleY(getScaleY() * zoomFactor);

            });
        } else {
            setOnScroll( e -> {});
        }
    }

    /**
     * Funkcja włączająca menu kontekstowe obiektów
     * @param b
     */
    public void setContextMenu(boolean b){
        if(b){
            ContextMenu contextMenu = new ContextMenu();

            MenuItem widthStrokeMenuItem = new MenuItem("Set width of stroke");
            widthStrokeMenuItem.setOnAction( e -> {
                setStrokeWidth(strokeComboBox.getValue());
            });

            MenuItem colorStrokeMenuItem = new MenuItem("Set color of stroke");
            colorStrokeMenuItem.setOnAction( e-> {
                setStroke(colorPicker.getValue());
                System.out.println("set stroke");
            });

            MenuItem fillMenuItem = new MenuItem("Fill in color");
            fillMenuItem.setOnAction( e -> {
                setFill(colorPicker.getValue());
                System.out.println("set Color");
            });

            MenuItem deleteMenuItem = new MenuItem("Delete");
            deleteMenuItem.setOnAction( e -> {
                ((Pane)this.getParent()).getChildren().remove(this);
            });

            contextMenu.getItems().addAll(widthStrokeMenuItem, colorStrokeMenuItem ,fillMenuItem, deleteMenuItem);

            setOnContextMenuRequested( e -> {
                contextMenu.show(this, e.getScreenX(), e.getScreenY());
            });
        }
    }


    public double getOldX() {
        return oldX;
    }

    public void setOldX(double oldX) {
        this.oldX = oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public void setOldY(double oldY) {
        this.oldY = oldY;
    }

    public double getOldTranslateX() {
        return oldTranslateX;
    }

    public void setOldTranslateX(double oldTranslateX) {
        this.oldTranslateX = oldTranslateX;
    }

    public double getOldTranslateY() {
        return oldTranslateY;
    }

    public void setOldTranslateY(double oldTranslateY) {
        this.oldTranslateY = oldTranslateY;
    }


    MyCircle(ComboBox<String> toolComboBox, ComboBox<Double> strokeComboBox, ColorPicker colorPicker){
        this.toolComboBox = toolComboBox;
        this.strokeComboBox = strokeComboBox;
        this.colorPicker = colorPicker;
        setCursor(Cursor.HAND);
    }
}
