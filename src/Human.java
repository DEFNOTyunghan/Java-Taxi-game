/**
 * Represents a human entity in the game, such as a driver or passenger.
 * The Human class provides properties and methods related to human movement,
 * health management, and collision detection with other objects in the game.
 */
public abstract class Human implements Collidable{
    private boolean inTaxi = false;
    private int TAXI_GET_IN_RADIUS;
    private int HUMAN_RADIUS;
    private double XPos;
    private double YPos;
    private double humanHealth;
    private int collisionTimeout = 0;
    private boolean moveForward;
    private final int SEPARATION_DURATION = 190;
    private final int SEPARATION_DISTANCE = 2;

    /**
     * Abstract method for a driver to enter a taxi at a specified position.
     *
     * @param taxiX the X coordinate of the taxi
     * @param taxiY the Y coordinate of the taxi
     */
    public abstract void enterTaxi(double taxiX, double taxiY);

    /**
     * Checks if the current object has collided with another object based on their positions and radius.
     *
     * @param XPos the X coordinate of the other object
     * @param YPos the Y coordinate of the other object
     * @param radius the radius of the other object
     * @return true if a collision occurs, false otherwise
     */
    @Override
    public boolean hasCollided(double XPos, double YPos, double radius) {
        double distance = Math.sqrt(Math.pow((this.XPos - XPos), 2) + Math.pow((this.YPos - YPos), 2));

        if (distance < HUMAN_RADIUS + radius) {
            return true;
        }

        return false;
    }

    // getters and setters
    public void setInTaxi(boolean inTaxi) {
        this.inTaxi = inTaxi;
    }

    public void setXPos(double XPos) {
        this.XPos = XPos;
    }

    public void setMoveForward(boolean moveForward) {
        this.moveForward = moveForward;
    }

    public void setCollisionTimeout(int collisionTimeout) {
        this.collisionTimeout = collisionTimeout;
    }

    public void setHUMAN_RADIUS(int HUMAN_RADIUS) {
        this.HUMAN_RADIUS = HUMAN_RADIUS;
    }

    public void setTAXI_GET_IN_RADIUS(int TAXI_GET_IN_RADIUS) {
        this.TAXI_GET_IN_RADIUS = TAXI_GET_IN_RADIUS;
    }

    public int getTAXI_GET_IN_RADIUS() {
        return TAXI_GET_IN_RADIUS;
    }

    public void setYPos(double YPos) {
        this.YPos = YPos;
    }

    public double getXPos() {
        return XPos;
    }

    public double getYPos() {
        return YPos;
    }

    public void setHumanHealth(double humanHealth) {
        this.humanHealth = humanHealth;
    }

    public double getHumanHealth() {
        return humanHealth;
    }

    public int getCollisionTimeout() {
        return collisionTimeout;
    }

    public boolean isMoveForward() {
        return moveForward;
    }

    public boolean isInTaxi() {
        return inTaxi;
    }

    public int getSEPARATION_DURATION() {
        return SEPARATION_DURATION;
    }

    public int getSEPARATION_DISTANCE() {
        return SEPARATION_DISTANCE;
    }
}
