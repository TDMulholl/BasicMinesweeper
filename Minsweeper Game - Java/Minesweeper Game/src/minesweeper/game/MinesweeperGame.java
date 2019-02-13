package minesweeper.game;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author Tyler Mulholland 
 * Student Number: 000287743 
 * Date: June 5th 2018 
 * Description: Assignment 5
 *
 * I, Tyler Mulholland, student number 000287743, certify that all code submitted is my own work; that I have not copied it from any other source.
 * I also certify that I have not allowed my work to be copied by others.
 *
 * Class description: This class contains all the main code that deals with the view of the minesweeper game.
 * It manages the event handling, different stages for the different difficulties, and displays the instructions and hi-scores.
 * 
 * NOTE: I couldn't seem o solve he problem with checking the surrounding zones as we discussed. So as of now the game only checked the zone that
 * are immediately surrounding the chosen zone. I may attempt to continue fixing this and hand it in later using some of my remaining late days.
 *
 * Features to add before possibly adding to portfolio: 
 * 1 - Save the hi-scores when the application closes. 
 * 2 - dim all background items on "Game Over" screen instead of adding nodes on top of them. 
 * 3 - Fix the method which does a continuous check of surrounding zones. 
 * 4 - Redesign hi-score list to look better
 * 5 - Put instructions in its own scene
 * 
 */
public class MinesweeperGame extends Application {

        /**
         * Initialize all the main instance variables that are used in this class. Ranges from stages, panes, scenes, the main font, labels, and the main menu button.
         */
        Stage stage;
        Stage boardStage;

        Pane root = new Pane();
        Pane beginnerBoardPane = new Pane();
        Pane interBoardPane = new Pane();
        Pane expertBoardPane = new Pane();
        Pane boardPane;

        Scene beginnerBoard = new Scene(beginnerBoardPane, 575, 300);
        Scene interBoard = new Scene(interBoardPane, 750, 475);
        Scene expertBoard = new Scene(expertBoardPane, 1150, 475);
        Scene chosenBoard;
        Scene menu = new Scene(root, 400, 725); // set the size here

        Font mainFont = Font.loadFont("file:.\\.\\.\\.\\resources\\fonts\\Blacklisted.ttf", 15);

        String finalPlayerName;
        String beginnerHiScorePlayer = "", interHiScorePlayer = "", expertHiScorePlayer = "";

        TextField playerName;

        Label noNameError = new Label("Please enter a player name before selecting difficulty");
        Label gameName = new Label("MINESWEEPER");
        Label beginnerHiScoreLabel, interHiScoreLabel, expertHiScoreLabel;
        Label currentPlayer;

        Button mainMenuButton = new Button("Main Menu");

        Image background = new Image("file:.\\.\\.\\.\\resources\\images\\Minesweeper Background.png");
        ImageView backgroundIV = new ImageView(background);

        GameBoard gameBoard;

        boolean noNameActive = false, startGame = false;
        int chosenDifficulty, width, subGrid;
        int beginnerHiScore = 0, interHiScore = 0, expertHiScore = 0;

        // TODO: Private Event Handlers and Helper Methods
        
        /**
         * Event handler for MouseEvent on beginner game difficulty button.
         * Used to check name entered and initialize the game on beginner.
         *
         * @param e
         */
        private void beginnerEvent(MouseEvent e) {
                nameCheck();
                if (startGame == true) {
                        chosenDifficulty = 1;
                        initializeGame(chosenDifficulty);
                }
        }

        /**
         * Event handler for MouseEvent on intermediate game difficulty button. 
         * Used to check name entered and initialize the game on intermediate.
         *
         * @param e
         */
        private void interEvent(MouseEvent e) {
                nameCheck();
                if (startGame == true) {
                        chosenDifficulty = 2;
                        initializeGame(chosenDifficulty);
                }
        }

        /**
         * Event handler for MouseEvent on expert game difficulty button. 
         * Used to check name entered and initialize the game on expert.
         *
         * @param e
         */
        private void expertEvent(MouseEvent e) {
                nameCheck();
                if (startGame == true) {
                        chosenDifficulty = 3;
                        initializeGame(chosenDifficulty);
                }
        }

        /**
         * Event handler for MouseEvent on main menu button.
         * Used to return to the main menu of the game. Only worked when contained in a try/catch.
         *
         * @param e
         */
        private void mainMenuEvent(MouseEvent e) {
                try {
                        root.getChildren().clear();
                        startGame = false;
                        playerName = new TextField("");
                        start(boardStage);
                } catch (Exception ex) {
                }
        }

        /**
         * This is where you create your components and the model and add event handlers.
         *
         * @param stage The main stage
         * @throws Exception
         */
        @Override
        public void start(Stage stage) throws Exception {
                this.boardStage = stage;
                stage.setTitle("Minesweeper"); // set the window title here
                stage.setScene(menu);

                // TODO: Add your GUI-building code here
                // 1. Create the model
                // 2. Create the GUI components
                Label welcome = new Label("Welcome to a simple game of Minesweeper");
                Label namePrompt = new Label("Player Name");
                Label difficulty = new Label("Please Select Difficulty");
                Label instructions = new Label("INSTRUCTIONS");
                Label instructions1 = new Label("1. A left click will reveal empty zones, reveal flags and detonate mines. It will always count as 1 move.");
                Label instructions2 = new Label("2. A right click will only defuse mines. However right clicks on any other squares still count as 1 move. ");
                Label instructions3 = new Label("3. The game ends either when you defuse all the mines or detonate a mine.");

                Button beginnerDiff = new Button("Beginner");
                Button interDiff = new Button("Intermediate");
                Button expertDiff = new Button("Expert");

                playerName = new TextField("");

                // Needs to be in a try/catch since gameBoard doesn't exist in the first run of the game.
                try {
                        beginnerHiScore = gameBoard.getBeginnerHiScore();
                        interHiScore = gameBoard.getInterHiScore();
                        expertHiScore = gameBoard.getExpertHiScore();
                        beginnerHiScorePlayer = gameBoard.getBeginnerHiScorePlayer();
                        interHiScorePlayer = gameBoard.getInterHiScorePlayer();
                        expertHiScorePlayer = gameBoard.getExpertHiScorePlayer();
                } catch (Exception ex) {
                }

                beginnerHiScoreLabel = new Label(" Beginner HI-SCORE: " + beginnerHiScorePlayer + " - " + Integer.toString(beginnerHiScore) + " Moves");
                interHiScoreLabel = new Label("Intermediate HI-SCORE: " + interHiScorePlayer + " - " + Integer.toString(interHiScore) + " Moves");
                expertHiScoreLabel = new Label("Expert HI-SCORE: " + expertHiScorePlayer + " - " + Integer.toString(expertHiScore) + " Moves");

                // 3. Add components to the root
                root.getChildren().add(backgroundIV);
                root.getChildren().add(welcome);
                root.getChildren().add(gameName);
                root.getChildren().add(namePrompt);
                root.getChildren().add(playerName);
                root.getChildren().add(difficulty);
                root.getChildren().add(beginnerDiff);
                root.getChildren().add(interDiff);
                root.getChildren().add(expertDiff);
                root.getChildren().add(beginnerHiScoreLabel);
                root.getChildren().add(interHiScoreLabel);
                root.getChildren().add(expertHiScoreLabel);
                root.getChildren().add(instructions);
                root.getChildren().add(instructions1);
                root.getChildren().add(instructions2);
                root.getChildren().add(instructions3);

                // 4. Configure the components (colors, fonts, size, location)
                // Unlike StackPane, regular Pane does not support centering. I used a workaround to center the label. 
                // Essentially taking the width of the pane, subtracting te width of the label then dividing by 2 
                // Need to use Font.loadFont on specific labels which are different font sizes
                
                gameName.setFont(Font.loadFont("file:.\\.\\.\\.\\resources\\fonts\\Blacklisted.ttf", 40));
                gameName.setStyle("-fx-text-fill: black; -fx-effect: dropshadow( gaussian , rgba(255,255,255,0.5) , 0,0,0,1 );");
                gameName.setUnderline(true);
                gameName.layoutXProperty().bind(root.widthProperty().subtract(gameName.widthProperty()).divide(2));
                gameName.setLayoutY(10);

                welcome.setFont(mainFont);
                welcome.setTextFill(Color.DARKGREY);
                welcome.layoutXProperty().bind(root.widthProperty().subtract(welcome.widthProperty()).divide(2));
                welcome.setLayoutY(70);

                namePrompt.setFont(mainFont);
                namePrompt.setTextFill(Color.DARKGREY);
                namePrompt.layoutXProperty().bind(root.widthProperty().subtract(namePrompt.widthProperty()).divide(2));
                namePrompt.setLayoutY(130);

                playerName.layoutXProperty().bind(root.widthProperty().subtract(playerName.widthProperty()).divide(2));
                playerName.setLayoutY(160);

                difficulty.setFont(mainFont);
                difficulty.setTextFill(Color.DARKGREY);
                difficulty.layoutXProperty().bind(root.widthProperty().subtract(difficulty.widthProperty()).divide(2));
                difficulty.setLayoutY(220);

                beginnerDiff.layoutXProperty().bind(root.widthProperty().subtract(beginnerDiff.widthProperty()).divide(2));
                beginnerDiff.setLayoutY(255);
                beginnerDiff.setMinWidth(150);
                beginnerDiff.setMinHeight(30);
                beginnerDiff.setStyle("-fx-effect: dropshadow( one-pass-box , black , 28 , 0.5 , 5 ,5 ); ");

                interDiff.layoutXProperty().bind(root.widthProperty().subtract(interDiff.widthProperty()).divide(2));
                interDiff.setLayoutY(310);
                interDiff.setMinWidth(150);
                interDiff.setMinHeight(30);
                interDiff.setStyle("-fx-effect: dropshadow( one-pass-box , black , 28 , 0.5 , 5 ,5 );");

                expertDiff.layoutXProperty().bind(root.widthProperty().subtract(expertDiff.widthProperty()).divide(2));
                expertDiff.setLayoutY(365);
                expertDiff.setMinWidth(150);
                expertDiff.setMinHeight(30);
                expertDiff.setStyle("-fx-effect: dropshadow( one-pass-box , black , 28 , 0.5 , 5 ,5 ); ");

                beginnerHiScoreLabel.setFont(mainFont);
                beginnerHiScoreLabel.setTextFill(Color.DARKGREY);
                beginnerHiScoreLabel.layoutXProperty().bind(root.widthProperty().subtract(beginnerHiScoreLabel.widthProperty()).divide(2));
                beginnerHiScoreLabel.setLayoutY(420);

                interHiScoreLabel.setFont(mainFont);
                interHiScoreLabel.setTextFill(Color.DARKGREY);
                interHiScoreLabel.layoutXProperty().bind(root.widthProperty().subtract(interHiScoreLabel.widthProperty()).divide(2));
                interHiScoreLabel.setLayoutY(455);

                expertHiScoreLabel.setFont(mainFont);
                expertHiScoreLabel.setTextFill(Color.DARKGREY);
                expertHiScoreLabel.layoutXProperty().bind(root.widthProperty().subtract(expertHiScoreLabel.widthProperty()).divide(2));
                expertHiScoreLabel.setLayoutY(490);

                instructions.setFont(Font.loadFont("file:.\\.\\.\\.\\resources\\fonts\\Blacklisted.ttf", 30));
                instructions.setTextFill(Color.BLACK);
                instructions.setUnderline(true);
                instructions.layoutXProperty().bind(root.widthProperty().subtract(instructions.widthProperty()).divide(2));
                instructions.setStyle("-fx-text-fill: black; -fx-effect: dropshadow( gaussian , rgba(255,255,255,0.5) , 0,0,0,1 );");
                instructions.setLayoutY(530);

                instructions1.setFont(Font.loadFont("file:.\\.\\.\\.\\resources\\fonts\\Blacklisted.ttf", 12.5));
                instructions1.setTextFill(Color.DARKGREY);
                instructions1.layoutXProperty().bind(root.widthProperty().subtract(instructions1.widthProperty()).divide(2));
                instructions1.setStyle("-fx-stroke: red; -fx-stroke-width: 2px;");
                instructions1.setPrefWidth(350);
                instructions1.setWrapText(true);
                instructions1.setLayoutY(580);

                instructions2.setFont(Font.loadFont("file:.\\.\\.\\.\\resources\\fonts\\Blacklisted.ttf", 12.5));
                instructions2.setTextFill(Color.DARKGREY);
                instructions2.layoutXProperty().bind(root.widthProperty().subtract(instructions2.widthProperty()).divide(2));
                instructions2.setStyle("-fx-stroke: red; -fx-stroke-width: 2px;");
                instructions2.setPrefWidth(350);
                instructions2.setWrapText(true);
                instructions2.setLayoutY(630);

                instructions3.setFont(Font.loadFont("file:.\\.\\.\\.\\resources\\fonts\\Blacklisted.ttf", 12.5));
                instructions3.setTextFill(Color.DARKGREY);
                instructions3.layoutXProperty().bind(root.widthProperty().subtract(instructions2.widthProperty()).divide(2));
                instructions3.setStyle("-fx-stroke: red; -fx-stroke-width: 2px;");
                instructions3.setPrefWidth(350);
                instructions3.setWrapText(true);
                instructions3.setLayoutY(675);

                // 5. Add Event Handlers and do final setup
                beginnerDiff.setOnMousePressed(this::beginnerEvent);
                interDiff.setOnMousePressed(this::interEvent);
                expertDiff.setOnMousePressed(this::expertEvent);

                // 6. Show the stage
                stage.show();
        }

        /**
         * Make no changes here.
         *
         * @param args unused
         */
        public static void main(String[] args) {
                launch(args);
        }

        /**
         * Method used to make sure player has entered a name. If they have signal that the game can start.
         * If not then display error message. Return if the game can start or not.
         * @return 
         */
        private boolean nameCheck() {
                finalPlayerName = playerName.getText();
                noNameActive = false;
                if (finalPlayerName.equals("") && (noNameActive == false)) {
                        root.getChildren().add(noNameError);
                        noNameError.setFont(Font.loadFont("file:.\\.\\.\\.\\resources\\fonts\\Blacklisted.ttf", 12));
                        noNameError.setTextFill(Color.RED);
                        noNameError.layoutXProperty().bind(root.widthProperty().subtract(noNameError.widthProperty()).divide(2));
                        noNameError.setLayoutY(190);
                        noNameActive = true;
                } else if (!finalPlayerName.equals("")) {
                        currentPlayer = new Label("Player: " + finalPlayerName);
                        startGame = true;
                }
                return startGame;
        }

        /**
         * Method used to initialize the game. Sets specific values backed on difficulty chosen on main screen.
         * @param chosenDifficulty 
         */
        private void initializeGame(int chosenDifficulty) {
                String diffTitle = "";

                switch (chosenDifficulty) {
                        case 1:
                                diffTitle = "Beginner";
                                chosenBoard = beginnerBoard;
                                boardPane = beginnerBoardPane;
                                width = 9;
                                subGrid = 50 + (25 * width);
                                break;
                        case 2:
                                diffTitle = "Intermediate";
                                chosenBoard = interBoard;
                                boardPane = interBoardPane;
                                width = 16;
                                subGrid = 50 + (25 * width);
                                break;
                        case 3:
                                diffTitle = "Expert";
                                chosenBoard = expertBoard;
                                boardPane = expertBoardPane;
                                width = 32;
                                subGrid = 50 + (25 * width);
                                break;
                        default:
                                break;
                }

                boardStage.setTitle("Minesweeper " + diffTitle);
                boardStage.setScene(chosenBoard);
                boardPane.getChildren().clear();
                boardPane.getChildren().add(backgroundIV);

                boardPane.getChildren().add(gameName);
                gameName.layoutXProperty().unbind();
                gameName.layoutXProperty().bind(((boardPane.widthProperty().subtract(subGrid)).subtract(gameName.widthProperty()).divide(2)).add(subGrid));

                boardPane.getChildren().add(currentPlayer);
                currentPlayer.setFont(mainFont);
                currentPlayer.layoutXProperty().bind(((boardPane.widthProperty().subtract(subGrid)).subtract(currentPlayer.widthProperty()).divide(2)).add(subGrid));
                currentPlayer.setLayoutY(75);
                currentPlayer.setTextFill(Color.DARKGREY);

                boardPane.getChildren().add(mainMenuButton);
                mainMenuButton.layoutXProperty().bind(((boardPane.widthProperty().subtract(subGrid)).subtract(mainMenuButton.widthProperty()).divide(2)).add(subGrid));
                mainMenuButton.setLayoutY(boardPane.getHeight() - 65);
                // Test for muliple lined setStyle command
                mainMenuButton.setStyle("-fx-effect: dropshadow( one-pass-box , black , 14 , 0.5 , 3 ,3 ); "
                          + "-fx-stroke: #0068BF;"
                          + "-fx-stroke-width: 3px;");
                mainMenuButton.setOnMousePressed(this::mainMenuEvent);

                gameBoard = new GameBoard(chosenDifficulty, boardPane, beginnerHiScore, interHiScore, expertHiScore, mainFont, finalPlayerName, mainMenuButton);
                boardStage.show();
        }

}
