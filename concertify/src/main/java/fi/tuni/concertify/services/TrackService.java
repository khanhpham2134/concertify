package fi.tuni.concertify.services;

import java.util.ArrayList;

import fi.tuni.concertify.models.Track;

/**
 * Service class for retrieving track data from Last.fm.
 * This class interacts with the LastFmAPIService to fetch top tracks based on
 * various criteria.
 */
public class TrackService {

  private LastFmAPIService lastFmAPIService = new LastFmAPIService();

  /**
   * Retrieves the top tracks sorted by a given criteria.
   * 
   * @param sortBy The sorting criteria (e.g., "listeners", "playcount").
   * @return A list of the top tracks sorted according to the given criteria.
   */
  public ArrayList<Track> getTopTracks(String sortBy) {
    ArrayList<Track> topTracks = lastFmAPIService.getTopChartTracks(sortBy, null);
    return topTracks;
  }

  /**
   * Retrieves the top tracks based on listeners from a specific country.
   * 
   * @param country The country code (e.g., "US", "GB").
   * @return A list of the top tracks from the specified country.
   */
  public ArrayList<Track> getTopTracksByCountry(String country) {
    ArrayList<Track> topTracks = lastFmAPIService.getTopChartTracks("listeners", country);
    return topTracks;
  }
}