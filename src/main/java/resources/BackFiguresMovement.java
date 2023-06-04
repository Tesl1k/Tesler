package resources;

import configs.Config;
import javafx.animation.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class BackFiguresMovement {

    private static Config config;
    private static List<Shape> shapesToRemove;

    public static void drowing(AnchorPane anchorPane, Color color){

        config = new Config("src/main/resources/properties/figureDrow.properties");

        if(config.get("figureType").equals("Rectangles")){
            shapesToRemove = new ArrayList<>();
            anchorPane.getChildren().forEach(node -> {
                if (node instanceof Shape) {
                    shapesToRemove.add((Shape) node);
                }
            });
            shapesToRemove.forEach(shape -> anchorPane.getChildren().remove(shape));
            setRectangleMoving(Figures.getRectangles(anchorPane, 20), 1920, color);
        }
        if(config.get("figureType").equals("Triangles")){
            shapesToRemove = new ArrayList<>();
            anchorPane.getChildren().forEach(node -> {
                if (node instanceof Shape) {
                    shapesToRemove.add((Shape) node);
                }
            });
            shapesToRemove.forEach(shape -> anchorPane.getChildren().remove(shape));
            setPolygonMoving(Figures.getPolygons(anchorPane, 20), 1920, color);
        }
        if(config.get("figureType").equals("Circles")){
            shapesToRemove = new ArrayList<>();
            anchorPane.getChildren().forEach(node -> {
                if (node instanceof Shape) {
                    shapesToRemove.add((Shape) node);
                }
            });
            shapesToRemove.forEach(shape -> anchorPane.getChildren().remove(shape));
            setCircleMoving(Figures.getCircles(anchorPane, 20), 1920, color);
        }
    }

    public static void setRectangleMoving(List<Rectangle> rectangles, double width, Color color){

        for (int i = 0; i < rectangles.size(); i++){

            rectangles.remove(rectangles);

            rectangles.get(i).setWidth(Math.random() * 100 + 80);
            rectangles.get(i).setHeight(rectangles.get(i).getWidth());

            if (i % 2 == 0)
                rectangles.get(i).setX(Math.random() * width / 2);
            else
                rectangles.get(i).setX(Math.random() * width / 2  + width / 2);

            rectangles.get(i).setY(Math.random() * 0 - 200);
            rectangles.get(i).setFill(new LinearGradient(0, 0, 1, 0, true,
                    CycleMethod.NO_CYCLE, new Stop(0, color), new Stop(1, Color.web("#16212b"))
            ));

            rectangles.get(i).setOpacity(Math.random() * 0.4 + 0.4);


        }

        List<Shape> list = new ArrayList<>();

        for (int i = 0; i < rectangles.size(); i++){
            list.add(rectangles.get(i));
        }

        setMov(list);
    }

    public static void setPolygonMoving(List<Polygon> polygons, double width, Color color){

        for (int i = 0; i < polygons.size(); i++){

            polygons.remove(polygons);

            double x1 = Math.random() * (width - 150);
            double y1 = Math.random() * 200 - 400;
            double x2 = x1 + 150;
            double y2 = y1 + 150;

            polygons.get(i).getPoints().addAll(new Double[]{
                    x1, y1, x2, y2, x1, y2});

            polygons.get(i).setFill(new LinearGradient(0, 0, 1, 0, true,
                    CycleMethod.NO_CYCLE, new Stop(0, color), new Stop(1, Color.web("#16212b"))
            ));
            polygons.get(i).setOpacity(Math.random() * 0.4 + 0.4);
        }

        List<Shape> list = new ArrayList<>();

        list.addAll(polygons);

        setMov(list);
    }

    public static void setCircleMoving(List<Circle> circles, double width, Color color){

        for (int i = 0; i < circles.size(); i++){

            circles.remove(circles);

            double x = Math.random() * (width - 150);
            double y = Math.random() * 200 - 400;
            double r = Math.random() * 50 + 50;

            circles.get(i).setCenterX(x);
            circles.get(i).setCenterY(y);
            circles.get(i).setRadius(r);

            circles.get(i).setFill(new LinearGradient(0, 0, 1, 0, true,
                    CycleMethod.NO_CYCLE, new Stop(0, color), new Stop(1, Color.web("#16212b"))
            ));
            circles.get(i).setOpacity(Math.random() * 0.4 + 0.4);

        }

        List<Shape> list = new ArrayList<>();

        list.addAll(circles);

        setMov(list);
    }

    private static void setMov(List<Shape> shapes){
        for (int i = 0; i < shapes.size(); i++){

            TranslateTransition transition = new TranslateTransition();
            RotateTransition rotate = new RotateTransition();
            PauseTransition pause = new PauseTransition(Duration.seconds(i / 2));

            transition.setNode(shapes.get(i));
            transition.setDuration(Duration.millis(Math.random() * 11001 + 5000));
            transition.setCycleCount(TranslateTransition.INDEFINITE);
            transition.setByY(1600);

            rotate.setNode(shapes.get(i));
            rotate.setDuration(Duration.millis(Math.random() * 12001 + 6000));
            rotate.setCycleCount(Animation.INDEFINITE);
            rotate.setByAngle(360);


            pause.setOnFinished(event -> transition.play());
            pause.play();
            rotate.play();




        }
    }

    
}
