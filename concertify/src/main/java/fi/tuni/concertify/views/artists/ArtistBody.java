package fi.tuni.concertify.views.artists;

import fi.tuni.concertify.interfaces.Component;
import fi.tuni.concertify.views.events.EventList;
import fi.tuni.concertify.views.home.NavBar;
import fi.tuni.concertify.views.home.NavigationManager;
import fi.tuni.concertify.controllers.ArtistController;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

/**
 * The ArtistBody class represents the main content area for displaying artist
 * information
 * in the application, including the artist's bio, statistics, and events.
 * This class implements the Component interface and contains methods to manage
 * and display
 * information related to an artist.
 */
public class ArtistBody implements Component {
  HBox root;
  private VBox artistBody = new VBox();
  private HBox artistBioTab = new HBox(40);
  private VBox topSongsAndAlbums = new VBox();

  private BarChart<String, Number> barChart;
  private ComboBox<String> metricComboBox;
  private ArtistController artistController = new ArtistController();

  /**
   * Constructor for initializing the ArtistBody with a root node.
   * 
   * @param root The root node to which this component will be added.
   */
  public ArtistBody(Node root) {
    this.root = (HBox) root;
  }

  /**
   * Creates a button that switches between different tabs (Artist Bio, Artist
   * Statistics, etc.).
   * 
   * @param parent    The parent container for the button.
   * @param text      The label for the button.
   * @param component The component to display when the tab is selected.
   * @return The created button.
   */
  private Button createTabButton(HBox parent, String text, Node component) {
    Button tabSwitch = new Button(text);
    tabSwitch.getStyleClass().add("tab-button");
    tabSwitch.setOnAction(e -> {
      artistBody.getChildren().clear();
      if (text.equals("Artist Bio")) {
        createArtistBio();
      } else {
        createArtistStats();
      }
      artistBody.getChildren().addAll(parent, component);
    });

    return tabSwitch;
  }

  /**
   * Creates and sets up the combo boxes for selecting metrics (e.g., "Listeners",
   * "Playcount").
   */
  private void createComboBoxes() {
    ObservableList<String> metrics = FXCollections.observableArrayList(
        "Listeners", "Playcount");
    metricComboBox = new ComboBox<>(metrics);
    metricComboBox.setValue("Listeners");
    metricComboBox.setStyle("-fx-pref-width: 200px;");
    metricComboBox.getStyleClass().addAll("combo-box", "combo-box:hover", "list-cell");
  }

  /**
   * Creates and configures a bar chart to display artist statistics.
   */
  private void createBarChart() {
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();

    barChart = new BarChart<>(xAxis, yAxis);
    barChart.setTitle("Top Songs");
    barChart.setStyle("-fx-background-color: white;");

    barChart.setPrefHeight(400);
    barChart.setPrefWidth(800);

    barChart.getStyleClass().addAll("chart", "chart-title", "chart-bar", "chart-bar:hover");
    xAxis.getStyleClass().add("axis-label");
    yAxis.getStyleClass().add("axis-label");
  }

  /**
   * Creates the artist bio section, including the artist's image and bio text.
   */
  private void createArtistBio() {
    artistBioTab.setStyle("-fx-background-color: #363636;");
    artistBioTab.getChildren().clear();
    artistBioTab.prefHeightProperty().bind(artistBody.heightProperty().multiply(0.5));
    artistBioTab.setPadding(new Insets(20));

    Image artistImage = new Image(artistController.getArtistProfilePicture(), true);
    ImageView artistImageView = new ImageView(artistImage);

    artistImageView.setFitHeight(300);
    artistImageView.setFitWidth(300);
    artistImageView.setSmooth(true);

    Rectangle clip = new Rectangle(300, 300);
    clip.setArcWidth(300);
    clip.setArcHeight(300);

    artistImageView.setClip(clip);

    HBox.setHgrow(artistImageView, Priority.ALWAYS);

    StackPane container = new StackPane(artistImageView);
    container.setStyle(
        "-fx-background-radius: 150px;");
    container.prefHeightProperty().bind(artistBioTab.heightProperty());

    String artistBio = artistController.getArtistBio();

    Text artistBioText = new Text(artistBio);
    artistBioText.setStyle("-fx-font-size: 20px; -fx-fill: #e5e5e5;");
    TextFlow textFlow = new TextFlow(artistBioText);

    textFlow.prefWidthProperty().bind(artistBody.widthProperty().multiply(0.75));
    textFlow.setLineSpacing(5);
    textFlow.setStyle("-fx-background-color: #363636;");
    textFlow.setPrefHeight(300);

    ScrollPane scrollPane = new ScrollPane(textFlow);
    scrollPane.setStyle("-fx-border-color: #363636; -fx-background-color: #363636;");
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefHeight(300);

    Label artistBioLabel = new Label("Artist Bio");
    VBox artistBioBox = new VBox();
    artistBioBox.setStyle("-fx-background-color: #363636;");
    artistBioLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ff8bb4;");
    artistBioBox.setSpacing(10);
    artistBioBox.getChildren().addAll(artistBioLabel, scrollPane);
    artistBioBox.setAlignment(Pos.CENTER_LEFT);

    artistBioTab.getChildren().addAll(container, artistBioBox);
  }

  /**
   * Creates the artist statistics section, including the bar chart for displaying
   * top songs and albums.
   */
  private void createArtistStats() {
    topSongsAndAlbums.getChildren().clear();
    createComboBoxes();
    createBarChart();

    metricComboBox.setOnAction(event -> {
      barChart.getData().clear();
      String selectedMetric = metricComboBox.getValue();
      barChart.getData().add(artistController.getTopTracksSeries(selectedMetric));
    });

    Label topSongsAndAlbumsLabel = new Label("Top Songs And Albums");
    topSongsAndAlbumsLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #9079FF;");

    HBox controlsBox = new HBox(20);
    controlsBox.setAlignment(Pos.CENTER);
    controlsBox.getChildren().add(metricComboBox);

    topSongsAndAlbums.setPadding(new Insets(20));
    topSongsAndAlbums.setSpacing(20);
    topSongsAndAlbums.getChildren().addAll(controlsBox, barChart);
    barChart.getData().add(artistController.getTopTracksSeries(metricComboBox.getValue()));
  }

  /**
   * Renders the artist body component, including tabs for Artist Bio, Artist
   * Statistics, and Artist Events.
   */
  public void render() {
    artistController.loadArtist(ArtistList.currentArtist.get());
    HBox artistTabHeader = new HBox();
    artistTabHeader.setSpacing(20);
    artistTabHeader.setAlignment(Pos.CENTER);

    Button backButton = new Button("<<");
    backButton.getStyleClass().add("back-button");
    backButton.setOnAction(e -> {
      if (NavBar.observableString.get().equals("Saved Artists")) {
        NavigationManager.switchMainContent("artist-saved");
      } else if (NavBar.observableString.get().equals("Search Artist")) {
        NavigationManager.switchMainContent("artist-search-results");
      }
    });

    Button artistBioTabSwitch = createTabButton(artistTabHeader, "Artist Bio", artistBioTab);
    Button artistStatsTabSwitch = createTabButton(artistTabHeader, "Artist Statistics", topSongsAndAlbums);
    Button artistEventsTabSwitch = createTabButton(artistTabHeader, "Artist's Events",
        new EventList(root, "artist").get());

    artistTabHeader.getChildren().addAll(backButton, artistBioTabSwitch, artistStatsTabSwitch, artistEventsTabSwitch);

    artistBody.getStylesheets().add(getClass().getResource("/styles/artist-body.css").toExternalForm());
    artistBody.getStylesheets().add(getClass().getResource("/styles/chart.css").toExternalForm());
    artistBody.setPadding(new Insets(20));
    artistBody.setAlignment(Pos.TOP_CENTER);
    artistBody.setSpacing(20);

    createArtistBio();
    artistBody.setStyle("-fx-background-color: #363636;");
    artistBody.getChildren().addAll(artistTabHeader, artistBioTab);
  }

  /**
   * Returns the VBox that contains the artist body content.
   * 
   * @return The VBox containing the artist body content.
   */
  @Override
  public VBox get() {
    artistBody.getChildren().clear();
    render();
    return artistBody;
  }
}
