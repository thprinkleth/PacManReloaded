package de.minecraft.plugin.spigot;

import de.minecraft.plugin.spigot.cmds.CmdSetSpawn;
import de.minecraft.plugin.spigot.listeners.JoinListener;
import de.minecraft.plugin.spigot.util.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class PacMan extends JavaPlugin {

    private static PacMan instance;
    private FileManager messageFile;
    private FileManager locationFile;

    /**
     * Is executed when starting the server, after the onLoad() function
     */
    @Override
    public void onEnable() {

        instance = this;
        messageFile = new FileManager("messages.yml");
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

        messageFile.getFileConfig().options().copyDefaults(true);
        messageFile.getFileConfig().options().header("All messages contained in the plugin.");

        if (messageFile.getFileConfig().get("Server.StartUp.Message") == null) {

            messageFile.getFileConfig().set("Server.StartUp.Message", "&aDas Plugin wurde gestartet. &7[PacMan]");
            messageFile.getFileConfig().set("Server.ShutDown.Message", "&cDas Plugin wurde gestoppt. &7[PacMan]");

            messageFile.getFileConfig().set("Commands.NoPlayer", "&cDer Befehl kann nur von einem Spieler ausgefuehrt werden.");
            messageFile.getFileConfig().set("Commands.SetSpawn.Syntax", "&cSyntax: &7/setSpawn");
            messageFile.getFileConfig().set("Commands.SetSpawn.Success", "&aDu hast den Spawnpunkt erfolgreich gesetzt. &7X: {XValue}, Y: {YValue}, Z: {ZValue}");

            messageFile.getFileConfig().set("Commands.NoPerm", "&cDu hast keine Rechte diesen Befehl auszufuehren.");

            messageFile.getFileConfig().set("World.Join", "&aDer Spieler &7{PlayerName} &aist dem Server beigetreten. &7Spieler: {ServerPlayers}");

            try {
                messageFile.getFileConfig().save(messageFile.getFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
}
