package fileUtil;

import game.player.ai.AIPlayer;
import game.player.ai.MiniMaxAlphaBetaPlayer;
import game.player.ai.evaluation.Evaluation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ResultWriter {
    private static int roundCounter = 1;
    public static void appendToFile(List<AIPlayer> population, String fileName) throws IOException {
        FileWriter fw = new FileWriter(fileName, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        out.println("Round " + roundCounter);
        System.out.println(roundCounter++);
        for (AIPlayer player :
                population) {
            out.println(player.getName() + " with win % of " + player.winPercentage());
            out.println(player.getName() + " wins: " + player.getWins());
            out.println(player.getName() + " losses: " + player.getLosses());
            out.println(player.getName() + " draws: " + player.getDraws());
            out.println(player.getEval().getThreeInARow() + " three in a row");
            out.println(player.getEval().getTwoInARow() + " two in a row");
            out.println(player.getEval().getCenterValue() + " center value");
            out.println();
        }
        out.close();
    }
}
