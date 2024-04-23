// Student name: Koichi Nakata (ID: knakata595)

package org.example.assignment13_multiplebouncingballs;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Assignment13 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MultipleBallPane ballPane = new MultipleBallPane();
        ballPane.setStyle("-fx-border-color: yellow");

        Button btAdd = new Button("+");
        Button btSub = new Button("-");
        Button btSubLargest = new Button("--");
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(btAdd, btSub, btSubLargest);
        buttonBox.setAlignment(Pos.CENTER);

        btAdd.setOnAction(e -> ballPane.add());
        btSub.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) { ballPane.subtract(); }
        });
        btSubLargest.setOnAction(e -> ballPane.subtractLargest());

        ballPane.setOnMousePressed(e -> ballPane.pause());
        ballPane.setOnMouseReleased(e -> ballPane.play());

        ScrollBar sbSpeed = new ScrollBar();
        sbSpeed.setMax(20);
        sbSpeed.setValue(10);
        ballPane.rateProperty().bind(sbSpeed.valueProperty());

        BorderPane pane = new BorderPane();
        pane.setCenter(ballPane);
        pane.setTop(buttonBox);
        pane.setBottom(sbSpeed);

        Scene scene = new Scene(pane, 450, 550);
        stage.setTitle("Assignment 13: Multiple Bouncing Balls");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private class MultipleBallPane extends Pane {
        private Timeline animation;

        public MultipleBallPane() {
            animation = new Timeline(new KeyFrame(Duration.millis(50), e -> moveBall()));
            animation.setCycleCount(Timeline.INDEFINITE);
            animation.play();
        }

        public void add() {
            Color color = new Color(Math.random(), Math.random(), Math.random(), 0.5);
            double radius = Math.random() * 22 - 2;
            getChildren().add(new Ball(30, 30, radius, color));
        }

        public void subtract() {
            if (getChildren().size() > 0) {
                getChildren().remove(getChildren().size() - 1);
            }
        }

        public void subtractLargest() {
            if (getChildren().size() > 0) {
                ObservableList<Node> list = getChildren();
                PriorityQueue<Ball> ballPQ = new PriorityQueue<>(new BallComparator());

                for (Node node : list) ballPQ.offer((Ball)node);
                getChildren().remove(ballPQ.remove());
            }
        }

        public void play() { animation.play(); }
        public void pause() { animation.pause(); }

        public void increaseSpeed() {
            animation.setRate(animation.getRate() + 0.1);
        }

        public void decreaseSpeed() {
            animation.setRate(
                    animation.getRate() > 0 ? animation.getRate() - 0.1 : 0
            );
        }

        public DoubleProperty rateProperty() { return animation.rateProperty(); }

        protected void moveBall() {
            for (Node node : getChildren()) {
                Ball ball = (Ball)node;
                double radius = ball.getRadius();
                double centerX = ball.getCenterX();
                double centerY = ball.getCenterY();

                if (centerX < radius || centerX > getWidth() - radius) ball.deltaX *= -1;
                if (centerY < radius || centerY > getHeight() - radius) ball.deltaY *= -1;

                ball.setCenterX(ball.deltaX + centerX);
                ball.setCenterY(ball.deltaY + centerY);
            }
        }
    }

    class Ball extends Circle {
        private double deltaX = 1;
        private double deltaY = 1;

        Ball(double x, double y, double radius, Color color) {
            super(x, y, radius);
            setFill(color);
        }
    }

    class BallComparator implements Comparator<Ball> {
        public int compare(Ball b1, Ball b2) {
            if (b1.getRadius() < b2.getRadius()) return 1;
            else if (b1.getRadius() == b2.getRadius()) return 0;
            else return -1;
        }
    }
}