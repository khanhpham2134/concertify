package fi.tuni.concertify.models;

/**
 * Represents a music track with its name, play count, and listeners.
 */
public class Track {
  private String name;
  private int playCount, listeners;

  /**
   * Constructs a new Track object with the specified name, play count, and
   * listener count.
   * 
   * @param name      the name of the track
   * @param playCount the number of times the track has been played
   * @param listeners the number of listeners of the track
   */
  public Track(String name, int playCount, int listeners) {
    this.name = name;
    this.playCount = playCount;
    this.listeners = listeners;
  }

  /**
   * Gets the name of the track.
   * 
   * @return the name of the track
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the track.
   * 
   * @param name the new name of the track
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the play count of the track.
   * 
   * @return the play count of the track
   */
  public int getPlayCount() {
    return playCount;
  }

  /**
   * Sets the play count of the track.
   * 
   * @param playCount the new play count of the track
   */
  public void setPlayCount(int playCount) {
    this.playCount = playCount;
  }

  /**
   * Gets the number of listeners of the track.
   * 
   * @return the number of listeners of the track
   */
  public int getListeners() {
    return listeners;
  }

  /**
   * Sets the number of listeners of the track.
   * 
   * @param listeners the new number of listeners of the track
   */
  public void setListeners(int listeners) {
    this.listeners = listeners;
  }
}