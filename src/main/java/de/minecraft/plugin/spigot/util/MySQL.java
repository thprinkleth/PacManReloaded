package de.minecraft.plugin.spigot.util;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Bukkit;

import java.sql.*;

public class MySQL {

    private static PacMan instance = PacMan.getInstance();

    private static Connection connection;
    private final String USERNAME;
    private final int PORT;
    private final String DATABASE;
    private final String HOST;
    private final String PASSWORD;

    public MySQL(String username, int port, String database, String host, String password) {
        this.USERNAME = username;
        this.PORT = port;
        this.DATABASE = database;
        this.HOST = host;
        this.PASSWORD = password;;
    }

    private static boolean isConnected() {
        return (connection != null);
    }

    public void connect() {

        if (isConnected()) {
            return;
        }

        try {
            Bukkit.getConsoleSender().sendMessage((String) instance.getMessageFile().getValue("Console.MySQL.Connect.Wait"));
            connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?autoReconnect=true", USERNAME, PASSWORD);
            Bukkit.getConsoleSender().sendMessage((String) instance.getMessageFile().getValue("Console.MySQL.Connect.Success"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static void disconnect() {

        if (!isConnected()) {
            return;
        }

        try {
            connection.close();
            Bukkit.getConsoleSender().sendMessage((String) instance.getMessageFile().getValue("MySQL.Disconnect.Success"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static void update(String query) {

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void createTable() {
        update("CREATE TABLE IF NOT EXISTS `PacMan` (`uid` VARCHAR(64), `uuid` VARCHAR(64), `score` INT(100), `playedGames` INT(100), `winsPacMan` INT(100), `losesPacMan` INT(100), `winsGhost` INT(100), `losesGhost` INT(100), `pacManEaten` INT(100)");
    }

    public static ResultSet query(String query) {

        ResultSet resultSet = null;

        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return resultSet;
    }

    public static boolean exists(String uuid) {

        ResultSet resultSet = query("SELECT * FROM `PacMan` WHERE `uuid` = '" + uuid + "';");

        try {
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public static void addScore(String uuid, int achievedScore) {
        update("UPDATE `PacMan` SET `score` = '" + (getScore(uuid) + achievedScore) + "' WHERE `uuid` = '" + uuid + "';");
    }

    public static int getScore(String uuid) {

        try {
            ResultSet resultSet = query("SELECT `score` FROM `PacMan` WHERE `uuid` = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("score");
            }
        } catch (SQLException ex) {}

        return 0;
    }


    public static void addGame(String uuid) {

        if (exists(uuid)) {
            update("UPDATE 'PacMan' SET `playedGames` = '" + (getGames(uuid) + 1) + "' WHERE `uuid` = '" + uuid + "';");
        } else {
            update("INSERT INTO `PacMan` (`uid`, `uuid`, `score`, `playedGames`, `winsPacMan`, `losesPacMan`, `winsGhost`, `losesGhost`, `pacManEaten`) VALUES ('" + Bukkit.getServer().getPlayer(uuid).getName() + "', '" + uuid + "', '0', '1', '0', '0', '0', '0', '0');");
        }
    }

    public static int getGames(String uuid) {

        try {
            ResultSet resultSet = query("SELECT `playedGames` FROM `PacMan` WHERE `uuid` = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("gamesPlayed");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    public static void addWinsPacMan(String uuid) {
        update("UPDATE `PacMan` SET `winsPacMan` = '" + (getWinsPacMan(uuid) + 1) + "' WHERE `uuid` = '" + uuid + "';");
    }

    public static int getWinsPacMan(String uuid) {

        try {
            ResultSet resultSet = query("SELECT `winsPacMan` FROM `PacMan` WHERE `uuid` = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("winsPacMan");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    public static void addLosesPacMan(String uuid) {
        update("UPDATE `PacMan` SET `losesPacMan` = '" + (getLosesPacMan(uuid) + 1) + "' WHERE `uuid` = '" + uuid + "';");
    }

    public static int getLosesPacMan(String uuid) {

        try {
            ResultSet resultSet = query("SELECT `losesPacMan` FROM `PacMan` WHERE `uuid` = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("losesPacMan");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    public static void addWinsGhost(String uuid) {
        update("UPDATE `PacMan` SET `winsGhost` = '" + (getLosesPacMan(uuid) + 1) + "' WHERE `uuid` = '" + uuid + "';");
    }

    public static int getWinsGhost(String uuid) {

        try {
            ResultSet resultSet = query("SELECT `winsGhost` FROM `PacMan` WHERE `uuid` = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("winsGhost");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    public static void addLosesGhost(String uuid) {
        update("UPDATE `PacMan` SET `losesGhost` = '" + (getLosesPacMan(uuid) + 1) + "' WHERE `uuid` = '" + uuid + "';");
    }

    public static int getLosesGhost(String uuid) {

        try {
            ResultSet resultSet = query("SELECT `losesGhost` FROM `PacMan` WHERE `uuid` = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("losesGhost");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    public static void addPacmanEaten(String uuid) {
        update("UPDATE `PacMan` SET `pacManEaten` = '" + (getLosesPacMan(uuid) + 1) + "' WHERE `uuid` = '" + uuid + "';");
    }

    public static int getPacmanEaten(String uuid) {

        try {
            ResultSet resultSet = query("SELECT `pacManEaten` FROM `PacMan` WHERE `uuid` = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("pacManEaten");
            }
        } catch (SQLException ex) {}

        return 0;
    }
}
