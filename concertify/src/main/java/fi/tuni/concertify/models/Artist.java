package fi.tuni.concertify.models;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Represents a musical artist with associated metadata.
 */
public class Artist {
  private UUID id;
  private String name, musicBrainzId, spotifyId, spotifyUrl, profilePicture, bio, ticketmasterId;
  private int listeners;
  private long playCount;
  private ArrayList<Track> topTracks;

  /**
   * Constructs an Artist with a name, listeners count, and play count.
   *
   * @param name      The name of the artist.
   * @param listeners The number of listeners for the artist.
   * @param playCount The play count for the artist.
   */
  public Artist(String name, int listeners, long playCount) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.listeners = listeners;
    this.playCount = playCount;
    this.profilePicture = "/images/music-note.png";
  }

  /**
   * Constructs an Artist with a name, MusicBrainz ID, listeners count, and play
   * count.
   *
   * @param name          The name of the artist.
   * @param musicBrainzId The MusicBrainz ID of the artist.
   * @param listeners     The number of listeners for the artist.
   * @param playCount     The play count for the artist.
   */
  public Artist(String name, String musicBrainzId, int listeners, long playCount) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.musicBrainzId = musicBrainzId;
    this.listeners = listeners;
    this.playCount = playCount;
    this.profilePicture = "/images/music-note.png";
  }

  /**
   * Constructs an Artist with detailed information including Spotify and
   * MusicBrainz data.
   *
   * @param name           The name of the artist.
   * @param musicBrainzId  The MusicBrainz ID of the artist.
   * @param spotifyId      The Spotify ID of the artist.
   * @param spotifyUrl     The Spotify URL of the artist.
   * @param profilePicture The profile picture URL of the artist.
   * @param listeners      The number of listeners for the artist.
   * @param playCount      The play count for the artist.
   */
  public Artist(String name, String musicBrainzId, String spotifyId, String spotifyUrl,
      String profilePicture, int listeners, long playCount) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.musicBrainzId = musicBrainzId;
    this.spotifyId = spotifyId;
    this.spotifyUrl = spotifyUrl;
    this.profilePicture = profilePicture;
    this.listeners = listeners;
    this.playCount = playCount;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMusicBrainzId() {
    return musicBrainzId;
  }

  public void setMusicBrainzId(String musicBrainzId) {
    this.musicBrainzId = musicBrainzId;
  }

  public String getSpotifyId() {
    return spotifyId;
  }

  public void setSpotifyId(String spotifyId) {
    this.spotifyId = spotifyId;
  }

  public String getSpotifyUrl() {
    return spotifyUrl;
  }

  public void setSpotifyUrl(String spotifyUrl) {
    this.spotifyUrl = spotifyUrl;
  }

  public String getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public int getListeners() {
    return listeners;
  }

  public void setListeners(int listeners) {
    this.listeners = listeners;
  }

  public long getPlayCount() {
    return playCount;
  }

  public void setPlayCount(long playCount) {
    this.playCount = playCount;
  }

  public ArrayList<Track> getTopTracks() {
    return topTracks;
  }

  public void setTopTracks(ArrayList<Track> topTracks) {
    this.topTracks = topTracks;
  }

  public String getTicketmasterId() {
    return ticketmasterId;
  }

  public void setTicketmasterId(String ticketmasterId) {
    this.ticketmasterId = ticketmasterId;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    Artist artist = (Artist) obj;
    return id.equals(artist.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
