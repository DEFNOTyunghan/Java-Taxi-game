import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

/**
 * Represents a smoke effect in the game, which can be rendered on the screen.
 * The smoke object has a position, an associated image, and a duration defined
 * by the number of frames it is displayed.
 */
public class Smoke extends Effect {
    private final Properties GAME_PROPS;
    private final Image SMOKE_IMAGE;

    /**
     * Constructs a Smoke object with specified properties, position, and frame settings.
     *
     * @param gameProps the game properties containing image and speed settings
     * @param XPos the initial X coordinate of the smoke
     * @param YPos the initial Y coordinate of the smoke
     */
    public Smoke(Properties gameProps, double XPos, double YPos) {
        this.GAME_PROPS = gameProps;
        setXPos(XPos);
        setYPos(YPos);
        SMOKE_IMAGE = new Image(GAME_PROPS.getProperty("gameObjects.smoke.image"));
        setFrames(Integer.parseInt(GAME_PROPS.getProperty("gameObjects.smoke.ttl")));
        setSCREEN_SPEED(Integer.parseInt(GAME_PROPS.getProperty("gameObjects.taxi.speedY")));
    }

    /**
     * Renders the smoke on the screen and updates its position based on user input.
     * Decreases frame count as the smoke persists.
     *
     * @param input the user input to determine movement direction
     * @param driverInTaxi indicates if the driver is currently in the taxi
     */
    @Override
    public void render(Input input, boolean driverInTaxi) {
        SMOKE_IMAGE.draw(getXPos(), getYPos());

        if (input.isDown(Keys.UP) && driverInTaxi) {
            setYPos(getYPos() + getSCREEN_SPEED());
        } else if (input.isDown(Keys.UP) && !driverInTaxi) {
            setYPos(getYPos() + 1);
        } else if (input.isDown(Keys.DOWN) && !driverInTaxi) {
            setYPos(getYPos() - 1);
        }

        if (getFrames() > 0) {
            setFrames(getFrames() - 1);
        }
    }
}
