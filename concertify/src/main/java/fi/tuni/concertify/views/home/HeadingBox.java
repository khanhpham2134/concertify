package fi.tuni.concertify.views.home;

import java.util.concurrent.atomic.AtomicBoolean;

import fi.tuni.concertify.controllers.ArtistController;
import fi.tuni.concertify.interfaces.Component;
import fi.tuni.concertify.views.artists.ArtistList;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;

/**
 * The HeadingBox class is responsible for displaying a heading in the user
 * interface.
 * It can either show a custom heading text or the name of the current artist.
 * Additionally, it supports the functionality of adding or removing the current
 * artist from favorites,
 * with a dynamic icon indicating the favorite status.
 * This class implements the Component interface for integration into the larger
 * UI structure.
 */
public class HeadingBox implements Component {
  HBox root;
  String textHeading;
  private HBox headingBox = new HBox();
  private ArtistController artistController = new ArtistController();

  /**
   * Constructs a HeadingBox instance with the provided root node.
   * The root node (HBox) is used to bind the dimensions of the heading container
   * and control its layout within the parent UI.
   * 
   * @param root The root node (HBox) used for dimension binding.
   */
  public HeadingBox(Node root) {
    this.root = (HBox) root;
  }

  /**
   * Constructs a HeadingBox instance with the provided root node and custom
   * heading text.
   * This constructor allows setting a specific heading text to be displayed.
   * 
   * @param root        The root node (HBox) used for dimension binding.
   * @param textHeading The custom heading text to display in the box.
   */
  public HeadingBox(Node root, String textHeading) {
    this.root = (HBox) root;
    this.textHeading = textHeading;
  }

  /**
   * Renders the heading box by setting up the layout, text, and favorite icon.
   * If a custom heading text is provided, it is displayed. Otherwise, the name of
   * the current artist
   * is shown. The favorite icon is dynamically displayed based on the artist's
   * favorite status,
   * and clicking the icon toggles the favorite status.
   */
  public void render() {
    HBox headingInfo = new HBox();
    headingInfo.setSpacing(30);
    headingInfo.setAlignment(Pos.CENTER);
    headingInfo.prefHeightProperty().bind(root.heightProperty().multiply(0.25));
    headingInfo.setPadding(new Insets(20, 0, 0, 0));

    headingBox.setSpacing(10);
    headingBox.setAlignment(Pos.CENTER);
    Label headingText = (textHeading != null)
        ? new Label(textHeading)
        : new Label(ArtistList.currentArtist.get());
    headingText.setStyle("-fx-font-size: 100px; -fx-font-weight: bold;-fx-text-fill: #e5e5e5;");

    if (textHeading != null) {
      headingInfo.getChildren().addAll(headingText);
    } else {
      Image favIcon = new Image(getClass().getResourceAsStream("/images/fav_icon.png"));
      Image favIconColored = new Image(getClass().getResourceAsStream("/images/fav_icon_colored.png"));

      AtomicBoolean isFavorited = new AtomicBoolean(ArtistList.currentArtistFavoriteStatus.get());

      ImageView favIconView = new ImageView(isFavorited.get() ? favIconColored : favIcon);
      favIconView.setFitHeight(40);
      favIconView.setFitWidth(40);
      favIconView.setSmooth(true);

      favIconView.setOnMouseClicked(event -> {
        if (!isFavorited.get()) {
          artistController.addToFavorites(ArtistList.currentArtist.get());
        } else {
          artistController.removeFromFavorites(ArtistList.currentArtist.get());
        }
        isFavorited.set(!isFavorited.get());
        favIconView.setImage(isFavorited.get() ? favIconColored : favIcon);
      });
      favIconView.setStyle("-fx-cursor: hand;");

      headingInfo.getChildren().addAll(headingText, favIconView);
    }

    headingBox.getChildren().addAll(headingInfo);
  }

  /**
   * Returns the heading box container (HBox) after it has been rendered.
   * This method should be called to get the heading element with the applied
   * layout properties.
   * 
   * @return The heading box container (HBox) with the applied render properties.
   */
  @Override
  public HBox get() {
    headingBox.getChildren().clear();
    render();
    return headingBox;
  }

}
