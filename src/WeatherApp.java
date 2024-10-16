import java.util.Scanner;

public class WeatherApp {

    public static void main(String[] args) {

        String apiKey = "38c11a66-7c12-48f1-898d-8817adb27610";
        String apiUrl = "https://api.weather.yandex.ru/v2/forecast";


        if (args.length >= 3) {
            try {
                double lat = Double.parseDouble(args[0]);
                double lon = Double.parseDouble(args[1]);
                int limit = Integer.parseInt(args[2]);

                YandexWeatherService weatherService = new YandexWeatherService(apiKey, apiUrl);

                String weatherResponse = weatherService.getWeatherByCoordinates(lat, lon);
                System.out.println("Ответ сервиса (JSON): " + weatherResponse);


                int temp = weatherService.extractTemperature(weatherResponse);
                System.out.println("Температура сейчас: " + temp + "°C");

                double averageTemp = weatherService.getAverageTemperature(lat, lon, limit);
                System.out.println("Средняя температура за " + limit + " запросов: " + averageTemp + "°C");

            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        } else {
            Scanner scanner = new Scanner(System.in);

            try {
                System.out.print("Введите широту (lat): ");
                double lat = Double.parseDouble(scanner.nextLine());

                System.out.print("Введите долготу (lon): ");
                double lon = Double.parseDouble(scanner.nextLine());

                System.out.print("Введите количество запросов для расчета средней температуры (limit): ");
                int limit = Integer.parseInt(scanner.nextLine());

                YandexWeatherService weatherService = new YandexWeatherService(apiKey, apiUrl);

                String weatherResponse = weatherService.getWeatherByCoordinates(lat, lon);
                System.out.println("Ответ сервиса (JSON): " + weatherResponse);

                int temp = weatherService.extractTemperature(weatherResponse);
                System.out.println("Температура сейчас: " + temp + "°C");

                double averageTemp = weatherService.getAverageTemperature(lat, lon, limit);
                System.out.println("Средняя температура за " + limit + " запросов: " + averageTemp + "°C");

            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }

            scanner.close();
        }
    }
}
