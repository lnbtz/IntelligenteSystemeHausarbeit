package game.player.ai;

import game.GameState;
import game.player.Player;

import java.awt.*;

import static game.Game.NUM_OF_COLUMNS;
import static game.Game.NUM_OF_ROWS;

public class MiniMaxPlayer implements Player {

    private static final int DEPTH = 9;
    private static final int WIN = 10000;
    private static final int THREE_IN_A_ROW = 15;
    private static final int TWO_IN_A_ROW = 5;
    private static final int CENTER_VALUE = 3;
    private String name;
    private Color color;

    public MiniMaxPlayer(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public int nextColumn(GameState gameState) {
        int[] result = miniMax(gameState, DEPTH, true);
        System.out.println("best score is: " + result[1] + " for move in column: " + result[0]);
        return result[0];
    }

    private int[] miniMax(GameState gameState, int depth, boolean maximizingPlayer) {
        Player currentPlayer = gameState.getLastMovedPlayer();
        if (currentPlayer == null) return new int[]{3, CENTER_VALUE};
        if (gameState.checkPlayerWon() && currentPlayer == this) return new int[]{0, WIN};
        else if (gameState.checkPlayerWon() && currentPlayer != this) return new int[]{0, -WIN};
        else if (gameState.isBoardFull()) return new int[]{0, 0};
        else if (depth == 0) return new int[]{0, boardEvaluation(gameState, this)};

        if (maximizingPlayer) {
            int bestScore = -10000000;
            int col = 0;
            for (int i = 0; i < NUM_OF_COLUMNS; i++) {
                if (!gameState.isColumnFull(i)) {
                    GameState gameCopy = new GameState(gameState);
                    gameCopy.insertPiece(i, this);
                    int score = miniMax(gameCopy, depth - 1, false)[1];
                    if (score > bestScore) {
                        bestScore = score;
                        col = i;
                    }
                }
            }
            return new int[]{col, bestScore};
        } else {
            int bestScore = 10000000;
            int col = 0;
            for (int i = 0; i < NUM_OF_COLUMNS; i++) {
                if (!gameState.isColumnFull(i)) {
                    GameState gameCopy = new GameState(gameState);
                    gameCopy.insertPiece(i, this.getNextPlayer());
                    int score = miniMax(gameCopy, depth - 1, true)[1];
                    if (score < bestScore) {
                        bestScore = score;
                        col = i;
                    }
                }
            }
            return new int[]{col, bestScore};
        }
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public String getName() {
        return name;
    }


    private int boardEvaluation(GameState gameState, Player currentPlayer) {
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
            if (gameState.getBoard()[i][NUM_OF_COLUMNS / 2] == currentPlayer) result += CENTER_VALUE;
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
                if (ct == 3 && empty == 1) result += THREE_IN_A_ROW;
                else if (ct == 2 && empty == 2) result += TWO_IN_A_ROW;
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
                if (ct == 3 && empty == 1) result += THREE_IN_A_ROW;
                else if (ct == 2 && empty == 2) result += TWO_IN_A_ROW;
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
                if (ct == 3 && empty == 1) result += THREE_IN_A_ROW;
                else if (ct == 2 && empty == 2) result += TWO_IN_A_ROW;
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
                if (ct == 3 && empty == 1) result += THREE_IN_A_ROW;
                else if (ct == 2 && empty == 2) result += TWO_IN_A_ROW;
                ct = 0;
                empty = 0;
            }
        }
        return result;
    }
}
