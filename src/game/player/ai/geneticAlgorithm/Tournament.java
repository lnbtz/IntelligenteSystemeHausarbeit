package game.player.ai.geneticAlgorithm;

import game.AIGame;
import game.player.ai.AIPlayer;

import java.util.List;

public class Tournament {
    private final List<AIPlayer> participantsList;

    public Tournament(List<AIPlayer> participantsList) {
        this.participantsList = participantsList;
    }

    void startTournament() {
        AIPlayer player1;
        AIPlayer player2;
        AIGame game;
        for (int i = 0; i < participantsList.size(); i++) {
            player1 = participantsList.get(i);
            for (int j = i + 1; j < participantsList.size(); j++) {
                player2 = participantsList.get(j);
                game = new AIGame(player1, player2);
                game.setStartingPlayer(player1);
                game.play();
                game.setStartingPlayer(player2);
                game.play();
            }
        }
    }
}
