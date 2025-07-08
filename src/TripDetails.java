import bagel.*;
import java.util.Properties;

/**
 * Represents the details of a taxi trip, including statistics for both ongoing
 * and completed trips.
 */
public class TripDetails {
    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;

    private final String CURRENT_TITLE;
    private final String EXP_TEXT;
    private double exp;
    private final String PRIORITY_TEXT;
    private Passenger passenger;
    private int priority;
    private int distanceTravelled;
    private final Font font;

    private final String LAST_TITLE;
    private final String PENALTY_TEXT;
    private final int FONT_SIZE;
    private final double PENALTY;
    private final double PENALTY_RATE;
    private double finalExp;

    private final int TEXT_X;
    private final int TEXT_Y;
    private final int FLAG_RADIUS;
    private boolean earningsCalculated = false;

    /**
     * Constructs a TripDetails object with the specified properties and trip information.
     *
     * @param messageProps properties containing text messages for the game
     * @param gameProps properties containing configuration values for the game
     * @param passenger the Passenger object associated with the trip
     * @param taxiX the X position of the taxi
     * @param taxiY the Y position of the taxi
     * @param flagX the X position of the trip end flag
     * @param flagY the Y position of the trip end flag
     */
    public TripDetails(Properties messageProps, Properties gameProps, Passenger passenger, double taxiX,
                       double taxiY, double flagX, double flagY) {
        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;

        // set current statistic details
        this.passenger = passenger;
        this.priority = passenger.getPriority();
        this.distanceTravelled = passenger.getDistanceY();
        CURRENT_TITLE = MESSAGE_PROPS.getProperty("gamePlay.onGoingTrip.title");
        EXP_TEXT = MESSAGE_PROPS.getProperty("gamePlay.trip.expectedEarning");
        exp = passenger.calculateExpected(priority, distanceTravelled);
        PRIORITY_TEXT = MESSAGE_PROPS.getProperty("gamePlay.trip.priority");
        FONT_SIZE = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.info.fontSize"));
        font = new Font("res/FSO8BITR.TTF", FONT_SIZE);
        FLAG_RADIUS = Integer.parseInt(GAME_PROPS.getProperty("gameObjects.tripEndFlag.radius"));

        // set last statistics data
        LAST_TITLE = MESSAGE_PROPS.getProperty("gamePlay.completedTrip.title");
        PENALTY_TEXT = MESSAGE_PROPS.getProperty("gamePlay.trip.penalty");
        PENALTY_RATE = Double.parseDouble(GAME_PROPS.getProperty("trip.penalty.perY"));
        PENALTY = calculatePenalty(taxiX, taxiY, flagX, flagY);

        TEXT_X = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.tripInfo.x"));
        TEXT_Y = Integer.parseInt(GAME_PROPS.getProperty("gamePlay.tripInfo.y"));
    }

    /**
     * Renders the statistics of the current trip on the screen.
     */
    public void renderCurrent() {
        font.drawString(CURRENT_TITLE, TEXT_X, TEXT_Y);
        font.drawString(EXP_TEXT + exp, TEXT_X, TEXT_Y + 30);
        font.drawString( PRIORITY_TEXT + priority, TEXT_X, TEXT_Y + 60);
    }

    /**
     * Renders the statistics of the last completed trip on the screen.
     */
    public void renderLast() {
        finalExp = calculateFinalExp(exp, PENALTY);

        font.drawString(LAST_TITLE, TEXT_X, TEXT_Y);
        font.drawString(EXP_TEXT + String.format("%.1f", finalExp), TEXT_X, TEXT_Y + 30);
        font.drawString( PRIORITY_TEXT + priority, TEXT_X, TEXT_Y + 60);
        font.drawString(PENALTY_TEXT + String.format("%.2f", PENALTY), TEXT_X, TEXT_Y + 90);
    }

    // calculate final earnings
    private double calculateFinalExp(double exp, double penalty) {
        double temp = exp - penalty;
        if (temp < 0) {
            return 0;
        }
        return temp;
    }

    // calculate penalty gained from in trip
    private double calculatePenalty(double taxiX, double taxiY, double flagX, double flagY) {
        double distance = Math.sqrt(Math.pow((taxiX - flagX), 2) - Math.pow((taxiY - flagY), 2));

        if (distance <= FLAG_RADIUS) {
            return 0;
        }

        return PENALTY_RATE * (Math.abs(taxiY - flagY));
    }

    //getters and setters
    public double getFinalExp() {
        return finalExp;
    }

    public boolean isEarningsCalculated() {
        return earningsCalculated;
    }

    public void setEarningsCalculated(boolean earningsCalculated) {
        this.earningsCalculated = earningsCalculated;
    }

}