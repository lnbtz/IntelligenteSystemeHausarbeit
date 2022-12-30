package game.player;

import game.GameState;

import java.awt.*;

public class HumanGUIPlayer implements Player {
    private String name;
    private Color color;

    public HumanGUIPlayer(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Diese Methode wird nie aufgerufen, da das Game direkt bei der Visualisierung die angeklickte Zeile erfragt,
     * wenn es sich beim aktuellen Spieler um einen <code>HumanGUIPlayer</code> handelt.
     */
    @Override
    public int nextColumn(GameState gameState) {
        return 42;
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
