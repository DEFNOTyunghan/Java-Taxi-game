import bagel.*;
import bagel.Font;
import bagel.Image;
import bagel.Window;

import java.util.Properties;

/**
 * Represents the player information screen in the game.
 * This screen allows the player to enter their name and provides instructions on how to start the game.
 * It initializes properties for the background image, title, font, and input handling for the player's name.
 */
public class PlayerInformationScreen {
    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    private final Image BACKGROUND_IMAGE;
    private final Font FONT;
    private final int FONT_SIZE;

    private final String TITLE;
    private final double TITLE_X;
    private final double TITLE_Y;

    private final String INSTRUCTION;
    private final double INSTRUCTION_X;
    private final double INSTRUCTION_Y;

    private String playerName = "";

    /**
     * Constructs a PlayerInformationScreen with specified game and message properties.
     *
     * @param gameProps the properties for game configuration, including background image and font size
     * @param messageProps the properties for messages, including player name and instructions
     */
    public PlayerInformationScreen(Properties gameProps, Properties messageProps) {

        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;

        // set properties for screen title
        BACKGROUND_IMAGE = new Image(GAME_PROPS.getProperty("backgroundImage.playerInfo"));
        TITLE = MESSAGE_PROPS.getProperty("playerInfo.playerName");
        FONT_SIZE = Integer.parseInt(GAME_PROPS.getProperty("playerInfo.fontSize"));
        FONT = new Font("res/FSO8BITR.TTF", FONT_SIZE);
        TITLE_X = (Window.getWidth() - FONT.getWidth(TITLE)) / 2;
        TITLE_Y = Double.parseDouble(GAME_PROPS.getProperty("playerInfo.playerName.y"));

        // set properties for instructions
        INSTRUCTION = messageProps.getProperty("playerInfo.start");
        INSTRUCTION_X = (Window.getWidth() - FONT.getWidth(INSTRUCTION)) / 2;
        INSTRUCTION_Y = Double.parseDouble(GAME_PROPS.getProperty("playerInfo.start.y"));
    }

    /**
     * Renders the player information screen, including the background image, title, instructions, and player name input.
     * This method draws the background at the center of the window, displays the title and instructions,
     * and draws the player's name input in black. The positions of these elements are calculated based
     * on the window dimensions and properties set during initialization.
     */
    public void render() {
        // draw background, title and instructions
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        FONT.drawString(TITLE, TITLE_X, TITLE_Y);
        FONT.drawString(INSTRUCTION, INSTRUCTION_X, INSTRUCTION_Y);

        //draw player name inputs in black
        double nameX = (Window.getWidth() - FONT.getWidth(playerName)) / 2;
        double nameY = Double.parseDouble(GAME_PROPS.getProperty("playerInfo.playerNameInput.y"));
        DrawOptions drawOptions = new DrawOptions().setBlendColour(0.0, 0.0, 0.0);
        FONT.drawString(playerName, nameX, nameY, drawOptions);
    }

    /**
     * Handles input from the player for entering their name.
     * This method checks for key presses to modify the player's name. If the BACKSPACE or DELETE key
     * is pressed, the last character of the player's name is removed. If any other key is pressed,
     * that character is appended to the player's name.
     *
     * @param input the input object containing the current key states
     */
    public void handleInput(Input input) {
        String key = MiscUtils.getKeyPress(input);

        if (input.wasPressed(Keys.BACKSPACE) || input.wasPressed(Keys.DELETE)) {
            if (!playerName.isEmpty()) {
                playerName = playerName.substring(0, playerName.length() - 1);
            }
        } else if (key != null) {
            playerName += key;
        }
    }

    // getters
    public String getPlayerName() {
        return playerName;
    }

}
