package de.minecraft.plugin.spigot.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class FileManager {

    public File folder;
    public File file;
    public YamlConfiguration fileConfig;

    /**
     * Setups the initial file
     * @param name
     */
    public FileManager(String name) {

        // Creates the folder in where the file will be saved
        if (!folder.exists())
            folder = new File("/PacMan/");

        folder.mkdir();

        // Creates the file and enables it to be modified
        file = new File(folder, name);
        fileConfig = YamlConfiguration.loadConfiguration(file);

        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets a value in the path in the file
     * @param path
     * @param value
     */
    public void setValue(String path, Object value) {
        fileConfig.set(path, value);
        saveFile();
    }

    /**
     * Returns the value in the path in the file and replaces color-codes
     * @param path
     * @return
     */
    public Object getValue(String path) {
        String string = (String) fileConfig.get(path);

        string = string.replace('&', '§');

        return string;
    }

    /**
     * Returns the value in the path in the file, replaces color-codes and Player-specific values
     * @param path
     * @param player
     * @return
     */
    public Object getValue(String path, Player player) {

        String string = (String) getValue(path);

        string = string.replace("{XValue}", String.valueOf(player.getLocation().getX()));
        string = string.replace("{YValue}", String.valueOf(player.getLocation().getY()));
        string = string.replace("{ZValue}", String.valueOf(player.getLocation().getZ()));
        string = string.replace("{YawValue}", String.valueOf(player.getLocation().getYaw()));
        string = string.replace("{PitchValue}", String.valueOf(player.getLocation().getPitch()));
        string = string.replace("{PitchValue}", String.valueOf(player.getLocation().getPitch()));
        string = string.replace("{WorldValue}", String.valueOf(player.getLocation().getWorld().getName()));
        string = string.replace("{ServerPlayers}", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));

        return string;
    }

    /**
     * Saves the location in the path in the file
     * @param path
     * @param location
     */
    public void setLocation(String path, Location location) {

        fileConfig.set(path + ".x", location.getX());
        fileConfig.set(path + ".y", location.getY());
        fileConfig.set(path + ".z", location.getZ());
        fileConfig.set(path + ".yaw", location.getYaw());
        fileConfig.set(path + ".pitch", location.getPitch());
        fileConfig.set(path + ".world", location.getWorld().getName());

        saveFile();
    }

    /**
     * Returns the location in the path in the file
     * @param path
     * @return
     */
    public Location getLocation(String path) {

        double x = (double) fileConfig.get(path + ".x");
        double y = (double) fileConfig.get(path + ".y");
        double z = (double) fileConfig.get(path + ".z");
        float yaw = (float) fileConfig.get(path + ".yaw");
        float pitch = (float) fileConfig.get(path + ".pitch");
        String world = (String) fileConfig.get(path + ".world");

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    /**
     * Saves the file
     */
    public void saveFile() {
        try {
            fileConfig.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getFileConfig() {
        return fileConfig;
    }

    public File getFolder() {
        return folder;
    }
}
