import java.util.Scanner;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class Geocode{ 

    public static String city_name=""; //Je réutilise dans Weather.java

    public static String prompt_location_name() {
        Scanner sc = new Scanner(System.in);
        String user_input = sc.nextLine();
        city_name = user_input;
        sc.close();
        return user_input;
    }

    public static String http_GET_REQUEST_1(String input, String owm_api_key) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://api.openweathermap.org/geo/1.0/direct?q="+input+"&limit="+"1"+"&appid="+owm_api_key))
        .build();
        HttpResponse<String> reponse = null;
		try {
			reponse = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        return reponse.body();
    }

    public static Map<String, String> getLatLon(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonArray = mapper.readTree(jsonString);
            JsonNode jsonObject = jsonArray.get(0);
            String lat = jsonObject.get("lat").asText();
            String lon = jsonObject.get("lon").asText();
            Map<String, String> map = new HashMap<>();
            map.put("lat", lat);
            map.put("lon", lon);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}