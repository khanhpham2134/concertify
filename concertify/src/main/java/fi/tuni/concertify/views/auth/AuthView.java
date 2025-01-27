package fi.tuni.concertify.views.auth;

import fi.tuni.concertify.interfaces.Component;
import fi.tuni.concertify.views.ComponentFactory;
import javafx.scene.layout.HBox;

/**
 * Represents the authentication view, which includes an image and a form for
 * user login or sign-up.
 * Implements the Component interface to render and retrieve the authentication
 * view.
 */
public class AuthView implements Component {
  private HBox authView = new HBox();
  ComponentFactory factory = new ComponentFactory();

  /**
   * Constructs an AuthView instance, initializing the component factory.
   * The factory is used to create components like the background image and
   * authentication form.
   *
   * @param factory The ComponentFactory used to create the necessary components
   *                for the authentication view.
   */
  public AuthView(ComponentFactory factory) {
    this.factory = factory;
  }

  /**
   * Renders the authentication view by creating the authentication image and form
   * using the component factory,
   * then adding them to the root container (HBox).
   */
  @Override
  public void render() {
    Component authImage = factory.createAuthImage(authView);
    Component authForm = factory.createAuthForm(authView);

    authView.getChildren().addAll(authImage.get(), authForm.get());
  }

  /**
   * Returns the authentication view (HBox) containing the image and form.
   * Ensures the components are rendered before being returned.
   *
   * @return The HBox containing the authentication view components.
   */
  @Override
  public HBox get() {
    render();
    return authView;
  }
}