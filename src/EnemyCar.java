import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Represents an enemy car in the game that can move on the screen and shoot fireballs.
 * The EnemyCar class handles its attributes, movement, rendering, and fireball generation.
 */
public class EnemyCar extends Car{
    private final Properties GAME_PROPS;
    private final Image ENEMY_CAR_IMAGE;

    private ArrayList<FireBall> fireBalls;
    private final int FIRE_BALL_SPAWN_RATE = 300;

    /**
     * Constructs an EnemyCar object with the given game properties.
     * Initializes the car's attributes such as speed, radius, damage, health, and lane positions.
     * Also, initializes the fireballs list.
     *
     * @param gameProps The properties object that holds the game configuration values.
     */
    public EnemyCar(Properties gameProps) {
        this.GAME_PROPS = gameProps;
        ENEMY_CAR_IMAGE = new Image(GAME_PROPS.getProperty("gameObjects.enemyCar.image"));

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

        this.fireBalls = new ArrayList<>();
    }

    /**
     * Renders the EnemyCar on the screen and manages its movement and fireball generation.
     * Also handles the collision timeout and adjusts the car's position.
     *
     * @param input The Input object to track user inputs.
     * @param driverInTaxi A boolean that indicates if the driver is still in the taxi.
     */
    public void render(Input input, boolean driverInTaxi) {
        generateFireBall(getXPos(), getYPos());

        for (FireBall fireball: fireBalls) {
            fireball.render(input, driverInTaxi);
        }
        removeFireBalls();

        ENEMY_CAR_IMAGE.draw(getXPos(), getYPos());

        if (getCollisionTimeout() > getSEPARATION_DURATION()) {
            //handle separation logic
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

    private void generateFireBall(double XPos, double YPos) {
        if (MiscUtils.canSpawn(FIRE_BALL_SPAWN_RATE)) {
            // Create a new OtherCar instance and add it to the list
            FireBall newFireBall = new FireBall(GAME_PROPS, XPos, YPos);
            fireBalls.add(newFireBall);
        }
    }

    private void removeFireBalls() {
        fireBalls.removeIf(fireBall -> fireBall.getYPos() < 0);
        fireBalls.removeIf(FireBall::HasCollided);
    }

    // getters and setters
    public ArrayList<FireBall> getFireBalls() {
        return fireBalls;
    }

    public void setFireBalls(ArrayList<FireBall> fireBalls) {
        this.fireBalls = fireBalls;
    }
}
