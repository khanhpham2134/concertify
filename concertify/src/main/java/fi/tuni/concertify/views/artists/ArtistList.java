package fi.tuni.concertify.views.artists;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import fi.tuni.concertify.controllers.ArtistController;
import fi.tuni.concertify.interfaces.Component;
import fi.tuni.concertify.models.Artist;
import fi.tuni.concertify.views.home.NavigationManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * The ArtistList class displays a list of artists in a grid, allowing users to
 * view and select
 * artists from a saved list or search results. Each artist's profile includes a
 * picture, name,
 * and a favorite button. The class also allows users to add or remove artists
 * from their favorites.
 */
public class ArtistList implements Component {
  HBox root;
  private double colWidth;
  private String listType;
  private GridPane gridPane = new GridPane();
  private HBox artistList = new HBox();
  private ScrollPane scrollPane = new ScrollPane();
  private ArtistController artistController = new ArtistController();
  private ArrayList<Artist> favoriteArtists = new ArrayList<Artist>();

  public static StringProperty currentArtist = new SimpleStringProperty("");
  public static BooleanProperty currentArtistFavoriteStatus = new SimpleBooleanProperty(false);

  /**
   * Constructs an ArtistList instance, initializing the root node and list type.
   * The root node is used to bind the artist list's layout to the parent
   * container,
   * and the list type determines whether to display saved favorite artists or
   * search results.
   *
   * @param root     The root node (HBox) to which the artist list will be bound.
   * @param listType A string indicating the type of list to display ("saved" for
   *                 favorite artists,
   *                 or "search" for search results).
   */
  public ArtistList(Node root, String listType) {
    this.root = (HBox) root;
    this.listType = listType;
  }

  /**
   * Renders the artist list, including the grid of artists and handling empty
   * results.
   */
  @Override
  public void render() {
    artistList.setStyle("-fx-background-color: #363636;");
    scrollPane.setStyle("-fx-background-color: #363636; -fx-border-color: #363636;");
    gridPane.setStyle("-fx-background-color: #363636; -fx-border-color: #363636;");
    favoriteArtists = artistController.getFavoriteArtists();

    ArrayList<Artist> artists = listType.equals("saved")
        ? favoriteArtists
        : artistController.searchArtist(SearchBar.searchTerm.get());

    artistList.setPadding(new Insets(40));
    artistList.prefWidthProperty().bind(root.widthProperty());
    artistList.prefHeightProperty().bind(root.heightProperty());

    if (artists.size() == 0) {
      Label noResultsLabel = new Label(listType.equals("saved") ? "No favorite artists" : " No results found");
      noResultsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
      artistList.getChildren().add(noResultsLabel);
      artistList.setAlignment(Pos.CENTER);
      scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    } else {
      artistList.setAlignment(Pos.TOP_LEFT);
      gridPane.setAlignment(Pos.TOP_LEFT);
      gridPane.setHgap(40);
      gridPane.setVgap(40);

      gridPane.getChildren().clear();

      colWidth = (1024 - 40 * 3 - 40 - 100) / 3;
      populateGrid(gridPane, artists, colWidth);

      root.widthProperty().addListener((observable, oldValue, newValue) -> {
        double newWidth = newValue.doubleValue();
        colWidth = (newWidth - 40 * 3 - 40 - 100) / 3;
        gridPane.getChildren().clear();
        populateGrid(gridPane, artists, colWidth);
      });

      root.heightProperty().addListener((observable, oldValue, newValue) -> {
        double parentHeight = newValue.doubleValue();

        double colHeight = colWidth * 5 / 4;
        int rows = (int) Math.ceil(artists.size() / 3.0);
        double componentHeight = rows * (colHeight + 40) + 40 + 100;

        if (componentHeight == 0)
          scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        else if (componentHeight < parentHeight) {
          scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        } else {
          scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        }
      });

      artistList.getChildren().clear();
      artistList.getChildren().add(gridPane);
    }

    scrollPane.setContent(artistList);
    scrollPane.setFitToWidth(true);
  }

  /**
   * Populates the GridPane with artist cards for each artist in the list.
   * 
   * @param gridPane The GridPane to populate.
   * @param artists  The list of artists to display.
   * @param colWidth The width of the columns in the grid.
   */
  private void populateGrid(GridPane gridPane, ArrayList<Artist> artists, double colWidth) {

    int columns = 3;

    AtomicInteger index = new AtomicInteger(0);

    artists.forEach(artist -> {
      VBox card = (VBox) createArtistCard(artist, colWidth);

      int currentIndex = index.getAndIncrement();
      int col = currentIndex % columns;
      int row = currentIndex / columns;

      gridPane.add(card, col, row);
    });
  }

  /**
   * Creates an individual artist card, including the artist's image, name, and
   * favorite button.
   * 
   * @param artist   The artist to display.
   * @param colWidth The width of the column for the card.
   * @return The VBox containing the artist card components.
   */
  private VBox createArtistCard(Artist artist, double colWidth) {
    VBox artistCard = new VBox();
    double trueWidth = colWidth - 20;

    Image artistImage = new Image(artist.getProfilePicture(), true);
    ImageView artistImageView = new ImageView(artistImage);
    artistImageView.setFitWidth(trueWidth);
    artistImageView.setFitHeight(trueWidth);

    artistImageView.setSmooth(true);

    Rectangle clip = new Rectangle(trueWidth, trueWidth);
    clip.setArcWidth(trueWidth);
    clip.setArcHeight(trueWidth);

    artistImageView.setClip(clip);

    String artistNameText = artist.getName();
    Label artistNameLabel = new Label(artistNameText);
    artistNameLabel.setAlignment(Pos.CENTER);
    artistNameLabel.setMaxWidth(trueWidth);
    artistNameLabel.setWrapText(false);
    artistNameLabel.setStyle("-fx-text-overrun: ellipsis; -fx-font-size: 20px;");

    StackPane container = new StackPane(artistImageView);
    container.setStyle(
        "-fx-background-radius: 50%");

    ImageView favButton = createFavButton(artist);

    artistCard.setSpacing(10);
    artistCard.getChildren().addAll(artistImageView, artistNameLabel, favButton);
    artistCard.setPrefSize(colWidth, colWidth * 5 / 4);
    artistCard.setAlignment(Pos.CENTER);
    artistCard
        .setStyle(
            "-fx-padding: 20; -fx-background-radius: 10px; -fx-background-color: #ff8bb4; -fx-cursor: hand;");

    artistCard.setOnMouseClicked(e -> {
      currentArtistFavoriteStatus.set(favoriteArtists.contains(artist));
      currentArtist.set(artist.getName());
      NavigationManager.switchMainContent("artist-single");
    });

    return artistCard;
  }

  /**
   * Creates the favorite button for each artist card.
   * 
   * @param artist The artist for whom to create the favorite button.
   * @return The ImageView representing the favorite button.
   */
  private ImageView createFavButton(Artist artist) {
    AtomicBoolean isFavorited = new AtomicBoolean(false);
    Image favIcon = new Image(getClass().getResourceAsStream("/images/fav_icon.png"));
    Image favIconColored = new Image(getClass().getResourceAsStream("/images/fav_icon_colored.png"));

    if (listType.equals("saved")) {
      isFavorited.set(true);
    } else {
      if (favoriteArtists.contains(artist)) {
        isFavorited.set(true);
      }
    }

    ImageView favIconView = new ImageView(isFavorited.get() ? favIconColored : favIcon);
    favIconView.setFitHeight(25);
    favIconView.setFitWidth(25);
    favIconView.setSmooth(true);

    favIconView.setOnMouseClicked(e -> {
      e.consume();
      if (!isFavorited.get()) {
        artistController.addToFavorites(artist.getName());
      } else {
        artistController.removeFromFavorites(artist.getName());
      }
      isFavorited.set(!isFavorited.get());
      favIconView.setImage(isFavorited.get() ? favIconColored : favIcon);
      favoriteArtists = artistController.getFavoriteArtists();
    });
    favIconView.setStyle("-fx-cursor: hand;");

    return favIconView;
  }

  /**
   * Returns the ScrollPane containing the artist list and related components.
   * 
   * @return The ScrollPane containing the artist list.
   */
  @Override
  public ScrollPane get() {
    artistList.getChildren().clear();
    scrollPane.setContent(artistList);
    render();
    return scrollPane;
  }
}
