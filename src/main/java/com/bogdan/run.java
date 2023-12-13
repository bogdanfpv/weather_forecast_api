public class run {
    public static void main(String[] args) {
        Weather.httpGET_AND_GRAPH(
            Geocode.getLatLon(
                Geocode.http_GET_REQUEST_1(Geocode.prompt_location_name(), owm_key.bgd_owm_key)), 
        owm_key.bgd_owm_key);
    }
}
