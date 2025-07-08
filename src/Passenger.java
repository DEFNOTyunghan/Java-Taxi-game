import bagel.*;
import java.util.Properties;

/**
 * Represents a passenger in the game who can interact with taxis and navigate
 * to a specified drop-off location. The passenger's behavior is influenced
 * by priority, proximity to the taxi, and trip status.
 */
public class Passenger extends Human{
    private final Properties GAME_PROPS;

    private final Image PASSENGER_IMAGE;
    private int priority;
    private int passengerEndX;
    private int distanceY;
    private final int PASSENGER_SPEED;
    private final int FONT_SIZE;

    private final int SCREEN_SPEED;
    private final int DETECT_RADIUS;
    private TripEndFlag tripEndFlag;
    private final int FLAG_RADIUS;
    private boolean flagPositionChanged = false;

    private boolean tripOngoing;
    private boolean driven = false;
    private boolean poweredUp = false;
    private final int HAS_UMBRELLA;
    private double exp;
    private final int EXP_X = 100;

    private final double RATE_PER_Y;
    private final int PRIORITY1_RATE;
    private final int PRIORITY2_RATE;
    private final int PRIORITY3_RATE;
    private final int PRIORITY_X = 30;

    /**
     * Constructs a Passenger object with specified properties.
     *
     * @param gameProps the game properties containing image paths and settings
     * @param passengerX the initial X position of the passenger
     * @param passengerY the initial Y position of the passenger
     * @param priority the priority level of the passenger
     * @param passengerEndX the X position where the passenger should be dropped off
     * @param distanceY the vertical distance from the passenger's position to the drop-off point
     * @param hasUmbrella indicates if the passenger has an umbrella (1 for yes, 0 for no)
     */
    public Passenger(Properties gameProps, double passengerX, double passengerY, String priority, int passengerEndX,
                     int distanceY, int hasUmbrella) {
        this.GAME_PROPS = gameProps;

        setTAXI_GET_IN_RADIUS(Integer.parseInt(GAME_PROPS.getProperty("gameObjects.driver.taxiGetInRadius")));

        // set passenger properties
        PASSENGER_IMAGE = new Image(GAME_PROPS.getProperty("gameObjects.passenger.image"));
        setXPos(passengerX);
        setYPos(passengerY);
        this.priority = Integer.parseInt(priority);
        this.passengerEndX = passengerEndX;
        this.distanceY = distanceY;
        this.HAS_UMBRELLA = hasUmbrella;
        DETECT_RADIUS = Integer.parseInt(GAME_PROPS.getProperty("gameObjects.passenger.taxiDetectRadius"));
        PASSENGER_SPEED = Integer.parseInt(GAME_PROPS.getProperty("gameObjects.passenger.walkSpeedX"));
        FONT_SIZE = Integer.parseInt(GAME_PROPS.getProperty("gameObjects.passenger.fontSize"));
        SCREEN_SPEED = Integer.parseInt(GAME_PROPS.getProperty("gameObjects.taxi.speedY"));
        tripEndFlag = new TripEndFlag(GAME_PROPS, passengerEndX, passengerY - distanceY);
        FLAG_RADIUS = Integer.parseInt(GAME_PROPS.getProperty("gameObjects.tripEndFlag.radius"));
        setHumanHealth(Double.parseDouble(GAME_PROPS.getProperty("gameObjects.driver.health")));
        setHUMAN_RADIUS(Integer.parseInt(GAME_PROPS.getProperty("gameObjects.passenger.radius")));

        // set calculation properties
        RATE_PER_Y = Double.parseDouble(GAME_PROPS.getProperty("trip.rate.perY"));
        PRIORITY1_RATE = Integer.parseInt(GAME_PROPS.getProperty("trip.rate.priority1"));
        PRIORITY2_RATE = Integer.parseInt(GAME_PROPS.getProperty("trip.rate.priority2"));
        PRIORITY3_RATE = Integer.parseInt(GAME_PROPS.getProperty("trip.rate.priority3"));
    }

    /**
     * Renders the passenger on the screen and updates their position based on user input and taxi state.
     *
     * @param input the user input to determine movement direction
     * @param taxiX the X position of the taxi
     * @param driverInTaxi indicates if the driver is currently in the taxi
     */
    public void render(Input input, double taxiX, boolean driverInTaxi) {
        if (!isInTaxi()) {
            PASSENGER_IMAGE.draw(getXPos(), getYPos());

            if (!tripOngoing && !driven) {
                Font font = new Font("res/FSO8BITR.TTF", FONT_SIZE);
                font.drawString(String.valueOf(priority), getXPos() - PRIORITY_X, getYPos());
                calculateExpected(priority, distanceY);
                font.drawString(String.valueOf(exp), getXPos() - EXP_X, getYPos());
            }

            if (tripOngoing && !driven) {
                if (input.isDown(Keys.LEFT)) {
                    setXPos(getXPos() - PASSENGER_SPEED);
                } else if (input.isDown(Keys.RIGHT)) {
                    setXPos(getXPos() + PASSENGER_SPEED);
                }
            } else {
                if (input.isDown(Keys.UP) && driverInTaxi) {
                    setYPos(getYPos() + SCREEN_SPEED);
                } else if (input.isDown(Keys.UP) && !driverInTaxi) {
                    setYPos(getYPos() + PASSENGER_SPEED);
                } else if (input.isDown(Keys.DOWN) && !driverInTaxi) {
                    setYPos(getYPos() - PASSENGER_SPEED);
                }
            }
        }

        // handle separation logic
        if (getCollisionTimeout() > getSEPARATION_DURATION()) {
            if (isMoveForward()) {
                setYPos(getYPos() - getSEPARATION_DISTANCE());
            } else {
                setYPos(getYPos() + getSEPARATION_DISTANCE());
            }
        }

        // render trip end flag
        if (tripOngoing && isInTaxi()) {
            tripEndFlag.render(input, driverInTaxi);
            // passenger follows taxi when on board
            setXPos(taxiX);
        } else if (tripOngoing) {
            tripEndFlag.render(input, driverInTaxi);
        }

    }

    /**
     * Checks if the taxi is near the passenger within the detection radius.
     *
     * @param taxiX the X position of the taxi
     * @param taxiY the Y position of the taxi
     * @return true if the taxi is within the detection radius, false otherwise
     */
    public boolean nearTaxi(double taxiX, double taxiY) {
        double distance = Math.sqrt(Math.pow(taxiX - getXPos(), 2) + Math.pow(taxiY - getYPos(), 2));
        return distance <= DETECT_RADIUS;
    }

    /**
     * Moves the passenger towards the taxi using the shortest route.
     *
     * @param taxiX the X position of the taxi
     * @param taxiY the Y position of the taxi
     */
    @Override
    public void enterTaxi(double taxiX, double taxiY) {

        double distance = Math.sqrt(Math.pow((getXPos() - taxiX), 2) + Math.pow((getYPos() - taxiY), 2));

        if (distance > getTAXI_GET_IN_RADIUS()) {
            // Calculate the direction
            double directionX = (taxiX - getXPos()) / distance;
            double directionY = (taxiY - getYPos()) / distance;

            // Move the passenger towards the taxi
            setXPos(getXPos() + (directionX * PASSENGER_SPEED));
            setYPos(getYPos() + (directionY * PASSENGER_SPEED));
        } else {
            setInTaxi(true);
            tripOngoing = true;

            if (!flagPositionChanged) {
                tripEndFlag.setFlagY(getYPos() - distanceY);
                flagPositionChanged = true;
            }
        }
    }

    /**
     * Checks the status of the trip and drops the passenger off if the trip end flag is reached.
     *
     * @param taxiX the X position of the taxi
     * @param taxiY the Y position of the taxi
     */
    public void checkTripPosition(double taxiX, double taxiY) {

        double taxiToFlagDistance = Math.sqrt(Math.pow((tripEndFlag.getFlagX() - taxiX), 2) +
                Math.pow((tripEndFlag.getFlagY() - taxiY), 2));

        // when flag is near drop passenger off
        if ((taxiY <= tripEndFlag.getFlagY() || taxiToFlagDistance <= FLAG_RADIUS) && (tripOngoing)) {
            setInTaxi(false);
            driven = true;

            // make passenger go to flag
            double passengerToFlagDistance = Math.sqrt(Math.pow((tripEndFlag.getFlagX() - getXPos()), 2) +
                    Math.pow((tripEndFlag.getFlagY() - getYPos()), 2));

            double directionX = (tripEndFlag.getFlagX() - getXPos()) / passengerToFlagDistance;
            double directionY = (tripEndFlag.getFlagY() - getYPos()) / passengerToFlagDistance;

            setXPos(getXPos() + (directionX * PASSENGER_SPEED));
            setYPos(getYPos() + (directionY * PASSENGER_SPEED));

            if (passengerToFlagDistance < 1) {
                tripOngoing = false;
            }
        }

    }

    /**
     * Calculates the priority of the passenger based on power-ups.
     *
     * @param priority the current priority of the passenger
     * @return the adjusted priority
     */
    public int calculatePriority(int priority) {
        if (poweredUp && priority > 1) {
            priority -= 1;
        }
        return priority;
    }

    /**
     * Calculates the expected earnings for the trip based on priority and distance traveled.
     *
     * @param priority the priority level of the passenger
     * @param distanceTravelled the distance traveled during the trip
     * @return the expected earnings
     */
    public double calculateExpected(int priority, int distanceTravelled) {
        int rate = 0;
        if (priority == 1) {
            rate = PRIORITY1_RATE;
        } else if (priority == 2) {
            rate = PRIORITY2_RATE;
        } else if (priority == 3) {
            rate = PRIORITY3_RATE;
        }
        exp = (distanceTravelled * RATE_PER_Y) + (priority * rate);

        return exp;
    }

    //getters and setters
    public void setPoweredUp(boolean poweredUp) {
        this.poweredUp = poweredUp;

        if (poweredUp) {
            this.priority = calculatePriority(this.priority);
        }
    }

    public boolean isPoweredUp() {
        return poweredUp;
    }

    public boolean isTripOngoing() {
        return tripOngoing;
    }

    public boolean isDriven() {
        return driven;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public int getDistanceY() {
        return distanceY;
    }

    public double getFlagY() {
        return tripEndFlag.getFlagY();
    }

    public double getFlagX() {
        return tripEndFlag.getFlagX();
    }

    public int getHAS_UMBRELLA() {
        return HAS_UMBRELLA;
    }
}
