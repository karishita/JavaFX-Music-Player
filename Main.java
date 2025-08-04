

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DialogPane;

import java.util.Arrays;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.animation.PauseTransition;
import javafx.util.Duration;





public class Main extends Application {


     private void playCurrentSong() {

    if (playlist.getCurrentSongFilePath() == null) return;

    // Stop any existing playback
    if (mediaPlayer != null) {
        mediaPlayer.stop();
    }

    String path = playlist.getCurrentSongFilePath();
    Media media = new Media(new File(path).toURI().toString());

    media.setOnError(() -> {
        System.out.println("Media error: " + media.getError());
    });

    mediaPlayer = new MediaPlayer(media);
    // Volume binding
    mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty());

    // Update time and progress bar
    mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
    if (!isSeeking) {
        Duration current = newTime;
        Duration total = mediaPlayer.getTotalDuration();

        if (total != null && !total.isUnknown()) {
            double progress = current.toMillis() / total.toMillis() * 100;
            progressBar.setDisable(false);
            progressBar.setValue(progress);
            timeLabel.setText(formatTime(current) + " / " + formatTime(total));
        }
    }
});



    mediaPlayer.setOnError(() -> {
        System.out.println("Player error: " + mediaPlayer.getError());
    });

    // Auto play next song when current ends
    mediaPlayer.setOnEndOfMedia(() -> {
        playlist.nextSong();
        playCurrentSong();  // Recursively play next
    });

    mediaPlayer.play();
    currentSongLabel.setText("Now Playing: " + playlist.getCurrentSongName());
    isPlaying = true;
    playButton.setText("Pause"); 
}

private String formatTime(Duration duration) {
    int minutes = (int) duration.toMinutes();
    int seconds = (int) duration.toSeconds() % 60;
    return String.format("%02d:%02d", minutes, seconds);
}

      PlayList playlist=new PlayList();
      MediaPlayer mediaPlayer;
      private boolean isPlaying = false;
      private boolean isSeeking = false;
       private Button playButton;
        private Button nextButton;
       private Button prevButton;
        private Button addButton ;
        private Button searchButton ;
        private Button showButton ;
        private Button deleteButton;
        private Label deleteStatus;
        private Slider progressBar;
        private Label timeLabel;
        

        private Slider volumeSlider;
       private Label volumeLabel;
       
       private static final String PLAYLIST_FILE = "playlist.txt";

      private void savePlaylistToFile() {
    try (PrintWriter writer = new PrintWriter(new FileWriter(PLAYLIST_FILE))) {
        SongNode temp = playlist.getHead();
        int flag = 0;
        while (temp != null && (temp != playlist.getHead() || flag == 0)) {
            flag = 1;
            writer.println(temp.songName + "|" + temp.filePath);
            temp = temp.next;
        }
    } catch (IOException e) {
        System.out.println("Failed to save playlist: " + e.getMessage());
    }
}

private void loadPlaylistFromFile() {
    File file = new File(PLAYLIST_FILE);
    if (!file.exists()) return;

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|", 2);
            if (parts.length == 2) {
                playlist.addSong(parts[0], parts[1]);
            }
        }
    } catch (IOException e) {
        System.out.println("Failed to load playlist: " + e.getMessage());
    }
}

    Label currentSongLabel;
    @Override
    public void start(Stage primaryStage) {
        //adding songs
        loadPlaylistFromFile();


        // Add the song names and file paths here

   // playlist.addSong("Ami Kan Pete Roi", "/home/ishita/MusicPlayer/audio/Ami Kaan Pete Roi.wav");
    
   
    Label titleLabel = new Label(" My Music Player");
     titleLabel.setId("titleLabel");  
        //adding buttons
        currentSongLabel=new Label("Current Song: "+ playlist.getCurrent()+"\n No song playing");
       
        //currentSongLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #e762bbff; -fx-font-weight: bold;");

        playButton=new Button("Play");
        nextButton=new Button("Next");
        prevButton=new Button("Previous");
         addButton = new Button("Add Song");
         searchButton = new Button("Search Song");
         showButton = new Button("Show Playlist");
        deleteButton = new Button("Delete Song");
         deleteStatus = new Label();
         

         progressBar = new Slider();
        progressBar = new Slider();
        progressBar.setMin(0);
        progressBar.setMax(100);
        progressBar.setValue(0);
        progressBar.setPrefWidth(300);
        progressBar.setDisable(true); // user shouldn't drag this yet


        //Progess bar dragging
         progressBar.setOnMousePressed(e -> {
            isSeeking = true;
        });

        // While dragging
        progressBar.setOnMouseDragged(e -> {
            isSeeking = true;
        });

        // User releases mouse → Seek now
        progressBar.setOnMouseReleased(e -> {
            if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
                double seekTo = progressBar.getValue() / 100.0;
                Duration total = mediaPlayer.getTotalDuration();
                mediaPlayer.seek(total.multiply(seekTo));
            }
            isSeeking = false;
        });

        timeLabel = new Label("00:00 / 00:00");

        VBox progressBox = new VBox(5, progressBar, timeLabel);
        progressBox.setAlignment(Pos.CENTER);

        // Volume control
        volumeLabel = new Label("Volume:");
        volumeSlider = new Slider(0, 1, 0.5);  // min, max, default
        volumeSlider.setPrefWidth(150);
     
VBox volumeBox = new VBox(5, volumeLabel, volumeSlider);
volumeBox.setAlignment(Pos.CENTER);
       

        // wiring the button to the functions

        //playbutton

    playButton.setOnAction(e -> {
    if (mediaPlayer == null) {
        // Nothing is playing yet, start the first song
        playCurrentSong();
    } else {
        MediaPlayer.Status status = mediaPlayer.getStatus();

        if (status == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            isPlaying = false;
            playButton.setText("Play");
            currentSongLabel.setText("Paused: " + playlist.getCurrentSongName());
        } else if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.STOPPED) {
            mediaPlayer.play();
            isPlaying = true;
            playButton.setText("Pause");
            currentSongLabel.setText("Now Playing: " + playlist.getCurrentSongName());
        }
    }
});


//next button

nextButton.setOnAction(e -> {
     if (mediaPlayer != null) {
        mediaPlayer.stop();
        mediaPlayer.dispose();
    }
    playlist.nextSong();

    String name = playlist.getCurrentSongName();
    currentSongLabel.setText(" Next Song: " + name);
    playCurrentSong();
});
 
//previous button
prevButton.setOnAction(e -> {
     if (mediaPlayer != null) {
        mediaPlayer.stop();
        mediaPlayer.dispose();
    }
    playlist.prevSong(); 
    String name = playlist.getCurrentSongName();
    currentSongLabel.setText(" Previous Song: " + name);
    playCurrentSong();
});

// add songs
addButton.setOnAction(e -> {
    // Ask for song name
    TextInputDialog nameDialog = new TextInputDialog();
    DialogPane pane = nameDialog.getDialogPane();
pane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
pane.getStyleClass().add("custom-dialog");
    nameDialog.setTitle("Add New Song");
    nameDialog.setHeaderText(null);
    nameDialog.setContentText("Enter song name:");
    
    nameDialog.showAndWait().ifPresent(songName -> {

        // Ask for file path
        TextInputDialog pathDialog = new TextInputDialog("/home/ishita/MusicPlayer/audio/" + songName + ".wav");
        DialogPane pane1 = pathDialog.getDialogPane();
pane1.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
pane1.getStyleClass().add("custom-dialog");
        pathDialog.setTitle("Add File Path");
        pathDialog.setHeaderText(null);
        pathDialog.setContentText("Enter full path to the .wav file:");

        pathDialog.showAndWait().ifPresent(filePath -> {
            // Ask where to insert
            List<String> choices = Arrays.asList("Beginning", "End", "After Current");
            ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("End", choices);
            DialogPane pane2 = choiceDialog.getDialogPane();
pane2.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
pane2.getStyleClass().add("custom-dialog");
            choiceDialog.setTitle("Insert Position");
            choiceDialog.setHeaderText(null);
            choiceDialog.setContentText("Where do you want to insert the song?");

            choiceDialog.showAndWait().ifPresent(position -> {
                switch (position) {
                    case "Beginning":
                        playlist.insertBegin(songName, filePath);
                        break;
                    case "End":
                        playlist.addSong(songName, filePath);
                        break;
                    case "After Current":
                        playlist.insertCurrent(songName, filePath);
                        break;
                }
                currentSongLabel.setText("Current Song: " + playlist.getCurrentSongName());
            });
        });
    });
});

// Search Songs
searchButton.setOnAction(e -> {
    TextInputDialog inputDialog = new TextInputDialog();
    DialogPane pane = inputDialog.getDialogPane();
pane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
pane.getStyleClass().add("custom-dialog");
    inputDialog.setTitle("Search Song");
    inputDialog.setHeaderText(null);
    inputDialog.setContentText("Enter song name to search:");

    inputDialog.showAndWait().ifPresent(songName -> {
        boolean found = playlist.searchAndPlay(songName);
        if (found) {
            currentSongLabel.setText("Current Song: " + playlist.getCurrentSongName());
             if (mediaPlayer != null) {
        mediaPlayer.stop();
        mediaPlayer.dispose();
        playCurrentSong();
    }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Not Found");
            alert.setHeaderText(null);
            alert.setContentText("Song not found in playlist.");
            alert.showAndWait();
        }
    });
});
// List all songs

showButton.setOnAction(e -> {
    String songs = playlist.showPlaylist(); 
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Playlist");
    alert.setHeaderText("All Songs in Playlist:");
    alert.setContentText(songs.isEmpty() ? "Playlist is empty." : songs);
    DialogPane pane = alert.getDialogPane();
   pane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
   pane.getStyleClass().add("custom-dialog");
    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE); // to allow multiline
    alert.showAndWait();
});

deleteButton.setOnAction(e -> {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Delete Song");
    dialog.setHeaderText("Enter the song name to delete:");
    dialog.setContentText("Song name:");
    DialogPane pane = dialog.getDialogPane();
   pane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
   pane.getStyleClass().add("custom-dialog");

    dialog.showAndWait().ifPresent(name -> {
        name = name.trim();
        if(name.equalsIgnoreCase(playlist.getCurrent().songName))
        {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            playlist.nextSong();
            playCurrentSong();
        }

        if (name.isEmpty()) {
            deleteStatus.setText("Field empty!");
        } else {
            boolean deleted = playlist.deletebyName(name);
            if (deleted) {
                deleteStatus.setText("Deleted: " + name);
                // Optionally refresh playlist view here
            } else {
                deleteStatus.setText("No such song found.");
            }
        }
         // DeleteStatus disappears after 3sec
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
       pause.setOnFinished(event -> deleteStatus.setText(""));  // Clear the message
       pause.play();
    });
});



        VBox layout =new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        //layout.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #f1f8e9);");
        HBox playbackControls = new HBox(10, prevButton, playButton, nextButton);
       playbackControls.setAlignment(Pos.CENTER);

     HBox utilityControls = new HBox(10, addButton, searchButton, showButton);
     utilityControls.setAlignment(Pos.CENTER);

        VBox deleteBox = new VBox(5, deleteButton, deleteStatus);
        deleteBox.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(
       titleLabel,
      currentSongLabel,
      playbackControls,
      utilityControls,
     progressBox,
     volumeBox,
     deleteBox
);
        Scene scene = new Scene(layout, 900, 700);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> savePlaylistToFile());

    }
  


    public static void main(String[] args) {
        launch(args);
    }
}




        
    
    

