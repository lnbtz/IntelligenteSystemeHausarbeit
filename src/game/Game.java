package game;

import game.player.HumanGUIPlayer;
import game.player.Player;
import game.player.ai.AIPlayer;
import visualization.ConnectFourVisualization;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Game {
    public static final int NUM_OF_ROWS = 6, NUM_OF_COLUMNS = 7;

    static GameState gameState;
    static Player currentPlayer;
    static ConnectFourVisualization vis;

    static List<AIPlayer> play() {
        boolean draw = true;
        currentPlayer = GameConfig.getRandomStartingPlayer();
        System.out.println("starting Player: " + currentPlayer.getName());
        gameState = new GameState(NUM_OF_ROWS, NUM_OF_COLUMNS, currentPlayer);
        vis = new ConnectFourVisualization("Connect Four", NUM_OF_ROWS, NUM_OF_COLUMNS, currentPlayer.getColor());
        vis.start();
        int turnCounter = 0;
        while (!gameState.isBoardFull()) {
//            System.out.println(currentPlayer.getName() + "'s turn.");
            int newPieceColumn;
            if (currentPlayer instanceof HumanGUIPlayer) {
                newPieceColumn = vis.nextClickedColumn();
            } else {
                long time = System.nanoTime();
                newPieceColumn = currentPlayer.nextColumn(gameState);
                System.out.printf(" (%.5f seconds)\n", (System.nanoTime() - time) / 1_000_000_000.0);
            }

//            System.out.println();

            int newPieceRow = gameState.insertPiece(newPieceColumn, currentPlayer);

            vis.colorPiece(newPieceRow, newPieceColumn,
                    currentPlayer.getColor(), currentPlayer.getNextPlayer().getColor());
            turnCounter++;
            if (gameState.checkPlayerWon()) {
                draw = false;
                break;
            }

            currentPlayer = currentPlayer.getNextPlayer();
        }
        System.out.println("game took " + turnCounter + " turns");

        List<AIPlayer> players = new ArrayList<>();
        players.add((AIPlayer) currentPlayer);
        players.add((AIPlayer) currentPlayer.getNextPlayer());

        if (draw) {
//            System.out.println("It's a draw!");
            ((AIPlayer) currentPlayer).incrementDraws();
        } else {
            System.out.println(currentPlayer.getName() + " wins!");
            ((AIPlayer) currentPlayer).incrementWins();
            ((AIPlayer) currentPlayer.getNextPlayer()).incrementLosses();
        }
        return players;
    }

    public static void main(String[] args) throws InterruptedException {
        Set<AIPlayer> players = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            players.addAll(Objects.requireNonNull(play()));
        }
        for (AIPlayer player :
                players) {
            System.out.println("////////////////////////////////////////////////////////////////////");
            System.out.println(player.getName() + " won " + player.winPercentage() + "% of the games");
            System.out.println(player.getEval().getCenterValue() + " center value");
            System.out.println(player.getEval().getThreeInARow() + " three in a row");
            System.out.println(player.getEval().getTwoInARow() + " two i a row");
            System.out.println("wins: " + player.getWins());
            System.out.println("losses: " + player.getLosses());
            System.out.println("draws: " + player.getDraws());
            System.out.println("///////////////////////////////////////////////////////////////////");
        }
    }
}
