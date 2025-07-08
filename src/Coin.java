import bagel.*;
import java.util.Properties;

/**
 * Represents a coin power-up in the game, which can be collected by the player.
 * The Coin class manages the coin's properties
 */
public class Coin extends PowerUp{
    private final Image COIN_IMAGE;

    /**
     * Constructs a Coin object with specified properties and initial position.
     *
     * @param gameProps the game properties containing image and speed settings
     * @param coinX the initial X coordinate of the coin
     * @param coinY the initial Y coordinate of the coin
     */
    public Coin(Properties gameProps, double coinX, double coinY) {
        // set coin properties
        COIN_IMAGE = new Image(gameProps.getProperty("gameObjects.coin.image"));
        setXPos(coinX);
        setYPos(coinY);
        setSCREEN_SPEED(Integer.parseInt(gameProps.getProperty("gameObjects.taxi.speedY")));
        setTAXI_RADIUS(gameProps.getProperty("gameObjects.taxi.radius"));
        setPOWER_UP_RADIUS(gameProps.getProperty("gameObjects.coin.radius"));
    }

    /**
     * Renders the coin on the screen if it hasn't collided and updates its position
     * based on user input.
     *
     * @param input the user input to determine movement direction
     * @param driverInTaxi indicates if the driver is currently in the taxi
     */
    public void render(Input input, boolean driverInTaxi) {
        if (!isHasCollided()) {
            COIN_IMAGE.draw(getXPos(), getYPos());
        }
        if (input.isDown(Keys.UP) && driverInTaxi) {
            setYPos(getYPos() + getSCREEN_SPEED());
        } else if (input.isDown(Keys.UP) && !driverInTaxi) {
            setYPos(getYPos() + 1);
        } else if (input.isDown(Keys.DOWN) && !driverInTaxi) {
            setYPos(getYPos() - 1);
        }
    }
}
