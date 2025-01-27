package fi.tuni.concertify.views.events;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import fi.tuni.concertify.interfaces.Component;
import fi.tuni.concertify.controllers.EventSearchChartController;

/**
 * Represents a bar chart for displaying global music statistics based on event
 * search data.
 * It includes a combo box for selecting the entity type (e.g., Songs, Artists)
 * and another for selecting the country.
 */
public class EventSearchChart implements Component {
  private VBox container = new VBox();
  private BarChart<String, Number> barChart;
  private ComboBox<String> entityTypeComboBox;
  private ComboBox<String> countryComboBox;
  private EventSearchChartController eventSearchChartController;

  /**
   * Renders the UI components of the EventSearchChart, including the combo boxes
   * for selecting entity type
   * and country, and the bar chart for displaying statistics. It also initializes
   * the controller to update the chart.
   */
  public void render() {
    container.setPadding(new Insets(20));
    container.setAlignment(Pos.CENTER);
    container.getStylesheets().add(getClass().getResource("/styles/chart.css").toExternalForm());

    createComboBoxes();
    createBarChart();
    eventSearchChartController = new EventSearchChartController(barChart, entityTypeComboBox, countryComboBox);
    eventSearchChartController.updateChart();

    HBox controlsBox = new HBox(20);
    controlsBox.setAlignment(Pos.CENTER);
    controlsBox.getChildren().addAll(entityTypeComboBox, countryComboBox);

    container.getChildren().addAll(controlsBox, barChart);
  }

  /**
   * Creates and initializes the combo boxes for selecting entity type
   * (Songs/Artists) and country
   * using the countries list from the EventSearchBar.
   */
  private void createComboBoxes() {
    // Create entity type ComboBox
    entityTypeComboBox = createComboBox(
        FXCollections.observableArrayList("Songs", "Artists"), "Artists");

    // Create country ComboBox using countriesList from EventSearchBar
    countryComboBox = createComboBox(EventSearchBar.countriesList,
        EventSearchBar.countriesList.isEmpty() ? null : EventSearchBar.countriesList.get(0));
  }

  /**
   * Creates a combo box with the given list of items and default value.
   * 
   * @param items        The list of items to populate the combo box.
   * @param defaultValue The default selected value in the combo box.
   * @return The initialized combo box.
   */
  private ComboBox<String> createComboBox(ObservableList<String> items, String defaultValue) {
    ComboBox<String> comboBox = new ComboBox<>(items);
    comboBox.setValue(defaultValue);
    comboBox.setStyle("-fx-pref-width: 200px;");
    comboBox.getStyleClass().addAll("combo-box", "combo-box:hover", "list-cell");
    return comboBox;
  }

  /**
   * Creates the bar chart for displaying global music statistics.
   * Initializes the axes (CategoryAxis for the x-axis and NumberAxis for the
   * y-axis),
   * and sets the chart's appearance and style.
   */
  private void createBarChart() {
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();

    barChart = new BarChart<>(xAxis, yAxis);
    barChart.setTitle("Global Music Statistics");
    barChart.setAnimated(false);
    barChart.setStyle("-fx-background-color: white;");

    barChart.setPrefHeight(400);
    barChart.setPrefWidth(800);

    barChart.getStyleClass().addAll("chart", "chart-title", "chart-bar", "chart-bar:hover");
    xAxis.getStyleClass().add("axis-label");
    yAxis.getStyleClass().add("axis-label");
  }

  /**
   * Returns the VBox container with the bar chart and combo boxes. It ensures the
   * UI is rendered
   * before being returned.
   *
   * @return The VBox containing the event search chart components.
   */
  @Override
  public VBox get() {
    container.getChildren().clear();
    render();
    return container;
  }
}
