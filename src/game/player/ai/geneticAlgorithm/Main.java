package game.player.ai.geneticAlgorithm;

import game.player.ai.AIPlayer;
import game.player.ai.MiniMaxAlphaBetaPlayer;
import game.player.ai.evaluation.Evaluation;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PopulationGenerator populationGenerator = new PopulationGenerator(null);
        // initialize starting population also in population generator?
        List<AIPlayer> population = populationGenerator.generateRandomPopulation(PopulationGenerator.POPULATION_SIZE);
//        AIPlayer myPlayer = new MiniMaxAlphaBetaPlayer("myPlayer");
//        myPlayer.setEvaluation(new Evaluation(15, 5, 3));
//        population.add(myPlayer);

        while (true) {
            // start tournament
            Tournament tournament = new Tournament(population);
            tournament.startTournament();
            System.out.println("tournament results");
            for (AIPlayer player :
                    population) {
                System.out.println(player.getName() + " with win % of " + player.winPercentage());
                System.out.println(player.getName() + " wins: " + player.getWins());
                System.out.println(player.getName() + " losses: " + player.getLosses());
                System.out.println(player.getEval().getThreeInARow() + " three in a row");
                System.out.println(player.getEval().getTwoInARow() + " two in a row");
                System.out.println(player.getEval().getCenterValue() + " center value");
            }
            // get new generation
            populationGenerator = new PopulationGenerator(population);
            population = populationGenerator.generateNewPopulation();
        }
        // start tournament make them play certain amount starting both sides
        // generate new population based on performance
        // do so until someone beats everyone ??? save best performer
    }
}
