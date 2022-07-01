package de.minecraft.plugin.spigot.gamestate;

public class GameStateManager {
    private GameState[] gameStates;
    private GameState current;

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
        current =  gameStates[id];
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
}
