package fi.tuni.concertify.services;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import fi.tuni.concertify.models.User;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserService userService;

    @Mock
    private AuthService authService;

    private ArrayList<User> testUsers;
    private User testUser;

    @BeforeEach
    public void setUp() {
        // Create a spy of UserService
        userService = spy(new UserService());
        
        // Initialize test data
        testUsers = new ArrayList<>();
        testUser = new User("testUser", "hashedPassword", false);
        testUsers.add(testUser);
        
        // Mock file operations with lenient() to avoid unnecessary stubbing errors
        lenient().doReturn(testUsers).when(userService).readFromFile(anyString(), any());
        lenient().doNothing().when(userService).writeToFile(anyString(), any());
        
        // Set the mocked AuthService
        userService.setAuthService(authService);
    }

    @Test
    @DisplayName("Should retrieve users when they exist in the database")
    public void shouldGetUsers_whenUsersExist() {
        ArrayList<User> users = userService.getUsers();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("testUser", users.get(0).getUsername());
    }

    @Test
    @DisplayName("Should get current user when a user is logged in")
    public void shouldGetCurrentUser_whenUserIsLoggedIn() {
        testUser.setIsCurrentLogin(true);
        User currentUser = userService.getCurrentUser();
        assertNotNull(currentUser);
        assertEquals("testUser", currentUser.getUsername());
    }

    @Test
    @DisplayName("Should return null when no user is logged in")
    public void shouldReturnNull_whenNoUserIsLoggedIn() {
        testUser.setIsCurrentLogin(false);
        User currentUser = userService.getCurrentUser();
        assertNull(currentUser);
    }

    @Test
    @DisplayName("Should successfully update current user")
    public void shouldUpdateCurrentUser_whenUserExists() {
        User updatedUser = new User("testUser", "newHashedPassword", true);
        updatedUser.setId(testUser.getId());
        userService.updateCurrentUser(updatedUser);
        verify(userService).writeToFile(anyString(), any());
    }

    @Test
    @DisplayName("Should successfully sign up new user with unique username")
    public void shouldSignUpSuccessfully_whenUsernameIsUnique() {
        String newUsername = "newUser";
        String password = "password123";
        
        // Mock behavior specific to this test
        lenient().when(authService.hash(password)).thenReturn("hashedPassword");
        
        userService.signUp(newUsername, password);
        verify(authService).hash(password);
        verify(userService).writeToFile(anyString(), any());
    }

    @Test
    @DisplayName("Should throw exception when signing up with existing username")
    public void shouldThrowException_whenSigningUpWithExistingUsername() {
        String existingUsername = "testUser";
        String password = "password123";
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userService.signUp(existingUsername, password);
        });
        
        assertEquals("User with username testUser already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Should successfully log in user with valid credentials")
    public void shouldLoginSuccessfully_whenCredentialsAreValid() {
        String username = "testUser";
        String password = "password123";
        
        // Mock behavior specific to this test
        lenient().when(authService.verify(password, "hashedPassword")).thenReturn(true);
        
        userService.login(username, password);
        verify(authService).verify(password, "hashedPassword");
        verify(userService).writeToFile(anyString(), any());
    }

    @Test
    @DisplayName("Should throw exception when logging in with invalid credentials")
    public void shouldThrowException_whenLoginCredentialsAreInvalid() {
        String username = "testUser";
        String password = "wrongPassword";
        
        // Mock behavior specific to this test
        lenient().when(authService.verify(password, "hashedPassword")).thenReturn(false);
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userService.login(username, password);
        });
        
        assertEquals("Invalid username or password.", exception.getMessage());
    }

    @Test
    @DisplayName("Should successfully log out existing user")
    public void shouldLogoutSuccessfully_whenUserExists() {
        String username = "testUser";
        userService.logout(username);
        verify(userService).writeToFile(anyString(), any());
    }

    @Test
    @DisplayName("Should throw exception when logging out non-existent user")
    public void shouldThrowException_whenLoggingOutNonexistentUser() {
        String username = "nonexistentUser";
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userService.logout(username);
        });
        
        assertEquals("Current user not found.", exception.getMessage());
    }
}