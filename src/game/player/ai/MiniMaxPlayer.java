package game.player.ai;

import game.GameConfig;
import game.GameState;
import game.player.Player;
import game.player.ai.evaluation.Evaluation;

import java.awt.*;

import static game.Game.NUM_OF_COLUMNS;

public class MiniMaxPlayer implements AIPlayer {
    // DEPTH + 1 moves calculated ahead
    private int currentMaxDepth = 1;
    private boolean depthIncrease;
    private static final int WIN = 10000;
    private final String name;
    private final Color color;
    private Evaluation eval;
    private int wins = 0;
    private int losses = 0;
    private int draws = 0;

    public MiniMaxPlayer(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public int nextColumn(GameState gameState) {
        long time = System.nanoTime();
        double turnTime = 0;
        int[] result = new int[0];
        while (turnTime < GameConfig.MAX_TURN_TIME) {
            depthIncrease = false;
            result = miniMax(gameState, currentMaxDepth, true);
            System.out.format("NOAB Player %s's best score possible score with %d moves calculated ahead is %d in column %d\n",
                    this.getName(), currentMaxDepth, result[1], result[0]);
            turnTime = (System.nanoTime() - time) / 1_000_000_000.0;
            if (turnTime < GameConfig.MAX_TURN_TIME && depthIncrease) currentMaxDepth++;
            else return result[0];
        }
        return result[0];
    }

    private int[] miniMax(GameState gameState, int depth, boolean maximizingPlayer) {
        Player currentPlayer = gameState.getLastMovedPlayer();
        if (currentPlayer != null && gameState.checkPlayerWon() && currentPlayer == this) return new int[]{0, WIN};
        else if (currentPlayer != null && gameState.checkPlayerWon() && currentPlayer != this)
            return new int[]{0, -WIN};
        else if (gameState.isBoardFull()) return new int[]{0, 0};
        else if (depth == 0) {
            depthIncrease = true;
            return new int[]{0, eval.boardEvaluation(gameState, this)};
        }

        if (maximizingPlayer) {
            int bestScore = -10000000;
            int col = 0;
            for (int i = 0; i < NUM_OF_COLUMNS; i++) {
                if (gameState.isColumnNotFull(i)) {
                    GameState gameCopy = new GameState(gameState);
                    gameCopy.insertPiece(i, this);
                    int score = miniMax(gameCopy, depth - 1, false)[1];
                    if (score > bestScore) {
                        bestScore = score;
                        col = i;
                    } else if (score == bestScore && depth == currentMaxDepth) {
                        double rand = Math.random();
                        if (rand < 0.5) {
                            col = i;
                        }
                    }
                    if (depth == currentMaxDepth) {
                        System.out.format("%s ", score);
                    }
                }
            }
            return new int[]{col, bestScore};
        } else {
            int bestScore = 10000000;
            int col = 0;
            for (int i = 0; i < NUM_OF_COLUMNS; i++) {
                if (gameState.isColumnNotFull(i)) {
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
