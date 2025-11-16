/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment2_edrictran;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MarathonController {

    private final List<Runner> runners = new ArrayList<>();
    private Image[] runnerImages = new Image[5];
    private FadeTransition fade;

    private Media introMedia;
    private MediaPlayer introPlayer;

    @FXML
    private void initialize() {
        createRunners();
        loadImages();
        loadIntroSound();
        playSlideshow();
    }

    private void createRunners() {
        runners.add(new Runner("Georges", 1, javafx.scene.paint.Color.BLUE, randSpeed()));
        runners.add(new Runner("Quentin", 2, javafx.scene.paint.Color.RED, randSpeed()));
        runners.add(new Runner("James", 3, javafx.scene.paint.Color.CYAN, randSpeed()));
        runners.add(new Runner("Edric", 4, javafx.scene.paint.Color.PURPLE, randSpeed()));
        runners.add(new Runner("Alex", 5, javafx.scene.paint.Color.GREEN, randSpeed()));
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
        final int[] currentIndex = {0};

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
                return;
            }

            slideshowImageView.setImage(runnerImages[currentIndex[0]]);
            slideshowImageView.setOpacity(1.0);
            updateInfo(currentIndex[0]);

            fade.playFromStart();
        });

        fade.play();
    }

    @FXML
    private StackPane centerStack;

    @FXML
    private Label greetingLabel;

    @FXML
    private Label runnerInfoLabel;

    @FXML
    private ImageView slideshowImageView;
}
