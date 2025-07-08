import bagel.*;
import java.util.Properties;

/**
 * Represents a taxi in the game that can move and interact with other objects.
 * The taxi can be in a damaged state and has properties such as position, speed,
 * and health.
 */
public class Taxi implements Collidable{
    private final Properties GAME_PROPS;

    private final Image TAXI_IMAGE;
    private final Image TAXI_DAMAGED_IMAGE;
    private double taxiX;
    private double taxiY;
    private final int TAXI_SPEED_X;
    private final int TAXI_SPEED_Y;
    private final double TAXI_RADIUS;
    private boolean taxiOccupied = false;
    private double taxiHealth;
    private double TAXI_DAMAGE;
    private int collisionTimeout = 0;
    private boolean hitFireBall = false;

    private Car lastCollidedCar;
    private boolean damaged = false;

    /**
     * Constructs a Taxi object with specified properties.
     *
     * @param gameProps the game properties containing image paths, speed, and other settings
     * @param taxiX the initial X position of the taxi
     * @param taxiY the initial Y position of the taxi
     */
    public Taxi(Properties gameProps, double taxiX, double taxiY) {
        this.GAME_PROPS = gameProps;

        // set taxi properties
        TAXI_IMAGE = new Image(GAME_PROPS.getProperty("gameObjects.taxi.image"));
        TAXI_DAMAGED_IMAGE = new Image(GAME_PROPS.getProperty("gameObjects.taxi.damagedImage"));
        this.taxiX = taxiX;
        this.taxiY = taxiY;
        TAXI_SPEED_X = Integer.parseInt(GAME_PROPS.getProperty("gameObjects.taxi.speedX"));
        TAXI_SPEED_Y = Integer.parseInt(GAME_PROPS.getProperty("gameObjects.taxi.speedY"));
        TAXI_RADIUS = Double.parseDouble(GAME_PROPS.getProperty("gameObjects.taxi.radius"));
        taxiHealth = Double.parseDouble(GAME_PROPS.getProperty("gameObjects.taxi.health"));
        TAXI_DAMAGE = Double.parseDouble(GAME_PROPS.getProperty("gameObjects.taxi.damage"));
    }

    /**
     * Renders the taxi on the screen based on its current state (damaged or not)
     * and updates its position based on user input.
     *
     * @param input the user input to determine movement direction
     * @param driverInTaxi indicates if the driver is currently in the taxi
     */
    public void render(Input input, boolean driverInTaxi) {
        if (!damaged) {
            TAXI_IMAGE.draw(taxiX, taxiY);
            if (driverInTaxi) {
                if (input.isDown(Keys.LEFT)) {
                    taxiX -= TAXI_SPEED_X;
                } else if (input.isDown(Keys.RIGHT)) {
                    taxiX += TAXI_SPEED_X;
                }
            } else {
                if (input.isDown(Keys.UP)) {
                    taxiY += 1;
                } else if (input.isDown(Keys.DOWN)) {
                    taxiY -= 1;
                }
            }
        } else {
            TAXI_DAMAGED_IMAGE.draw(taxiX, taxiY);
            if (driverInTaxi) {
                if (input.isDown(Keys.UP)) {
                    taxiY += TAXI_SPEED_Y;
                }
            } else {
                if (input.isDown(Keys.UP)) {
                    taxiY += 1;
                } if (input.isDown(Keys.DOWN)) {
                    taxiY -= 1;
                }
            }
        }
    }

    /**
     * Checks if the taxi has collided with another object based on its position
     * and radius.
     *
     * @param XPos the X position of the other object
     * @param YPos the Y position of the other object
     * @param radius the radius of the other object
     * @return true if a collision has occurred, false otherwise
     */
    @Override
    public boolean hasCollided(double XPos, double YPos, double radius) {
        double distance = Math.sqrt(Math.pow((XPos - taxiX), 2) + Math.pow((YPos - taxiY), 2));

        if (distance < TAXI_RADIUS + radius) {
            return true;
        }

        return false;
    }

    // getters ans setters
    public double getTaxiX() {
        return taxiX;
    }

    public double getTaxiY() {
        return taxiY;
    }

    public boolean getTaxiOccupied() {
        return taxiOccupied;
    }

    public void setTaxiOccupied(boolean taxiOccupied) {
        this.taxiOccupied = taxiOccupied;
    }

    public void setTaxiHealth(double damageTaken) {
        this.taxiHealth -= damageTaken;
    }

    public double getTaxiHealth() {
        return taxiHealth;
    }

    public double getTAXI_DAMAGE() {
        return TAXI_DAMAGE;
    }

    public int getCollisionTimeout() {
        return collisionTimeout;
    }

    public void setCollisionTimeout(int collisionTimeout) {
        this.collisionTimeout = collisionTimeout;
    }

    public double getTAXI_RADIUS() {
        return TAXI_RADIUS;
    }

    public void setLastCollidedCar(Car lastCollidedCar) {
        this.lastCollidedCar = lastCollidedCar;
    }

    public Car getLastCollidedCar() {
        return lastCollidedCar;
    }

    public void setTaxiY(double taxiY) {
        this.taxiY = taxiY;
    }

    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public boolean isHitFireBall() {
        return hitFireBall;
    }

    public void setHitFireBall(boolean hitFireBall) {
        this.hitFireBall = hitFireBall;
    }
}
