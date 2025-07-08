import bagel.Input;

import java.awt.*;
import java.util.Properties;

/**
 * Represents a car in the game that can collide with other objects.
 * The Car class implements the Collidable interface, allowing it to handle
 * collision detection with other cars or game objects.
 */
public class Car implements Collidable{
    private double XPos;
    private double YPos;
    private int SCREEN_SPEED;
    private int CAR_MAX_SPEED;
    private int CAR_MIN_SPEED;
    private int CAR_SPEED;
    private double CAR_RADIUS;
    private double CAR_DAMAGE;
    private double carHealth;
    private int collisionTimeout = 0;
    private final int SEPARATION_DURATION = 190;

    private int ROAD_LANE_1;
    private int ROAD_LANE_2;
    private int ROAD_LANE_3;

    private Car lastCollidedCar;
    private boolean moveForward;

    /**
     * Generates a random spawn location for the car within the defined road lanes.
     * The car's X position is randomly assigned to one of the predefined road lanes,
     * and its Y position is randomly selected from a range that starts off-screen
     */
    public void getSpawnLocation() {
        int[] lanes = {ROAD_LANE_1, ROAD_LANE_2, ROAD_LANE_3};
        XPos = lanes[MiscUtils.getRandomInt(0, lanes.length)];
        YPos = MiscUtils.selectAValue(-50, 768);
    }

    /**
     * Checks if this car has collided with another object based on their positions and radius.
     * @param XPos the X position of the other object
     * @param YPos the Y position of the other object
     * @param radius the radius of the other object
     * @return true if a collision is detected; false otherwise
     */
    @Override
    public boolean hasCollided(double XPos, double YPos, double radius) {
        double distance = Math.sqrt(Math.pow((this.XPos - XPos), 2) + Math.pow((this.YPos - YPos), 2));

        if (distance < CAR_RADIUS + radius) {
            return true;
        }

        return false;
    }

    // getters and setters
    public void setCarHealth(double carHealth) {
        this.carHealth = carHealth;
    }

    public void setCollisionTimeout(int collisionTimeout) {
        this.collisionTimeout = collisionTimeout;
    }

    public void setMoveForward(boolean moveForward) {
        this.moveForward = moveForward;
    }

    public void setXPos(double XPos) {
        this.XPos = XPos;
    }

    public void setYPos(double YPos) {
        this.YPos = YPos;
    }

    public void setSCREEN_SPEED(int SCREEN_SPEED) {
        this.SCREEN_SPEED = SCREEN_SPEED;
    }

    public void setCAR_MAX_SPEED(int CAR_MAX_SPEED) {
        this.CAR_MAX_SPEED = CAR_MAX_SPEED;
    }

    public void setCAR_MIN_SPEED(int CAR_MIN_SPEED) {
        this.CAR_MIN_SPEED = CAR_MIN_SPEED;
    }

    public void setCAR_SPEED(int CAR_SPEED) {
        this.CAR_SPEED = CAR_SPEED;
    }

    public void setCAR_RADIUS(double CAR_RADIUS) {
        this.CAR_RADIUS = CAR_RADIUS;
    }

    public void setCAR_DAMAGE(double CAR_DAMAGE) {
        this.CAR_DAMAGE = CAR_DAMAGE;
    }

    public void setROAD_LANE_1(int ROAD_LANE_1) {
        this.ROAD_LANE_1 = ROAD_LANE_1;
    }

    public void setROAD_LANE_2(int ROAD_LANE_2) {
        this.ROAD_LANE_2 = ROAD_LANE_2;
    }

    public void setROAD_LANE_3(int ROAD_LANE_3) {
        this.ROAD_LANE_3 = ROAD_LANE_3;
    }

    public double getXPos() {
        return XPos;
    }

    public double getYPos() {
        return YPos;
    }

    public int getSCREEN_SPEED() {
        return SCREEN_SPEED;
    }

    public int getCAR_MAX_SPEED() {
        return CAR_MAX_SPEED;
    }

    public int getCAR_MIN_SPEED() {
        return CAR_MIN_SPEED;
    }

    public int getCAR_SPEED() {
        return CAR_SPEED;
    }

    public double getCAR_RADIUS() {
        return CAR_RADIUS;
    }

    public double getCAR_DAMAGE() {
        return CAR_DAMAGE;
    }

    public double getCarHealth() {
        return carHealth;
    }

    public int getCollisionTimeout() {
        return collisionTimeout;
    }

    public boolean isMoveForward() {
        return moveForward;
    }

    public int getSEPARATION_DURATION() {
        return SEPARATION_DURATION;
    }
}
