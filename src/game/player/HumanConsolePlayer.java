package game.player;

import game.Game;
import game.GameState;

import java.awt.*;
import java.util.Scanner;

public class HumanConsolePlayer implements Player {
    private static final Scanner SCANNER = new Scanner(System.in);
    private String name;
    private Color color;

    public HumanConsolePlayer(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public int nextColumn(GameState gameState) {
        System.out.print("Choose a column: ");
        int column = SCANNER.nextInt();

        while (column < 0 || column >= Game.NUM_OF_COLUMNS || gameState.getBoardCopy()[0][column] != null) {
            System.out.println("Not a valid column.");
            System.out.print("Choose a column: ");
            column = SCANNER.nextInt();
        }

        return column;
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
