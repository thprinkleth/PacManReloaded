package de.minecraft.plugin.spigot;

import de.minecraft.plugin.spigot.cmds.CmdSetSpawn;
import de.minecraft.plugin.spigot.listeners.JoinListener;
import de.minecraft.plugin.spigot.util.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class PacMan extends JavaPlugin {

    private static PacMan instance;
    private FileManager messageFile;
    private FileManager locationFile;

    private int cooldownScheduler = 0;

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

            messageFile.getFileConfig().set("World.Join", "&aDer Spieler &7{PlayerName} &aist dem Server beigetreten. &7({ServerPlayers})");

            try {
                messageFile.getFileConfig().save(messageFile.getFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Activates the lobby countdown to start the game
     * When 5 Players or more are in the lobby, the countdown starts
     */
    public void countdown() {

        int onlinePlayersAmount = Bukkit.getServer().getOnlinePlayers().size();

        cooldownScheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {

            int countdown = 60 * 5;
            int pastCountdown = 0;

            @Override
            public void run() {
                if (onlinePlayersAmount >= 5) {
                    if (pastCountdown % 60 == 0 || pastCountdown == countdown - 30 || pastCountdown == countdown - 20 || pastCountdown == countdown - 15 || pastCountdown == countdown - 10 ||
                            pastCountdown == countdown - 5 || pastCountdown == countdown - 4 || pastCountdown == countdown - 3 || pastCountdown == countdown - 2 || pastCountdown == countdown - 1) {

                        // Sends every person on the server the message at every minute, 30 seconds, 20 seconds, 15 seconds, 10 seconds, 5 seconds, 4 seconds, 3 seconds, 2 seconds and 1 seconds

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage((String) getMessageFile().getValue("Lobby.Cooldown.Counting", player, pastCountdown / 60));
                        }
                    }
                    pastCountdown++;
                } else if (pastCountdown == countdown) {
                    Bukkit.getScheduler().cancelTask(cooldownScheduler);
                } else {
                    pastCountdown = 0;
                }
            }
        }, 0, 20);
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

    public int getCooldownScheduler() {
        return cooldownScheduler;
    }
}
