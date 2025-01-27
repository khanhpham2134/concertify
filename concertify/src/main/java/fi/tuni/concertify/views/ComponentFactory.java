package fi.tuni.concertify.views;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import fi.tuni.concertify.interfaces.Component;
import fi.tuni.concertify.interfaces.UIFactory;
import fi.tuni.concertify.views.auth.AuthForm;
import fi.tuni.concertify.views.auth.AuthImage;
import fi.tuni.concertify.views.auth.AuthView;
import fi.tuni.concertify.views.auth.Login;
import fi.tuni.concertify.views.auth.SignUp;
import fi.tuni.concertify.views.events.EventMap;
import fi.tuni.concertify.views.events.EventSearchBar;
import fi.tuni.concertify.views.events.EventList;
import fi.tuni.concertify.views.home.Body;
import fi.tuni.concertify.views.home.Content;
import fi.tuni.concertify.views.home.Header;
import fi.tuni.concertify.views.home.HeadingBox;
import fi.tuni.concertify.views.home.Home;
import fi.tuni.concertify.views.home.NavBar;
import fi.tuni.concertify.views.artists.SearchBar;
import fi.tuni.concertify.views.artists.ArtistsSongsChart;
import fi.tuni.concertify.views.artists.ArtistList;
import fi.tuni.concertify.views.artists.ArtistBody;
import fi.tuni.concertify.views.events.EventSearchChart;

/**
 * The ComponentFactory class is responsible for creating various UI components
 * in the Concertify application. It implements the UIFactory interface,
 * providing
 * methods for creating components such as authentication views, home page
 * components,
 * artist-related views, event views, and other UI elements.
 * 
 * The factory pattern is used to abstract the creation of different types of UI
 * components
 * and their dependencies, centralizing the creation logic and making it easier
 * to manage
 * and extend the user interface.
 */
public class ComponentFactory implements UIFactory {

  /**
   * Creates and returns a new AuthView component.
   * 
   * @return A new instance of AuthView.
   */
  @Override
  public Component createAuthView() {
    return new AuthView(this);
  }

  /**
   * Creates and returns a new AuthForm component, using the provided root node.
   * 
   * @param root The root node to bind the form to.
   * @return A new instance of AuthForm.
   */
  @Override
  public Component createAuthForm(Node root) {
    return new AuthForm(this, root);
  }

  /**
   * Creates and returns a new AuthImage component, using the provided root node.
   * 
   * @param root The root node to bind the image to.
   * @return A new instance of AuthImage.
   */
  @Override
  public Component createAuthImage(Node root) {
    return new AuthImage(root);
  }

  /**
   * Creates and returns a new Login component with an event handler for switching
   * to the SignUp view.
   * 
   * @param switchToSignUp The event handler for switching to the SignUp view.
   * @return A new instance of Login.
   */
  @Override
  public Component createLogin(EventHandler<MouseEvent> switchToSignUp) {
    return new Login(switchToSignUp);
  }

  /**
   * Creates and returns a new SignUp component with an event handler for
   * switching
   * to the Login view.
   * 
   * @param switchToLogin The event handler for switching to the Login view.
   * @return A new instance of SignUp.
   */
  @Override
  public Component createSignUp(EventHandler<MouseEvent> switchToLogin) {
    return new SignUp(switchToLogin);
  }

  /**
   * Creates and returns a new Home component.
   * 
   * @return A new instance of Home.
   */
  @Override
  public Component createHome() {
    return new Home(this);
  }

  /**
   * Creates and returns a new Content component, using the provided root node.
   * 
   * @param root The root node to bind the content to.
   * @return A new instance of Content.
   */
  @Override
  public Component createContent(Node root) {
    return new Content(root);
  }

  /**
   * Creates and returns a new Header component, using the provided root node.
   * 
   * @param root The root node to bind the header to.
   * @return A new instance of Header.
   */
  @Override
  public Component createHeader(Node root) {
    return new Header(root);
  }

  /**
   * Creates and returns a new Body component, using the provided root node.
   * 
   * @param root The root node to bind the body to.
   * @return A new instance of Body.
   */
  @Override
  public Component createBody(Node root) {
    return new Body(root);
  }

  /**
   * Creates and returns a new NavBar component, using the provided root node.
   * 
   * @param root The root node to bind the navbar to.
   * @return A new instance of NavBar.
   */
  @Override
  public Component createNavBar(Node root) {
    return new NavBar(this, root);
  }

  /**
   * Creates and returns a new SearchBar component.
   * 
   * @return A new instance of SearchBar.
   */
  @Override
  public Component createSearchBar() {
    return new SearchBar();
  }

  /**
   * Creates and returns a new ArtistsSongsChart component.
   * 
   * @return A new instance of ArtistsSongsChart.
   */
  @Override
  public Component createArtistsSongsChart() {
    return new ArtistsSongsChart();
  }

  /**
   * Creates and returns a new HeadingBox component for a single artist heading,
   * using the provided root node.
   * 
   * @param root The root node to bind the heading to.
   * @return A new instance of HeadingBox.
   */
  @Override
  public Component createSingleArtistHeading(Node root) {
    return new HeadingBox(root);
  }

  /**
   * Creates and returns a new HeadingBox component with the specified heading
   * text,
   * using the provided root node.
   * 
   * @param root        The root node to bind the heading to.
   * @param headingText The text to display in the heading.
   * @return A new instance of HeadingBox.
   */
  @Override
  public Component createHeadingBox(Node root, String headingText) {
    return new HeadingBox(root, headingText);
  }

  /**
   * Creates and returns a new ArtistBody component, using the provided root node.
   * 
   * @param root The root node to bind the artist body to.
   * @return A new instance of ArtistBody.
   */
  @Override
  public Component createArtistBody(Node root) {
    return new ArtistBody(root);
  }

  /**
   * Creates and returns a new ArtistList component for the specified list type
   * (e.g., "saved" or "searched"), using the provided root node.
   * 
   * @param root     The root node to bind the artist list to.
   * @param listType The type of the artist list (e.g., "saved" or "searched").
   * @return A new instance of ArtistList.
   */
  @Override
  public Component createArtistList(Node root, String listType) {
    return new ArtistList(root, listType);
  }

  /**
   * Creates and returns a new EventSearch component.
   * 
   * @return A new instance of EventSearchBar.
   */
  @Override
  public Component createEventSearch() {
    return new EventSearchBar();
  }

  /**
   * Creates and returns a new EventList component for the specified list type
   * (e.g., "saved" or "searched"), using the provided root node.
   * 
   * @param root     The root node to bind the event list to.
   * @param listType The type of the event list (e.g., "saved" or "searched").
   * @return A new instance of EventList.
   */
  @Override
  public Component createEventList(Node root, String listType) {
    return new EventList(root, listType);
  }

  /**
   * Creates and returns a new EventSearchChart component.
   * 
   * @return A new instance of EventSearchChart.
   */
  @Override
  public Component createEventSearchChart() {
    return new EventSearchChart();
  }

  /**
   * Creates and returns a new EventMap component, using the provided root node.
   * 
   * @param root The root node to bind the event map to.
   * @return A new instance of EventMap.
   */
  @Override
  public Component createEventMap(Node root) {
    return new EventMap(root);
  }
}