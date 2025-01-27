package fi.tuni.concertify.interfaces;

import javafx.scene.Node;

/**
 * The Component interface represents a generic UI component that can be
 * rendered
 * and provide a corresponding JavaFX Node representation.
 */
public interface Component {

  /**
   * Renders the UI component.
   * This method is responsible for initializing and preparing the component for
   * display.
   */
  void render();

  /**
   * Retrieves the JavaFX Node associated with the component.
   *
   * @return The Node representation of the component.
   */
  Node get();
}