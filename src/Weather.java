/**
 * Represents the weather conditions in the game, including the type of weather
 * and the time range during which it occurs.
 */
public class Weather {
    private String weather;
    private int start;
    private int end;

    /**
     * Constructs a Weather object with specified weather conditions and time range.
     *
     * @param weather the type of weather (e.g., "sunny", "rainy", "cloudy")
     * @param start   the starting time (e.g., in hours) for the weather condition
     * @param end     the ending time (e.g., in hours) for the weather condition
     */
    public Weather(String weather, int start, int end) {
        this.weather = weather;
        this.start = start;
        this.end = end;
    }

    // getters
    public String getWeather() {
        return weather;
    }

    public int getEnd() {
        return end;
    }
}
