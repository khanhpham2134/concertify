package fi.tuni.concertify.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Service class for interacting with the MusicBrainz API.
 * This class provides methods to retrieve artist information and Spotify IDs.
 */
public class MusicBrainzAPIService extends IOAbstractService {
  private static final String MUSICBRAINZ_API_PREFIX = "https://musicbrainz.org/ws/2/artist/";
  private static final String MUSICBRAINZ_API_SUFFIX = "?inc=url-rels&fmt=json";

  /**
   * Retrieves data from the MusicBrainz API.
   * 
   * @param method The method to call on the MusicBrainz API (either
   *               "get-artist-info" or "query-artist").
   * @param key    The identifier for the artist (either an MBID or search query).
   * @param limit  The maximum number of results to return if using the
   *               "query-artist" method.
   * @return A JSON string containing the data from the MusicBrainz API.
   * @throws IllegalArgumentException if the provided method is invalid.
   */
  public String getDataOnMusicBrainz(String method, String key, int limit) {
    String methodString;
    String data = null;

    if (method.equals("get-artist-info")) {
      methodString = key;
    } else if (method.equals("query-artist")) {
      methodString = String.format("?query=%s&limit=%d", key, limit);
    } else {
      throw new IllegalArgumentException("Invalid method for MusicBrainz API.");
    }

    String apiURL = String.format("%s%s%s", MUSICBRAINZ_API_PREFIX, methodString, MUSICBRAINZ_API_SUFFIX);

    try {
      data = retrieveDataFromAPI(apiURL);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return data;
  }

  /**
   * Retrieves the Spotify ID for an artist based on their MusicBrainz ID (MBID).
   * 
   * @param mbid The MusicBrainz ID of the artist.
   * @return The Spotify ID of the artist, or null if not found.
   */
  public String getSpotifyId(String mbid) {
    String data = getDataOnMusicBrainz("get-artist-info", mbid, 0);

    if (data == null) {
      return null;
    }

    JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
    JsonArray relations = jsonObject.getAsJsonArray("relations");
    String spotifyId = null;

    for (JsonElement element : relations) {
      JsonObject relation = element.getAsJsonObject();
      JsonObject url = relation.getAsJsonObject("url");
      if (url != null && url.get("resource").getAsString().contains("spotify")) {
        String spotifyUrl = url.get("resource").getAsString();
        spotifyId = spotifyUrl.substring(spotifyUrl.lastIndexOf('/') + 1);
        break;
      }
    }

    return spotifyId;
  }
}