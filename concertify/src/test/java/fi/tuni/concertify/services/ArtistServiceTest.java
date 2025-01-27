package fi.tuni.concertify.services;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import fi.tuni.concertify.models.Artist;
import fi.tuni.concertify.models.Track;
import fi.tuni.concertify.models.User;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {

    private ArtistService artistService;

    @Mock
    private UserService userService;

    @Mock
    private LastFmAPIService lastFmAPIService;

    private ArrayList<Artist> testArtists;
    private Artist testArtist;
    private User testUser;

    @BeforeEach
    public void setUp() {
        // Create a spy of ArtistService
        artistService = spy(new ArtistService());
        
        // Initialize test data
        testArtists = new ArrayList<>();
        testArtist = new Artist("Test Artist", 1000, 5000);
        testArtists.add(testArtist);
        
        testUser = new User("testUser", "hashedPassword", true);
        testUser.setFavoriteArtists(new ArrayList<>());
        
        // Mock file operations with lenient() to avoid unnecessary stubbing errors
        lenient().doReturn(testArtists).when(artistService).readFromFile(anyString(), any());
        lenient().doNothing().when(artistService).writeToFile(anyString(), any());
        
        // Set mocked services
        artistService.setUserService(userService);
        artistService.setLastFmAPIService(lastFmAPIService);
    }

    @Test
    @DisplayName("Should find artists when searching by key")
    public void shouldSearchArtistByKey_whenKeyProvided() {
        // Arrange
        String searchKey = "test";
        ArrayList<Artist> searchResults = new ArrayList<>();
        searchResults.add(testArtist);
        
        // Mock LastFmAPIService behavior specifically for this test
        lenient().when(lastFmAPIService.getArtists(any(), eq("artist.search"), eq(searchKey), any()))
            .thenReturn(searchResults);
        
        // Act
        ArrayList<Artist> results = artistService.searchArtistByKey(searchKey);
        
        // Assert
        assertNotNull(results, "Search results should not be null");
        assertEquals(1, results.size(), "Should find one artist");
        assertEquals("Test Artist", results.get(0).getName(), "Should find the test artist");
    }

    @Test
    @DisplayName("Should get favorite artists for logged-in user")
    public void shouldGetFavoriteArtists_whenUserIsLoggedIn() {
        // Mock UserService for this specific test
        lenient().when(userService.getCurrentUser()).thenReturn(testUser);
        
        ArrayList<Artist> favorites = artistService.getFavoriteArtists();
        
        assertNotNull(favorites, "Favorites list should not be null");
        verify(userService).getCurrentUser();
    }

    @Test
    @DisplayName("Should throw exception when getting favorites for logged-out user")
    public void shouldThrowException_whenGettingFavoritesForLoggedOutUser() {
        // Mock the logged-out state
        lenient().when(userService.getCurrentUser()).thenReturn(null);
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            artistService.getFavoriteArtists();
        });
        
        assertEquals("User is not logged in", exception.getMessage());
    }

    @Test
    @DisplayName("Should get top chart artists when sort criteria provided")
    public void shouldGetTopChartArtist_whenSortByProvided() {
        // Arrange
        String sortBy = "listeners";
        ArrayList<Artist> chartArtists = new ArrayList<>();
        chartArtists.add(testArtist);
        
        // Mock the chart retrieval
        lenient().when(lastFmAPIService.getArtists(any(), eq("chart.gettopartists"), eq(sortBy), any()))
            .thenReturn(chartArtists);
        
        // Act
        ArrayList<Artist> results = artistService.getTopChartArtist(sortBy);
        
        // Assert
        assertNotNull(results, "Chart results should not be null");
        assertEquals(1, results.size(), "Should have one artist in charts");
    }

    @Test
    @DisplayName("Should get top artists by country when country provided")
    public void shouldGetTopCountryArtist_whenCountryProvided() {
        // Arrange
        String country = "Finland";
        ArrayList<Artist> countryArtists = new ArrayList<>();
        countryArtists.add(testArtist);
        
        // Mock the country-specific artist retrieval
        lenient().when(lastFmAPIService.getArtists(any(), eq("geo.gettopartists"), eq("listeners"), eq(country)))
            .thenReturn(countryArtists);
        
        // Act
        ArrayList<Artist> results = artistService.getTopCountryArtist(country);
        
        // Assert
        assertNotNull(results, "Country results should not be null");
        assertEquals(1, results.size(), "Should have one artist from country");
    }

    @Test
    @DisplayName("Should get artist details when searching by name")
    public void shouldGetArtistByName_whenNameExists() {
        // Arrange
        String artistName = "Test Artist";
        HashMap<String, String> artistInfo = new HashMap<>();
        artistInfo.put("listeners", "1000");
        artistInfo.put("playcount", "5000");
        artistInfo.put("bio", "Test bio");
        ArrayList<Track> topTracks = new ArrayList<>();
        
        // Mock both artist info and top tracks retrieval
        lenient().when(lastFmAPIService.getArtistInfo(artistName)).thenReturn(artistInfo);
        lenient().when(lastFmAPIService.getArtistTopTracks(artistName)).thenReturn(topTracks);
        
        // Act
        Artist result = artistService.getArtistByName(artistName);
        
        // Assert
        assertNotNull(result, "Artist result should not be null");
        assertEquals(artistName, result.getName(), "Should find the correct artist");
        assertEquals("Test bio", result.getBio(), "Should update artist bio");
    }

    @Test
    @DisplayName("Should add artist to favorites when user is logged in")
    public void shouldAddArtistToFavorite_whenUserLoggedInAndArtistExists() {
        // Mock logged-in user state
        lenient().when(userService.getCurrentUser()).thenReturn(testUser);
        
        artistService.addArtistToFavorite("Test Artist");
        
        verify(userService).updateCurrentUser(any());
    }

    @Test
    @DisplayName("Should throw exception when adding artist without being logged in")
    public void shouldThrowException_whenAddingArtistToFavoritesWithoutLogin() {
        lenient().when(userService.getCurrentUser()).thenReturn(null);
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            artistService.addArtistToFavorite("Test Artist");
        });
        
        assertEquals("User is not logged in", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when adding non-existent artist")
    public void shouldThrowException_whenAddingNonexistentArtistToFavorites() {
        // Mock logged-in user but searching for non-existent artist
        lenient().when(userService.getCurrentUser()).thenReturn(testUser);
        String nonexistentArtist = "Nonexistent Artist";
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artistService.addArtistToFavorite(nonexistentArtist);
        });
        
        assertEquals("Artist not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should remove artist from favorites when user is logged in")
    public void shouldRemoveArtistFromFavorite_whenUserLoggedInAndArtistExists() {
        // Set up user with the artist already in favorites
        lenient().when(userService.getCurrentUser()).thenReturn(testUser);
        testUser.getFavoriteArtists().add(testArtist);
        
        artistService.removeArtistFromFavorite("Test Artist");
        
        verify(userService).updateCurrentUser(any());
    }

    @Test
    @DisplayName("Should throw exception when removing artist without being logged in")
    public void shouldThrowException_whenRemovingArtistFromFavoritesWithoutLogin() {
        lenient().when(userService.getCurrentUser()).thenReturn(null);
        
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            artistService.removeArtistFromFavorite("Test Artist");
        });
        
        assertEquals("User is not logged in", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when removing non-existent artist")
    public void shouldThrowException_whenRemovingNonexistentArtistFromFavorites() {
        // Mock logged-in user but trying to remove non-existent artist
        lenient().when(userService.getCurrentUser()).thenReturn(testUser);
        String nonexistentArtist = "Nonexistent Artist";
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            artistService.removeArtistFromFavorite(nonexistentArtist);
        });
        
        assertEquals("Artist not found", exception.getMessage());
    }
}