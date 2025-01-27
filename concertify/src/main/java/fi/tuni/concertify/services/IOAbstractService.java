package fi.tuni.concertify.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Abstract service class providing methods for reading from and writing to
 * files,
 * as well as retrieving data from external APIs.
 */
public abstract class IOAbstractService {

  private final Gson gson;

  /**
   * Constructs a new IOAbstractService instance and sets up Gson with a custom
   * date format.
   */
  public IOAbstractService() {
    this.gson = new GsonBuilder()
        .setDateFormat("MMM dd, yyyy, h:mm:ss a") // Matches "Oct 26, 2024, 7:40:37 AM"
        .create();
  }

  /**
   * Reads data from a JSON file located at the given file path.
   *
   * @param filePath  The path of the JSON file to be read.
   * @param typeClass The class type of the objects to read.
   * @return The JSON data read from the file as an ArrayList of objects.
   */
  public <T> ArrayList<T> readFromFile(String filePath, Class<T> typeClass) {
    try (FileReader fileReader = new FileReader(filePath)) {
      JsonElement jsonElement = JsonParser.parseReader(fileReader);

      if (jsonElement.isJsonArray()) {
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        ArrayList<T> list = new ArrayList<>();

        jsonArray.forEach(element -> list.add(gson.fromJson(element, typeClass)));

        return list;
      } else {
        throw new IllegalStateException("The file does not contain a JSON array.");
      }
    } catch (IOException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  /**
   * Writes the provided data to a file at the specified file path.
   *
   * @param filePath The path where the JSON data will be written.
   * @param data     The data to be written to the file.
   */
  public <T> void writeToFile(String filePath, ArrayList<T> data) {
    try (FileWriter fileWriter = new FileWriter(filePath)) {
      gson.toJson(data, fileWriter);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Retrieves data from an external API using the provided URL.
   * 
   * @param apiUrl the URL of the API to fetch data from
   * @return the raw JSON response from the API
   * @throws IOException if an error occurs while reading from the API
   */
  public String retrieveDataFromAPI(String apiUrl) throws IOException {
    URL url = new URL(apiUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    int responseCode = connection.getResponseCode();

    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();
      connection.disconnect();
      return response.toString();
    }
    throw new IOException("Failed to retrieve data from the API. Response code: " + responseCode);
  }
}