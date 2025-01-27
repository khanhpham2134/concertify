package fi.tuni.concertify.views.home;

import fi.tuni.concertify.interfaces.Component;
import fi.tuni.concertify.views.ComponentFactory;
import javafx.scene.layout.HBox;

/**
 * The Home class represents the home screen of the application.
 * It contains a layout with an HBox to manage and display the content.
 * The class is responsible for creating and displaying the navigation bar.
 * This class implements the Component interface for integration into the larger
 * UI structure.
 */
public class Home implements Component {
  private HBox home = new HBox();
  private ComponentFactory factory;

  /**
   * Constructs a Home instance with the provided ComponentFactory.
   * The factory is used to create the various components, such as the navigation
   * bar,
   * that will be displayed on the home screen.
   * 
   * @param factory The component factory used for creating various UI components.
   */
  public Home(ComponentFactory factory) {
    this.factory = factory;
  }

  /**
   * Renders the content of the home screen by clearing the previous children from
   * the layout,
   * creating a new navigation bar using the factory, and adding it to the home
   * container.
   */
  @Override
  public void render() {
    home.getChildren().clear();
    Component navBar = factory.createNavBar(home);
    home.getChildren().add(navBar.get());
  }

  /**
   * Returns the home container (HBox) after it has been rendered with the
   * navigation bar.
   * This method should be called to get the home screen UI element with the
   * applied render properties.
   * 
   * @return The home container (HBox) with the navigation bar.
   */
  @Override
  public HBox get() {
    render();
    return home;
  }
}
