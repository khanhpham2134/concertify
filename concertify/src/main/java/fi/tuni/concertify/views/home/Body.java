package fi.tuni.concertify.views.home;

import fi.tuni.concertify.interfaces.Component;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Represents the body section of the user interface. It is a container that
 * holds the main content
 * of the view, such as charts, maps, or other UI elements. The body adjusts its
 * size based on the
 * dimensions of the root container and is styled with a dark background.
 */
public class Body implements Component {
  VBox root;
  HBox body = new HBox();

  /**
   * Constructs a Body instance with the provided root node.
   * The root node (VBox) is used to bind the dimensions of the body container
   * and control its layout within the parent UI.
   * 
   * @param root The root node (VBox) used for dimension binding.
   */
  public Body(Node root) {
    this.root = (VBox) root;
  }

  /**
   * Renders the body of the interface by setting its preferred width and height
   * relative to the root container.
   * The body is aligned to the center, given a background color, and set to grow
   * horizontally as needed.
   */
  public void render() {
    body.prefWidthProperty().bind(root.widthProperty().multiply(0.8));
    body.prefHeightProperty().bind(root.heightProperty().multiply(0.75));
    body.setAlignment(Pos.CENTER);
    body.setStyle("-fx-background-color: #363636;");
    HBox.setHgrow(body, Priority.ALWAYS);
  }

  /**
   * Returns the body container (HBox) after rendering. This method ensures that
   * the body is rendered
   * before it is added to the UI.
   * 
   * @return The HBox containing the body section of the interface.
   */
  public HBox get() {
    render();
    return body;
  }
}
