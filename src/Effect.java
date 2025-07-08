import bagel.Input;

/**
 * Represents an abstract effect that can be rendered on the screen.
 * This class serves as a base for different types of visual effects in the game.
 */
public abstract class Effect {
    private double XPos;
    private double YPos;
    private int frames;
    private int SCREEN_SPEED;

    /**
     * Renders the effect on the screen based on the current input and whether the driver is in the taxi.
     * Subclasses must provide an implementation for this method to define how the effect is visually
     * represented during the game.
     *
     * @param input the current input from the user, allowing for interaction with the effect
     * @param driverInTaxi a boolean indicating if the driver is currently in the taxi, affecting the effect's behavior
     */
    public abstract void render(Input input, boolean driverInTaxi);

    //getters and setters
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

    public int getFrames() {
        return frames;
    }

    public int getSCREEN_SPEED() {
        return SCREEN_SPEED;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public void setSCREEN_SPEED(int SCREEN_SPEED) {
        this.SCREEN_SPEED = SCREEN_SPEED;
    }
}
