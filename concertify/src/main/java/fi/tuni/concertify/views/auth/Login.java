package fi.tuni.concertify.views.auth;

import fi.tuni.concertify.controllers.UserController;
import fi.tuni.concertify.interfaces.Component;
import fi.tuni.concertify.views.StageManager;
import fi.tuni.concertify.views.home.NavigationManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Represents the login form for user authentication, including fields for
 * username and password.
 * Implements the Component interface to render and retrieve the login form.
 */
public class Login implements Component {
  VBox loginField = new VBox(20);
  EventHandler<MouseEvent> switchToSignUp;
  UserController userController = new UserController();

  /**
   * Constructs a Login instance, initializing the event handler for switching to
   * the sign-up form.
   * 
   * @param switchToSignUp An event handler for switching from the login form to
   *                       the sign-up form.
   */
  public Login(EventHandler<MouseEvent> switchToSignUp) {
    this.switchToSignUp = switchToSignUp;
  }

  /**
   * Renders the login form with input fields for username and password, a login
   * button,
   * and a link to switch to the sign-up form. Handles user login via the
   * UserController.
   */
  public void render() {
    TextField userNameField = new TextField();
    userNameField.setPromptText("Username");
    userNameField.getStyleClass().add("text-field");

    PasswordField passwordField = new PasswordField();
    passwordField.setPromptText("Password");
    passwordField.getStyleClass().add("text-field");

    Text alertText = new Text("");

    Button loginButton = new Button("Login");
    loginButton.setPrefWidth(200);
    loginButton.getStyleClass().add("auth-button");

    EventHandler<ActionEvent> loginHandler = e -> {
      try {
        String status = userController.login(userNameField, passwordField);
        if (status.equals("success")) {
          alertText.setText("");
          NavigationManager.home.render();
          StageManager.switchScene("home");
        } else {
          alertText.setText(status);
        }
      } catch (Exception error) {
        error.printStackTrace();
      }

      userNameField.clear();
      passwordField.clear();
    };

    loginButton.setOnAction(loginHandler);

    HBox helpText = new HBox(2);
    Text pre = new Text("New to Concertify?");
    Text post = new Text("Sign up now!");
    pre.getStyleClass().add("change-form-text");
    post.getStyleClass().add("call-to-text");
    helpText.getChildren().addAll(pre, post);
    helpText.setAlignment(Pos.CENTER);

    post.setOnMouseClicked(switchToSignUp);

    loginField.getChildren().addAll(alertText, userNameField, passwordField, loginButton, helpText);
    loginField.getStyleClass().add("login-field");

  }

  /**
   * Returns the VBox containing the login form components.
   * Ensures the login form is rendered before being returned.
   *
   * @return The VBox containing the login form.
   */
  @Override
  public VBox get() {
    render();
    return loginField;
  }
}
