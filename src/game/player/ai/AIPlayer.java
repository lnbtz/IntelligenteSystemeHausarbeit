package game.player.ai;

import game.player.Player;
import game.player.ai.evaluation.Evaluation;

public interface AIPlayer extends Player {
    Evaluation getEval();

    void setEvaluation(Evaluation evaluation);

    double winPercentage();

    void incrementWins();

    void incrementLosses();

    void incrementDraws();

    int getWins();

    int getLosses();

    int getDraws();

    void resetStats();
}
