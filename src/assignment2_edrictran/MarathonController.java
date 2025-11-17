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
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MarathonController {

    private final List<Runner> runners = new ArrayList<>();
    private final Image[] runnerImages = new Image[5];
    private FadeTransition fade;
    private Image runningGif;

    private final Pane racePane = new Pane();
    private final List<ImageView> runnerViews = new ArrayList<>();
    private final List<TranslateTransition> moveTransitions = new ArrayList<>();
    private boolean raceFinished = false;

    //race track dimensions and coordinates
    private final double trackWidth = 800;
    private final double trackHeight = 350;
    private final double startX = 80;
    private final double finishX = 720;
    private final double laneHeight = 60;
    private final double topPadding = 30;

    private Button startButton;
    private Button pauseButton;
    private Button exitButton;

    private MediaPlayer introPlayer;

    @FXML
    private void initialize() {
        greetingAndStatusLabel.setText("\nWelcome to the Marathon Simulator!");
        controlsVBox.setAlignment(Pos.CENTER);
        
        createRunners();
        loadImages();
        loadIntroSound();
        loadRunningGif();
        controlButtons();
        playSlideshow();
    }

    //Creating the different marathoners / runners
    private void createRunners() {
        runners.add(new Runner("Georges", 1, Color.web("#3f48cc"), randSpeed()));
        runners.add(new Runner("Quentin", 2, Color.web("#ec1c24"), randSpeed()));
        runners.add(new Runner("James", 3, Color.web("#00a8f3"), randSpeed()));
        runners.add(new Runner("Edric", 4, Color.web("#b83dba"), randSpeed()));
        runners.add(new Runner("Alex", 5, Color.web("#0ed145"), randSpeed()));
    }

    //Generating a random speed for each runner (250-400 pixels per second)
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
    
    private void loadVictorySound() {
        File file = new File("sounds/victorySound.mp3");

        String soundPath = file.toURI().toString();

        Media media = new Media(soundPath);
        introPlayer = new MediaPlayer(media);

        introPlayer.setAutoPlay(true);
        introPlayer.setVolume(0.5);
    }

    //Updating the runnerInfoLabel every time the image of the runner switches
    private void updateInfo(int i) {
        Runner r = runners.get(i);
        runnerInfoAndExtraInfoLabel.setStyle("-fx-font-size: 18");
        runnerInfoAndExtraInfoLabel.setText("Runner: " + r.getName() + " | #" + r.getNumber());
    }

    private void playSlideshow() {
        final int[] currentIndex = {0};

        slideshowImageView.setImage(runnerImages[currentIndex[0]]);
        updateInfo(currentIndex[0]);

        fade = new FadeTransition(Duration.millis(2000), slideshowImageView);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        fade.setOnFinished(e -> {
            currentIndex[0]++;

            //Clears the slideshow once the last runner presentation animation 
            //is finished
            if (currentIndex[0] >= runnerImages.length) {
                introPlayer.stop();
                runnerInfoAndExtraInfoLabel.setText("");
                centerStack.setStyle("-fx-background-color: white");
                controlsVBox.setStyle("-fx-background-color: white");

                //Plays the marathon simulator once the slideshow finishes
                marathonSimulator();

                //Adjusts the VBox and StackPane height so that there's no gap
                controlsVBox.setPrefHeight(200);
                centerStack.setPrefHeight(trackHeight + 20);

                //Enables the buttons once the slideshow finishes
                startButton.setDisable(false);
                pauseButton.setDisable(false);
                exitButton.setDisable(false);
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
        //Clears the slideshow
        centerStack.getChildren().clear();

        racePane.setPrefSize(trackWidth, trackHeight);
        racePane.setStyle("-fx-background-color: white; -fx-border-color: black;");
        centerStack.getChildren().add(racePane);
        drawRaceTrack();
        createGifRunners();
    }

    //Making the control buttons and implementing their logic
    private void controlButtons() {
        startButton = new Button("START");
        pauseButton = new Button("PAUSE");
        exitButton = new Button("EXIT");

        startButton.setPrefSize(150, 60);
        pauseButton.setPrefSize(150, 60);
        exitButton.setPrefSize(150, 60);

        startButton.setDisable(true);
        pauseButton.setDisable(true);
        exitButton.setDisable(true);

        startButton.setStyle("-fx-font-size: 16; -fx-font-weight: bold;"
                + "-fx-background-color: green; -fx-text-fill: white");
        pauseButton.setStyle("-fx-font-size: 16; -fx-font-weight: bold;"
                + "-fx-background-color: orange; -fx-text-fill: white");
        exitButton.setStyle("-fx-font-size: 16; -fx-font-weight: bold;"
                + "-fx-background-color: red; -fx-text-fill: white");

        //Putting the buttons in an HBox and centering it
        HBox buttonRow = new HBox(30, startButton, pauseButton, exitButton);
        buttonRow.setAlignment(Pos.CENTER);

        controlsVBox.getChildren().add(0, buttonRow);

        startButton.setOnAction(e -> {
            if (raceFinished) {
                return; // race already finished: ignore
            }
            startButton.setDisable(true);
            pauseButton.setDisable(false);
            startRace();
        });
        pauseButton.setOnAction(e -> {
            if (raceFinished) {
                return; // race already finished: ignore
            }
            for (TranslateTransition tt : moveTransitions) {
                tt.pause();
            }
            startButton.setDisable(false);
            pauseButton.setDisable(true);
            greetingAndStatusLabel.setText("\nRace paused");
        });
        exitButton.setOnAction(e -> {
            Platform.exit();
        });
    }

    //Making the race track
    private void drawRaceTrack() {
        //Making the green start rectangle at the start
        Rectangle startBar = new Rectangle(startX - 60, topPadding, 50, laneHeight * 5);
        startBar.setFill(Color.LIGHTGREEN);
        startBar.setStroke(Color.DARKGREEN);

        //Making the START text inside the green start rectangle
        Text startText = new Text("S\tT\tA\tR\tT");
        startText.setFill(Color.DARKGREEN);
        startText.setStyle("-fx-font-weight: bold;");
        startText.setRotate(-90);

        double centerTextX = startBar.getX() + startBar.getWidth() / 2.0;
        double centerTextY = startBar.getY() + startBar.getHeight() / 2.0;

        double textWidth = startText.getLayoutBounds().getWidth();
        double textHeight = startText.getLayoutBounds().getHeight();

        //Centering the START text
        startText.setX(centerTextX - textWidth / 2.0);
        startText.setY(centerTextY + textHeight / 2.0);

        //Drawing 6 horizontal lines to make 5 lanes for each runner
        for (int i = 0; i <= 5; i++) {
            double y = topPadding + i * laneHeight;
            Line lane = new Line(startX - 20, y, finishX + 40, y);
            lane.setStroke(Color.LIGHTGRAY);
            lane.setStrokeWidth(2);
            racePane.getChildren().add(lane);
        }

        double finishLineWidth = 40;
        double finishLineHeight = laneHeight * 5;
        double checkerSquareSize = 10;

        //Making the finish line
        for (int row = 0; row < finishLineHeight / checkerSquareSize; row++) {
            for (int col = 0; col < finishLineWidth / checkerSquareSize; col++) {
                Rectangle checkerSquare = new Rectangle(finishX + col * checkerSquareSize,
                        topPadding + row * checkerSquareSize,
                        checkerSquareSize, checkerSquareSize);

                //Alternating black and white for the checker colors
                if ((row + col) % 2 == 0) {
                    checkerSquare.setFill(Color.BLACK);
                } else {
                    checkerSquare.setFill(Color.WHITE);
                }

                racePane.getChildren().add(checkerSquare);
            }
        }
        racePane.getChildren().addAll(startBar, startText);
    }

    //Making the animation for the runners using GIF
    private void createGifRunners() {
        for (int i = 0; i < runners.size(); i++) {
            Runner r = runners.get(i);

            //Making an imageview for each runner 
            //Setting their size so that they fit in the lane
            ImageView iv = new ImageView(runningGif);
            iv.setFitWidth(60);
            iv.setFitHeight(60);

            //Centering each runners GIF
            double laneCenterY = topPadding + laneHeight * (i + 0.5);

            iv.setLayoutX(20);
            iv.setLayoutY(laneCenterY - 30);

            racePane.getChildren().add(iv);

            //Storing the imageview so that it can be changed to an image when 
            //arriving to the finish line
            runnerViews.add(iv);

            double distance = finishX - 20;
            double time = distance / r.getSpeed();  // time = distance / speed

            TranslateTransition tt
                    = new TranslateTransition(Duration.seconds(time), iv);
            tt.setToX(distance);
            tt.setCycleCount(1);

            final Runner runner = r;
            tt.setOnFinished(e -> onRunnerFinished(runner));

            moveTransitions.add(tt);
        }
    }

    //Starts the race
    private void startRace() {
        greetingAndStatusLabel.setText("\nRace in progress...");

        for (TranslateTransition tt : moveTransitions) {
            tt.play();
        }
    }
    
    private void onRunnerFinished(Runner r) {
        int idx = runners.indexOf(r);

        //Converts the running GIF to the runner's image when finish
        if (idx < runnerViews.size()) {
            ImageView iv = runnerViews.get(idx);
            iv.setImage(runnerImages[idx]);
        }

        //The first that finishes the race is the winner
        if (!raceFinished) {
            raceFinished = true;

            String msg = "\nCongratulations " + r.getName() + " (#" + r.getNumber() + ")!";
            greetingAndStatusLabel.setText(msg);
            greetingAndStatusLabel.setStyle("-fx-font-size: 20");
            runnerInfoAndExtraInfoLabel.setStyle("-fx-font-size: 12");
            runnerInfoAndExtraInfoLabel.setText("Race finished \nAnimations used:"
                    + " Fade, TranslateTransition, GIF animation"
                    + " \nLayout details: BorderPane as root, Pane for the race track"
                    + ", VBox for the buttons and labels at the bottom");
            showWinnerWindow(r);
        }
    }
    
    //Displays a new window to announce the winner
    private void showWinnerWindow(Runner r) {
        loadVictorySound();
        Label winnerLabel = new Label(
                "Winner: " + r.getName() + " ( #" + r.getNumber() + " )\n"
                + "Congratulations!"
        );
        winnerLabel.setStyle("-fx-font-size: 18; -fx-padding: 20; -fx-text-alignment: center;");

        VBox vbox = new VBox(winnerLabel);
        vbox.setStyle("-fx-alignment: center; -fx-background-color: white;");

        Stage winnerStage = new Stage();
        winnerStage.setTitle("Race Result");

        Scene scene = new Scene(vbox, 300, 180);
        winnerStage.setScene(scene);

        winnerStage.show();
    }

    @FXML
    private StackPane centerStack;

    @FXML
    private VBox controlsVBox;

    @FXML
    private Label greetingAndStatusLabel;

    @FXML
    private Label runnerInfoAndExtraInfoLabel;

    @FXML
    private ImageView slideshowImageView;
}
