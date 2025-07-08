import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

/**
 * Represents a fire effect in the game, which is rendered at a specified position.
 * The Fire class handles its attributes, rendering, and frame management.
 */
public class Fire extends Effect {
    private final Properties GAME_PROPS;
    private final Image FIRE_IMAGE;

    /**
     * Constructs a Fire object with specified properties, position, and frame settings.
     *
     * @param gameProps the game properties containing image and speed settings
     * @param XPos the initial X coordinate of the fire
     * @param YPos the initial Y coordinate of the fire
     */
    public Fire(Properties gameProps, double XPos, double YPos) {
        this.GAME_PROPS = gameProps;
        setXPos(XPos);
        setYPos(YPos);
        FIRE_IMAGE = new Image(GAME_PROPS.getProperty("gameObjects.fire.image"));
        setFrames(Integer.parseInt(GAME_PROPS.getProperty("gameObjects.smoke.ttl")));
        setSCREEN_SPEED(Integer.parseInt(GAME_PROPS.getProperty("gameObjects.taxi.speedY")));
    }

    /**
     * Renders the fire on the screen and updates its position based on user input.
     * Decreases frame count as the fire persists.
     *
     * @param input the user input to determine movement direction
     * @param driverInTaxi indicates if the driver is currently in the taxi
     */
    @Override
    public void render(Input input, boolean driverInTaxi) {
        FIRE_IMAGE.draw(getXPos(), getYPos());

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
