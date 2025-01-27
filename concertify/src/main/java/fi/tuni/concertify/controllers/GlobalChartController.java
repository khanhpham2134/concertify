package fi.tuni.concertify.controllers;

import fi.tuni.concertify.models.Artist;
import fi.tuni.concertify.models.Track;
import fi.tuni.concertify.services.ArtistService;
import fi.tuni.concertify.services.TrackService;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;

/**
 * The GlobalChartController class manages the logic for displaying global
 * charts
 * based on selected entity types (e.g., Songs or Artists) and metrics (e.g.,
 * Listeners, Playcount).
 */
public class GlobalChartController {
  private TrackService trackService;
  private ArtistService artistService;
  private BarChart<String, Number> barChart;
  private ComboBox<String> entityTypeComboBox;
  private ComboBox<String> metricComboBox;

  /**
   * Constructs a GlobalChartController and initializes required services and UI
   * components.
   *
   * @param barChart           The BarChart component used to display data.
   * @param entityTypeComboBox The ComboBox for selecting the entity type (e.g.,
   *                           Songs or Artists).
   * @param metricComboBox     The ComboBox for selecting the metric type (e.g.,
   *                           Listeners, Playcount).
   */
  public GlobalChartController(BarChart<String, Number> barChart,
      ComboBox<String> entityTypeComboBox,
      ComboBox<String> metricComboBox) {
    this.trackService = new TrackService();
    this.artistService = new ArtistService();
    this.barChart = barChart;
    this.entityTypeComboBox = entityTypeComboBox;
    this.metricComboBox = metricComboBox;

    initialize();
  }

  /**
   * Initializes the controller by setting up event listeners and updating the
   * chart on startup.
   */
  private void initialize() {
    entityTypeComboBox.setOnAction(event -> updateChart());
    metricComboBox.setOnAction(event -> updateChart());
    updateChart();
  }

  /**
   * Updates the bar chart based on the selected entity type and metric.
   */
  public void updateChart() {
    String entityType = entityTypeComboBox.getValue();
    String metric = metricComboBox.getValue();
    String sortMetric = metric.equals("Listeners") ? "listeners" : "playcount";

    barChart.getData().clear();

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName(entityType + " by " + metric);

    if (entityType.equals("Songs")) {
      ArrayList<Track> tracks = trackService.getTopTracks(sortMetric);
      for (Track track : tracks) {
        String name = track.getName();
        Number value = track.getListeners();
        series.getData().add(new XYChart.Data<>(name, value));
      }
    } else {
      ArrayList<Artist> artists = artistService.getTopChartArtist(sortMetric);
      for (Artist artist : artists) {
        String name = artist.getName();
        Number value = sortMetric.equals("listeners") ? artist.getListeners() : artist.getPlayCount();
        series.getData().add(new XYChart.Data<>(name, value));
      }
    }

    barChart.getData().add(series);
    barChart.setTitle("Top " + entityType + " by " + metric);
  }
}
