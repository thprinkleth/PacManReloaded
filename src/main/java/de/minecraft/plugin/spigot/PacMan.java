package de.minecraft.plugin.spigot;

import de.minecraft.plugin.spigot.cmds.CmdLeaderboard;
import de.minecraft.plugin.spigot.cmds.CmdSetup;
import de.minecraft.plugin.spigot.cmds.CmdStart;
import de.minecraft.plugin.spigot.cmds.CmdStats;
import de.minecraft.plugin.spigot.gamestate.GameState;
import de.minecraft.plugin.spigot.gamestate.GameStateManager;
import de.minecraft.plugin.spigot.listeners.*;
import de.minecraft.plugin.spigot.minimap.CoinDotHandler;
import de.minecraft.plugin.spigot.minimap.PowerUpDotHandler;
import de.minecraft.plugin.spigot.powerup.PickupableItemStacks;
import de.minecraft.plugin.spigot.powerup.PowerUpHandler;
import de.minecraft.plugin.spigot.role.RoleHandler;
import de.minecraft.plugin.spigot.score.ScoreHandler;
import de.minecraft.plugin.spigot.util.FileManager;
import de.minecraft.plugin.spigot.util.ItemBuilder;
import de.minecraft.plugin.spigot.util.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;

public class PacMan extends JavaPlugin {

    /**
     * TODO:
     * - GameState start/stop überlegen, wo was reinkommt
     * - MySQL Unterstützung
     * - Stats command
     * - Ranking command
     * - Bossbar (Derzeitiges Powerup + Ablaufzeit des PowerUps)
     * - Titel/Subtitel bei gewinnen/verlieren
     * - Effekte bei Spielende
     */

    private static PacMan instance;

    private FileManager messageFile;
    private FileManager locationFile;
    private FileManager configFile;
    private FileManager mySqlFile;

    private RoleHandler roleHandler;
    private GameStateManager gameStateManager;
    private PickupableItemStacks pickupableItemStacks;
    private PowerUpHandler powerUpHandler;
    private ScoreHandler scoreHandler;

    private CoinDotHandler coinDotHandler;
    private PowerUpDotHandler powerUpDotHandler;

    private ArrayList <Player> playerList;

    private Inventory setupInventory;
    private Inventory leaderboardInventory;
    private Inventory statsInventory;

    private MySQL mySQL;



    /**
     * Is executed when starting the server
     */
    @Override
    public void onEnable() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer("");
        }

        instance = this;

        messageFile = new FileManager("messages.yml");
        locationFile = new FileManager("locations.yml");
        configFile = new FileManager("config.yml");
        mySqlFile = new FileManager("MySQL.yml");

        defaultMessage();

        mySQL = new MySQL();


        roleHandler = new RoleHandler();
        playerList = new ArrayList<>();

        gameStateManager = new GameStateManager();
        pickupableItemStacks = new PickupableItemStacks();
        scoreHandler = new ScoreHandler();
        powerUpHandler = new PowerUpHandler();
        coinDotHandler = new CoinDotHandler();
        powerUpDotHandler = new PowerUpDotHandler();

        gameStateManager.setCurrent(GameState.PREGAME_STATE);

        initInventories();


        registerCommands();
        registerListeners();

        gameStateManager.setCurrent(GameState.PREGAME_STATE);

        Bukkit.getConsoleSender().sendMessage(messageFile.getValue("Server.StartUp.Message"));
    }

    /**
     * Is executed when stopping the server
     */
    @Override
    public void onDisable() {

        Bukkit.getConsoleSender().sendMessage(messageFile.getValue("Server.ShutDown.Message"));

        mySQL.disconnect();
    }

    private void registerCommands() {

        getCommand("setup").setExecutor(new CmdSetup());
        getCommand("start").setExecutor(new CmdStart());
        getCommand("stats").setExecutor(new CmdStats());
        getCommand("ranking").setExecutor(new CmdLeaderboard());
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
        pluginManager.registerEvents(new PlayerMoveListener(), this);
        pluginManager.registerEvents(new ItemPickUpListener(), this);
    }

    /**
     * Setup of the initial message.yml file
     */
    private void defaultMessage() {

        messageFile.getFileConfig().options().copyDefaults(true);
        messageFile.getFileConfig().options().header("All strings contained in the plugin.");

        if (messageFile.getFileConfig().get("Server.StartUp.Message") == null) {

            messageFile.getFileConfig().set("Server.Prefix", "&bPacMan &7|");

            messageFile.getFileConfig().set("MySQL.Connect.Wait", "{Prefix} &6Verbinde mit MySQL-Datenbank...");
            messageFile.getFileConfig().set("MySQL.Connect.Success", "{Prefix} &aVerbindung mit MySQL-Datenbank erfolgreich hergestellt.");
            messageFile.getFileConfig().set("MySQL.Disconnect.Success", "{Prefix} &aVerbindung mit MySQL-Datenbank erfolgreich abgebrochen.");

            messageFile.getFileConfig().set("Server.StartUp.Message", "{Prefix} &aDas Plugin wurde gestartet.");
            messageFile.getFileConfig().set("Server.ShutDown.Message", "{Prefix} &cDas Plugin wurde gestoppt.");

            messageFile.getFileConfig().set("Commands.NoPlayer", "{Prefix} &cDer Befehl kann nur von einem Spieler ausgefuehrt werden.");

            messageFile.getFileConfig().set("Commands.Setup.Syntax", "{Prefix} &cSyntax: &6/setup");
            messageFile.getFileConfig().set("Commands.Start.Syntax", "{Prefix} &cSyntax: &6/start");
            messageFile.getFileConfig().set("Commands.Leaderboard.Syntax", "{Prefix} &cSyntax: &6/ranking");
            messageFile.getFileConfig().set("Commands.Stats.Syntax", "{Prefix} &cSyntax: &6/stats <Spielername>");

            messageFile.getFileConfig().set("Commands.NoPerm", "{Prefix} &cDu hast keine Rechte diesen Befehl auszufuehren.");

            messageFile.getFileConfig().set("World.Join", "{Prefix} &aDer Spieler &6{PlayerName} &aist dem Server beigetreten. &7({ServerPlayers})");
            messageFile.getFileConfig().set("World.Quit", "{Prefix} &cDer Spieler &6{PlayerName} &cist von dem Server gegangen. &7({Number})");

            messageFile.getFileConfig().set("Setup.Spawn.Set.Lobby.Success", "{Prefix} &aDu hast den Spawnpunkt für die Lobby erfolgreich gesetzt.");
            messageFile.getFileConfig().set("Setup.Spawn.Set.Lobby.NotSuccess", "{Prefix} &cDer Spawnpunkt konnte nicht gesetzt werden.");
            messageFile.getFileConfig().set("Setup.Spawn.Set.Ghosts.Success", "{Prefix} &aDu hast den Spawnpunkt für die Geister erfolgreich gesetzt.");
            messageFile.getFileConfig().set("Setup.Spawn.Set.Ghosts.NotSuccess", "{Prefix} &cDer Spawnpunkt konnte nicht gesetzt werden.");
            messageFile.getFileConfig().set("Setup.Spawn.Set.PacMan.Success", "{Prefix} &aDu hast den Spawnpunkt PacMan erfolgreich gesetzt.");
            messageFile.getFileConfig().set("Setup.Spawn.Set.PacMan.NotSuccess", "{Prefix} &cDer Spawnpunkt konnte nicht gesetzt werden.");
            messageFile.getFileConfig().set("Setup.Spawn.Set.Coin.Success", "{Prefix} &aDu hast die &6{Number}&ate Position für einen Coin erfolgreich gesetzt.");
            messageFile.getFileConfig().set("Setup.Spawn.Set.AutoCoin.Success", "{Prefix} &aEs wurde(n) &6{Number} Position(en) für einen Coin erfolgreich automatisch gespeichert.");
            messageFile.getFileConfig().set("Setup.Spawn.Set.Coin.NotSuccess", "{Prefix} &cDer Spawnpunkt konnte nicht gesetzt werden.");
            messageFile.getFileConfig().set("Setup.Spawn.Set.PowerUp.Success", "{Prefix} &aDu hast &6{Number}&ate Position für ein Powerup erfolgreich gesetzt.");
            messageFile.getFileConfig().set("Setup.Spawn.Set.PowerUp.NotSuccess", "{Prefix} &cDer Spawnpunkt konnte nicht gesetzt werden.");

            messageFile.getFileConfig().set("Setup.Spawn.Set.AutoCoin.FirstLocation", "{Prefix} &aEs wurde die &6erste Position &afür das automatische Berechnen aller Coin-Positionen gesetzt.");
            messageFile.getFileConfig().set("Setup.Spawn.Set.AutoCoin.SecondLocation", "{Prefix} &aEs wurde die &6zweite Position &afür das automatische Berechnen aller Coin-Positionen gesetzt. &6Berechne...");

            messageFile.getFileConfig().set("Lobby.Countdown.Counting", "{Prefix} &aDas Spiel startet in {Number} Sekunde(n).");
            messageFile.getFileConfig().set("Lobby.Countdown.Starting", "{Prefix} &aDas Spiel startet jetzt.");
            messageFile.getFileConfig().set("Lobby.Countdown.NotEnoughPlayers", "{Prefix} &cEs müssen &6{PlayersNeededToStart} Spieler &cin der Lobby sein, um das Spiel zu starten.");

            messageFile.getFileConfig().set("Game.NextLevel.PacMan.Title", "&aDu hast das nächste Level erreicht.");
            messageFile.getFileConfig().set("Game.NextLevel.PacMan.SubTitle", "&7Neues Level: &6{Number}");
            messageFile.getFileConfig().set("Game.NextLevel.Ghost.Title", "&cPacMan hat das nächste Level erreicht.");
            messageFile.getFileConfig().set("Game.NextLevel.Ghost.SubTitle", "&7Neues Level: &6{Number}");

            messageFile.getFileConfig().set("Game.Score.PacMan", "&aDein Score: &6{Number}");
            messageFile.getFileConfig().set("Game.Score.Ghost", "&bScore von PacMan: &6{Number}");

            configFile.getFileConfig().set("Inventory.SetupInventory.Name", "&aSetup-Inventar");
            configFile.getFileConfig().set("Inventory.LeaderboardInventory.Name", "&aRanking-Inventar");

            configFile.getFileConfig().set("Game.Amount.Locations.Coins", 0);
            configFile.getFileConfig().set("Game.Amount.Locations.PowerUps", 0);
            configFile.getFileConfig().set("Game.PlayersNeededToStart", 5);

            configFile.getFileConfig().set("Inventory.SetupInventory.Items.LobbySpawn.Name", "&bLobby-Spawnpunkt");
            configFile.getFileConfig().set("Inventory.SetupInventory.Items.LobbySpawn.Lore", "&7Setze die Position, wo die Spieler vor und nach dem Spiel erscheinen sollen.");
            configFile.getFileConfig().set("Inventory.SetupInventory.Items.GhostSpawn.Name", "&bGhost-Spawnpunkt");
            configFile.getFileConfig().set("Inventory.SetupInventory.Items.GhostSpawn.Lore", "&7Setze die Position, wo die Geister in dem Spiel erscheinen sollen.");
            configFile.getFileConfig().set("Inventory.SetupInventory.Items.PacManSpawn.Name", "&bPacMan-Spawnpunkt");
            configFile.getFileConfig().set("Inventory.SetupInventory.Items.PacManSpawn.Lore", "&7Setze die Position, wo PacMan in dem Spiel erscheinen soll.");
            configFile.getFileConfig().set("Inventory.SetupInventory.Items.PowerUpSpawn.Name", "&bPowerUp-Spawnpunkt");
            configFile.getFileConfig().set("Inventory.SetupInventory.Items.PowerUpSpawn.Lore", "&7Setze die Position, wo ein PowerUp liegen soll.");
            configFile.getFileConfig().set("Inventory.SetupInventory.Items.CoinSpawn.Name", "&bCoin-Spawnpunkt");
            configFile.getFileConfig().set("Inventory.SetupInventory.Items.CoinSpawn.Lore", "&7Setze die Position, wo ein Coin liegen soll.");

            configFile.getFileConfig().set("Items.Setup.Name", "&bSetupItem");
            configFile.getFileConfig().set("Items.Setup.Lore", "&7Rechtsklick auf einen Block um das Location-Inventar zu oöffnen.");
            configFile.getFileConfig().set("Items.SetupCoin.Name", "&bSetupCoins");
            configFile.getFileConfig().set("Items.SetupCoin.Lore", "&7Rechtsklick auf einen Block um einen Coin zu setzen.");

            mySqlFile.getFileConfig().set("Login.Username", "sql8505251");
            mySqlFile.getFileConfig().set("Login.Port", 3306);
            mySqlFile.getFileConfig().set("Login.Database", "sql8505251");
            mySqlFile.getFileConfig().set("Login.Host", "sql8.freemysqlhosting.net");
            mySqlFile.getFileConfig().set("Login.Password", "iKETWBjSd6");

            try {
                messageFile.getFileConfig().save(messageFile.getFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Makes the inventory for the setup-itemstack
    private void initInventories() {

        setupInventory = Bukkit.getServer().createInventory(null, 3 * 9, getConfigFile().getValue("Inventory.SetupInventory.Name"));

        ItemStack lobbySpawnItemStack = new ItemBuilder(Material.COMPASS)
                .setName(getConfigFile().getValue("Inventory.SetupInventory.Items.LobbySpawn.Name"))
                .addLoreLine(getConfigFile().getValue("Inventory.SetupInventory.Items.LobbySpawn.Lore")).toItemStack();
        setupInventory.setItem(2, lobbySpawnItemStack);

        ItemStack ghostSpawnItemStack = new ItemBuilder(Material.TOTEM)
                .setName(getConfigFile().getValue("Inventory.SetupInventory.Items.GhostSpawn.Name"))
                .addLoreLine(getConfigFile().getValue("Inventory.SetupInventory.Items.GhostSpawn.Lore")).toItemStack();
        setupInventory.setItem(4, ghostSpawnItemStack);

        ItemStack pacManSpawnItemStack = new ItemBuilder(Material.GOLD_BLOCK)
                .setName(getConfigFile().getValue("Inventory.SetupInventory.Items.PacManSpawn.Name"))
                .addLoreLine(getConfigFile().getValue("Inventory.SetupInventory.Items.PacManSpawn.Lore")).toItemStack();
        setupInventory.setItem(6, pacManSpawnItemStack);

        ItemStack powerUpSpawnItemStack = new ItemBuilder(Material.IRON_NUGGET)
                .setName(getConfigFile().getValue("Inventory.SetupInventory.Items.PowerUpSpawn.Name"))
                .addLoreLine(getConfigFile().getValue("Inventory.SetupInventory.Items.PowerUpSpawn.Lore")).toItemStack();
        setupInventory.setItem(12, powerUpSpawnItemStack);

        ItemStack coinSpawnItemStack = new ItemBuilder(Material.GOLD_NUGGET)
                .setName(getConfigFile().getValue("Inventory.SetupInventory.Items.CoinSpawn.Name"))
                .addLoreLine(getConfigFile().getValue("Inventory.SetupInventory.Items.CoinSpawn.Lore")).toItemStack();
        setupInventory.setItem(14, coinSpawnItemStack);

        leaderboardInventory = Bukkit.getServer().createInventory(null, 3 * 9, getConfigFile().getValue("Inventory.LeaderboardInventory.Name"));

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

    public void addToPlayerList(Player player) {
        playerList.add(player);
    }

    public void removeFromPlayerList(Player player) {
        playerList.remove(player);
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public PickupableItemStacks getPickupableItemStacks() {
        return pickupableItemStacks;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public PowerUpHandler getPowerUpHandler() {
        return powerUpHandler;
    }

    public ScoreHandler getScoreHandler() {
        return scoreHandler;
    }
    public CoinDotHandler getCoinDotHandler() {
        return coinDotHandler;
    }

    public PowerUpDotHandler getPowerUpDotHandler() {
        return powerUpDotHandler;
    }

    public FileManager getConfigFile() {
        return configFile;
    }

    public FileManager getMySqlFile() {
        return mySqlFile;
    }
}

