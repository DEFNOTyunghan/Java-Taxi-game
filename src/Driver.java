import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

/**
 * Represents a driver in the game, extending the Human class.
 * The Driver class handles the driver's movement and interactions,
 * including entering a taxi and rendering the driver's appearance on the screen.
 * It also manages the driver's invincibility state and collision behavior.
 */
public class Driver extends Human{
    private final Properties GAME_PROPS;

    private final Image DRIVER_IMAGE;
    private final int DRIVER_X_SPEED;
    private final int DRIVER_Y_SPEED;

    private boolean invincible = false;

    /**
     * Constructs a Driver object with specified properties and initial position.
     *
     * @param gameProps the properties file containing configuration values for the driver
     * @param driverX the initial X position of the driver
     * @param driverY the initial Y position of the driver
     */
    public Driver(Properties gameProps, double driverX, double driverY) {
        this.GAME_PROPS = gameProps;

        setTAXI_GET_IN_RADIUS(Integer.parseInt(GAME_PROPS.getProperty("gameObjects.driver.taxiGetInRadius")));

        DRIVER_IMAGE = new Image(GAME_PROPS.getProperty("gameObjects.driver.image"));
        setXPos(driverX);
        setYPos(driverY);
        DRIVER_X_SPEED = Integer.parseInt(GAME_PROPS.getProperty("gameObjects.driver.walkSpeedX"));
        DRIVER_Y_SPEED = Integer.parseInt(GAME_PROPS.getProperty("gameObjects.driver.walkSpeedY"));
        setHUMAN_RADIUS(Integer.parseInt(GAME_PROPS.getProperty("gameObjects.driver.radius")));
        setHumanHealth(Double.parseDouble(GAME_PROPS.getProperty("gameObjects.driver.health")));
    }

    /**
     * Renders the driver on the screen based on user input and the driver's state.
     * @param input the current input from the user for controlling the driver
     * @param taxiX the X position of the taxi for positioning the driver when in the taxi
     */
    public void render(Input input, double taxiX) {
        if (getCollisionTimeout() > getSEPARATION_DURATION() && !invincible) {
            if (isMoveForward()) {
                setYPos(getYPos() - getSEPARATION_DISTANCE());
            } else {
                setYPos(getYPos() + getSEPARATION_DISTANCE());
            }
        }

        if (getCollisionTimeout() == 0 && invincible) {
            invincible = false;
        }

        if (!isInTaxi()) {
            DRIVER_IMAGE.draw(getXPos(), getYPos());
            if (input.isDown(Keys.LEFT)) {
                setXPos(getXPos() - DRIVER_X_SPEED);
            } else if (input.isDown(Keys.RIGHT)) {
                setXPos(getXPos() + DRIVER_X_SPEED);
            }
        } else {
            setXPos(taxiX);
        }
    }

    /**
     * Allows the driver to enter the taxi if within the specified radius.
     * @param taxiX the X position of the taxi
     * @param taxiY the Y position of the taxi (currently unused)
     */
    @Override
    public void enterTaxi(double taxiX, double taxiY) {

        double distance = Math.sqrt(Math.pow((getXPos() - taxiX), 2) + Math.pow((getYPos() - taxiY), 2));

        if (distance < getTAXI_GET_IN_RADIUS()) {
            setInTaxi(true);
        }
    }

    // setters and getters
    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public boolean isInvincible() {
        return invincible;
    }
}
