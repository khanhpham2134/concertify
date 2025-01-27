package fi.tuni.concertify.views.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import fi.tuni.concertify.controllers.ArtistController;
import fi.tuni.concertify.controllers.EventController;
import fi.tuni.concertify.interfaces.Component;
import fi.tuni.concertify.models.Artist;
import fi.tuni.concertify.models.Event;
import fi.tuni.concertify.utilities.CountriesRetrieval;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Represents a map view for displaying events based on user preferences.
 * Allows filtering by favorite events, artists' events, or searched locations.
 * It displays a map showing event locations and their details.
 */
public class EventMap implements Component {
  HBox root;
  private ComboBox<String> entityTypeComboBox;
  private ComboBox<String> metricComboBox;
  private VBox eventMap = new VBox(20);
  private ArtistController artistController = new ArtistController();
  private EventController eventController = new EventController();
  private ObservableList<Event> selectedEvents = FXCollections.observableArrayList();
  private IntegerProperty numberOfFilteredEvents = new SimpleIntegerProperty(0);
  private HashMap<String, String> countriesMap;
  Gson gson = new Gson();

  /**
   * Constructs an EventMap instance, initializing the root HBox and setting up
   * the map view.
   *
   * @param root The parent node to which the event map will be attached.
   */
  public EventMap(Node root) {
    this.root = (HBox) root;
  }

  /**
   * Creates and initializes the combo boxes for selecting event types and metrics
   * (artists or locations).
   * Listens for changes in selection to update the displayed events accordingly.
   *
   * @param artists   A list of favorite artists.
   * @param events    A list of favorite events.
   * @param locations A list of recently searched locations.
   */
  private void createComboBoxes(ArrayList<Artist> artists, ArrayList<Event> events, ArrayList<String> locations) {
    ObservableList<String> entityTypes = FXCollections.observableArrayList(
        "Favorite Events", "Favorite Artists' Events", "Recently Searched Locations' Events");
    entityTypeComboBox = new ComboBox<>(entityTypes);
    entityTypeComboBox.setValue("Favorite Events");
    entityTypeComboBox.setStyle("-fx-pref-width: 200px;");
    entityTypeComboBox.getStyleClass().add("combo-box");
    entityTypeComboBox.getStyleClass().add("combo-box:hover");
    entityTypeComboBox.getStyleClass().add("list-cell");

    ObservableList<String> metrics = FXCollections.observableArrayList();
    metricComboBox = new ComboBox<>(metrics);
    metricComboBox.setValue("");
    metricComboBox.setDisable(true);
    metricComboBox.setStyle("-fx-pref-width: 200px;");
    metricComboBox.getStyleClass().add("combo-box");
    metricComboBox.getStyleClass().add("combo-box:hover");
    metricComboBox.getStyleClass().add("list-cell");

    selectedEvents.setAll(events);
    numberOfFilteredEvents.set(filterNullLocationEvents(new ArrayList<>(selectedEvents)).size());

    entityTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.equals("Favorite Events")) {
        metricComboBox.setDisable(true);
        selectedEvents.setAll(events);
      } else if (newValue.equals("Favorite Artists' Events")) {
        if (artists.size() > 0) {
          metricComboBox.setDisable(false);
          List<String> favoriteArtistNames = artists.stream().map(Artist::getName).collect(Collectors.toList());
          metrics.setAll(favoriteArtistNames);
          metricComboBox.setValue(favoriteArtistNames.get(0));
        }
      } else {
        if (locations.size() > 0) {
          metricComboBox.setDisable(false);
          metrics.setAll(locations);
          metricComboBox.setValue(locations.get(0));
        }
      }
    });

    metricComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
      if (entityTypeComboBox.getValue().equals("Favorite Artists' Events")) {
        selectedEvents.setAll(eventController.getEventsRelatedToArtist(newValue));
      } else if (entityTypeComboBox.getValue().equals("Recently Searched Locations' Events")) {
        HashMap<String, String> geoData = parseLocation(newValue);
        selectedEvents.setAll(eventController.getEventsByLocation(geoData.get("country"), geoData.get("city")));
      }
    });

  }

  /**
   * Parses a location string (city and country) into a hashmap containing the
   * city and country values.
   *
   * @param location A location string containing both city and country
   *                 information.
   * @return A hashmap containing the city and country extracted from the location
   *         string.
   */
  public HashMap<String, String> parseLocation(String location) {
    String city = null;
    String country = null;

    Pattern cityPattern = Pattern.compile("City:\\s*(.*?)(,|$)");
    Pattern countryPattern = Pattern.compile("Country:\\s*(.*)");

    Matcher cityMatcher = cityPattern.matcher(location);
    if (cityMatcher.find()) {
      city = cityMatcher.group(1);
    }

    Matcher countryMatcher = countryPattern.matcher(location);
    if (countryMatcher.find()) {
      country = countryMatcher.group(1);
    }

    city = city != null ? city : "";
    country = country != null ? countriesMap.get(country) : "";

    HashMap<String, String> geodata = new HashMap<>();
    geodata.put("city", city);
    geodata.put("country", country);

    return geodata;
  }

  /**
   * Filters out events that do not have valid geographical coordinates (latitude
   * and longitude).
   *
   * @param allEvents A list of all events to be filtered.
   * @return A list of events with valid geographical coordinates.
   */
  private List<Event> filterNullLocationEvents(ArrayList<Event> allEvents) {
    List<Event> filteredEvents = allEvents.stream()
        .filter(event -> event.getLat() != 0 && event.getLon() != 0)
        .collect(Collectors.toList());

    return filteredEvents;
  }

  /**
   * Toggles the visibility of the map and the empty label depending on whether
   * there are valid events to display.
   *
   * @param webView       The WebView that displays the map.
   * @param mapEmptyLabel The label shown when there are no valid events to
   *                      display on the map.
   */
  private void toggleMapLabel(WebView webView, Label mapEmptyLabel) {
    if (numberOfFilteredEvents.get() == 0) {
      webView.setVisible(false);
      webView.setManaged(false);
      mapEmptyLabel.setVisible(true);
      mapEmptyLabel.setManaged(true);
    } else {
      webView.setVisible(true);
      webView.setManaged(true);
      mapEmptyLabel.setVisible(false);
      mapEmptyLabel.setManaged(false);
    }
  }

  /**
   * Renders the event map with filtering options, event count, and the map
   * displaying the filtered events.
   * Initializes the combo boxes and updates the map and event list when the
   * selection changes.
   */
  public void render() {
    eventMap.setAlignment(Pos.CENTER);

    ArrayList<Artist> savedArtists = artistController.getFavoriteArtists();
    ArrayList<Event> savedEvents = eventController.getFavoriteEvents();
    ArrayList<String> searchedLocations = eventController.getSearchedLocations();

    countriesMap = CountriesRetrieval.getCountriesFromDB();

    createComboBoxes(savedArtists, savedEvents, searchedLocations);
    HBox controlsBox = new HBox(20);
    controlsBox.setAlignment(Pos.CENTER);
    controlsBox.getChildren().addAll(entityTypeComboBox, metricComboBox);

    Label countLabel = new Label(
        String.format(
            "%s event(s) found - %s event(s) with invalid geolocation coordinates - %s event(s) displayed on maps",
            selectedEvents.size(), selectedEvents.size() - numberOfFilteredEvents.get(), numberOfFilteredEvents.get()));
    countLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");

    Label mapEmptyLabel = new Label(
        String.format(
            "Start customizing your event map by searching for events, or by adding events/artists as your favorites! ",
            selectedEvents.size(), selectedEvents.size() - numberOfFilteredEvents.get(), numberOfFilteredEvents.get()));
    mapEmptyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");

    WebView webView = new WebView();
    webView.prefWidthProperty().bind(root.widthProperty().multiply(0.6));
    webView.setPrefHeight(400);

    WebEngine webEngine = webView.getEngine();
    webEngine.load(getClass().getResource("/map.html").toExternalForm());
    webEngine.documentProperty().addListener((observable1, oldDoc, newDoc) -> {
      if (newDoc != null && numberOfFilteredEvents.get() > 0) {
        List<Event> filteredEvents = filterNullLocationEvents(new ArrayList<>(selectedEvents));
        String stringifiedEvents = gson.toJson(filteredEvents);
        webEngine.executeScript("initializeMap(" + stringifiedEvents + ");");
      }
      toggleMapLabel(webView, mapEmptyLabel);
    });

    eventMap.setPadding(new Insets(20));
    eventMap.getChildren().addAll(controlsBox, countLabel, webView, mapEmptyLabel);

    selectedEvents.addListener((ListChangeListener<Event>) change -> {
      List<Event> filteredEvents = filterNullLocationEvents(new ArrayList<>(selectedEvents));
      numberOfFilteredEvents.set(filteredEvents.size());
      String stringifiedEvents = gson.toJson(filteredEvents);
      countLabel.setText(
          String.format(
              "%s event(s) found - %s event(s) with invalid geolocation coordinates - %s event(s) displayed on maps",
              selectedEvents.size(), selectedEvents.size() - numberOfFilteredEvents.get(),
              numberOfFilteredEvents.get()));
      if (numberOfFilteredEvents.get() > 0)
        webEngine.executeScript("initializeMap(" + stringifiedEvents + ");");
      toggleMapLabel(webView, mapEmptyLabel);
    });
  }

  /**
   * Returns the VBox containing the event map. It ensures the event map is
   * rendered before being returned.
   *
   * @return A VBox containing the event map.
   */
  public VBox get() {
    eventMap.getChildren().clear();
    render();
    return eventMap;
  }
}
