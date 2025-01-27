package fi.tuni.concertify.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Service class for interacting with the Spotify API.
 * This class provides methods to retrieve artist information and handle access
 * token management.
 */
public class SpotifyAPIService extends IOAbstractService {
  private final String SPOTIFY_TOKEN_DB = "./database/spotify_token.json";
  private final String SPOTIFY_TOKEN_URL = "https://accounts.spotify.com/api/token";
  private final String SPOTIFY_ARTIST_API = "https://api.spotify.com/v1/artists/";

  private static final Dotenv dotenv = Dotenv.load();
  private static final String SPOTIFY_CLIENT_ID = System.getenv("SPOTIFY_CLIENT_ID");
  private static final String SPOTIFY_CLIENT_SECRET = dotenv.get("SPOTIFY_CLIENT_SECRET");

  private final Gson gson = new Gson();
  private ArrayList<AccessToken> savedTokens = readFromFile(SPOTIFY_TOKEN_DB, AccessToken.class);
  public String accessToken = savedTokens.size() > 0 ? savedTokens.get(0).getAccessToken() : null;
  private long tokenExpirationTime = savedTokens.size() > 0 ? savedTokens.get(0).getTokenExpirationTime() : 0;

  /**
   * Retrieves information about an artist from Spotify using their artist ID.
   * 
   * @param artistId The Spotify ID of the artist.
   * @return A JSON string containing the artist's information from Spotify.
   * @throws Exception If an error occurs during the API call.
   */
  public String getArtist(String artistId) throws Exception {
    ensureValidAccessToken();

    URL url = new URL(SPOTIFY_ARTIST_API + artistId);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Authorization", "Bearer " + accessToken);

    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    StringBuilder content = new StringBuilder();
    String inputLine;
    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }
    in.close();
    conn.disconnect();

    return content.toString();
  }

  /**
   * Retrieves the Spotify URL and profile picture URL of an artist.
   * 
   * @param spotifyId The Spotify ID of the artist.
   * @return A HashMap containing the Spotify URL and profile picture URL.
   */
  public HashMap<String, String> getArtistUrlAndAvatar(String spotifyId) {
    try {
      String jsonResponse = getArtist(spotifyId);

      JsonObject jsonObject = new Gson().fromJson(jsonResponse, JsonObject.class);

      String spotifyUrl = jsonObject.getAsJsonObject("external_urls").get("spotify").getAsString();
      JsonArray imagesArray = jsonObject.getAsJsonArray("images");
      String profilePicture = (imagesArray != null && imagesArray.size() > 0)
          ? imagesArray.get(0).getAsJsonObject().get("url").getAsString()
          : "/images/music-note.png";

      HashMap<String, String> result = new HashMap<>();
      result.put("spotifyUrl", spotifyUrl);
      result.put("profilePicture", profilePicture);

      return result;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Refreshes the Spotify access token using client credentials.
   * This method retrieves a new token and saves it to a local database for future
   * use.
   * 
   * @throws Exception If an error occurs during the token refresh process.
   */
  public void refreshAccessToken() throws Exception {
    String auth = SPOTIFY_CLIENT_ID + ":" + SPOTIFY_CLIENT_SECRET;
    String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

    URL url = new URL(SPOTIFY_TOKEN_URL);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    conn.setDoOutput(true);

    String body = "grant_type=client_credentials";
    try (OutputStream os = conn.getOutputStream()) {
      os.write(body.getBytes());
    }

    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    StringBuilder content = new StringBuilder();
    String inputLine;
    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }
    in.close();
    conn.disconnect();

    AccessToken tokenResponse = gson.fromJson(content.toString(), AccessToken.class);
    accessToken = tokenResponse.getAccessToken();
    tokenResponse.setTokenExpirationTime();
    tokenExpirationTime = tokenResponse.getTokenExpirationTime();

    ArrayList<AccessToken> tokens = new ArrayList<AccessToken>();
    tokens.add(tokenResponse);

    writeToFile(SPOTIFY_TOKEN_DB, tokens);
  }

  /**
   * Ensures that the current Spotify access token is valid.
   * If the token is expired or null, it will refresh the token.
   * 
   * @throws Exception If an error occurs during the token validation or refresh
   *                   process.
   */
  public void ensureValidAccessToken() throws Exception {
    if (accessToken == null || System.currentTimeMillis() >= tokenExpirationTime) {
      refreshAccessToken();
    }
  }

  /**
   * Helper class representing an access token response from Spotify.
   */
  private class AccessToken {
    private String access_token;
    private int expires_in;
    private long token_expiration_time;

    public String getAccessToken() {
      return access_token;
    }

    public void setTokenExpirationTime() {
      this.token_expiration_time = System.currentTimeMillis() + (expires_in * 1000);
    }

    public long getTokenExpirationTime() {
      return token_expiration_time;
    }
  }
}