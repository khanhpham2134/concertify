package fi.tuni.concertify.interfaces;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * The UIFactory interface defines methods for creating various UI components.
 * These components are designed for use in the Concertify application.
 */
public interface UIFactory {

  /**
   * Creates the authentication view.
   *
   * @return A Component representing the authentication view.
   */
  Component createAuthView();

  /**
   * Creates the authentication form.
   *
   * @param root The parent Node for the form.
   * @return A Component representing the authentication form.
   */
  Component createAuthForm(Node root);

  /**
   * Creates an image component for the authentication view.
   *
   * @param root The parent Node for the image.
   * @return A Component representing the authentication image.
   */
  Component createAuthImage(Node root);

  /**
   * Creates the login view.
   *
   * @param switchToSignUp The event handler for switching to the signup view.
   * @return A Component representing the login view.
   */
  Component createLogin(EventHandler<MouseEvent> switchToSignUp);

  /**
   * Creates the signup view.
   *
   * @param switchToLogin The event handler for switching to the login view.
   * @return A Component representing the signup view.
   */
  Component createSignUp(EventHandler<MouseEvent> switchToLogin);

  /**
   * Creates the home view.
   *
   * @return A Component representing the home view.
   */
  Component createHome();

  /**
   * Creates the content section of the UI.
   *
   * @param root The parent Node for the content.
   * @return A Component representing the content section.
   */
  Component createContent(Node root);

  /**
   * Creates the header component.
   *
   * @param root The parent Node for the header.
   * @return A Component representing the header.
   */
  Component createHeader(Node root);

  /**
   * Creates the body component.
   *
   * @param root The parent Node for the body.
   * @return A Component representing the body.
   */
  Component createBody(Node root);

  /**
   * Creates the navigation bar.
   *
   * @param root The parent Node for the navigation bar.
   * @return A Component representing the navigation bar.
   */
  Component createNavBar(Node root);

  /**
   * Creates the search bar.
   *
   * @return A Component representing the search bar.
   */
  Component createSearchBar();

  /**
   * Creates a chart for artists and songs.
   *
   * @return A Component representing the artists and songs chart.
   */
  Component createArtistsSongsChart();

  /**
   * Creates a heading for a single artist.
   *
   * @param root The parent Node for the heading.
   * @return A Component representing the single artist heading.
   */
  Component createSingleArtistHeading(Node root);

  /**
   * Creates a heading box with specified text.
   *
   * @param root        The parent Node for the heading box.
   * @param headingText The text to display in the heading box.
   * @return A Component representing the heading box.
   */
  Component createHeadingBox(Node root, String headingText);

  /**
   * Creates the body section for an artist.
   *
   * @param root The parent Node for the artist body.
   * @return A Component representing the artist body.
   */
  Component createArtistBody(Node root);

  /**
   * Creates a list of artists with a specific type.
   *
   * @param root     The parent Node for the artist list.
   * @param listType The type of the list (e.g., "favorites", "popular").
   * @return A Component representing the artist list.
   */
  Component createArtistList(Node root, String listType);

  /**
   * Creates the event search view.
   *
   * @return A Component representing the event search view.
   */
  Component createEventSearch();

  /**
   * Creates a list of events with a specific type.
   *
   * @param root     The parent Node for the event list.
   * @param listType The type of the list (e.g., "upcoming", "past").
   * @return A Component representing the event list.
   */
  Component createEventList(Node root, String listType);

  /**
   * Creates a chart for event searches.
   *
   * @return A Component representing the event search chart.
   */
  Component createEventSearchChart();

  /**
   * Creates a map for displaying event locations.
   *
   * @param root The parent Node for the event map.
   * @return A Component representing the event map.
   */
  Component createEventMap(Node root);
}