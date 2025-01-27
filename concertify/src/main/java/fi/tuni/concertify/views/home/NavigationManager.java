package fi.tuni.concertify.views.home;

import fi.tuni.concertify.interfaces.Component;
import fi.tuni.concertify.views.ComponentFactory;
import fi.tuni.concertify.views.StageManager;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The NavigationManager class handles the dynamic content switching and layout
 * management
 * of the main user interface. It manages the different UI components for
 * various pages
 * and updates the layout (header, body, and content) based on the current page.
 * It also provides functionality to switch between different views, such as
 * home, search results,
 * saved artists, and events.
 */
public class NavigationManager {
  public static ComponentFactory componentFactory = StageManager.componentFactory;

  public static Component home = componentFactory.createHome();
  public static HBox homeComponent = (HBox) home.get();

  private static Component content = componentFactory.createContent(homeComponent);
  private static VBox contentComponent = (VBox) content.get();

  private static Component header = componentFactory.createHeader(contentComponent);
  private static HBox headerComponent = (HBox) header.get();

  private static Component body = componentFactory.createBody(contentComponent);
  private static HBox bodyComponent = (HBox) body.get();

  private static Component artistsSongsChart = componentFactory.createArtistsSongsChart();
  private static Component searchBar = componentFactory.createSearchBar();
  private static Component singleArtistHeading = componentFactory.createSingleArtistHeading(headerComponent);
  private static Component homeHeading = componentFactory.createHeadingBox(headerComponent, "Your ConcertiMap");
  private static Component savedArtistHeading = componentFactory.createHeadingBox(headerComponent, "Saved Artists");
  private static Component savedEventHeading = componentFactory.createHeadingBox(headerComponent, "Saved Events");
  private static Component searchHeading = componentFactory.createHeadingBox(headerComponent, "Search Results");
  private static Component artistBody = componentFactory.createArtistBody(bodyComponent);
  private static Component artistSaved = componentFactory.createArtistList(bodyComponent, "saved");
  private static Component artistSearchResults = componentFactory.createArtistList(bodyComponent, "searched");
  private static Component eventSearchBar = componentFactory.createEventSearch();
  private static Component eventSaved = componentFactory.createEventList(bodyComponent, "saved");
  private static Component eventSearchResultsComponent = componentFactory.createEventList(bodyComponent, "searched");
  private static Component eventSearchChart = componentFactory.createEventSearchChart();
  private static Component eventMap = componentFactory.createEventMap(bodyComponent);

  /**
   * Switches the main content based on the provided page identifier. This method
   * updates
   * the header and body sections of the UI and dynamically loads the appropriate
   * content
   * (e.g., artist search, event search, saved artists) into the main layout.
   * 
   * @param page The page identifier that determines which content to load (e.g.,
   *             "home", "artist-search").
   */
  public static void switchMainContent(String page) {
    headerComponent.getChildren().clear();
    bodyComponent.getChildren().clear();
    contentComponent.getChildren().clear();
    homeComponent.getChildren().remove(contentComponent);

    switch (page) {
      case "home":
        headerComponent.getChildren().add(homeHeading.get());
        bodyComponent.getChildren().add(eventMap.get());
        break;
      case "artist-search":
        headerComponent.getChildren().add(searchBar.get());
        bodyComponent.getChildren().add(artistsSongsChart.get());
        break;
      case "event-search":
        headerComponent.getChildren().add(eventSearchBar.get());
        bodyComponent.getChildren().add(eventSearchChart.get());
        break;
      case "artist-saved":
        headerComponent.getChildren().add(savedArtistHeading.get());
        bodyComponent.getChildren().add(artistSaved.get());
        break;
      case "artist-single":
        headerComponent.getChildren().add(singleArtistHeading.get());
        bodyComponent.getChildren().add(artistBody.get());
        break;
      case "artist-search-results":
        headerComponent.getChildren().add(searchHeading.get());
        bodyComponent.getChildren().add(artistSearchResults.get());
        break;
      case "saved-events":
        headerComponent.getChildren().add(savedEventHeading.get());
        bodyComponent.getChildren().add(eventSaved.get());
        break;
      case "event-search-results":
        headerComponent.getChildren().add(searchHeading.get());
        bodyComponent.getChildren().add(eventSearchResultsComponent.get());
        break;
    }

    contentComponent.getChildren().addAll(headerComponent, bodyComponent);
    homeComponent.getChildren().add(contentComponent);
  }
}
