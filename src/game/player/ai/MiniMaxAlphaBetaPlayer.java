package game.player.ai;

import game.GameConfig;
import game.GameState;
import game.player.Player;
import game.player.ai.evaluation.Evaluation;

import java.awt.*;

import static game.Game.NUM_OF_COLUMNS;

public class MiniMaxAlphaBetaPlayer implements AIPlayer {
    private int currentMaxDepth = 1;
    private boolean increaseDepth;
    private static final int WIN = 1_000_000;
    private final String name;
    private Color color;
    private Evaluation eval;
    private int wins = 0;
    private int losses = 0;
    private int draws = 0;

    public MiniMaxAlphaBetaPlayer(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public MiniMaxAlphaBetaPlayer(String name) {
        this.name = name;
    }

    @Override
    public int nextColumn(GameState gameState) {
        long time = System.nanoTime();
        double turnTime = 0;
        int[] result = new int[0];
        while (turnTime < GameConfig.MAX_TURN_TIME) {
            increaseDepth = false;
            result = miniMax(gameState, currentMaxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            turnTime = (System.nanoTime() - time) / 1_000_000_000.0;
//            System.out.format("AB Player %s's best score possible score with %d moves calculated ahead is %d in column %d\n",
//                    this.getName(), depth, result[1], result[0]);
            if (turnTime < GameConfig.MAX_TURN_TIME && increaseDepth) currentMaxDepth++;
            else return result[0];
        }
        return result[0];
    }

    private int[] miniMax(GameState gameState, int depth, int alpha, int beta, boolean maximizingPlayer) {
        Player currentPlayer = gameState.getLastMovedPlayer();
        if (currentPlayer != null && gameState.checkPlayerWon() && currentPlayer == this)
            return new int[]{gameState.getLastMoveColumn(), WIN};
        else if (currentPlayer != null && gameState.checkPlayerWon() && currentPlayer != this)
            return new int[]{gameState.getLastMoveColumn(), -WIN};
        else if (gameState.isBoardFull()) return new int[]{gameState.getLastMoveColumn(), 0};
        else if (depth == 0) {
            increaseDepth = true;
            return new int[]{gameState.getLastMoveColumn(), eval.boardEvaluation(gameState, this)};
        }

        int bestScore;
        int col = -1;
        if (maximizingPlayer) {
            bestScore = -10_000_000;
            for (int i = 0; i < NUM_OF_COLUMNS; i++) {
                if (gameState.isColumnNotFull(i)) {
                    GameState gameCopy = new GameState(gameState);
                    gameCopy.insertPiece(i, this);
                    int score = miniMax(gameCopy, depth - 1, alpha, beta, false)[1];
                    if (score > bestScore) {
                        bestScore = score;
                        col = i;
                    } else if (score == bestScore && depth == currentMaxDepth) {
                        double rand = Math.random();
                        if (rand < 0.5) {
                            col = i;
                        }
                    }
                    alpha = Integer.max(alpha, bestScore);
                    if (beta < alpha)
                        break;
                }
            }
        } else {
            bestScore = 10_000_000;
            for (int i = 0; i < NUM_OF_COLUMNS; i++) {
                if (gameState.isColumnNotFull(i)) {
                    GameState gameCopy = new GameState(gameState);
                    gameCopy.insertPiece(i, this.getNextPlayer());
                    int score = miniMax(gameCopy, depth - 1, alpha, beta, true)[1];
                    if (score < bestScore) {
                        bestScore = score;
                        col = i;
                    }
                    beta = Integer.min(beta, bestScore);
                    if (beta < alpha)
                        break;
                }
            }
        }
        return new int[]{col, bestScore};
    }

    @Override
    public void setCurrentMaxDepth(int currentMaxDepth) {
        this.currentMaxDepth = currentMaxDepth;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Evaluation getEval() {
        return eval;
    }

    @Override
    public void setEvaluation(Evaluation evaluation) {
        this.eval = evaluation;
    }

    @Override
    public void incrementWins() {
        this.wins++;
    }

    @Override
    public void incrementLosses() {
        this.losses++;
    }

    @Override
    public void incrementDraws() {
        this.draws++;
    }

    @Override
    public double winPercentage() {
        double winPercentage;
        if (wins == 0) return 0.0;
        else if (losses == 0) return 100.0;
        else {
            double wins = this.wins;
            double losses = this.losses;
            winPercentage = (wins / (losses + wins)) * 100;
        }
        return winPercentage;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }

    @Override
    public void resetStats() {
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
    }
}
