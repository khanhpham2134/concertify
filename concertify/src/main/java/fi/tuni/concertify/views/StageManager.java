package fi.tuni.concertify.views;

import fi.tuni.concertify.interfaces.Component;
import fi.tuni.concertify.views.home.NavigationManager;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * The StageManager class manages the JavaFX Stage and Scene for the Concertify
 * application.
 * It handles setting the primary stage, creating scenes for different views
 * (authentication, home, etc.),
 * and switching between scenes based on user interactions.
 * 
 * The class acts as a controller for the application's user interface, updating
 * the stage with
 * different views such as the authentication view and the home page.
 */
public class StageManager {
  private static final int WINDOW_HEIGHT = 720;
  private static final int WINDOW_WIDTH = 1280;

  private static Stage stage;
  public static ComponentFactory componentFactory = new ComponentFactory();
  public static Component authView = componentFactory.createAuthView();

  private static Scene authScene = createScene((HBox) authView.get());
  private static Scene homeScene = createScene((HBox) NavigationManager.homeComponent);

  /**
   * Creates a Scene with the specified root node and default dimensions.
   * 
   * @param root The root node to display in the scene.
   * @return A new Scene object configured with the root and styles.
   */
  private static Scene createScene(HBox root) {
    Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    scene.getStylesheets().add(StageManager.class.getResource("/styles/app.css").toExternalForm());
    return scene;
  }

  /**
   * Sets the primary stage for the application.
   * 
   * @param primaryStage The primary stage to set.
   */
  public static void setStage(Stage primaryStage) {
    stage = primaryStage;
  }

  /**
   * Switches between different scenes on the primary stage.
   * 
   * @param scene A string identifying the scene to switch to. Valid values are
   *              "auth" and "home".
   */
  public static void switchScene(String scene) {
    if (scene.equals("auth")) {
      stage.setScene(authScene);
    } else if (scene.equals("home")) {
      stage.setScene(homeScene);
      NavigationManager.switchMainContent("home");
    }
  }
}
