package de.minecraft.plugin.spigot;

import de.minecraft.plugin.spigot.cmds.CmdSetSpawn;
import de.minecraft.plugin.spigot.listeners.JoinListener;
import de.minecraft.plugin.spigot.util.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PacMan extends JavaPlugin {

    private PacMan instance;
    private FileManager messageFile;
    private FileManager locationFile;

    /**
     * Is executed when starting the server, after the onLoad() function
     */
    @Override
    public void onEnable() {

        instance = this;
        messageFile = new FileManager("message.yml");
        locationFile = new FileManager("locations.yml");

        defaultMessage();

        registerCommands();
        registerListeners();

        Bukkit.getConsoleSender().sendMessage((String) messageFile.getValue("Server.StartUp.Message"));
    }

    /**
     * Is executed when stopping the server
     */
    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage((String) messageFile.getValue("Server.ShutDown.Message"));
    }

    /**
     * Is executed while starting the server, before the onEnable() function
     */
    @Override
    public void onLoad() {
        Bukkit.getConsoleSender().sendMessage((String) messageFile.getValue("Server.LoadUp.Message"));
    }

    private void registerCommands() {
        getCommand("setSpawn").setExecutor(new CmdSetSpawn());
    }

    /**
     * Activates the Listeners
     */
    private void registerListeners() {

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new JoinListener(), instance);

    }

    /**
     * Setup of the initial message.yml file
     */
    private void defaultMessage() {
        messageFile.getFileConfig().addDefault("Server.StartUp.Message", "&aDer Server wurde gestartet. &7[PacMan]");
        messageFile.getFileConfig().addDefault("Server.LoadUp.Message", "&aDer Server wurde geladen. &7[PacMan]");
        messageFile.getFileConfig().addDefault("Server.ShutDown.Message", "&cDer Server wurde gestoppt. &7[PacMan]");

        messageFile.getFileConfig().addDefault("Commands.NoPlayer", "&cDer Befehl kann nur von einem Spieler ausgefuehrt werden.");
        messageFile.getFileConfig().addDefault("Commands.SetSpawn.Syntax", "&cSyntax: &7/setSpawn");
        messageFile.getFileConfig().addDefault("Commands.SetSpawn.Success", "&aDu hast den Spawnpunkt erfolgreich gesetzt. &7X: {XValue}, Y: {YValue}, Z: {ZValue}");

        messageFile.getFileConfig().addDefault("Commands.NoPerm", "&cDu hast keine Rechte diesen Befehl auszuführen.");

        messageFile.getFileConfig().addDefault("World.Join", "&aDer Spieler &7{PlayerName} &aist dem Server beigetreten. &7Spieler: {ServerPlayers}");
    }

    public PacMan getInstance() {
        return instance;
    }

    public FileManager getMessageFile() {
        return messageFile;
    }

    public FileManager getLocationFile() {
        return locationFile;
    }
}
