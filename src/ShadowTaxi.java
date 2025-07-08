import bagel.*;
import java.util.Properties;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2024
 * YUNG HAN WONG
 */
public class ShadowTaxi extends AbstractGame {

    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    private final HomeScreen homeScreen;
    private PlayerInformationScreen playerInformationScreen;
    private GamePlayScreen gamePlayScreen;
    private GameEndScreen gameEndScreen;
    private final int HOME_SCREEN = 1;
    private final int PLAYER_INFORMATION_SCREEN = 2;
    private final int GAME_PLAY_SCREEN = 3;
    private final int GAME_END_SCREEN = 4;
    private int gameState = 1;

    public ShadowTaxi(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                messageProps.getProperty("home.title"));

        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;

        //initialize screen variables
        homeScreen = new HomeScreen(GAME_PROPS, MESSAGE_PROPS);
        playerInformationScreen = new PlayerInformationScreen(GAME_PROPS, MESSAGE_PROPS);
        gamePlayScreen = new GamePlayScreen(GAME_PROPS, MESSAGE_PROPS);
        gameEndScreen = new GameEndScreen(GAME_PROPS, MESSAGE_PROPS);
    }

    /**
     * Render the relevant screens and game objects based on the keyboard input
     * given by the user and the status of the game play.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        if (gameState == HOME_SCREEN) {
            homeScreen.render();

            if (input.wasPressed(Keys.ENTER)) {
                gameState = PLAYER_INFORMATION_SCREEN;
            }
        } else if (gameState == PLAYER_INFORMATION_SCREEN) {
            playerInformationScreen.render();
            playerInformationScreen.handleInput(input);

            if (input.wasPressed(Keys.ENTER)) {
                gameState = GAME_PLAY_SCREEN;
            }
        } else if (gameState == GAME_PLAY_SCREEN) {
            gamePlayScreen.render(input);
            gamePlayScreen.handleInput(input);

            if (gamePlayScreen.isGameWon() || gamePlayScreen.isGameLost()) {
                gameState = 4;
            }
        } else if (gameState == GAME_END_SCREEN) {
            gameEndScreen.setPlayerName(playerInformationScreen.getPlayerName());
            gameEndScreen.setFinalScore(gamePlayScreen.getTotalPay());
            gameEndScreen.setWonGame(gamePlayScreen.isGameWon());
            gameEndScreen.render();

            if (input.wasPressed(Keys.SPACE)) {
                //restart the game
                playerInformationScreen = new PlayerInformationScreen(GAME_PROPS, MESSAGE_PROPS);
                gamePlayScreen = new GamePlayScreen(GAME_PROPS, MESSAGE_PROPS);
                gameEndScreen = new GameEndScreen(GAME_PROPS, MESSAGE_PROPS);

                gameState = HOME_SCREEN;
            }
        }
    }

    public static void main(String[] args) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");
        ShadowTaxi game = new ShadowTaxi(game_props, message_props);
        game.run();
    }
}
