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
 * Represents the sign-up form for user registration, including fields for
 * username and password.
 * Implements the Component interface to render and retrieve the sign-up form.
 */
public class SignUp implements Component {
  VBox signUp = new VBox(20);
  UserController userController = new UserController();
  EventHandler<MouseEvent> switchToLogin;

  /**
   * Constructs a SignUp instance, initializing the event handler for switching to
   * the login form.
   * 
   * @param switchToLogin An event handler for switching from the sign-up form to
   *                      the login form.
   */
  public SignUp(EventHandler<MouseEvent> switchToLogin) {
    this.switchToLogin = switchToLogin;
  }

  /**
   * Renders the sign-up form with input fields for username and password, a
   * sign-up button,
   * and a link to switch to the login form. Handles user sign-up via the
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

    Button signUpButton = new Button("Sign up");
    signUpButton.setPrefWidth(200);
    signUpButton.getStyleClass().add("auth-button");

    EventHandler<ActionEvent> signUpHandler = e -> {
      try {
        String status = userController.signup(userNameField, passwordField);
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

    signUpButton.setOnAction(signUpHandler);

    HBox helpText = new HBox(2);
    Text pre = new Text("Already a member?");
    Text post = new Text("Log in here!");
    post.getStyleClass().add("call-to-text");
    pre.getStyleClass().add("change-form-text");
    helpText.getChildren().addAll(pre, post);
    helpText.setAlignment(Pos.CENTER);

    post.setOnMouseClicked(switchToLogin);

    signUp.getChildren().addAll(alertText, userNameField, passwordField, signUpButton, helpText);
    signUp.getStyleClass().add("login-field");
  }

  /**
   * Returns the VBox containing the sign-up form components.
   * Ensures the sign-up form is rendered before being returned.
   *
   * @return The VBox containing the sign-up form.
   */
  @Override
  public VBox get() {
    render();
    return signUp;
  }
}
