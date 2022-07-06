package de.minecraft.plugin.spigot;

import de.minecraft.plugin.spigot.cmds.CmdRanking;
import de.minecraft.plugin.spigot.cmds.CmdSetup;
import de.minecraft.plugin.spigot.cmds.CmdStart;
import de.minecraft.plugin.spigot.cmds.CmdStats;
import de.minecraft.plugin.spigot.gamestate.GameState;
import de.minecraft.plugin.spigot.gamestate.GameStateManager;
import de.minecraft.plugin.spigot.listeners.*;
import de.minecraft.plugin.spigot.powerup.PickupableItemStacks;
import de.minecraft.plugin.spigot.role.RoleHandler;
import de.minecraft.plugin.spigot.util.FileManager;
import de.minecraft.plugin.spigot.util.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;

public class PacMan extends JavaPlugin {

    private static PacMan instance;
    private FileManager messageFile;
    private FileManager locationFile;
    private RoleHandler roleHandler;
    private GameStateManager gameStateManager;
    private PickupableItemStacks pickupableItemStacks;

    private ArrayList <Player> playerList;

    private Inventory setupInventory;
    private Inventory rankingInventory;

    private MySQL mySQL;

    /**
     * Is executed when starting the server
     */
    @Override
    public void onEnable() {
        instance = this;
        messageFile = new FileManager("messages.yml");
        locationFile = new FileManager("locations.yml");
        defaultMessage();
        mySQL = new MySQL("", 3306, "", "", ""); // TODO: MySQL-Server
        gameStateManager = new GameStateManager();
        roleHandler = new RoleHandler();
        playerList = new ArrayList<>();
        initInventories();
        gameStateManager.setCurrent(GameState.LOBBY_STATE);
        pickupableItemStacks = new PickupableItemStacks();

        registerCommands();
        registerListeners();

        Bukkit.getConsoleSender().sendMessage(messageFile.getValue("Server.StartUp.Message").toString());
    }

    /**
     * Is executed when stopping the server
     */
    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(messageFile.getValue("Server.ShutDown.Message").toString());
    }

    private void registerCommands() {
        getCommand("setup").setExecutor(new CmdSetup());
        getCommand("start").setExecutor(new CmdStart());
        getCommand("stats").setExecutor(new CmdStats());
        getCommand("ranking").setExecutor(new CmdRanking());
    }

    /**
     * Activates the listeners
     */
    private void registerListeners() {

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new CancelledListeners(), this);
        pluginManager.registerEvents(new DamageListener(), this);
        pluginManager.registerEvents(new InteractListener(), this);
        pluginManager.registerEvents(new InventoryClickListener(), this);
        pluginManager.registerEvents(new PlayerLoginListener(), this);
        pluginManager.registerEvents(new QuitListener(), this);
    }

    /**
     * Setup of the initial message.yml file
     */
    private void defaultMessage() {

        messageFile.getFileConfig().options().copyDefaults(true);
        messageFile.getFileConfig().options().header("All strings contained in the plugin.");

        if (messageFile.getFileConfig().get("Server.StartUp.Message") == null) {

            messageFile.getFileConfig().set("Server.StartUp.Message", "&aDas Plugin wurde gestartet. &7[PacMan]");
            messageFile.getFileConfig().set("Server.ShutDown.Message", "&cDas Plugin wurde gestoppt. &7[PacMan]");

            messageFile.getFileConfig().set("Commands.NoPlayer", "&cDer Befehl kann nur von einem Spieler ausgefuehrt werden.");

            messageFile.getFileConfig().set("Commands.Setup.Syntax", "&cSyntax: &7/setup");
            messageFile.getFileConfig().set("Commands.Setup.ItemGiven", "&aDu hast das Item, mit dem du alle Positionen für das Spiel setzen kannst.");

            messageFile.getFileConfig().set("Commands.Start.Syntax", "&cSyntax: &7/start");

            messageFile.getFileConfig().set("Commands.Ranking.Syntax", "&cSyntax: &7/ranking");

            messageFile.getFileConfig().set("Commands.Stats.Syntax", "&cSyntax: &7/stats <Spielername>");

            messageFile.getFileConfig().set("Commands.NoPerm", "&cDu hast keine Rechte diesen Befehl auszufuehren.");

            messageFile.getFileConfig().set("World.Join", "&aDer Spieler &7{PlayerName} &aist dem Server beigetreten. &7({ServerPlayers})");
            messageFile.getFileConfig().set("World.Quit", "&cDer Spieler &7{PlayerName} &cist von dem Server gegangen. &7({ServerPlayers})");

            messageFile.getFileConfig().set("Inventory.SetupInventory.Name", "&aSetze die Position für irgentwas keine Ahnung");

            messageFile.getFileConfig().set("Items.Setup.Name", "&bLocationSetter");
            messageFile.getFileConfig().set("Items.Setup.Lore", "&7Rechtsklick auf einen Block um das Location-Inventar zu öffnen.");

            messageFile.getFileConfig().set("Game.Amount.Locations.Points", "0");
            messageFile.getFileConfig().set("Game.Amount.Locations.PowerUps", "0");

            messageFile.getFileConfig().set("Game.PlayersNeededToStart", "5");

            messageFile.getFileConfig().set("Setup.Spawn.Set.Lobby.Success", "&aDu hast den Spawnpunkt für die Lobby erfolgreich gesetzt. &7X: {XValue}, Y: {YValue}, Z: {ZValue}");
            messageFile.getFileConfig().set("Setup.Spawn.Set.Ghosts.Success", "&aDu hast den Spawnpunkt für die Geister erfolgreich gesetzt. &7X: {XValue}, Y: {YValue}, Z: {ZValue}");
            messageFile.getFileConfig().set("Setup.Spawn.Set.PacMan.Success", "&aDu hast den Spawnpunkt PacMan erfolgreich gesetzt. &7X: {XValue}, Y: {YValue}, Z: {ZValue}");
            messageFile.getFileConfig().set("Setup.Spawn.Set.Point.Success", "&aDu hast die {Number}te Position für einen Punkt erfolgreich gesetzt. &7X: {XValue}, Y: {YValue}, Z: {ZValue}");
            messageFile.getFileConfig().set("Setup.Spawn.Set.PowerUp.Success", "&aDu hast {Number}te Position für ein Powerup erfolgreich gesetzt. &7X: {XValue}, Y: {YValue}, Z: {ZValue}");

            messageFile.getFileConfig().set("Lobby.Countdown.Counting", "&aDas Spiel startet in {Number} Sekunde(n).");
            messageFile.getFileConfig().set("Lobby.Countdown.NotEnoughPlayers", "&cEs müssen &6{PlayersNeededToStart} Spieler &cin der Lobby sein, um das Spiel zu starten.");

            try {
                messageFile.getFileConfig().save(messageFile.getFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Makes the inventory for the setup-itemstack
    private void initInventories() {

        setupInventory = Bukkit.getServer().createInventory(null, 3 * 9, getMessageFile().getValue("Inventory.SetupInventory.Name").toString());

        /**
         * TODO:
         * - ItemStack for LobbySpawn
         * - ItemStack for GhostSpawn
         * - ItemStack for PacManSpawn
         * - ItemStack for PowerUpSpawn
         * - ItemStack for PointSpawn
         */

        rankingInventory = Bukkit.getServer().createInventory(null, 3 * 9, getMessageFile().getValue("Inventory.RankingInventory.Name").toString());

        updateRankingInventory();
    }

    public void updateRankingInventory() {
        /**
         * TODO:
         * - Get top 3 players
         * - Get their heads
         * - Get their stats
         * - Put into slots
         */
    }

    public static PacMan getInstance() {
        return instance;
    }

    public FileManager getMessageFile() {
        return messageFile;
    }

    public FileManager getLocationFile() {
        return locationFile;
    }

    public Inventory getSetupInventory() {
        return setupInventory;
    }

    public RoleHandler getRoleHandler() {
        return roleHandler;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public PickupableItemStacks getPickupableItemStacks() {
        return pickupableItemStacks;
    }
}

