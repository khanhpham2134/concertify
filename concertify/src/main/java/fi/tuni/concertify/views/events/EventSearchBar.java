package fi.tuni.concertify.views.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import fi.tuni.concertify.controllers.EventController;
import fi.tuni.concertify.interfaces.Component;
import fi.tuni.concertify.utilities.CountriesRetrieval;
import fi.tuni.concertify.views.home.NavigationManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Represents a search bar for searching events by city and country.
 * Allows users to search events based on their selected location.
 */
public class EventSearchBar implements Component {
  private HBox eventSearchBar = new HBox();
  public static ObservableMap<String, String> countriesMap = FXCollections.observableHashMap();
  public static ObservableList<String> countriesList = FXCollections.observableArrayList();
  public static StringProperty searchCity = new SimpleStringProperty("");
  public static StringProperty searchCountryCode = new SimpleStringProperty("");
  public static ComboBox<String> comboBoxCountry;
  private EventController eventController = new EventController();

  /**
   * Renders the search bar UI components, including the text field for city name,
   * a combo box for selecting a country, and a search button.
   * Also loads the list of countries from the database and sets up search
   * functionality.
   */
  public void render() {
    HashMap<String, String> countries = CountriesRetrieval.getCountriesFromDB();
    countriesMap.putAll(countries);
    ArrayList<String> countriesArr = new ArrayList<>(new ArrayList<>(countries.keySet()));
    Collections.sort(countriesArr);
    countriesList.addAll(countriesArr);

    TextField eventSearchBarField = new TextField();
    eventSearchBar.getStylesheets().add(getClass().getResource("/styles/search-bar.css").toExternalForm());
    eventSearchBar.setSpacing(10);
    eventSearchBar.setPadding(new Insets(80, 20, 70, 20));

    eventSearchBarField.setPromptText("Enter city name");
    eventSearchBarField.setPrefWidth(300);
    eventSearchBarField.getStyleClass().add("text-field");

    comboBoxCountry = new ComboBox<>(countriesList);
    comboBoxCountry.setPrefWidth(300);
    comboBoxCountry.setPrefHeight(10);
    comboBoxCountry.setValue("Choose a country");
    comboBoxCountry.getStyleClass().add("combo-box");

    Button searchButton = new Button("Search");
    searchButton.getStyleClass().add("search-button");

    searchButton.setOnAction(e -> {
      String country = comboBoxCountry.getValue();
      String countryCode = country != null && !country.equals("Choose a country") ? country : "";
      searchCountryCode.set(countriesMap.get(countryCode));
      searchCity.set(eventSearchBarField.getText());
      eventController.addToSearchedLocation(eventSearchBarField.getText(),
          !country.equals("Choose a country") ? comboBoxCountry.getValue() : "");
      NavigationManager.switchMainContent("event-search-results");
    });

    eventSearchBar.getChildren().addAll(eventSearchBarField, comboBoxCountry, searchButton);
  }

  /**
   * Returns the event search bar HBox containing the text field, combo box, and
   * search button.
   * It ensures the search bar is rendered before being returned.
   *
   * @return The HBox representing the event search bar.
   */
  @Override
  public HBox get() {
    eventSearchBar.getChildren().clear();
    render();
    return eventSearchBar;
  }
}
