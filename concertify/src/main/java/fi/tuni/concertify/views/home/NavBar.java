package fi.tuni.concertify.views.home;

import fi.tuni.concertify.controllers.UserController;
import fi.tuni.concertify.interfaces.Component;
import fi.tuni.concertify.views.ComponentFactory;
import fi.tuni.concertify.views.StageManager;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The NavBar class represents the navigation bar for the application.
 * It provides navigation options to the user, including buttons for different
 * pages
 * such as Home, Search Artist, Search Event, Saved Artists, and Saved Events.
 * The class dynamically updates the active button based on the current page.
 */
public class NavBar implements Component {
  HBox root;
  private VBox navBar = new VBox();
  private VBox buttonsBox = new VBox(30);
  public static StringProperty observableString = new SimpleStringProperty("Home");
  private UserController userController = new UserController();

  /**
   * Constructs a NavBar instance with the provided ComponentFactory and root
   * node.
   * The factory is used to create components like buttons, and the root is the
   * parent
   * node to which this navigation bar is attached.
   * 
   * @param factory The component factory used for creating UI components.
   * @param root    The root node (HBox) used for dimension binding and layout.
   */
  public NavBar(ComponentFactory factory, Node root) {
    this.root = (HBox) root;
  }

  /**
   * Creates a navigation bar button with the given text and associated page
   * switch.
   * The button is styled based on whether it is the currently chosen button.
   * 
   * @param text       The text to display on the button.
   * @param pageSwitch The page to switch to when the button is clicked.
   * @param isChosen   A boolean indicating whether this button is the active one.
   * @return A Button object styled and configured for navigation.
   */
  private Button createNavBarButton(String text, String pageSwitch, Boolean isChosen) {
    Button button = new Button(text);
    button.getStyleClass().add("navbar-button");
    button.setMaxWidth(Double.MAX_VALUE);
    button.setOnAction(e -> {
      NavigationManager.switchMainContent(pageSwitch);
      observableString.setValue(text);
      buttonsBox.getChildren().clear();
      createButtonsBox();
    });
    if (isChosen) {
      button.setStyle("-fx-background-color: #181818; -fx-text-fill: white;");
    }

    return button;
  }

  /**
   * Creates a stylable button for the navigation bar with the provided text and
   * page switch.
   * This method also checks whether the button corresponds to the currently
   * active page.
   * 
   * @param text       The text to display on the button.
   * @param pageSwitch The page to switch to when the button is clicked.
   * @return A stylable button configured for navigation.
   */
  private Button createStylableButton(String text, String pageSwitch) {
    return createNavBarButton(text, pageSwitch, observableString.get().equals(text));
  }

  /**
   * Creates the buttons for the navigation bar and adds them to the buttons
   * container.
   * The buttons represent different pages such as Home, Search Artist, Search
   * Event, etc.
   */
  private void createButtonsBox() {
    Button homeButton = createStylableButton("Home", "home");
    Button searchArtistButton = createStylableButton("Search Artist", "artist-search");
    Button searchEventButton = createStylableButton("Search Event", "event-search");
    Button savedArtistsButton = createStylableButton("Saved Artists", "artist-saved");
    Button savedEventsButton = createStylableButton("Saved Events", "saved-events");

    buttonsBox.getChildren().addAll(
        homeButton,
        searchArtistButton,
        searchEventButton,
        savedArtistsButton,
        savedEventsButton);
  }

  /**
   * Renders the navigation bar UI, including the logo, welcome message, buttons,
   * and logout button.
   * This method sets up the layout, styles, and interactions for the navigation
   * bar.
   */
  @Override
  public void render() {
    navBar.getStylesheets().add(getClass().getResource("/styles/navbar-style.css").toExternalForm());
    navBar.getStyleClass().add("navbar");
    navBar.setAlignment(Pos.TOP_CENTER);
    navBar.prefWidthProperty().bind(root.widthProperty().multiply(0.2));

    navBar.setPadding(new Insets(30, 20, 30, 20));

    Image image = new Image(getClass().getResourceAsStream("/images/logo1-white-trim.png"));
    ImageView homeImage = new ImageView(image);
    homeImage.setPreserveRatio(true);
    homeImage.setSmooth(true);
    homeImage.setFitHeight(80);

    navBar.setSpacing(30);

    String currentUsername = userController.getCurrentUsername();
    Label welcomeLabel = new Label(String.format("Hi, %s", currentUsername));
    welcomeLabel.getStyleClass().add("welcome-label");
    welcomeLabel.setStyle("-fx-font-size: 20px;");

    Button logOutButton = new Button("Log Out");
    logOutButton.getStyleClass().add("logout-button");
    logOutButton.setMaxWidth(Double.MAX_VALUE);
    logOutButton.setOnAction(e -> {
      userController.logout();
      StageManager.switchScene("auth");
    });

    createButtonsBox();

    navBar.getChildren().addAll(
        homeImage,
        welcomeLabel,
        buttonsBox,
        logOutButton);
  }

  /**
   * Returns the navigation bar (VBox) with all the UI elements after they have
   * been rendered.
   * This method should be called to retrieve the entire navigation bar UI for
   * displaying.
   * 
   * @return The navigation bar container (VBox) with all elements.
   */
  @Override
  public VBox get() {
    render();
    return navBar;
  }
}
