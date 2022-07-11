package de.minecraft.plugin.spigot.gamestate;

import de.minecraft.plugin.spigot.PacMan;

public class GameStateManager {

    private final GameState[] GAME_STATES;
    private final PacMan INSTANCE = PacMan.getInstance();
    private GameState current;

    public GameStateManager() {
        GAME_STATES = new GameState[3];
        GAME_STATES[GameState.PREGAME_STATE] = new PreGameState();
        GAME_STATES[GameState.INGAME_STATE] = new IngameState();
        GAME_STATES[GameState.POSTGAME_STATE] = new PostGameState();
    }

    // Stoppt den derzeitigen Spielstatus
    public void stopCurrent() {
        if (current != null) {
            current.stop();
            current = null;
        }
    }

    // Übergibt den derzetigen Spielstatus
    public GameState getCurrent() {
        return current;
    }

    // Setzt den derzeitigen Spielstatus und startet ihn
    public void setCurrent(int id) {
        stopCurrent();
        current = GAME_STATES[id];
        current.start();
    }

    // Überprüft, ob alle notwendigen Positionen gesetzt wurden, um das Spiel zu starten
    public boolean canGameStart() {

        try {
            if (INSTANCE.getLocationFile().getLocation("Game.Location.Lobby") == null) {
                return false;
            }
            if (INSTANCE.getLocationFile().getLocation("Game.Location.Ghost") == null) {
                return false;
            }
            if (INSTANCE.getLocationFile().getLocation("Game.Location.PacMan") == null) {
                return false;
            }
            if (INSTANCE.getConfigFile().getIntValue("Game.Amount.Locations.Coins") == 0) {
                return false;
            }

            return INSTANCE.getConfigFile().getIntValue("Game.Amount.Locations.PowerUps") != 0;

        } catch (NullPointerException ex) {
            return false;
        }
    }
}
