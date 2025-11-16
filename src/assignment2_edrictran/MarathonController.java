/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment2_edrictran;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MarathonController {

    private final List<Runner> runners = new ArrayList<>();
    private Image[] runnerImages = new Image[5];
    private FadeTransition fade;
    private Image runningGif;

    private Pane racePane;
    private final List<ImageView> runnerViews = new ArrayList<>();
    private final List<TranslateTransition> moveTransitions = new ArrayList<>();
    private boolean raceFinished = false;

    //race track 
    private final double trackWidth = 800;
    private final double trackHeight = 350;
    private final double startX = 80;
    private final double finishX = 720;
    private final double laneHeight = 60;
    private final double topPadding = 30;

    private MediaPlayer introPlayer;

    @FXML
    private void initialize() {
        createRunners();
        loadImages();
        loadIntroSound();
        loadRunningGif();
        playSlideshow();
    }

    private void createRunners() {
        runners.add(new Runner("Georges", 1, Color.web("#3f48cc"), randSpeed()));
        runners.add(new Runner("Quentin", 2, Color.web("#ec1c24"), randSpeed()));
        runners.add(new Runner("James", 3, Color.web("#00a8f3"), randSpeed()));
        runners.add(new Runner("Edric", 4, Color.web("#b83dba"), randSpeed()));
        runners.add(new Runner("Alex", 5, Color.web("#0ed145"), randSpeed()));
    }

    private double randSpeed() {
        return 250 + Math.random() * 150;
    }

    private void loadImages() {

        for (int i = 0; i < runnerImages.length; i++) {
            int imageNumber = i + 1;
            String path = "file:images/runner" + imageNumber + ".png";
            runnerImages[i] = new Image(path);
        }
    }

    private void loadIntroSound() {
        File file = new File("sounds/applause.mp3");

        String soundPath = file.toURI().toString();

        Media media = new Media(soundPath);
        introPlayer = new MediaPlayer(media);

        introPlayer.setAutoPlay(true);
        introPlayer.setVolume(0.5);

    }

    private void updateInfo(int i) {
        Runner r = runners.get(i);
        runnerInfoLabel.setText("Runner: " + r.getName() + " | #" + r.getNumber());
    }

    private void playSlideshow() {
        final int[] currentIndex = {4};

        slideshowImageView.setImage(runnerImages[currentIndex[0]]);
        updateInfo(currentIndex[0]);

        fade = new FadeTransition(Duration.millis(2000), slideshowImageView);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        fade.setOnFinished(e -> {
            currentIndex[0]++;

            if (currentIndex[0] >= runnerImages.length) {
                if (introPlayer != null) {
                    introPlayer.stop();
                }
                greetingLabel.setText("");
                runnerInfoLabel.setText("");
                centerStack.setStyle("-fx-background-color: white");
                controlsVBox.setStyle("-fx-background-color: white");
                return;
            }

            slideshowImageView.setImage(runnerImages[currentIndex[0]]);
            slideshowImageView.setOpacity(1.0);
            updateInfo(currentIndex[0]);

            fade.playFromStart();
        });

        fade.play();
    }

    private void loadRunningGif() {
        runningGif = new Image("file:images/running.gif");
    }

    private void marathonSimulator() {
        centerStack.getChildren().clear();

        racePane = new Pane();
        racePane.setPrefSize(trackWidth, trackHeight);
        racePane.setStyle("-fx-background-color: white; -fx-border-color: black;");
        centerStack.getChildren().add(racePane);

        //drawRaceTrack();
        //createGifRunners();
        //startRace();
    }

    private void drawTrack() {
    Rectangle startBar = new Rectangle(startX - 60, topPadding, 50, laneHeight * 5);
    startBar.setFill(Color.LIGHTGREEN);
    startBar.setStroke(Color.DARKGREEN);

    Text startText = new Text("START");
    startText.setFill(Color.DARKGREEN);
    startText.setStyle("-fx-font-weight: bold;");
    startText.setRotate(-90);

    double barCenterX = startBar.getX() + startBar.getWidth() / 2.0;
    double barCenterY = startBar.getY() + startBar.getHeight() / 2.0;

    double textWidth = startText.getLayoutBounds().getWidth();
    double textHeight = startText.getLayoutBounds().getHeight();

    startText.setX(barCenterX - textWidth / 2.0);
    startText.setY(barCenterY + textHeight / 2.0);

    for (int i = 0; i <= 5; i++) {
        double y = topPadding + i * laneHeight;
        Line lane = new Line(startX - 20, y, finishX + 40, y);
        lane.setStroke(Color.LIGHTGRAY);
        lane.setStrokeWidth(2);
        racePane.getChildren().add(lane);
    }

    double finishLineWidth = 40;
    double finishLineHeight = laneHeight * 5;
    double cellSize = 10;

    for (int row = 0; row < finishLineHeight / cellSize; row++) {
        for (int col = 0; col < finishLineWidth / cellSize; col++) {
            Rectangle cell = new Rectangle(
                    finishX + col * cellSize,
                    topPadding + row * cellSize,
                    cellSize, cellSize
            );

            if ((row + col) % 2 == 0) {
                cell.setFill(Color.BLACK);
            } else {
                cell.setFill(Color.WHITE);
            }

            racePane.getChildren().add(cell);
        }
    }
    racePane.getChildren().addAll(startBar, startText);
}

    @FXML
    private StackPane centerStack;

    @FXML
    private VBox controlsVBox;

    @FXML
    private Label greetingLabel;

    @FXML
    private Label runnerInfoLabel;

    @FXML
    private ImageView slideshowImageView;
}
