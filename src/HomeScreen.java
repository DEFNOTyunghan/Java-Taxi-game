import bagel.*;
import java.util.Properties;

/**
 * Represents the home screen of the game.
 * This screen displays the title of the game and instructions for the player.
 * It initializes properties for the background image, title, and instructions
 * based on provided game and message properties.
 */
public class HomeScreen {
    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    private final Image BACKGROUND_IMAGE;

    private final String title;
    private final Font TITLE_FONT;
    private final double TITLE_X;
    private final double TITLE_Y;
    private final int TITLE_FONT_SIZE;

    private final String INSTRUCTIONS;
    private final Font INSTRUCTION_FONT;
    private final double INSTRUCTION_X;
    private final int INSTRUCTION_FONT_SIZE;
    private final double INSTRUCTION_Y;

    /**
     * Constructs a HomeScreen with specified game and message properties.
     *
     * @param gameProps the properties for game configuration, including background image and font sizes
     * @param messageProps the properties for messages, including the title and instructions to display
     */
    public HomeScreen(Properties gameProps, Properties messageProps) {

        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;

        // set properties for screen title
        BACKGROUND_IMAGE = new Image(gameProps.getProperty("backgroundImage.home"));
        TITLE_FONT_SIZE = Integer.parseInt(GAME_PROPS.getProperty("home.title.fontSize"));
        title = MESSAGE_PROPS.getProperty("home.title");
        TITLE_FONT = new Font("res/FSO8BITR.TTF", TITLE_FONT_SIZE);
        TITLE_X = (Window.getWidth() - TITLE_FONT.getWidth(title)) / 2;
        TITLE_Y = Double.parseDouble(GAME_PROPS.getProperty("home.title.y"));

        // set properties for screen instructions
        INSTRUCTIONS = MESSAGE_PROPS.getProperty("home.instruction");
        INSTRUCTION_FONT_SIZE = Integer.parseInt(GAME_PROPS.getProperty("home.instruction.fontSize"));
        INSTRUCTION_FONT = new Font("res/FSO8BITR.TTF", INSTRUCTION_FONT_SIZE);
        INSTRUCTION_X = (Window.getWidth() - INSTRUCTION_FONT.getWidth(INSTRUCTIONS)) / 2;
        INSTRUCTION_Y = Double.parseDouble(GAME_PROPS.getProperty("home.instruction.y"));
    }

    /**
     * Renders the home screen, including the background image, title, and instructions.
     * This method draws the background at the center of the window and displays the title
     * and instructions centered on the screen. The positions of these elements are calculated
     * based on the window dimensions and properties set during initialization.
     */
    public void render() {
        // draw home screen
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        TITLE_FONT.drawString(title, TITLE_X, TITLE_Y);
        INSTRUCTION_FONT.drawString(INSTRUCTIONS, INSTRUCTION_X, INSTRUCTION_Y);
    }
}
