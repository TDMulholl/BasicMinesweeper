package minesweeper.game;

import java.util.Observable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

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
 * Class description: This class contains all the code that deals with the Zone object.
 *
 * It extends Observer because there is an update method in the GameBoard class that listens for changes in this class
 *
 */
public class Zone extends Observable {

        /**
         * Initialize all instance variables. Ranges from details about mines, hi scores, labels, etc.
         */
        private boolean isMine, isFlag, clearActivated = false, isMineDefused = false;

        private int x, y, col, row, flagValue = 0, zoneValue, width, height;

        private Button zoneButton;

        private Text displayText = new Text("");

        private String zoneType;

        private int[] surroundingZones;

        /**
         * Constructor for zone object.
         *
         * @param x
         * @param y
         * @param boardPane
         * @param col
         * @param row
         * @param zoneValue
         * @param width
         * @param height
         */
        Zone(int x, int y, Pane boardPane, int col, int row, int zoneValue, int width, int height) {
                this.x = x;
                this.y = y;
                this.col = col;
                this.row = row;
                this.zoneValue = zoneValue;
                this.width = width;
                this.height = height;
                zoneButton = new Button();
                zoneButton.setOnMousePressed(this::mouseListener);
        }

        /**
         * Method to return Col value of zone.
         * @return
         */
        public int getCol() {
                return col;
        }

        /**
         * Method to get Row value of zone.
         * @return
         */
        public int getRow() {
                return row;
        }

        /**
         * Method to set a zone to be a mine
         * @param isMine
         */
        public void setIsMine(boolean isMine) {
                this.isMine = isMine;
        }

        /**
         * Method to check if a zone is a mine
         * @return
         */
        public boolean getIsMine() {
                return isMine;
        }

        /**
         * Method to check if a mine is defused
         * @return
         */
        public boolean getIsMineDefused() {
                return isMineDefused;
        }

        /**
         * Method to check if the zone is a flag
         * @return
         */
        public boolean getIsFlag() {
                if (flagValue > 0) {
                        isFlag = true;
                }
                return isFlag;
        }

        /**
         * Method to check the "Zone Type." Essentially returns the location of the zone on the board.
         * @return
         */
        public String getZoneType() {
                if ((getCol() > 1) && (getRow() > 1) && (getCol() < width) && (getRow() < height)) {
                        zoneType = "Center";
                } else if ((getCol() == 1) && (getRow() == 1)) {
                        zoneType = "Top Left";
                } else if ((getCol() == 1) && (getRow() == height)) {
                        zoneType = "Bottom Left";
                } else if ((getCol() == width) && (getRow() == 1)) {
                        zoneType = "Top Right";
                } else if ((getCol() == width) && (getRow() == height)) {
                        zoneType = "Bottom Right";
                } else if ((getCol() == 1) && (getRow() > 1) && (getRow() < height)) {
                        zoneType = "Left Perimeter";
                } else if ((getRow() == 1) && (getCol() > 1) && (getCol() < width)) {
                        zoneType = "Top Perimeter";
                } else if ((getCol() == width) && (getRow() > 1) && (getRow() < height)) {
                        zoneType = "Right Perimeter";
                } else if ((getRow() == height) && (getCol() > 1) && (getCol() < width)) {
                        zoneType = "Bottom Perimeter";
                }
                return zoneType;
        }

        /**
         * Method to determine the surrounding zones.
         * @param indexValue
         * @return
         */
        public int[] surroundingZones(int indexValue) {
                if ((getCol() > 1) && (getRow() > 1) && (getCol() < width) && (getRow() < height)) {
                        int surroundingZones[] = {(indexValue - width - 1), (indexValue - width), (indexValue - width + 1), (indexValue - 1), (indexValue + 1), (indexValue + width - 1), (indexValue + width), (indexValue + width + 1)};
                } else if ((getCol() == 1) && (getRow() == 1)) {
                        int surroundingZones[] = {(indexValue + 1), (indexValue + width), (indexValue + width + 1)};
                } else if ((getCol() == 1) && (getRow() == height)) {
                        int surroundingZones[] = {(indexValue + 1), (indexValue - width), (indexValue - width + 1)};
                } else if ((getCol() == width) && (getRow() == 1)) {
                        int surroundingZones[] = {(indexValue - 1), (indexValue + width), (indexValue + width - 1)};
                } else if ((getCol() == width) && (getRow() == height)) {
                        int surroundingZones[] = {(indexValue - 1), (indexValue - width), (indexValue - width - 1)};
                } else if ((getCol() == 1) && (getRow() > 1) && (getRow() < height)) {
                        int surroundingZones[] = {(indexValue - width), (indexValue - width + 1), (indexValue + 1), (indexValue + width), (indexValue + width + 1)};
                } else if ((getRow() == 1) && (getCol() > 1) && (getCol() < width)) {
                        int surroundingZones[] = {(indexValue + width - 1), (indexValue - 1), (indexValue + 1), (indexValue + width), (indexValue + width + 1)};
                } else if ((getCol() == width) && (getRow() > 1) && (getRow() < height)) {
                        int surroundingZones[] = {(indexValue - width), (indexValue - width - 1), (indexValue - 1), (indexValue + width), (indexValue + width - 1)};
                } else if ((getRow() == height) && (getCol() > 1) && (getCol() < width)) {
                        int surroundingZones[] = {(indexValue - width - 1), (indexValue - 1), (indexValue + 1), (indexValue - width), (indexValue - width + 1)};
                }

                return surroundingZones;
        }

        /**
         * Method to increase the flag value of the zone.
         */
        public void increaseFlagValue() {
                flagValue++;
        }

        /**
         * Method to check if the zone has been clear and activated.
         * @return
         */
        public boolean isClearActivated() {
                return clearActivated;
        }

        /**
         * Method to draw zone button onto the screen.
         * @param boardPane
         */
        public void draw(Pane boardPane) {
                zoneButton.setStyle("-fx-border-color: black;");
                zoneButton.setLayoutX(x);
                zoneButton.setLayoutY(y);
                zoneButton.setMinHeight(25);
                zoneButton.setMinWidth(25);
                boardPane.getChildren().add(zoneButton);
        }

        /**
         * Method which listens for a mouse click. Depending on the combination of which mouse click and zone properties do different things. 
         * Left click on Mine = Lose
         * Right on Mine = Defuse
         * Left on flag ( around mine ) = show flag 
         * Left click on clear area = clear area
         *
         * @param e
         */
        public void mouseListener(MouseEvent e) {

                if (e.isPrimaryButtonDown() && (getIsMine())) {
                        displayText = new Text("M");
                        displayText.setStyle("-fx-fill: red; -fx-font: 10pt Tahoma;  -fx-stroke: red;  -fx-stroke-width: 1pt;");
                        zoneButton.setBackground(Background.EMPTY);
                        zoneButton.setGraphic(displayText);
                        setChanged();
                        notifyObservers(zoneValue);
                } else if (e.isSecondaryButtonDown() && (getIsMine())) {
                        displayDefusedMine();
                        isMineDefused = true;
                        setChanged();
                        notifyObservers(zoneValue);
                } else if (e.isPrimaryButtonDown() && (getIsFlag())) {
                        displayFlagValue();
                        setChanged();
                        notifyObservers(zoneValue);
                } else if (e.isPrimaryButtonDown() && (!getIsMine()) && (!getIsFlag())) {
                        activateClearZone();
                        setChanged();
                        notifyObservers(zoneValue);
                } else if (e.isSecondaryButtonDown() && (!getIsMine())) {
                        setChanged();
                        notifyObservers(zoneValue);
                }
        }

        /**
         * Method which activates a clear zone. Changes background and disables button.
         */
        public void activateClearZone() {
                zoneButton.setBackground(Background.EMPTY);
                zoneButton.setDisable(true);
                zoneButton.setOpacity(100);
                clearActivated = true;
        }

        /**
         * Method sets the display text on zone to be its flag value.
         */
        public void displayFlagValue() {
                displayText = new Text(Integer.toString(flagValue));
                if (flagValue == 1) {
                        displayText.setStyle("-fx-fill: white; -fx-font: 10pt Tahoma;  -fx-stroke: white;  -fx-stroke-width: 1pt;");
                } else if (flagValue == 2) {
                        displayText.setStyle("-fx-fill: yellow; -fx-font: 10pt Tahoma; -fx-stroke: yellow; -fx-stroke-width: 1pt;");
                } else if (flagValue == 3) {
                        displayText.setStyle("-fx-fill: orange; -fx-font: 10pt Tahoma; -fx-stroke: orange; -fx-stroke-width: 1pt;");
                } else if (flagValue > 3) {
                        displayText.setStyle("-fx-fill: red; -fx-font: 10pt Tahoma; -fx-stroke: red; -fx-stroke-width: 1pt;");
                }

                zoneButton.setBackground(Background.EMPTY);
                zoneButton.setGraphic(displayText);
                zoneButton.setDisable(true);
                zoneButton.setOpacity(100);
        }

        /**
         * Method sets the display text on zone that is a mine once it has been defused 
         */
        public void displayDefusedMine() {
                displayText = new Text("D");
                displayText.setStyle("-fx-fill: red; -fx-font: 10pt Tahoma;  -fx-stroke: red;  -fx-stroke-width: 1pt;");

                zoneButton.setBackground(Background.EMPTY);
                zoneButton.setGraphic(displayText);
                zoneButton.setDisable(true);
                zoneButton.setOpacity(100);
        }

}
