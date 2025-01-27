package fi.tuni.concertify.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import fi.tuni.concertify.models.Artist;

/**
 * Utility class for handling operations related to Artist data.
 * Provides methods for caching artists and retrieving their MusicBrainz ID from
 * a JSON object.
 */
public class ArtistUtils {

  /**
   * Converts an ArrayList of Artist objects into a HashMap, where the key is the
   * artist's name.
   * 
   * @param artists The list of artists to be cached.
   * @return A HashMap with artist names as keys and Artist objects as values.
   */
  public static HashMap<String, Artist> getCachedArtists(ArrayList<Artist> artists) {
    return artists.stream()
        .collect(Collectors.toMap(
            Artist::getName,
            artist -> artist,
            (existing, replacement) -> existing,
            HashMap::new));
  }

  /**
   * Retrieves the MusicBrainz ID from a given JSON object.
   * 
   * @param jsonObj The JSON object containing external links for the artist.
   * @return The MusicBrainz ID if found, otherwise null.
   */
  public static String getMusicBrainzId(JsonObject jsonObj) {
    if (!jsonObj.has("externalLinks")) {
      return null;
    }

    JsonObject externalLinks = jsonObj.getAsJsonObject("externalLinks");
    if (!externalLinks.has("musicbrainz") || externalLinks.get("musicbrainz").isJsonNull()) {
      return null;
    }

    JsonArray musicBrainzArray = externalLinks.get("musicbrainz").getAsJsonArray();
    if (musicBrainzArray.size() == 0 || musicBrainzArray.get(0).isJsonNull()) {
      return null;
    }

    JsonObject musicBrainzEntry = musicBrainzArray.get(0).getAsJsonObject();
    if (!musicBrainzEntry.has("id") || musicBrainzEntry.get("id").isJsonNull()) {
      return null;
    }

    return musicBrainzEntry.get("id").getAsString();
  }

}