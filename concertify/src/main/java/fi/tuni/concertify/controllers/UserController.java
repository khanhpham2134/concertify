package fi.tuni.concertify.controllers;

import fi.tuni.concertify.models.User;
import fi.tuni.concertify.services.UserService;
import javafx.scene.control.TextField;

/**
 * The UserController class handles user-related actions such as authentication,
 * signing up, and logging out. It acts as an intermediary between the UI and
 * UserService.
 */
public class UserController {
  private UserService userService = new UserService();

  /**
   * Determines the initial scene based on the user's authentication status.
   *
   * @return "home" if a user is logged in, otherwise "auth".
   */
  public String getInitialScene() {
    return (userService.getCurrentUser() != null) ? "home" : "auth";
  }

  /**
   * Retrieves the username of the currently logged-in user.
   *
   * @return The username of the current user, or an empty string if no user is
   *         logged in.
   */
  public String getCurrentUsername() {
    User user = userService.getCurrentUser();
    return (user != null) ? user.getUsername() : "";
  }

  /**
   * Handles the user signup process.
   *
   * @param usernameField The TextField containing the username.
   * @param passwordField The TextField containing the password.
   * @return "success" if signup is successful, otherwise an error message.
   */
  public String signup(TextField usernameField, TextField passwordField) {
    try {
      String username = usernameField.getText();
      String password = passwordField.getText();

      // Input validation
      if (username.isEmpty() || password.length() < 8) {
        return "Username must not be empty. Password must be at least 8 characters.";
      }

      userService.signUp(username, password);
      return "success";
    } catch (Exception e) {
      e.printStackTrace();
      return e.getMessage();
    }
  }

  /**
   * Handles the user login process.
   *
   * @param usernameField The TextField containing the username.
   * @param passwordField The TextField containing the password.
   * @return "success" if login is successful, otherwise an error message.
   */
  public String login(TextField usernameField, TextField passwordField) {
    try {
      String username = usernameField.getText();
      String password = passwordField.getText();

      userService.login(username, password);
      return "success";
    } catch (Exception e) {
      e.printStackTrace();
      return e.getMessage();
    }
  }

  /**
   * Logs out the currently logged-in user.
   */
  public void logout() {
    try {
      User currentUser = userService.getCurrentUser();
      if (currentUser != null) {
        userService.logout(currentUser.getUsername());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}