package fi.tuni.concertify.views.home;

import fi.tuni.concertify.interfaces.Component;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Represents the content section of the user interface, usually containing
 * elements like charts,
 * maps, or other components. The content container adjusts its width relative
 * to the root container
 * and is styled with a dark background color.
 */
public class Content implements Component {
  HBox root;
  VBox content = new VBox();

  /**
   * Constructs a Content instance with the provided root node.
   * The root node (HBox) is used to bind the dimensions of the content container
   * and control its layout within the parent UI.
   * 
   * @param root The root node (HBox) used for dimension binding.
   */
  public Content(Node root) {
    this.root = (HBox) root;
  }

  public void render() {
    content.prefWidthProperty().bind(root.widthProperty().multiply(0.8));
    content.setStyle("-fx-background-color: #363636;");
  }

  public VBox get() {
    render();
    return content;
  }
}
