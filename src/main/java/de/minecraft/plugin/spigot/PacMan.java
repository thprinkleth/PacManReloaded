package de.minecraft.plugin.spigot;

import de.minecraft.plugin.spigot.cmds.CmdRanking;
import de.minecraft.plugin.spigot.cmds.CmdSetup;
import de.minecraft.plugin.spigot.cmds.CmdStart;
import de.minecraft.plugin.spigot.cmds.CmdStats;
import de.minecraft.plugin.spigot.gamestate.GameState;
import de.minecraft.plugin.spigot.gamestate.GameStateManager;
import de.minecraft.plugin.spigot.listeners.*;
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

    private static PacMan instance;
    private FileManager messageFile;
    private FileManager locationFile;
    private RoleHandler roleHandler;
    private GameStateManager gameStateManager;
    private PickupableItemStacks pickupableItemStacks;
    private PowerUpHandler powerUpHandler;
    private ScoreHandler scoreeHandler;

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
        scoreeHandler = new ScoreHandler();
        powerUpHandler = new PowerUpHandler();

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

            messageFile.getFileConfig().set("Items.Setup.Name", "&bSetupItem");
            messageFile.getFileConfig().set("Items.Setup.Lore", "&7Rechtsklick auf einen Block um das Location-Inventar zu öffnen.");
            messageFile.getFileConfig().set("Items.SetupPoint.Name", "&bSetupPoint");
            messageFile.getFileConfig().set("Items.SetupPoint.Lore", "&7Rechtsklick auf einen Block, um einen Point zu setzen.");

            messageFile.getFileConfig().set("Game.Amount.Locations.Points", "0");
            messageFile.getFileConfig().set("Game.Amount.Locations.PowerUps", "0");

            messageFile.getFileConfig().set("Game.PlayersNeededToStart", "5");

            messageFile.getFileConfig().set("Setup.Spawn.Set.Lobby.Success", "&aDu hast den Spawnpunkt für die Lobby erfolgreich gesetzt.");
            messageFile.getFileConfig().set("Setup.Spawn.Set.Ghosts.Success", "&aDu hast den Spawnpunkt für die Geister erfolgreich gesetzt.");
            messageFile.getFileConfig().set("Setup.Spawn.Set.PacMan.Success", "&aDu hast den Spawnpunkt PacMan erfolgreich gesetzt.");
            messageFile.getFileConfig().set("Setup.Spawn.Set.Point.Success", "&aDu hast die {Number}te Position für einen Punkt erfolgreich gesetzt.");
            messageFile.getFileConfig().set("Setup.Spawn.Set.PowerUp.Success", "&aDu hast {Number}te Position für ein Powerup erfolgreich gesetzt.");
            messageFile.getFileConfig().set("Setup.Spawn.Set.Point.NotSuccess", "&cDie Location konnte nicht gesetzt werden.");

            messageFile.getFileConfig().set("Lobby.Countdown.Counting", "&aDas Spiel startet in {Number} Sekunde(n).");
            messageFile.getFileConfig().set("Lobby.Countdown.NotEnoughPlayers", "&cEs müssen &6{PlayersNeededToStart} Spieler &cin der Lobby sein, um das Spiel zu starten.");

            messageFile.getFileConfig().set("Inventory.SetupInventory.Items.LobbySpawn.Name", "&6Lobby-spawn");
            messageFile.getFileConfig().set("Inventory.SetupInventory.Items.LobbySpawn.Lore", "&7Setze die Position, wo die Spieler vor und nach dem Spiel spawnen werden.");
            messageFile.getFileConfig().set("Inventory.SetupInventory.Items.GhostSpawn.Name", "&6Ghost-spawn");
            messageFile.getFileConfig().set("Inventory.SetupInventory.Items.GhostSpawn.Lore", "&7Setze die Position, wo die Geister spawnen werden.");
            messageFile.getFileConfig().set("Inventory.SetupInventory.Items.PacManSpawn.Name", "&6PacMan-spawn");
            messageFile.getFileConfig().set("Inventory.SetupInventory.Items.PacManSpawn.Lore", "&7Setze die Position, wo PacMan spawnen soll.");
            messageFile.getFileConfig().set("Inventory.SetupInventory.Items.PowerUpSpawn.Name", "&6PowerUp-spawn");
            messageFile.getFileConfig().set("Inventory.SetupInventory.Items.PowerUpSpawn.Lore", "&7Setze die Position, wo die PowerUps spawnen sollen.");
            messageFile.getFileConfig().set("Inventory.SetupInventory.Items.PointSpawn.Name", "&6Point-spawn");
            messageFile.getFileConfig().set("Inventory.SetupInventory.Items.PointSpawn.Lore", "&7Setze die Position, wo die Points spawnen sollen.");

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

        ItemStack lobbySpawnItemStack = new ItemBuilder(Material.COMPASS)
                .setName(getMessageFile().getValue("Inventory.SetupInventory.Items.LobbySpawn.Name").toString())
                .addLoreLine(getMessageFile().getValue("Inventory.SetupInventory.Items.LobbySpawn.Lore").toString()).toItemStack();
        setupInventory.setItem(2, lobbySpawnItemStack);

        ItemStack ghostSpawnItemStack = new ItemBuilder(Material.TOTEM)
                .setName(getMessageFile().getValue("Inventory.SetupInventory.Items.GhostSpawn.Name").toString())
                .addLoreLine(getMessageFile().getValue("Inventory.SetupInventory.Items.GhostSpawn.Lore").toString()).toItemStack();
        setupInventory.setItem(4, ghostSpawnItemStack);

        ItemStack pacManSpawnItemStack = new ItemBuilder(Material.GOLD_BLOCK)
                .setName(getMessageFile().getValue("Inventory.SetupInventory.Items.PacManSpawn.Name").toString())
                .addLoreLine(getMessageFile().getValue("Inventory.SetupInventory.Items.PacManSpawn.Lore").toString()).toItemStack();
        setupInventory.setItem(6, pacManSpawnItemStack);

        ItemStack powerUpSpawnItemStack = new ItemBuilder(Material.IRON_NUGGET)
                .setName(getMessageFile().getValue("Inventory.SetupInventory.Items.PowerUpSpawn.Name").toString())
                .addLoreLine(getMessageFile().getValue("Inventory.SetupInventory.Items.PowerUpSpawn.Lore").toString()).toItemStack();
        setupInventory.setItem(12, powerUpSpawnItemStack);

        ItemStack pointSpawnItemStack = new ItemBuilder(Material.GOLD_NUGGET)
                .setName(getMessageFile().getValue("Inventory.SetupInventory.Items.PointSpawn.Name").toString())
                .addLoreLine(getMessageFile().getValue("Inventory.SetupInventory.Items.PointSpawn.Lore").toString()).toItemStack();
        setupInventory.setItem(14, pointSpawnItemStack);

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

    public MySQL getMySQL() {
        return mySQL;
    }

    public PowerUpHandler getPowerUpHandler() {
        return powerUpHandler;
    }

    public ScoreHandler getScoreeHandler() {
        return scoreeHandler;
    }
}

