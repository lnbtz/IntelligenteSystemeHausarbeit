package game.player.ai.geneticAlgorithm;

import fileUtil.ResultWriter;
import game.player.ai.AIPlayer;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        PopulationGenerator populationGenerator = new PopulationGenerator(null);
        // initialize starting population also in population generator?
        List<AIPlayer> population = populationGenerator.generateRandomStartingPopulation(PopulationGenerator.POPULATION_SIZE);
        int roundCounter = 0;
        while (roundCounter < 10) {
            // start tournament
            Tournament tournament = new Tournament(population);
            tournament.startTournament();
            // write results to file
            ResultWriter.appendToFile(population, "/Users/leonbeitz/Documents/uni/s05/is/intelligentesystemehausarbeit/resources/" + "6.txt");
            // get new generation
            populationGenerator = new PopulationGenerator(population);
            population = populationGenerator.generateNewPopulation();
            roundCounter++;
        }
    }
}
