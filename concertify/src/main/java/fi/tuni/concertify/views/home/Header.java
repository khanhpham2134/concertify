package fi.tuni.concertify.views.home;

import fi.tuni.concertify.interfaces.Component;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * The Header class represents the header section of the user interface.
 * It is responsible for defining the layout and style of the header area
 * and ensures it adapts to the size of the parent container (VBox).
 * This class implements the Component interface, making it suitable to be
 * integrated into the larger UI structure.
 */
public class Header implements Component {
  VBox root;
  HBox header = new HBox();

  /**
   * Constructs a Header instance with the provided root node.
   * The root node (VBox) is used to bind the dimensions of the header container
   * and control its layout within the parent UI.
   * 
   * @param root The root node (VBox) used for dimension binding.
   */
  public Header(Node root) {
    this.root = (VBox) root;
  }

  /**
   * Renders the header by configuring its layout properties.
   * This method binds the header's height to a percentage (25%) of the parent
   * container's height,
   * sets the alignment of the content within the header to the center, and
   * applies a dark background color.
   */
  public void render() {
    header.prefHeightProperty().bind(root.heightProperty().multiply(0.25));
    header.setAlignment(Pos.CENTER);
    header.setStyle("-fx-background-color: #363636;");
    HBox.setHgrow(header, Priority.ALWAYS);

  }

  /**
   * Returns the header container (HBox) after it has been rendered.
   * This method should be called to get the header element after layout
   * properties are applied.
   * 
   * @return The header container (HBox) with the applied render properties.
   */
  public HBox get() {
    render();
    return header;
  }
}
