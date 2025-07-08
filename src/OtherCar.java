import bagel.Input;
import bagel.Image;
import bagel.Keys;

import java.util.Properties;

/**
 * Represents an other car object in the game that can move and interact with
 * the environment. This class is a subclass of the Car class and manages its
 * properties, rendering, and behavior based on user input and game state.
 */
public class OtherCar extends Car{
    private final Properties GAME_PROPS;
    private final Image OTHER_CAR_IMAGE;


    /**
     * Constructs an OtherCar object with specified properties and a random image.
     *
     * @param gameProps the game properties containing image, speed, and other settings
     */
    public OtherCar(Properties gameProps) {
        this.GAME_PROPS = gameProps;
        // get random other car image
        int randomNum = MiscUtils.getRandomInt(1, 3);
        String imagePath = String.format(GAME_PROPS.getProperty("gameObjects.otherCar.image"), randomNum);
        OTHER_CAR_IMAGE = new Image(imagePath);

        setCAR_MAX_SPEED(Integer.parseInt(GAME_PROPS.getProperty("gameObjects.enemyCar.maxSpeedY")));
        setCAR_MIN_SPEED(Integer.parseInt(GAME_PROPS.getProperty("gameObjects.enemyCar.minSpeedY")));
        setSCREEN_SPEED(Integer.parseInt(GAME_PROPS.getProperty("gameObjects.taxi.speedY")));
        setCAR_RADIUS(Double.parseDouble(GAME_PROPS.getProperty("gameObjects.otherCar.radius")));
        setCAR_DAMAGE(Double.parseDouble(GAME_PROPS.getProperty("gameObjects.otherCar.damage")));
        setCarHealth(Double.parseDouble(GAME_PROPS.getProperty("gameObjects.otherCar.health")));

        setROAD_LANE_1(Integer.parseInt(GAME_PROPS.getProperty("roadLaneCenter1")));
        setROAD_LANE_2(Integer.parseInt(GAME_PROPS.getProperty("roadLaneCenter2")));
        setROAD_LANE_3(Integer.parseInt(GAME_PROPS.getProperty("roadLaneCenter3")));

        this.getSpawnLocation();
        setCAR_SPEED(MiscUtils.getRandomInt(getCAR_MIN_SPEED(), getCAR_MAX_SPEED() + 1));
    }

    /**
     * Renders the other car on the screen, updates its position based on collision logic,
     * and adjusts its movement based on user input.
     *
     * @param input the user input to determine movement direction
     * @param driverInTaxi indicates if the driver is currently in the taxi
     */
    public void render(Input input, boolean driverInTaxi) {
        OTHER_CAR_IMAGE.draw(getXPos(), getYPos());

        if (getCollisionTimeout() > getSEPARATION_DURATION()) {
            // handle separation logic
            if (isMoveForward()) {
                setYPos(getYPos() - 1);
            } else {
                setYPos(getYPos() + 1);
            }
            setCAR_SPEED(MiscUtils.getRandomInt(getCAR_MIN_SPEED(), getCAR_MAX_SPEED() + 1));
        } else if (getCollisionTimeout() > 0) {
            // do nothing
        } else {
            setYPos(getYPos() - getCAR_SPEED());
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
