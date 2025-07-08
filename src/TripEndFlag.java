import bagel.*;
import java.util.Properties;

/**
 * Represents a trip end flag in the game, indicating the end of a trip for the taxi.
 * The flag is rendered on the screen and can move based on user input and driver status.
 */
public class TripEndFlag {
    private final Image FLAG_IMAGE;
    private final int FLAG_SPEED;
    private final int flagX;
    private double flagY;

    /**
     * Constructs a TripEndFlag with specified properties.
     *
     * @param gameProps the game properties containing flag image and speed
     * @param flagX the X coordinate of the flag
     * @param flagY the Y coordinate of the flag
     */
    public TripEndFlag(Properties gameProps, int flagX, double flagY) {
        // set end flag properties
        FLAG_IMAGE = new Image(gameProps.getProperty("gameObjects.tripEndFlag.image"));
        FLAG_SPEED = Integer.parseInt(gameProps.getProperty("gameObjects.taxi.speedY"));
        this.flagX = flagX;
        this.flagY = flagY;
    }

    /**
     * Renders the trip end flag on the screen.
     * Updates the flag's Y position based on user input and whether the driver is in the taxi.
     *
     * @param input the user input to determine movement direction
     * @param driverInTaxi indicates if the driver is currently in the taxi
     */
    public void render(Input input, boolean driverInTaxi) {
        FLAG_IMAGE.draw(flagX, flagY);

        if (driverInTaxi) {
            if (input.isDown(Keys.UP)) {
                flagY += FLAG_SPEED;
            }
        } else {
            if (input.isDown(Keys.UP)) {
                flagY++;
            } else if (input.isDown(Keys.DOWN)) {
                flagY--;
            }
        }
    }

    // getters and setters
    public double getFlagY() {
        return flagY;
    }

    public int getFlagX() {
        return flagX;
    }

    public void setFlagY(double flagY) {
        this.flagY = flagY;
    }
}
