package fi.tuni.concertify;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import fi.tuni.concertify.controllers.UserController;
import fi.tuni.concertify.views.StageManager;

/**
 * The MainApplication class is the entry point for the Concertify application.
 * It extends JavaFX's Application class and is responsible for initializing
 * the application's primary stage, setting up the initial scene, and launching
 * the user interface.
 */
public class MainApplication extends Application {

  /**
   * This method is called when the application starts. It sets up the primary
   * stage,
   * initializes necessary controllers, and switches to the appropriate scene.
   * 
   * @param stage The primary stage where the application will be displayed.
   * @throws IOException If there is an issue loading resources for the scenes.
   */
  public void start(Stage stage) throws IOException {
    StageManager.setStage(stage);

    UserController userController = new UserController();
    StageManager.switchScene(userController.getInitialScene());

    stage.show();
  }

  /**
   * The main method serves as the entry point to launch the JavaFX application.
   * It prints a welcome message and calls the launch method to start the
   * application.
   * 
   * @param args Command-line arguments (not used in this application).
   */
  public static void main(String[] args) {
    System.out.println("Welcome to the Concertify Application");
    launch();
  }
}