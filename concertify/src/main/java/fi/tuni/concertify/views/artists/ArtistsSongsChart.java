package fi.tuni.concertify.views.artists;

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
import fi.tuni.concertify.controllers.GlobalChartController;
import fi.tuni.concertify.interfaces.Component;

/**
 * The ArtistsSongsChart class displays a bar chart for global music statistics.
 * It allows users to view statistics for different entities (e.g., songs or
 * artists)
 * and metrics (e.g., listeners or playcount) through dropdown menus.
 * The chart updates based on the selected entity and metric.
 */
public class ArtistsSongsChart implements Component {
  private VBox container = new VBox();
  private BarChart<String, Number> barChart;
  private ComboBox<String> entityTypeComboBox;
  private ComboBox<String> metricComboBox;
  private GlobalChartController globalChartController;

  /**
   * Renders the chart container with combo boxes for selecting entity types and
   * metrics,
   * and the bar chart to display the global music statistics.
   */
  public void render() {
    container.setPadding(new Insets(20));
    container.setAlignment(Pos.CENTER);
    container.getStylesheets().add(getClass().getResource("/styles/chart.css").toExternalForm());

    createComboBoxes();
    createBarChart();
    globalChartController = new GlobalChartController(barChart, entityTypeComboBox, metricComboBox);
    globalChartController.updateChart();

    HBox controlsBox = new HBox(20);
    controlsBox.setAlignment(Pos.CENTER);
    controlsBox.getChildren().addAll(entityTypeComboBox, metricComboBox);

    container.getChildren().addAll(controlsBox, barChart);

  }

  /**
   * Creates the combo boxes for selecting entity type (Songs/Artists) and metric
   * (Listeners/Playcount).
   */
  private void createComboBoxes() {

    entityTypeComboBox = createComboBox(
        FXCollections.observableArrayList("Songs", "Artists"), "Artists");

    metricComboBox = createComboBox(
        FXCollections.observableArrayList("Listeners", "Playcount"), "Listeners");
  }

  /**
   * Creates a combo box with the given list of items and default value.
   *
   * @param items        The list of items for the combo box.
   * @param defaultValue The default value for the combo box.
   * @return The created ComboBox.
   */
  private ComboBox<String> createComboBox(ObservableList<String> items, String defaultValue) {
    ComboBox<String> comboBox = new ComboBox<>(items);
    comboBox.setValue(defaultValue);
    comboBox.setStyle("-fx-pref-width: 200px;");
    comboBox.getStyleClass().addAll("combo-box", "combo-box:hover", "list-cell");
    return comboBox;
  }

  /**
   * Creates a bar chart with appropriate axis for displaying global music
   * statistics.
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
   * Returns the container containing the chart and controls.
   *
   * @return VBox containing the chart and combo boxes.
   */
  @Override
  public VBox get() {
    container.getChildren().clear();
    render();
    return container;
  }

}
