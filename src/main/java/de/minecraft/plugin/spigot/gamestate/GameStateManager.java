package de.minecraft.plugin.spigot.gamestate;

import de.minecraft.plugin.spigot.PacMan;

public class GameStateManager {
    private GameState[] gameStates;
    private GameState current;
    private PacMan instance = PacMan.getInstance();

    public GameStateManager(){
        gameStates = new GameState[3];
        gameStates[GameState.LOBBY_STATE] = new LobbyState();
        gameStates[GameState.INGAME_STATE] = new IngameState();
        gameStates[GameState.END_STATE] = new EndState();
    }

    public void setCurrent(int id){
        if(current != null){
            current.stop();
        }
        current = gameStates[id];
        current.start();
    }

    public void stopCurrent(){
        if (current != null){
            current.stop();
            current = null;
        }
    }

    public GameState getCurrent() {
        return current;
    }

    public boolean canGameStart() {
        /**
         * Looks if every position needed is there
         * - Lobbyspawn
         * - Pacmanspawn
         * - Ghostspawn
         * - PointSpawns >= 1
         * - PowerUps optional?
         */
        if (instance.getLocationFile().getLocation("Game.Location.Lobby") == null) {
            return false;
        }
        if (instance.getLocationFile().getLocation("Game.Location.Ghost") == null) {
            return false;
        }
        if (instance.getLocationFile().getLocation("Game.Location.PacMan") == null) {
            return false;
        }
        if ((int) instance.getMessageFile().getValue("Game.Amount.Locations.Points") == 0) {
            return false;
        }
        /*
        if ((int) instance.getMessageFile().getValue("Game.Amount.Locations.PowerUps") == 0) {
            return false;
        }
         */

        return true;
    }
}
