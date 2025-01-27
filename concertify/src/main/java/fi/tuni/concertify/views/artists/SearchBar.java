package fi.tuni.concertify.views.artists;

import fi.tuni.concertify.interfaces.Component;
import fi.tuni.concertify.views.home.NavigationManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * The SearchBar class provides a user interface element for searching artists
 * by name.
 * It consists of a text field for input and a search button.
 * The entered search term is used to display the search results in a new view.
 */
public class SearchBar implements Component {
  private HBox searchBar = new HBox();
  public static StringProperty searchTerm = new SimpleStringProperty("");

  /**
   * Renders the search bar with a text field for artist name input
   * and a button to trigger the search action. The button updates
   * the search term and switches to the search results view.
   */
  public void render() {
    TextField searchBarField = new TextField();
    searchBar.getStylesheets().add(getClass().getResource("/styles/search-bar.css").toExternalForm());
    searchBar.setSpacing(10);
    searchBar.setPadding(new Insets(80, 20, 70, 20));

    searchBarField.setPromptText("Enter artist name");
    searchBarField.setPrefWidth(600);
    searchBarField.getStyleClass().add("text-field");

    Button searchButton = new Button("Search");
    searchButton.setStyle("-fx-font-weight: bold;");
    searchButton.getStyleClass().add("search-button");

    searchButton.setOnAction(e -> {
      searchTerm.set(searchBarField.getText());
      NavigationManager.switchMainContent("artist-search-results");
    });

    searchBar.getChildren().addAll(searchBarField, searchButton);
  }

  /**
   * Returns the search bar container (HBox) that holds the text field and search
   * button.
   *
   * @return HBox containing the search bar components.
   */
  @Override
  public HBox get() {
    searchBar.getChildren().clear();
    render();
    return searchBar;
  }
}
