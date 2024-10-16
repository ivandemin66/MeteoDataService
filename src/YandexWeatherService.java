import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class YandexWeatherService {

    private final String apiKey;
    private final String apiUrl;

    public YandexWeatherService(String apiKey, String apiUrl) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }

    public String getWeatherByCoordinates(double lat, double lon) throws Exception {
        String urlString = String.format("%s?lat=%s&lon=%s", apiUrl, lat, lon);
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Yandex-Weather-Key", apiKey);

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Ошибка запроса: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        connection.disconnect();

        return content.toString();
    }

    public int extractTemperature(String jsonResponse) {
        String searchKey = "\"temp\":";
        int startIndex = jsonResponse.indexOf(searchKey);
        if (startIndex == -1) {
            throw new RuntimeException("Температура не найдена в ответе.");
        }
        int valueStartIndex = startIndex + searchKey.length();
        int valueEndIndex = jsonResponse.indexOf(",", valueStartIndex);
        String tempString = jsonResponse.substring(valueStartIndex, valueEndIndex).trim();
        return Integer.parseInt(tempString);
    }

    public double getAverageTemperature(double lat, double lon, int limit) throws Exception {
        double totalTemp = 0;
        for (int i = 0; i < limit; i++) {
            String weatherResponse = getWeatherByCoordinates(lat, lon);
            totalTemp += extractTemperature(weatherResponse);
        }
        return totalTemp / limit;
    }
}
