package fi.tuni.concertify.views.auth;

import fi.tuni.concertify.interfaces.Component;
import fi.tuni.concertify.views.ComponentFactory;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The AuthForm class provides the user interface for authentication (login and
 * sign-up).
 * It displays either the login form or the sign-up form, allowing users to
 * switch between them.
 */
public class AuthForm implements Component {
  private HBox root;
  private VBox form = new VBox();
  private VBox loginField;
  private VBox signUpField;
  private ComponentFactory factory;

  /**
   * Constructs an AuthForm instance, initializing the factory and root node.
   * The factory is used to create the login and sign-up components, and the root
   * node
   * is used to bind the form's layout to the parent container.
   *
   * @param factory The ComponentFactory used to create login and sign-up forms.
   * @param root    The root node (HBox) to which the authentication form will be
   *                bound.
   */
  public AuthForm(ComponentFactory factory, Node root) {
    this.factory = factory;
    this.root = (HBox) root;
  }

  /**
   * Renders the authentication form with an image (logo) and either the login or
   * sign-up form
   * based on the user's interaction. The user can toggle between the login and
   * sign-up forms.
   */
  @Override
  public void render() {
    form.getStylesheets().add(getClass().getResource("/styles/auth-form.css").toExternalForm());

    form.prefWidthProperty().bind(root.widthProperty().multiply(0.4));
    form.setAlignment(Pos.CENTER);

    Image image = new Image(getClass().getResourceAsStream("/images/logo1.png"));
    ImageView imageView = new ImageView(image);
    imageView.setPreserveRatio(true);
    imageView.setSmooth(true);

    root.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
      double paneWidth = root.getWidth() * 0.4;
      imageView.setFitWidth(paneWidth);
    });

    EventHandler<MouseEvent> switchToSignUp = e -> {
      form.getChildren().remove(loginField);
      form.getChildren().add(signUpField);
    };

    EventHandler<MouseEvent> switchToLogin = e -> {
      form.getChildren().remove(signUpField);
      form.getChildren().add(loginField);
    };

    Component login = factory.createLogin(switchToSignUp);
    loginField = (VBox) login.get();

    Component signUp = factory.createSignUp(switchToLogin);
    signUpField = (VBox) signUp.get();

    form.getChildren().addAll(imageView, loginField);
  }

  /**
   * Returns the root container (VBox) for the authentication form, which contains
   * the logo
   * and the currently displayed form (login or sign-up).
   *
   * @return VBox containing the authentication form components.
   */
  @Override
  public VBox get() {
    render();
    return form;
  }
}
