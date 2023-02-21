package game.player.ai.evaluation;

import game.GameState;
import game.player.Player;

import static game.Game.NUM_OF_COLUMNS;
import static game.Game.NUM_OF_ROWS;

public class Evaluation {
    public Evaluation(int three, int two, int center) {
        threeInARow = three;
        twoInARow = two;
        centerValue = center;
    }

    private int threeInARow;
    private int twoInARow;
    private int centerValue;

    public int boardEvaluation(GameState gameState, Player currentPlayer) {
        int result = 0;
        // eval center position
        result += evalCenter(gameState, currentPlayer);
        // eval columns
        result += evalColumns(gameState, currentPlayer);
        // eval rows
        result += evalRows(gameState, currentPlayer);
        // eval diagonals
        result += evalDiagonals(gameState, currentPlayer);
        return result;
    }

    private int evalCenter(GameState gameState, Player currentPlayer) {
        int result = 0;
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            if (gameState.getBoard()[i][NUM_OF_COLUMNS / 2] == currentPlayer) result += centerValue;
        }
        return result;
    }

    private int evalDiagonals(GameState gameState, Player currentPlayer) {
        int ct;
        int empty;
        int result = 0;
        int row;
        int column;
        // score from top left to bot right
        for (int r = 0; r < NUM_OF_ROWS - 3; r++) {
            for (int c = 0; c < NUM_OF_COLUMNS - 3; c++) {
                column = c;
                row = r;
                ct = 0;
                empty = 0;
                while (row < NUM_OF_ROWS && column < NUM_OF_COLUMNS) {
                    if (gameState.getBoard()[row][column] == currentPlayer) ct++;
                    else if (gameState.getBoard()[row][column] == null) empty++;
                    row++;
                    column++;
                }
                if (ct == 3 && empty == 1) result += threeInARow;
                else if (ct == 2 && empty == 2) result += twoInARow;
            }
        }
        // top right to bot left
        for (int r = 0; r < NUM_OF_ROWS - 3; r++) {
            for (int c = NUM_OF_COLUMNS - 1; c >= 3; c--) {
                column = c;
                row = r;
                ct = 0;
                empty = 0;
                while (row < NUM_OF_ROWS && column >= 0) {
                    if (gameState.getBoard()[row][column] == currentPlayer) ct++;
                    else if (gameState.getBoard()[row][column] == null) empty++;
                    row++;
                    column--;
                }
                if (ct == 3 && empty == 1) result += threeInARow;
                else if (ct == 2 && empty == 2) result += twoInARow;
            }
        }
        return result;
    }

    private int evalColumns(GameState gameState, Player currentPlayer) {
        int ct = 0;
        int empty = 0;
        int result = 0;
        for (int i = 0; i < NUM_OF_COLUMNS; i++) {
            // 3 different blocks of 4
            for (int j = 0; j < 3; j++) {
                for (int k = j; k < j + 4; k++) {
                    if (gameState.getBoard()[k][i] == null) empty++;
                    else if (gameState.getBoard()[k][i].equals(currentPlayer)) ct++;
                }
                if (ct == 3 && empty == 1) result += threeInARow;
                else if (ct == 2 && empty == 2) result += twoInARow;
                ct = 0;
                empty = 0;
            }
        }
        return result;
    }

    private int evalRows(GameState gameState, Player currentPlayer) {
        int result = 0;
        int ct = 0;
        int empty = 0;
        int lineLength = 4;
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            // check blocks of 4
            for (int j = 0; j < NUM_OF_COLUMNS - 3; j++) {
                for (int k = j; k < lineLength + j; k++) {
                    if (gameState.getBoard()[i][k] == currentPlayer) ct++;
                    else if (gameState.getBoard()[i][k] == null) empty++;
                }
                if (ct == 3 && empty == 1) result += threeInARow;
                else if (ct == 2 && empty == 2) result += twoInARow;
                ct = 0;
                empty = 0;
            }
        }
        return result;
    }

    public int getThreeInARow() {
        return threeInARow;
    }

    public void setThreeInARow(int threeInARow) {
        this.threeInARow = threeInARow;
    }

    public int getTwoInARow() {
        return twoInARow;
    }

    public void setTwoInARow(int twoInARow) {
        this.twoInARow = twoInARow;
    }

    public int getCenterValue() {
        return centerValue;
    }

    public void setCenterValue(int centerValue) {
        this.centerValue = centerValue;
    }
}
