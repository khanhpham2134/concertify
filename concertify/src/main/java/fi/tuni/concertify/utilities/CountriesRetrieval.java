package fi.tuni.concertify.utilities;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Utility class for retrieving country data from a JSON file.
 * The countries are stored in a HashMap where the key is the country code and
 * the value is the country name.
 */
public class CountriesRetrieval {

  /**
   * Retrieves a list of countries from the database (JSON file).
   * The countries are returned as a HashMap where the key is the country code and
   * the value is the country name.
   * 
   * @return A HashMap containing the country codes as keys and country names as
   *         values.
   */
  public static HashMap<String, String> getCountriesFromDB() {
    String filePath = "./database/countries.json";

    Gson gson = new Gson();

    Type type = new TypeToken<HashMap<String, String>>() {
    }.getType();

    HashMap<String, String> countriesMap = new HashMap<>();

    try (FileReader reader = new FileReader(filePath)) {
      countriesMap = gson.fromJson(reader, type);

    } catch (IOException e) {
      e.printStackTrace();
    }

    return countriesMap;
  }
}