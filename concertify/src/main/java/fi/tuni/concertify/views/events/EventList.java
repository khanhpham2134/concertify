package fi.tuni.concertify.views.events;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import fi.tuni.concertify.controllers.EventController;
import fi.tuni.concertify.interfaces.Component;
import fi.tuni.concertify.models.Event;
import fi.tuni.concertify.views.artists.ArtistList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

/**
 * Represents a list of events displayed in a scrollable pane, which can be
 * filtered by saved events, artist, or location.
 * Implements the Component interface to render and retrieve the event list.
 */
public class EventList implements Component {
  HBox root;
  private final ScrollPane scrollPane = new ScrollPane();
  private final VBox eventList = new VBox();
  private EventController eventController = new EventController();
  private String listType;
  private static final double CARD_HEIGHT = 210;
  private ArrayList<Event> favoriteEvents = new ArrayList<Event>();

  /**
   * Constructs an EventList instance, initializing the root HBox and list type
   * for filtering.
   *
   * @param root     The parent node to which the event list will be attached.
   * @param listType The type of events to display (e.g., "saved", "artist",
   *                 "location").
   */
  public EventList(Node root, String listType) {
    this.root = (HBox) root;
    this.listType = listType;
  }

  /**
   * Creates a VBox containing the information about an event, such as the event
   * name, artists, date, and location.
   *
   * @param event The event whose information is to be displayed.
   * @return A VBox containing event details.
   */
  private VBox createEventInfoBox(Event event) {
    Label eventName = new Label(event.getName());
    eventName.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    HBox titleBox = new HBox();
    titleBox.setSpacing(5);
    titleBox.setAlignment(Pos.CENTER_LEFT);
    titleBox.getChildren().addAll(eventName, createFavButton(event));

    Label eventArtists = new Label(formatEventArtists(event));
    eventArtists.setStyle("-fx-font-size: 18px;");

    Label eventDateTime = new Label(event.getDateTimeStart().toString());
    eventDateTime.setStyle("-fx-font-size: 18px;");

    String location = String.format("%s (%s, %s)", event.getLocationName(), event.getCity(),
        event.getCountry());
    Label eventLocation = new Label(location);
    eventLocation.setStyle("-fx-font-size: 18px;");

    VBox eventInfoBox = new VBox();
    eventInfoBox.setStyle("-fx-padding: 15;");
    eventInfoBox.getChildren().addAll(titleBox, eventArtists, eventDateTime, eventLocation);
    return eventInfoBox;
  }

  /**
   * Creates an ImageView for displaying the event's banner image.
   *
   * @param event The event whose image is to be displayed.
   * @return An ImageView showing the event's banner image.
   */
  private ImageView createEventImageView(Event event) {
    Image eventImage = new Image(event.getBannerImage(), true);
    ImageView eventImageView = new ImageView(eventImage);
    eventImageView.setFitHeight(CARD_HEIGHT);
    eventImageView.setPreserveRatio(true);
    return eventImageView;
  }

  /**
   * Creates an image button to toggle the event's favorited status. If the event
   * is not favorited,
   * it will be added to the favorites; otherwise, it will be removed.
   *
   * @param event The event whose favorited status is being toggled.
   * @return An ImageView representing the favorite button.
   */
  private ImageView createFavButton(Event event) {
    AtomicBoolean isFavorited = new AtomicBoolean(false);

    Image favIcon = new Image(getClass().getResourceAsStream("/images/fav_icon.png"));
    Image favIconColored = new Image(getClass().getResourceAsStream("/images/fav_icon_colored.png"));

    isFavorited.set(favoriteEvents.contains(event));

    ImageView favIconView = new ImageView(isFavorited.get() ? favIconColored : favIcon);
    favIconView.setFitHeight(25);
    favIconView.setFitWidth(25);
    favIconView.setSmooth(true);

    favIconView.setOnMouseClicked(e -> {
      e.consume();
      if (!isFavorited.get()) {
        eventController.addEventToFavorites(event);
      } else {
        eventController.removeEventFromFavorites(event);
      }
      isFavorited.set(!isFavorited.get());
      favIconView.setImage(isFavorited.get() ? favIconColored : favIcon);
    });
    favIconView.setStyle("-fx-cursor: hand;");

    return favIconView;
  }

  /**
   * Formats the list of artist names for an event into a string.
   * If no artists are available, it returns a default message.
   *
   * @param event The event whose artist names need to be formatted.
   * @return A formatted string of artist names or a default message.
   */
  private String formatEventArtists(Event event) {
    ArrayList<String> artistNames = event.getArtistNames();
    if (artistNames == null || artistNames.size() == 0) {
      return new String("No artist given");
    }
    if (artistNames.size() == 1) {
      return artistNames.get(0);
    }
    return String.join(", ", artistNames);
  }

  /**
   * Creates a card to display information and image about an event. This includes
   * the event's name, artists, date, location, and image.
   *
   * @param event The event to be displayed on the card.
   * @return An HBox containing the event card.
   */
  private HBox createEventCard(Event event) {
    HBox eventCard = new HBox();

    eventCard.setSpacing(10);
    eventCard.setPrefHeight(CARD_HEIGHT);
    eventCard.setStyle("-fx-background-color: #ff8bb4;");

    AnchorPane apLeft = new AnchorPane(createEventInfoBox(event));
    HBox.setHgrow(apLeft, Priority.ALWAYS);
    AnchorPane apRight = new AnchorPane(createEventImageView(event));

    eventCard.getChildren().addAll(apLeft, apRight);

    return eventCard;
  }

  /**
   * Renders the event list based on the selected list type (e.g., saved events,
   * artist-related events, or location-based events).
   * If no events are found, a message is displayed instead.
   */
  @Override
  public void render() {
    favoriteEvents = eventController.getFavoriteEvents();
    eventList.setStyle("-fx-background-color: #363636;");
    scrollPane.setStyle("-fx-background-color: #363636; -fx-border-color: #363636;");

    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(false);
    HBox.setHgrow(scrollPane, Priority.ALWAYS);

    eventList.prefWidthProperty().bind(root.widthProperty());
    eventList.prefHeightProperty().bind(root.heightProperty());

    eventList.setPadding(new Insets(30));
    eventList.setAlignment(Pos.CENTER);
    eventList.setSpacing(20);

    ArrayList<Event> events = listType.equals("saved")
        ? favoriteEvents
        : listType.equals("artist")
            ? eventController.getEventsRelatedToArtist(ArtistList.currentArtist.get())
            : eventController.getEventsByLocation(
                EventSearchBar.searchCountryCode.get(),
                EventSearchBar.searchCity.get());

    eventList.getChildren().clear();

    if (events.size() == 0) {
      Label noResultsLabel = new Label(listType.equals("saved") ? "No favorite events" : " No results found");
      noResultsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
      eventList.getChildren().add(noResultsLabel);
      eventList.setAlignment(Pos.CENTER);
      scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    } else {
      events.forEach(event -> {
        HBox card = createEventCard(event);
        eventList.getChildren().add(card);
      });
    }

    scrollPane.setContent(eventList);
    scrollPane.setFitToWidth(true);
  }

  /**
   * Returns the ScrollPane containing the event list. It ensures the event list
   * is rendered before being returned.
   *
   * @return A ScrollPane containing the event list.
   */
  @Override
  public ScrollPane get() {
    eventList.getChildren().clear();
    scrollPane.setContent(eventList);
    render();
    return scrollPane;
  }
}
