/**
 * Represents a power-up object in the game that can collide with a taxi.
 * The power-up's position is defined by its X and Y coordinates, and it has
 * a radius that determines its collision area with the taxi.
 */
public class PowerUp {
    private double XPos;
    private double YPos;

    private String TAXI_RADIUS;
    private int SCREEN_SPEED;
    private String POWER_UP_RADIUS;
    private boolean hasCollided = false;

    /**
     * Checks for a collision between the coin and the taxi.
     *
     * This method calculates the distance between the coin and the taxi and determines
     * if they have collided based on their combined radii. If a collision is detected,
     * it sets the collision state to true.
     *
     * @param taxiX the X-coordinate of the taxi
     * @param taxiY the Y-coordinate of the taxi
     */
    public void checkCollision(double taxiX, double taxiY) {
        if (!hasCollided) {
            double radius = Double.parseDouble(TAXI_RADIUS) + Double.parseDouble(POWER_UP_RADIUS);

            double coinToTaxiDistance = Math.sqrt(Math.pow((XPos - taxiX), 2) +
                    Math.pow((YPos - taxiY), 2));

            if (coinToTaxiDistance <= radius) {
                hasCollided = true;
            }
        }
    }

    // getters and setters
    public double getXPos() {
        return XPos;
    }

    public double getYPos() {
        return YPos;
    }

    public void setXPos(double XPos) {
        this.XPos = XPos;
    }

    public void setYPos(double YPos) {
        this.YPos = YPos;
    }

    public int getSCREEN_SPEED() {
        return SCREEN_SPEED;
    }

    public void setTAXI_RADIUS(String TAXI_RADIUS) {
        this.TAXI_RADIUS = TAXI_RADIUS;
    }

    public void setSCREEN_SPEED(int SCREEN_SPEED) {
        this.SCREEN_SPEED = SCREEN_SPEED;
    }

    public void setPOWER_UP_RADIUS(String POWER_UP_RADIUS) {
        this.POWER_UP_RADIUS = POWER_UP_RADIUS;
    }

    public boolean isHasCollided() {
        return hasCollided;
    }

}
