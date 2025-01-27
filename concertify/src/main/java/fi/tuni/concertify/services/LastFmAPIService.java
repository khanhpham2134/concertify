package fi.tuni.concertify.services;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fi.tuni.concertify.models.Artist;
import fi.tuni.concertify.models.Track;
import fi.tuni.concertify.utilities.ArtistUtils;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Service class to interact with the Last.fm API to fetch data related to
 * artists, tracks, and charts.
 * This class supports fetching information about artists, their top tracks, and
 * global or country-specific charts.
 * The class also enriches the retrieved data with additional information from
 * MusicBrainz and Spotify where possible.
 */
public class LastFmAPIService extends IOAbstractService {
  private static final Dotenv dotenv = Dotenv.load();
  private final String LASTFM_API_PREFIX = "https://ws.audioscrobbler.com/2.0/";
  private final String LASTFM_API_KEY = dotenv.get("LASTFM_API_KEY");
  private final String LASTFM_API_SUFFIX = String.format("&api_key=%s&format=json", LASTFM_API_KEY);
  private MusicBrainzAPIService musicBrainzAPIService = new MusicBrainzAPIService();
  private SpotifyAPIService spotifyAPIService = new SpotifyAPIService();

  /**
   * Fetches data from the Last.fm API based on the provided method, key, and
   * limit.
   * 
   * @param method The method to use for the API request (e.g., "artist.search",
   *               "geo.gettopartists").
   * @param key    The search key (e.g., artist name, country).
   * @param limit  The number of results to return.
   * @return A JSON string containing the response from the Last.fm API, or null
   *         if an error occurs.
   */
  private String getDataOnLastFM(String method, String key, int limit) {
    String methodString;
    String data = null;
    try {
      String encodedKey = URLEncoder.encode(key, "UTF-8");

      if (method.startsWith("artist")) {
        methodString = String.format("?method=%s&artist=%s", method, encodedKey);
      } else if ((method.startsWith("geo"))) {
        methodString = String.format("?method=%s&country=%s", method, encodedKey);
      } else if (method.startsWith("chart")) {
        methodString = String.format("?method=%s", method);
      } else {
        throw new IllegalArgumentException("Invalid method for Last.fm API.");
      }

      String apiURL = String.format("%s%s&limit=%d%s", LASTFM_API_PREFIX, methodString, limit, LASTFM_API_SUFFIX);

      data = retrieveDataFromAPI(apiURL);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return data;
  }

  /**
   * Searches for artists on Last.fm based on a given search key.
   * 
   * @param key The search key (artist name).
   * @return A JSON array of artist results from the Last.fm API, or an empty
   *         array if an error occurs.
   */
  private JsonArray searchArtists(String key) {
    try {
      String data = getDataOnLastFM("artist.search", key, 6);
      JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
      JsonObject results = jsonObject.getAsJsonObject("results");
      JsonObject artistmatches = results.getAsJsonObject("artistmatches");
      JsonArray artists = artistmatches.getAsJsonArray("artist");

      return artists;
    } catch (Exception e) {
      return new JsonArray();
    }
  }

  /**
   * Fetches the top chart artists from Last.fm, optionally sorted by a given
   * metric (e.g., listeners or playcount).
   * 
   * @param sortBy  The metric to sort by, either "listeners" or "playcount".
   * @param country The country for which to fetch top artists (null for global).
   * @return A JSON array of the top 10 artists from the chart.
   */
  private JsonArray getTopChartArtist(String sortBy, String country) {
    String data = country == null
        ? getDataOnLastFM("chart.gettopartists", "", 50)
        : getDataOnLastFM("geo.gettopartists", country, 50);

    JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
    JsonObject chart = jsonObject.getAsJsonObject(country == null ? "artists" : "topartists");
    JsonArray artists = chart.getAsJsonArray("artist");

    List<JsonObject> sortedList = StreamSupport.stream(artists.spliterator(), false)
        .map(JsonElement::getAsJsonObject)
        .sorted(Comparator.comparing(
            o -> sortBy.equals("listeners")
                ? Integer.parseInt(o.get(sortBy).getAsString())
                : Long.parseLong(o.get(sortBy).getAsString()),
            Comparator.reverseOrder()))
        .collect(Collectors.toList());

    JsonArray sortedArtists = new JsonArray();
    sortedList.stream().limit(10).forEach(sortedArtists::add);

    return sortedArtists;
  }

  /**
   * Retrieves a list of artists either by search or top chart, enriching the data
   * with information from cached artists,
   * MusicBrainz, and Spotify if available.
   * 
   * @param artistsFile The list of artists to check against the cache.
   * @param method      The method to use (either "artist.search" or a chart-based
   *                    method).
   * @param key         The search key (e.g., artist name or country).
   * @param country     The country for top chart (null for global).
   * @return A list of Artist objects containing enriched information from
   *         Last.fm, MusicBrainz, and Spotify.
   */
  public ArrayList<Artist> getArtists(ArrayList<Artist> artistsFile, String method, String key, String country) {
    JsonArray artists = method.equals("artist.search") ? searchArtists(key) : getTopChartArtist(key, country);
    ArrayList<Artist> searchArtists = new ArrayList<>();
    HashMap<String, Artist> cachedArtists = ArtistUtils.getCachedArtists(artistsFile);

    artists.forEach(element -> {
      Artist newArtist;
      JsonObject artistJson = element.getAsJsonObject();
      String name = artistJson.get("name").getAsString();

      JsonElement listenerEl = artistJson.get("listeners");
      JsonElement playCountEl = artistJson.get("playcount");

      int listeners = listenerEl != null ? Integer.parseInt(listenerEl.getAsString()) : 0;
      long playCount = playCountEl != null ? Long.parseLong(playCountEl.getAsString()) : 0;

      Artist cached = cachedArtists.get(name);

      if (cached != null) {
        newArtist = cached;
        newArtist.setListeners(listeners);
        newArtist.setPlayCount(playCount);
      } else {
        String mbid = artistJson.has("mbid") && !artistJson.get("mbid").getAsString().isEmpty()
            ? artistJson.get("mbid").getAsString()
            : null;
        if (mbid == null) {
          newArtist = new Artist(name, listeners, playCount);
        } else {
          String spotifyId = musicBrainzAPIService.getSpotifyId(mbid);
          if (spotifyId == null) {
            newArtist = new Artist(name, mbid, listeners, playCount);
          } else {
            HashMap<String, String> spotifyData = spotifyAPIService.getArtistUrlAndAvatar(spotifyId);
            String spotifyUrl = spotifyData.get("spotifyUrl");
            String profilePicture = spotifyData.get("profilePicture");

            newArtist = new Artist(name, mbid, spotifyId, spotifyUrl, profilePicture, listeners, playCount);
          }
        }
        artistsFile.add(newArtist);
      }

      searchArtists.add(newArtist);
    });

    return searchArtists;
  }

  /**
   * Retrieves detailed information about an artist from Last.fm.
   * 
   * @param name The name of the artist.
   * @return A map containing the artist's listeners, playcount, and bio.
   */
  public HashMap<String, String> getArtistInfo(String name) {
    HashMap<String, String> artistInfoMap = new HashMap<>();

    String artistDataString = getDataOnLastFM("artist.getInfo", name, 1);

    JsonObject jsonObject = JsonParser.parseString(artistDataString).getAsJsonObject();
    JsonObject artistObject = jsonObject.getAsJsonObject("artist");

    String listeners = artistObject.getAsJsonObject("stats").get("listeners").getAsString();
    String playCount = artistObject.getAsJsonObject("stats").get("playcount").getAsString();
    String bio = artistObject.getAsJsonObject("bio").get("summary").getAsString();

    artistInfoMap.put("listeners", listeners);
    artistInfoMap.put("playcount", playCount);
    artistInfoMap.put("bio", bio);

    return artistInfoMap;
  }

  /**
   * Retrieves the top 10 tracks for a given artist from Last.fm.
   * 
   * @param key The name of the artist for whom to fetch the top tracks.
   * @return A list of Track objects representing the top tracks for the specified
   *         artist.
   *         Each track contains the track name, playcount, and listeners.
   */
  public ArrayList<Track> getArtistTopTracks(String key) {
    ArrayList<Track> topTracks = new ArrayList<>();

    String artistTopTrackDataString = getDataOnLastFM("artist.gettoptracks", key, 10);

    JsonObject responseObject = JsonParser.parseString(artistTopTrackDataString).getAsJsonObject();
    JsonArray trackArray = responseObject.getAsJsonObject("toptracks").getAsJsonArray("track");

    trackArray.forEach(trackElement -> {
      JsonObject trackObject = trackElement.getAsJsonObject();

      String name = trackObject.get("name").getAsString();
      String playcount = trackObject.get("playcount").getAsString();
      String listeners = trackObject.get("listeners").getAsString();

      Track track = new Track(name, Integer.parseInt(playcount), Integer.parseInt(listeners));

      topTracks.add(track);
    });

    return topTracks;
  }

  /**
   * Retrieves the top tracks globally or by country from Last.fm, optionally
   * sorting by a given metric.
   * The results are limited to the top 10 tracks.
   * 
   * @param sortBy  The metric to sort by. Can be either "listeners" or
   *                "playcount".
   * @param country The country to fetch the top tracks for. If null, the global
   *                top tracks are fetched.
   * @return A list of Track objects representing the top tracks.
   *         Each track contains the track name and number of listeners.
   */
  public ArrayList<Track> getTopChartTracks(String sortBy, String country) {
    ArrayList<Track> topTracks = new ArrayList<>();

    String data = country == null
        ? getDataOnLastFM("chart.gettoptracks", "", 50)
        : getDataOnLastFM("geo.gettoptracks", country, 50);

    JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
    JsonObject chart = jsonObject.getAsJsonObject("tracks");
    JsonArray tracks = chart.getAsJsonArray("track");

    List<JsonObject> sortedList = StreamSupport.stream(tracks.spliterator(), false)
        .map(JsonElement::getAsJsonObject)
        .sorted(Comparator.comparing(
            o -> sortBy.equals("listeners")
                ? Integer.parseInt(o.get(sortBy).getAsString())
                : Long.parseLong(o.get(sortBy).getAsString()),
            Comparator.reverseOrder()))
        .collect(Collectors.toList());

    JsonArray sortedTracks = new JsonArray();
    sortedList.stream().limit(10).forEach(sortedTracks::add);

    sortedTracks.forEach(trackElement -> {
      JsonObject trackObject = trackElement.getAsJsonObject();

      String name = trackObject.get("name").getAsString();
      String listeners = trackObject.get("listeners").getAsString();

      Track track = new Track(name, 0, Integer.parseInt(listeners));

      topTracks.add(track);
    });

    return topTracks;
  }
}
