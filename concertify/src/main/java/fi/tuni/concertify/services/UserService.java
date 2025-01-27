package fi.tuni.concertify.services;

import java.util.ArrayList;
import java.util.Date;

import fi.tuni.concertify.models.User;

/**
 * Service class for managing user data and authentication.
 * This class handles user registration, login, logout, and user information
 * retrieval.
 */
public class UserService extends IOAbstractService {

  private final String USER_DB = "./database/user.json";
  private AuthService authService;

  /**
   * Constructor to initialize the UserService with an AuthService.
   */
  public UserService() {
    this.authService = new AuthService();
  }

  /**
   * Allows dependency injection for AuthService, useful for testing.
   * 
   * @param authService The AuthService instance to use.
   */
  public void setAuthService(AuthService authService) {
    this.authService = authService;
  }

  /**
   * Retrieves the list of all users.
   * 
   * @return An ArrayList of all users.
   */
  public ArrayList<User> getUsers() {
    return readFromFile(USER_DB, User.class);
  }

  /**
   * Retrieves the currently logged-in user.
   * 
   * @return The currently logged-in user, or null if no user is logged in.
   */
  public User getCurrentUser() {
    ArrayList<User> users = getUsers();
    return users.stream().filter(user -> user.getIsCurrentLogin()).findAny().orElse(null);
  }

  /**
   * Updates the information of the currently logged-in user.
   * 
   * @param updatedUser The updated user data to save.
   */
  public void updateCurrentUser(User updatedUser) {
    ArrayList<User> users = getUsers();
    users.stream()
        .filter(user -> user.getId().equals(updatedUser.getId()))
        .findFirst()
        .ifPresent(user -> {
          int index = users.indexOf(user);
          users.set(index, updatedUser);
        });

    writeToFile(USER_DB, users);
  }

  /**
   * Signs up a new user with the given username and password.
   * 
   * @param username The username of the new user.
   * @param password The password of the new user.
   * @throws IllegalStateException If a user with the same username already
   *                               exists.
   */
  public void signUp(String username, String password) {
    ArrayList<User> users = getUsers();
    User existedUserWithUsername = users.stream().filter(user -> user.getUsername().equals(username)).findAny()
        .orElse(null);

    if (existedUserWithUsername != null) {
      throw new IllegalStateException("User with username " + username + " already exists");
    }

    String passwordHash = authService.hash(password);
    User newUser = new User(username, passwordHash, true);
    users.add(newUser);

    writeToFile(USER_DB, users);
  }

  /**
   * Logs in a user with the provided username and password.
   * 
   * @param username The username of the user trying to log in.
   * @param password The password of the user.
   * @throws IllegalStateException If the username or password is incorrect.
   */
  public void login(String username, String password) {
    ArrayList<User> users = getUsers();

    User user = users.stream()
        .filter(u -> u.getUsername().equals(username) && authService.verify(password, u.getPasswordHash()))
        .findAny().orElse(null);

    if (user == null) {
      throw new IllegalStateException("Invalid username or password.");
    }

    user.setIsCurrentLogin(true);
    user.setLastLogin(new Date());

    writeToFile(USER_DB, users);
  }

  /**
   * Logs out the current user.
   * 
   * @param username The username of the user to log out.
   * @throws IllegalStateException If the user is not found.
   */
  public void logout(String username) {
    ArrayList<User> users = getUsers();

    User user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);

    if (user == null) {
      throw new IllegalStateException("Current user not found.");
    }

    user.setIsCurrentLogin(false);

    writeToFile(USER_DB, users);
  }
}