package minesweeper.game;

import java.util.Observable;
import java.util.Observer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author Tyler Mulholland
 * Student Number: 000287743 
 * Date: June 5th 2018 
 * Description: Assignment 5
 *
 * I, Tyler Mulholland, student number 000287743, certify that all code submitted is my own work; that I have not copied it from any other source. I also certify that I have not allowed my work to be copied by others.
 *
 * Class description: This class contains all the code that deals with the GameBoard object. It creates the zone objects, the grid of zones, generates the mines, generates the flags, and handles all he updates from the zone objects on mouse click events based on the zone type.
 * 
 * It implements Observer because there is an update method that listens for changes in the Observable class Zone 
 *
 */
public class GameBoard implements Observer {

        /**
         * Initialize all instance variables. Ranges from details about mines, hi scores, labels, etc.
         * diffMineCount = Mine Count based on difficulty chosen.
         * subGrid = Helps with the display of the mine count and moves made. Essentially is the size of the grid made to adjust the centering of the previously mentioned labels.
         */
        private int chosenDifficulty, height, width, mineDecision, diffMineCount, numberOfMines, zoneCount, updatedMineCount, score, beginnerHiScore, interHiScore, expertHiScore, subGrid, labelSpacer, i, j;
        
        private Pane boardPane;
        
        private Zone[] zones;
        private String player, beginnerHiScorePlayer, interHiScorePlayer, expertHiScorePlayer;
        
        private Label displayMines, displayScore;
        private Label gameOver = new Label("Game Over!");
        private Label gameWon = new Label("You Win!");
        
        private Button mainMenuButton;
        
        private Font mainFont;
        
        private Image background = new Image("file:.\\.\\.\\.\\resources\\images\\Minesweeper Background.png");
        private ImageView backgroundIV = new ImageView(background);
        
        // Not sure if needed. Possibly delete.
        private boolean newClear = false;
        private int newCheck = 0;

        /**
         * Constructor method for GameBoard Class.
         * Sets certain variables based on difficulty then uses tat information to generate the board, mines, flags and various displays on the board. 
         * @param chosenDifficulty
         * @param boardPane
         * @param beginnerHiScore
         * @param interHiScore
         * @param expertHiScore
         * @param mainFont
         * @param player
         * @param mainMenuButton 
         */
        GameBoard(int chosenDifficulty, Pane boardPane, int beginnerHiScore, int interHiScore, int expertHiScore, Font mainFont, String player, Button mainMenuButton) {
                this.chosenDifficulty = chosenDifficulty;
                this.beginnerHiScore = beginnerHiScore;
                this.interHiScore = interHiScore;
                this.expertHiScore = expertHiScore;
                this.boardPane = boardPane;
                this.mainFont = mainFont;
                this.player = player;
                this.mainMenuButton = mainMenuButton;
                switch (chosenDifficulty) {
                        case 1:
                                this.height = 9;
                                this.width = 9;
                                diffMineCount = 10;
                                updatedMineCount = 10;
                                labelSpacer = 25;
                                break;
                        case 2:
                                this.height = 16;
                                this.width = 16;
                                diffMineCount = 40;
                                updatedMineCount = 40;
                                labelSpacer = 35;
                                break;
                        case 3:
                                this.height = 16;
                                this.width = 32;
                                diffMineCount = 99;
                                updatedMineCount = 99;
                                labelSpacer = 35;
                                break;
                        default:
                                break;
                }

                score = 0;
                genBoard();
                genMines();
                genFlags();
                boardDisplay(chosenDifficulty);
        }

        /**
         * Method used to generate board. Basic nested for loop based on height/width decided in constructor.
         * Also instantiates the zones[] array based on those values.
         */
        private void genBoard() {
                zones = new Zone[height * width + 1];
                zoneCount = 1;
                for (i = 1; i <= height; i++) {
                        for (j = 1; j <= width; j++) {
                                zones[zoneCount] = new Zone(25 + (j * 25), 10 + (i * 25), boardPane, j, i, zoneCount, width, height);
                                zones[zoneCount].addObserver(this);
                                zones[zoneCount].draw(boardPane);
                                zoneCount++;
                        }
                }
        }

        /**
         * Method used to generate the mines in the zone grid. 
         * Randomly generates a number of mines based on difficulty and makes sure mines aren't placed in the same zone twice.
         */
        private void genMines() {
                i = 1;
                while (i <= diffMineCount) {
                        mineDecision = 1 + (int) (Math.random() * (zones.length - 1));
                        if (!zones[mineDecision].getIsMine()) {
                                zones[mineDecision].setIsMine(true);
                                i++;
                        }
                }
        }

        /**
         * Method used to generate flags in zones. Essentially the histogram method from Assignment for. 
         * Checks each zone and based on the zone type (location on the board) it increments a counter for that zone object.
         */
        private void genFlags() {
                for (i = 1; i < zones.length; i++) {
                        if (zones[i].getIsMine()) {
                                switch (zones[i].getZoneType()) {
                                        case "Center":
                                                if (!zones[i - 1].getIsMine()) {
                                                        zones[i - 1].increaseFlagValue();
                                                }
                                                if (!zones[i + 1].getIsMine()) {
                                                        zones[i + 1].increaseFlagValue();
                                                }
                                                if (!zones[i - width].getIsMine()) {
                                                        zones[i - width].increaseFlagValue();
                                                }
                                                if (!zones[i - (width - 1)].getIsMine()) {
                                                        zones[i - (width - 1)].increaseFlagValue();
                                                }
                                                if (!zones[i - (width + 1)].getIsMine()) {
                                                        zones[i - (width + 1)].increaseFlagValue();
                                                }
                                                if (!zones[i + width].getIsMine()) {
                                                        zones[i + width].increaseFlagValue();
                                                }
                                                if (!zones[i + (width - 1)].getIsMine()) {
                                                        zones[i + (width - 1)].increaseFlagValue();
                                                }
                                                if (!zones[i + (width + 1)].getIsMine()) {
                                                        zones[i + (width + 1)].increaseFlagValue();
                                                }
                                                break;
                                        case "Top Left":
                                                if (!zones[i + 1].getIsMine()) {
                                                        zones[i + 1].increaseFlagValue();
                                                }
                                                if (!zones[i + width].getIsMine()) {
                                                        zones[i + width].increaseFlagValue();
                                                }
                                                if (!zones[i + (width + 1)].getIsMine()) {
                                                        zones[i + (width + 1)].increaseFlagValue();
                                                }
                                                break;
                                        case "Bottom Left":
                                                if (!zones[i + 1].getIsMine()) {
                                                        zones[i + 1].increaseFlagValue();
                                                }
                                                if (!zones[i - width].getIsMine()) {
                                                        zones[i - width].increaseFlagValue();
                                                }
                                                if (!zones[i - (width + 1)].getIsMine()) {
                                                        zones[i - (width + 1)].increaseFlagValue();
                                                }
                                                break;
                                        case "Top Right":
                                                if (!zones[i - 1].getIsMine()) {
                                                        zones[i - 1].increaseFlagValue();
                                                }
                                                if (!zones[i + width].getIsMine()) {
                                                        zones[i + width].increaseFlagValue();
                                                }
                                                if (!zones[i + (width - 1)].getIsMine()) {
                                                        zones[i + (width - 1)].increaseFlagValue();
                                                }
                                                break;
                                        case "Bottom Right":
                                                if (!zones[i - 1].getIsMine()) {
                                                        zones[i - 1].increaseFlagValue();
                                                }
                                                if (!zones[i - width].getIsMine()) {
                                                        zones[i - width].increaseFlagValue();
                                                }
                                                if (!zones[i - width - 1].getIsMine()) {
                                                        zones[i - width - 1].increaseFlagValue();
                                                }
                                                break;
                                        case "Left Perimeter":
                                                if (!zones[i + 1].getIsMine()) {
                                                        zones[i + 1].increaseFlagValue();
                                                }
                                                if (!zones[i - width].getIsMine()) {
                                                        zones[i - width].increaseFlagValue();
                                                }
                                                if (!zones[i - width + 1].getIsMine()) {
                                                        zones[i - width + 1].increaseFlagValue();
                                                }
                                                if (!zones[i + width].getIsMine()) {
                                                        zones[i + width].increaseFlagValue();
                                                }
                                                if (!zones[i + (width + 1)].getIsMine()) {
                                                        zones[i + (width + 1)].increaseFlagValue();
                                                }
                                                break;
                                        case "Top Perimeter":
                                                if (!zones[i - 1].getIsMine()) {
                                                        zones[i - 1].increaseFlagValue();
                                                }
                                                if (!zones[i + 1].getIsMine()) {
                                                        zones[i + 1].increaseFlagValue();
                                                }
                                                if (!zones[i + width].getIsMine()) {
                                                        zones[i + width].increaseFlagValue();
                                                }
                                                if (!zones[i + (width + 1)].getIsMine()) {
                                                        zones[i + (width + 1)].increaseFlagValue();
                                                }
                                                if (!zones[i + (width - 1)].getIsMine()) {
                                                        zones[i + (width - 1)].increaseFlagValue();
                                                }
                                                break;
                                        case "Right Perimeter":
                                                if (!zones[i - 1].getIsMine()) {
                                                        zones[i - 1].increaseFlagValue();
                                                }
                                                if (!zones[i + width].getIsMine()) {
                                                        zones[i + width].increaseFlagValue();
                                                }
                                                if (!zones[i + (width - 1)].getIsMine()) {
                                                        zones[i + (width - 1)].increaseFlagValue();
                                                }
                                                if (!zones[i - width].getIsMine()) {
                                                        zones[i - width].increaseFlagValue();
                                                }
                                                if (!zones[i - (width - 1)].getIsMine()) {
                                                        zones[i - (width - 1)].increaseFlagValue();
                                                }
                                                break;
                                        case "Bottom Perimeter":
                                                if (!zones[i - 1].getIsMine()) {
                                                        zones[i - 1].increaseFlagValue();
                                                }
                                                if (!zones[i + 1].getIsMine()) {
                                                        zones[i + 1].increaseFlagValue();
                                                }
                                                if (!zones[i - width].getIsMine()) {
                                                        zones[i - width].increaseFlagValue();
                                                }
                                                if (!zones[i - (width - 1)].getIsMine()) {
                                                        zones[i - (width - 1)].increaseFlagValue();
                                                }
                                                if (!zones[i - (width + 1)].getIsMine()) {
                                                        zones[i - (width + 1)].increaseFlagValue();
                                                }
                                                break;
                                        default:
                                                break;
                                }
                        }

                }
        }

        /**
         * Used to return the beginner high score player for the main menu display.
         * @return 
         */
        public String getBeginnerHiScorePlayer() {
                return beginnerHiScorePlayer;
        }

        /**
         * Used to return the intermediate high score player for the main menu display.
         * @return 
         */
        public String getInterHiScorePlayer() {
                return interHiScorePlayer;
        }

        /**
         * Used to return the expert high score player for the main menu display.
         * @return 
         */
        public String getExpertHiScorePlayer() {
                return expertHiScorePlayer;
        }

        /**
         * Used to return the beginner high score for the main menu display.
         * @return 
         */
        public int getBeginnerHiScore() {
                return beginnerHiScore;
        }

         /**
         * Used to return the intermediate high score for the main menu display.
         * @return 
         */
        public int getInterHiScore() {
                return interHiScore;
        }

        /**
         * Used to return the expert high score for the main menu display.
         * @return 
         */
        public int getExpertHiScore() {
                return expertHiScore;
        }

        /**
         * Method which adds some display nodes to the scene/pane. Specifically amount of mines left and amount of turns taken.
         * @param chosenDifficulty 
         */
        private void boardDisplay(int chosenDifficulty) {

                displayMines = new Label(updatedMineCount + " Mines Left!");
                displayScore = new Label(score + " Moves Made!");

                if (chosenDifficulty == 1) {
                        displayMines.setFont(Font.loadFont("file:.\\.\\.\\.\\resources\\fonts\\Blacklisted.ttf", 30));
                        displayScore.setFont(Font.loadFont("file:.\\.\\.\\.\\resources\\fonts\\Blacklisted.ttf", 30));
                        displayMines.layoutYProperty().bind((boardPane.heightProperty().subtract(displayMines.heightProperty()).divide(2).subtract(labelSpacer)).add(10));
                        displayScore.layoutYProperty().bind((boardPane.heightProperty().subtract(displayScore.heightProperty()).divide(2).add(labelSpacer)).add(10));
                } else {
                        displayMines.setFont(Font.loadFont("file:.\\.\\.\\.\\resources\\fonts\\Blacklisted.ttf", 37));
                        displayScore.setFont(Font.loadFont("file:.\\.\\.\\.\\resources\\fonts\\Blacklisted.ttf", 37));
                        displayMines.layoutYProperty().bind((boardPane.heightProperty().subtract(displayMines.heightProperty()).divide(2).subtract(labelSpacer)));
                        displayScore.layoutYProperty().bind((boardPane.heightProperty().subtract(displayScore.heightProperty()).divide(2).add(labelSpacer)));
                }

                boardPane.getChildren().add(displayMines);
                subGrid = 50 + (25 * width);
                displayMines.layoutXProperty().bind(((boardPane.widthProperty().subtract(subGrid)).subtract(displayMines.widthProperty()).divide(2)).add(subGrid));
                displayMines.setTextFill(Color.RED);

                boardPane.getChildren().add(displayScore);
                displayScore.layoutXProperty().bind(((boardPane.widthProperty().subtract(subGrid)).subtract(displayScore.widthProperty()).divide(2)).add(subGrid));
                displayScore.setTextFill(Color.RED);

        }

        /**
         * Method used specifically to call other methods which update certain things whenever the observable object (Zone) sets that there has been a change. 
         * @param Zone
         * @param zoneValue 
         */
        @Override
        public void update(Observable Zone, Object zoneValue) {
                int indexValue = (int) zoneValue;
                zoneUpdate(indexValue);
                gameWinCheck();
        }

        /**
         * Whenever a zone is changed, checks index value of specified zone for multiple things.
         * If zone changed is a mine and it is defused, remove an active mine from mine count and update screen text.
         * If zones changed is a mine and it isn't defused, essentially take over screen with game over screen with only a button to main menu. 
         * If a zone is not a mine and it has been marked as clear, attempt to clear adjacent clear zones as well. 
         * If zone was just a flag, increment score.
         * @param indexValue 
         */
        public void zoneUpdate(int indexValue) {
                if ((zones[indexValue].getIsMine()) && (zones[indexValue].getIsMineDefused())) {
                        updatedMineCount--;
                        displayMines.setText(updatedMineCount + " Mines Left!");
                        score++;
                        displayScore.setText(score + " Moves Made!");
                } else if ((zones[indexValue].getIsMine()) && (!zones[indexValue].getIsMineDefused())) {  //Mine is not defused
                        score++;
                        displayScore.setText(score + " Moves Made!");
                        
                        boardPane.getChildren().clear();
                        boardPane.getChildren().add(backgroundIV);
                        boardPane.getChildren().add(mainMenuButton);
                        boardPane.getChildren().add(gameOver);
                        
                        mainMenuButton.layoutXProperty().bind(boardPane.widthProperty().subtract(mainMenuButton.widthProperty()).divide(2));
                        mainMenuButton.setLayoutY(boardPane.getHeight() - 50);
                        
                        gameOver.setTextFill(Color.YELLOW);
                        gameOver.layoutXProperty().bind(boardPane.widthProperty().subtract(gameOver.widthProperty()).divide(2));
                        gameOver.layoutYProperty().bind(boardPane.heightProperty().subtract(gameOver.heightProperty()).divide(2));
                        gameOver.setFont(Font.loadFont("file:.\\.\\.\\.\\resources\\fonts\\Blacklisted.ttf", 80));
                        gameOver.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.5) , 5,5,5,1 );");
                } else if ((!zones[indexValue].getIsMine()) && (zones[indexValue].isClearActivated())) {
                        score++;
                        displayScore.setText(score + " Moves Made!");

                        int clearCheckCenter[] = {(indexValue - width - 1), (indexValue - width), (indexValue - width + 1), (indexValue - 1), (indexValue + 1), (indexValue + width - 1), (indexValue + width), (indexValue + width + 1)};
                        int clearCheckTL[] = {(indexValue + 1), (indexValue + width), (indexValue + width + 1)};
                        int clearCheckTR[] = {(indexValue - 1), (indexValue + width), (indexValue + width - 1)};
                        int clearCheckBL[] = {(indexValue + 1), (indexValue - width), (indexValue - width + 1)};
                        int clearCheckBR[] = {(indexValue - 1), (indexValue - width), (indexValue - width - 1)};
                        int clearCheckLP[] = {(indexValue - width), (indexValue - width + 1), (indexValue + 1), (indexValue + width), (indexValue + width + 1)};
                        int clearCheckTP[] = {(indexValue + width - 1), (indexValue - 1), (indexValue + 1), (indexValue + width), (indexValue + width + 1)};
                        int clearCheckRP[] = {(indexValue - width), (indexValue - width - 1), (indexValue - 1), (indexValue + width), (indexValue + width - 1)};
                        int clearCheckBP[] = {(indexValue - width - 1), (indexValue - 1), (indexValue + 1), (indexValue - width), (indexValue - width + 1)};
                        // Experimenting with arrays of arrays.
                        int clearCheck[][] = {clearCheckCenter, clearCheckTL, clearCheckBL, clearCheckTR, clearCheckBR, clearCheckLP, clearCheckTP, clearCheckRP, clearCheckBP};

                        // INSERT SOME SORT OF LOOP TO CHECK OUTER CLEARED SPACES
                        // INSERT SOLUTION FOR IF INDEX + or - 1 and INDEX - or + WIDTH ARE MINES OR FLAGS. THE DIAGONALS SHOULD NOT CLEAR
                        switch (zones[indexValue].getZoneType()) {
                                case "Center":
                                        for (i = 0; i < clearCheck[0].length; i++) {
                                                if (!zones[clearCheck[0][i]].getIsMine() && !zones[clearCheck[0][i]].getIsFlag()) {
                                                        zones[clearCheck[0][i]].activateClearZone();
                                                        //newCheck++;
                                                        //System.out.println("New Check: " + newCheck);
                                                        //continueCheck(indexValue, clearCheck[0][i], clearCheck);
                                                }
                                        }
                                        break;

                                case "Top Left":
                                        for (i = 0; i < clearCheck[1].length; i++) {
                                                newClear = false;
                                                if (!zones[clearCheck[1][i]].getIsMine() && !zones[clearCheck[1][i]].getIsFlag()) {
                                                        zones[clearCheck[1][i]].activateClearZone();
                                                        //continueCheck(indexValue, clearCheck[1][i], clearCheck);
                                                }
                                        }
                                        break;
                                case "Bottom Left":
                                        for (i = 0; i < clearCheck[2].length; i++) {
                                                newClear = false;
                                                if (!zones[clearCheck[2][i]].getIsMine() && !zones[clearCheck[2][i]].getIsFlag()) {
                                                        zones[clearCheck[2][i]].activateClearZone();
                                                        //continueCheck(indexValue, clearCheck[2][i], clearCheck);
                                                }
                                        }
                                        break;
                                case "Top Right":
                                        for (i = 0; i < clearCheck[3].length; i++) {
                                                newClear = false;
                                                if (!zones[clearCheck[3][i]].getIsMine() && !zones[clearCheck[3][i]].getIsFlag()) {
                                                        zones[clearCheck[3][i]].activateClearZone();
                                                        // continueCheck(indexValue, clearCheck[3][i], clearCheck);
                                                }
                                        }
                                        break;
                                case "Bottom Right":
                                        for (i = 0; i < clearCheck[4].length; i++) {
                                                newClear = false;
                                                if (!zones[clearCheck[4][i]].getIsMine() && !zones[clearCheck[4][i]].getIsFlag()) {
                                                        zones[clearCheck[4][i]].activateClearZone();
                                                        //continueCheck(indexValue, clearCheck[4][i], clearCheck);
                                                }
                                        }
                                        break;
                                case "Left Perimeter":
                                        for (i = 0; i < clearCheck[5].length; i++) {
                                                newClear = false;
                                                if (!zones[clearCheck[5][i]].getIsMine() && !zones[clearCheck[5][i]].getIsFlag()) {
                                                        zones[clearCheck[5][i]].activateClearZone();
                                                        //continueCheck(indexValue, clearCheck[5][i], clearCheck);
                                                }
                                        }
                                        break;
                                case "Top Perimeter":
                                        for (i = 0; i < clearCheck[6].length; i++) {
                                                newClear = false;
                                                if (!zones[clearCheck[6][i]].getIsMine() && !zones[clearCheck[6][i]].getIsFlag()) {
                                                        zones[clearCheck[6][i]].activateClearZone();
                                                        //continueCheck(indexValue, clearCheck[6][i], clearCheck);
                                                }
                                        }
                                        break;
                                case "Right Perimeter":
                                        for (i = 0; i < clearCheck[7].length; i++) {
                                                newClear = false;
                                                if (!zones[clearCheck[7][i]].getIsMine() && !zones[clearCheck[7][i]].getIsFlag()) {
                                                        zones[clearCheck[7][i]].activateClearZone();
                                                        //continueCheck(indexValue, clearCheck[7][i], clearCheck);
                                                }
                                        }
                                        break;
                                case "Bottom Perimeter":
                                        for (i = 0; i < clearCheck[8].length; i++) {
                                                newClear = false;
                                                if (!zones[clearCheck[8][i]].getIsMine() && !zones[clearCheck[8][i]].getIsFlag()) {
                                                        zones[clearCheck[8][i]].activateClearZone();
                                                        //continueCheck(indexValue, clearCheck[8][i], clearCheck);
                                                }
                                        }
                                        break;
                                default:
                                        break;
                        }
                } else {
                        // No matter the observable action add one to the score.
                        score++;
                        displayScore.setText(score + " Moves Made!");
                }
        }

        /**
         * Method to check if the game has been won by defusing all the mines. 
         */
        public void gameWinCheck() {
                if (updatedMineCount == 0) {
                        switch (chosenDifficulty) {
                                case 1:
                                        if ((score < beginnerHiScore) && (beginnerHiScore != 0)) {
                                                beginnerHiScorePlayer = player;
                                                beginnerHiScore = score;
                                        } else {
                                                beginnerHiScorePlayer = player;
                                                beginnerHiScore = score;
                                        }
                                        break;
                                case 2:
                                        if ((score < interHiScore) && (interHiScore != 0)) {
                                                interHiScorePlayer = player;
                                                interHiScore = score;
                                        } else {
                                                interHiScorePlayer = player;
                                                interHiScore = score;
                                        }
                                        break;
                                case 3:
                                        if ((score < expertHiScore) && (expertHiScore != 0)) {
                                                expertHiScorePlayer = player;
                                                expertHiScore = score;
                                        } else {
                                                expertHiScorePlayer = player;
                                                expertHiScore = score;
                                        }
                                        break;
                                default:
                                        break;
                        }

                        boardPane.getChildren().clear();
                        boardPane.getChildren().add(backgroundIV);
                        boardPane.getChildren().add(mainMenuButton);
                        boardPane.getChildren().add(gameWon);
                        
                        mainMenuButton.layoutXProperty().bind(boardPane.widthProperty().subtract(mainMenuButton.widthProperty()).divide(2));
                        mainMenuButton.setLayoutY(boardPane.getHeight() - 50);
                        
                        gameWon.setTextFill(Color.GREEN);
                        gameWon.layoutXProperty().bind(boardPane.widthProperty().subtract(gameWon.widthProperty()).divide(2));
                        gameWon.layoutYProperty().bind(boardPane.heightProperty().subtract(gameWon.heightProperty()).divide(2));
                        gameWon.setFont(Font.loadFont("file:.\\.\\.\\.\\resources\\fonts\\Blacklisted.ttf", 80));
                        gameWon.setStyle("-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.5) , 2.5,2.5,2.5,1 );");
                }
        }

        /**
         * Method to continue checking zones around a zone which was just cleared.
         * Recursive method to coninue checking and clearing cells. I couldn' t actually get it working. 
         * 
         * @param originalIndex
         * @param newIndex
         * @param clearCheck 
         */
        public void continueCheck(int originalIndex, int newIndex, int[][] clearCheck) {

                //if( !zones[newIndex].equals(zones[originalIndex])) // if cell is surrounding the original index then ignore
                switch (zones[newIndex].getZoneType()) {
                        case "Center":
                                for (i = 0; i < clearCheck[0].length; i++) {
                                        newClear = false;
                                        if (!zones[clearCheck[0][i]].getIsMine() && !zones[clearCheck[0][i]].getIsFlag()) {
                                                zones[clearCheck[0][i]].activateClearZone();
                                                newCheck++;
                                                System.out.println("Nested Center New Check: " + newCheck);
                                                continueCheck(newIndex, clearCheck[0][i], clearCheck);
                                        }
                                }
                                break;
                        case "Top Left":
                                for (i = 0; i < clearCheck[1].length; i++) {
                                        newClear = false;
                                        if (!zones[clearCheck[1][i]].getIsMine() && !zones[clearCheck[1][i]].getIsFlag()) {
                                                zones[clearCheck[1][i]].activateClearZone();
                                                newCheck++;
                                                System.out.println("Nested TL New Check: " + newCheck);
                                                continueCheck(newIndex, clearCheck[1][i], clearCheck);
                                        }
                                }
                                break;
                        case "Bottom Left":
                                for (i = 0; i < clearCheck[2].length; i++) {
                                        newClear = false;
                                        if (!zones[clearCheck[2][i]].getIsMine() && !zones[clearCheck[2][i]].getIsFlag()) {
                                                zones[clearCheck[2][i]].activateClearZone();
                                                newCheck++;
                                                System.out.println("Nested BL New Check: " + newCheck);
                                                continueCheck(newIndex, clearCheck[2][i], clearCheck);
                                        }
                                }
                                break;
                        case "Top Right":
                                for (i = 0; i < clearCheck[3].length; i++) {
                                        newClear = false;
                                        if (!zones[clearCheck[3][i]].getIsMine() && !zones[clearCheck[3][i]].getIsFlag()) {
                                                zones[clearCheck[3][i]].activateClearZone();
                                                newCheck++;
                                                System.out.println("Nested TR New Check: " + newCheck);
                                                continueCheck(newIndex, clearCheck[3][i], clearCheck);
                                        }
                                }
                                break;
                        case "Bottom Right":
                                for (i = 0; i < clearCheck[4].length; i++) {
                                        newClear = false;
                                        if (!zones[clearCheck[4][i]].getIsMine() && !zones[clearCheck[4][i]].getIsFlag()) {
                                                zones[clearCheck[4][i]].activateClearZone();
                                                newCheck++;
                                                System.out.println("Nested BR New Check: " + newCheck);
                                                continueCheck(newIndex, clearCheck[4][i], clearCheck);
                                        }
                                }
                                break;
                        case "Left Perimeter":
                                for (i = 0; i < clearCheck[5].length; i++) {
                                        newClear = false;
                                        if (!zones[clearCheck[5][i]].getIsMine() && !zones[clearCheck[5][i]].getIsFlag()) {
                                                zones[clearCheck[5][i]].activateClearZone();
                                                newCheck++;
                                                System.out.println("Nested LP New Check: " + newCheck);
                                                continueCheck(newIndex, clearCheck[5][i], clearCheck);
                                        }
                                }
                                break;
                        case "Top Perimeter":
                                for (i = 0; i < clearCheck[6].length; i++) {
                                        newClear = false;
                                        if (!zones[clearCheck[6][i]].getIsMine() && !zones[clearCheck[6][i]].getIsFlag()) {
                                                zones[clearCheck[6][i]].activateClearZone();
                                                newCheck++;
                                                System.out.println("Nested TP New Check: " + newCheck);
                                                continueCheck(newIndex, clearCheck[6][i], clearCheck);
                                        }
                                }
                                break;
                        case "Right Perimeter":
                                for (i = 0; i < clearCheck[7].length; i++) {
                                        newClear = false;
                                        if (!zones[clearCheck[7][i]].getIsMine() && !zones[clearCheck[7][i]].getIsFlag()) {
                                                zones[clearCheck[7][i]].activateClearZone();
                                                newCheck++;
                                                System.out.println("Nested RP New Check: " + newCheck);
                                                continueCheck(newIndex, clearCheck[7][i], clearCheck);
                                        }
                                }
                                break;
                        case "Bottom Perimeter":
                                for (i = 0; i < clearCheck[8].length; i++) {
                                        newClear = false;
                                        if (!zones[clearCheck[8][i]].getIsMine() && !zones[clearCheck[8][i]].getIsFlag()) {
                                                zones[clearCheck[8][i]].activateClearZone();
                                                newCheck++;
                                                System.out.println("Nested BP New Check: " + newCheck);
                                                continueCheck(newIndex, clearCheck[8][i], clearCheck);
                                        }
                                }
                                break;
                        default:
                                break;
                }
        }
}
