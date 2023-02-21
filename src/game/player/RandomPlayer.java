package game.player;

import game.Game;
import game.GameState;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomPlayer implements Player {
    private final String name;
    private final Color color;

    public RandomPlayer(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    Random random = new Random();

    /**
     * Chooses a random column that's not already full (assuming there is at least one column that's not full).
     */
    @Override
    public int nextColumn(GameState gameState) {
        List<Integer> randomColumns = IntStream.range(0, Game.NUM_OF_COLUMNS).boxed().collect(Collectors.toList());
        Collections.shuffle(randomColumns);
        for (Integer randomColumn : randomColumns) {
            if(gameState.getBoardCopy()[0][randomColumn] == null)
                return randomColumn;
        }
        // error: board is full
        return -1;
    }

    @Override
    public void setCurrentMaxDepth(int currentMaxDepth) {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Color getColor() {
        return color;
    }
}
