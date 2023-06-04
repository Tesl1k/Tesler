package resources;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Figures {

    public static List<Rectangle> getRectangles(AnchorPane anchorPane, int amount){
        List<Rectangle> rectangles = new ArrayList<>();

        for (int i = 0; i < amount; i++){
            Rectangle rec = new Rectangle();
            rectangles.add(rec);
            anchorPane.getChildren().add(rec);
            rec.toBack();
        }

        return rectangles;
    }

    public static List<Polygon> getPolygons(AnchorPane anchorPane, int amount){
        List<Polygon> polygons = new ArrayList<>();

        for (int i = 0; i < amount; i++){
            Polygon polygon = new Polygon();
            polygons.add(polygon);
            anchorPane.getChildren().add(polygon);
            polygon.toBack();
        }

        return polygons;
    }

    public static List<Circle> getCircles(AnchorPane anchorPane, int amount){
        List<Circle> circles = new ArrayList<>();

        for (int i = 0; i < amount; i++){
            Circle circle = new Circle();
            circles.add(circle);
            anchorPane.getChildren().add(circle);
            circle.toBack();
        }

        return circles;
    }

}
