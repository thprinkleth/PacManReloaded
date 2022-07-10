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
     * Setzt die initiale Datei auf
     *
     * @param name
     */
    public FileManager(String name) {

        folder = new File("plugins/PacMan");

        // Creates the folder in where the file will be saved
        if (!folder.exists()) {
            folder.mkdir();
        }

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
     * Weist einem Pfad in der Datei ein Wert zu
     *
     * @param path
     * @param value
     */
    public void setValue(String path, Object value) {
        fileConfig.set(path, value);
        saveFile();
    }

    /**
     * Weist einem Pfad in der Datei ein Wert zu und ersetzt den Farbcode und den Prefix
     *
     * @param path
     * @return
     */
    public String getValue(String path) {
        String string = fileConfig.get(path).toString();

        string = string.replace('&', '§');
        string = string.replace("{Prefix}", fileConfig.get("Server.Prefix").toString().replace('&', '§'));

        return string;
    }

    /**
     * Weist einem Pfad in der Datei ein Wert zu und ersetzt den Farbcode, den Prefix und spielerspezifische Attribute
     *
     * @param path
     * @param player
     * @return
     */
    public String getValue(String path, Player player) {

        String string = getValue(path);

        string = string.replace("{XValue}", String.valueOf(player.getLocation().getX()));
        string = string.replace("{YValue}", String.valueOf(player.getLocation().getY()));
        string = string.replace("{ZValue}", String.valueOf(player.getLocation().getZ()));
        string = string.replace("{YawValue}", String.valueOf(player.getLocation().getYaw()));
        string = string.replace("{PitchValue}", String.valueOf(player.getLocation().getPitch()));
        string = string.replace("{PitchValue}", String.valueOf(player.getLocation().getPitch()));
        string = string.replace("{WorldValue}", String.valueOf(player.getLocation().getWorld().getName()));
        string = string.replace("{ServerPlayers}", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));
        string = string.replace("{PlayersNeededToStart}", String.valueOf(getValue("Game.PlayersNeededToStart")));
        string = string.replace("{PlayerName}", String.valueOf(player.getDisplayName()));

        return string;
    }

    /**
     * Weist einem Pfad in der Datei ein Wert zu und ersetzt den Farbcode, den Prefix, spielerspezifische Attribute und eine Zahl
     *
     * @param path
     * @param player
     * @return
     */
    public String getValue(String path, Player player, int numberToReplace) {

        String string = getValue(path, player);

        string = string.replace("{Number}", String.valueOf(numberToReplace));

        return string;
    }

    public int getIntValue(String path) {
        return fileConfig.getInt(path);
    }

    /**
     * Speichert eine Position in einem Pfad in der Datei
     *
     * @param path
     * @param location
     */
    public void setLocation(String path, Location location) {

        fileConfig.set(path + ".x", location.getX());
        fileConfig.set(path + ".y", location.getY());
        fileConfig.set(path + ".z", location.getZ());
        fileConfig.set(path + ".yaw", Double.valueOf(location.getYaw()));
        fileConfig.set(path + ".pitch", Double.valueOf(location.getPitch()));
        fileConfig.set(path + ".world", location.getWorld().getName());

        saveFile();
    }

    /**
     * Returns the location in the path in the file with y += 1
     * Gibt die Position von einem Pfad in der Datei zurück mit Verschiebung in x- und z-Richtung in 0.5 und 1 in y-Richtung
     *
     * @param path
     * @return
     */
    public Location getSpawn(String path) {

        double x = (double) fileConfig.get(path + ".x") + 0.5;
        double y = (double) fileConfig.get(path + ".y") + 1;
        double z = (double) fileConfig.get(path + ".z") + 0.5;
        double yaw = (double) fileConfig.get(path + ".yaw");
        double pitch = (double) fileConfig.get(path + ".pitch");
        String world = (String) fileConfig.get(path + ".world");

        return new Location(Bukkit.getWorld(world), x, y, z, Float.valueOf(String.valueOf(yaw)), Float.valueOf(String.valueOf(pitch)));
    }

    /**
     * Gibt die POsition von einem Pfad in der Datei zurück
     *
     * @param path
     * @return
     */
    public Location getLocation(String path) {

        double x = (double) fileConfig.get(path + ".x");
        double y = (double) fileConfig.get(path + ".y");
        double z = (double) fileConfig.get(path + ".z");
        double yaw = (double) fileConfig.get(path + ".yaw");
        double pitch = (double) fileConfig.get(path + ".pitch");
        String world = (String) fileConfig.get(path + ".world");

        return new Location(Bukkit.getWorld(world), x, y, z, Float.valueOf(String.valueOf(yaw)), Float.valueOf(String.valueOf(pitch)));
    }

    /**
     * Speichert die Datei
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
}
