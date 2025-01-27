package fi.tuni.concertify.views.auth;

import fi.tuni.concertify.interfaces.Component;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Represents a background image component for the authentication screen.
 * The image is dynamically resized to fill a specified portion of the parent
 * container.
 * Implements the Component interface for rendering and retrieving the image
 * pane.
 */
public class AuthImage implements Component {
  HBox root;
  Pane imagePane = new StackPane();

  /**
   * Constructs an AuthImage instance, initializing the root node and the image
   * container (StackPane).
   * The root node is used to bind the image layout to the parent container,
   * adjusting its size dynamically.
   *
   * @param root The root node (HBox) that will be used for layout binding of the
   *             image container.
   */
  public AuthImage(Node root) {
    this.root = (HBox) root;
  }

  /**
   * Renders the background image by loading it from the resources and binding its
   * size to the root node's dimensions.
   * The image is resized to fit 60% of the root node's width and the full height
   * of the root node.
   */
  public void render() {
    imagePane.prefWidthProperty().bind(root.widthProperty().multiply(0.6));

    Image image = new Image(getClass().getResourceAsStream("/images/auth_bg.png"));
    ImageView imageView = new ImageView(image);
    imageView.setPreserveRatio(false);
    imageView.setSmooth(true);

    root.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
      double paneWidth = newVal.getWidth() * 0.6;
      double paneHeight = newVal.getHeight();

      imageView.setFitWidth(paneWidth);
      imageView.setFitHeight(paneHeight);
    });

    imagePane.getChildren().add(imageView);
  }

  /**
   * Returns the image pane containing the resized background image.
   * This method ensures that the image is properly rendered before being
   * returned.
   *
   * @return The Pane containing the background image.
   */

  @Override
  public Pane get() {
    render();
    return imagePane;
  }
}
