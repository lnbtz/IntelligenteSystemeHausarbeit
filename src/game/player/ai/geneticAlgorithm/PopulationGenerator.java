package game.player.ai.geneticAlgorithm;

import game.player.ai.AIPlayer;
import game.player.ai.MiniMaxAlphaBetaPlayer;
import game.player.ai.evaluation.Evaluation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class PopulationGenerator {
    public static final double CROSSOVER_PARENT_SELECTION = 0.5;
    private static final double MUTATION_ODDS = 0.3;
    public static final double UP_OR_DOWN = 0.5;
    private static final double MUTATION_FACTOR = 10;
    public static final int POPULATION_SIZE = 50;
    private static final double TOP_PERFORMERS = 0.3;
    private static final int PARAMETER_STARTING_RANDOM = 100;
    private final List<AIPlayer> aiPlayers;
    public static int playerCounter = POPULATION_SIZE;

    public PopulationGenerator(List<AIPlayer> aiPlayers) {
        this.aiPlayers = aiPlayers;
    }

    // keep the top performers
    public List<AIPlayer> generateNewPopulation() {
        evaluateFitness();
        List<AIPlayer> topPlayers = selection();
        List<AIPlayer> children = crossover(topPlayers);
        mutate(children);
        resetWinsAndLosses(children);
        return children;
    }

    private void resetWinsAndLosses(List<AIPlayer> children) {
        for (AIPlayer player :
                children) {
            player.resetStats();
        }
    }

    public List<AIPlayer> generateRandomPopulation(int number) {
        Random rng = new Random();
        List<AIPlayer> population = new ArrayList<>();
        AIPlayer newPlayer;
        for (int i = 0; i < number; i++) {
            newPlayer = new MiniMaxAlphaBetaPlayer("player" + i);
            newPlayer.setEvaluation(new Evaluation(rng.nextInt(PARAMETER_STARTING_RANDOM), rng.nextInt(PARAMETER_STARTING_RANDOM), rng.nextInt(PARAMETER_STARTING_RANDOM)));
            population.add(newPlayer);
        }
        return population;
    }

    public void evaluateFitness() {
        aiPlayers.sort(Comparator.comparing(AIPlayer::winPercentage).reversed());
    }

    public List<AIPlayer> selection() {
        List<AIPlayer> topPerformers = new ArrayList<>();
        long numberOfTopPerformers = (long) Math.ceil(aiPlayers.size() * TOP_PERFORMERS);
        for (int i = 0; i < numberOfTopPerformers; i++) {
            topPerformers.add(aiPlayers.get(i));
        }
        return topPerformers;
    }

    public List<AIPlayer> crossover(List<AIPlayer> topPlayers) {
        Random rng = new Random();
        List<AIPlayer> children = new ArrayList<>(topPlayers);
        for (int i = topPlayers.size(); i < POPULATION_SIZE; i++) {
            AIPlayer parent1 = topPlayers.get(rng.nextInt(topPlayers.size()));
            AIPlayer parent2 = topPlayers.get(rng.nextInt(topPlayers.size()));
            AIPlayer child = new MiniMaxAlphaBetaPlayer("player" + playerCounter++);
            child.setEvaluation(new Evaluation(0, 0, 0));
            setCenterValue(parent1, parent2, child);
            setTwoInARow(parent1, parent2, child);
            setThreeInARow(parent1, parent2, child);
            children.add(child);
        }
        return children;
    }

    private static void setTwoInARow(AIPlayer parent1, AIPlayer parent2, AIPlayer child) {
        if (Math.random() < CROSSOVER_PARENT_SELECTION) {
            child.getEval().setTwoInARow(parent1.getEval().getTwoInARow());
        } else {
            child.getEval().setTwoInARow(parent2.getEval().getTwoInARow());
        }
    }

    private static void setThreeInARow(AIPlayer parent1, AIPlayer parent2, AIPlayer child) {
        if (Math.random() < CROSSOVER_PARENT_SELECTION) {
            child.getEval().setThreeInARow(parent1.getEval().getThreeInARow());
        } else {
            child.getEval().setThreeInARow(parent2.getEval().getThreeInARow());
        }
    }

    private static void setCenterValue(AIPlayer parent1, AIPlayer parent2, AIPlayer child) {
        if (Math.random() < CROSSOVER_PARENT_SELECTION) {
            child.getEval().setCenterValue(parent1.getEval().getCenterValue());
        } else {
            child.getEval().setCenterValue(parent2.getEval().getCenterValue());
        }
    }

    private void mutate(List<AIPlayer> children) {
        for (AIPlayer child :
                children) {
            mutateCenterValue(child);
            mutateTwoInARow(child);
            mutateThreeInARow(child);
        }
    }

    private void mutateThreeInARow(AIPlayer child) {
        if (Math.random() < MUTATION_ODDS) {
            int newThreeInARow;
            if (Math.random() < UP_OR_DOWN) {
                newThreeInARow = (int) (child.getEval().getThreeInARow() - (MUTATION_FACTOR * Math.random()));
            } else {
                newThreeInARow = (int) (child.getEval().getThreeInARow() + (MUTATION_FACTOR * Math.random()));
            }
            child.getEval().setThreeInARow(newThreeInARow);
        }
    }

    private void mutateTwoInARow(AIPlayer child) {
        if (Math.random() < MUTATION_ODDS) {
            int newTwoInARow;
            if (Math.random() < UP_OR_DOWN) {
                newTwoInARow = (int) (child.getEval().getTwoInARow() - (MUTATION_FACTOR * Math.random()));
            } else {
                newTwoInARow = (int) (child.getEval().getTwoInARow() + (MUTATION_FACTOR * Math.random()));
            }
            child.getEval().setTwoInARow(newTwoInARow);
        }
    }

    private void mutateCenterValue(AIPlayer child) {
        if (Math.random() < MUTATION_ODDS) {
            int newCenterValue;
            if (Math.random() < UP_OR_DOWN) {
                newCenterValue = (int) (child.getEval().getCenterValue() - (MUTATION_FACTOR * Math.random()));
            } else {
                newCenterValue = (int) (child.getEval().getCenterValue() + (MUTATION_FACTOR * Math.random()));
            }
            child.getEval().setCenterValue(newCenterValue);
        }
    }
}
