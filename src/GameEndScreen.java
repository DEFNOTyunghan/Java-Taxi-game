import bagel.*;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Represents the game end screen that displays the final scores and game status.
 * This screen shows whether the player won or lost the game and displays the highest scores
 * recorded in the game.
 */
public class GameEndScreen {
    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;

    private final Image BACKGROUND_IMAGE;
    private final String SCORES_FILE;

    private final String TITLE;
    private final int SCORES_FONT_SIZE;
    private final Font SCORES_FONT;
    private final int STATUS_FONT_SIZE;
    private final Font STATUS_FONT;
    private final double TITLE_X;
    private final int TITLE_Y;

    private final double WIN_X;
    private final double LOSE_X;
    private final String WIN_TEXT;
    private final String LOSE_TEXT;
    private final int STATUS_Y;
    private boolean wonGame;

    private String playerName;
    private double finalScore;
    private boolean scoreWritten = false;
    private ArrayList<String[]> scoreBoard;

    /**
     * Constructs a GameEndScreen with specified game and message properties.
     * Initializes the properties needed to display the end game screen, including
     * background image, score file, title, fonts, and win/lose messages.
     *
     * @param gameProps the properties for game configuration, including background image and font sizes
     * @param messageProps the properties for messages, including titles and win/lose text
     */
    public GameEndScreen(Properties gameProps, Properties messageProps) {
        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;

        // set properties required for game end screen
        BACKGROUND_IMAGE = new Image(GAME_PROPS.getProperty("backgroundImage.gameEnd"));
        SCORES_FILE = GAME_PROPS.getProperty("gameEnd.scoresFile");
        TITLE = MESSAGE_PROPS.getProperty("gameEnd.highestScores");
        SCORES_FONT_SIZE = Integer.parseInt(GAME_PROPS.getProperty("gameEnd.scores.fontSize"));
        SCORES_FONT = new Font("res/FSO8BITR.TTF", SCORES_FONT_SIZE);
        STATUS_FONT_SIZE = Integer.parseInt(GAME_PROPS.getProperty("gameEnd.status.fontSize"));
        STATUS_FONT = new Font("res/FSO8BITR.TTF", STATUS_FONT_SIZE);
        TITLE_X = (Window.getWidth() - SCORES_FONT.getWidth(TITLE)) / 2;
        TITLE_Y = Integer.parseInt(GAME_PROPS.getProperty("gameEnd.scores.y"));

        WIN_TEXT = MESSAGE_PROPS.getProperty("gameEnd.won");
        LOSE_TEXT = MESSAGE_PROPS.getProperty("gameEnd.lost");
        WIN_X = (Window.getWidth() - SCORES_FONT.getWidth(WIN_TEXT)) / 2;
        LOSE_X = (Window.getWidth() - SCORES_FONT.getWidth(LOSE_TEXT)) / 2;
        STATUS_Y = Integer.parseInt(GAME_PROPS.getProperty("gameEnd.status.y"));
    }

    /**
     * Renders the game end screen, displaying the final scores and the game status (win or lose).
     * This method checks if the score needs to be written to the file, draws the background image,
     * displays the title and the top five scores from the scoreboard, and indicates whether the player won or lost the game.
     */
    public void render() {
        if (!scoreWritten) {
            writeToFile(playerName, finalScore);
        }

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        SCORES_FONT.drawString(TITLE, TITLE_X, TITLE_Y);

        // generate the first five scores from the sorted scoreBoard array list
        generateScoreBoard();
        for (int i = 0; (i < scoreBoard.size()) && (i < 5); i++) {
            String scoreLine = scoreBoard.get(i)[0];

            double scoreX = (Window.getWidth() - SCORES_FONT.getWidth(scoreLine)) / 2;
            SCORES_FONT.drawString(scoreLine, scoreX, 240 + (40 * i));
        }

        if (wonGame) {
            STATUS_FONT.drawString(WIN_TEXT, WIN_X, STATUS_Y);
        } else {
            STATUS_FONT.drawString(LOSE_TEXT, LOSE_X, STATUS_Y);
        }

    }

    // write game statistics into scores file
    private void writeToFile(String playerName, double finalScore) {
        String score = playerName + " - " + String.format("%.2f", finalScore);

        IOUtils.writeScoreToFile(SCORES_FILE, score);
        scoreWritten = true;
    }

    // generate a scoreBoard array list with scores ordered in descending order
    private void generateScoreBoard() {
        scoreBoard = new ArrayList<>();

        String[][] score = IOUtils.readCommaSeparatedFile(SCORES_FILE);

        for (String[] row : score) {
            scoreBoard.add(row);
        }

        // sort the scoreBoard array list based on scores
        scoreBoard.sort((score1, score2) -> {
            // Extract and parse the scores from the string arrays
            String scoreString1 = score1[0];
            String scoreString2 = score2[0];

            // extract scores to be compared
            double scoreValue1 = Double.parseDouble(scoreString1.split(" - ")[1]);
            double scoreValue2 = Double.parseDouble(scoreString2.split(" - ")[1]);

            // sort in descending order
            return Double.compare(scoreValue2, scoreValue1);
        });
    }

    // setters
    public void setWonGame(boolean wonGame) {
        this.wonGame = wonGame;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }

}
