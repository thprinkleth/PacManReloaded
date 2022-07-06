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

    // Opens a connection to the MySQL-Server
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

    // Disconnects from the MySQL-Server
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

    // Updates the database
    public static void update(String query) {

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Create new table in the MySQL-Database
    public static void createTable() {
        update("CREATE TABLE IF NOT EXISTS `PacMan` (`uid` VARCHAR(64), `uuid` VARCHAR(64), `score` INT(100), `playedGames` INT(100), `winsPacMan` INT(100), `losesPacMan` INT(100), `winsGhost` INT(100), `losesGhost` INT(100), `pacManEaten` INT(100)");
    }

    // Executed an query on the database
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

    // Checks if the player has already an entry in the database
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

    // Saves the new Highscore of the player in the database
    public static void addScore(String uuid, int achievedScore) {
        if (achievedScore < getScore(uuid)) {
            return;
        }
        update("UPDATE `PacMan` SET `score` = '" + achievedScore + "' WHERE `uuid` = '" + uuid + "';");
    }

    // Gets the current Highscore of the player
    public static int getScore(String uuid) {

        try {
            ResultSet resultSet = query("SELECT `score` FROM `PacMan` WHERE `uuid` = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("score");
            }
        } catch (SQLException ex) {}

        return 0;
    }


    // Adds one game to the players entry in the database
    public static void addGame(String uuid) {

        if (exists(uuid)) {
            update("UPDATE 'PacMan' SET `playedGames` = '" + (getGames(uuid) + 1) + "' WHERE `uuid` = '" + uuid + "';");
        } else {
            update("INSERT INTO `PacMan` (`uid`, `uuid`, `score`, `playedGames`, `winsPacMan`, `losesPacMan`, `winsGhost`, `losesGhost`, `pacManEaten`) VALUES ('" + Bukkit.getServer().getPlayer(uuid).getName() + "', '" + uuid + "', '0', '1', '0', '0', '0', '0', '0');");
        }
    }

    // Gets the played games of a player
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

    // Adds one add as pacman to the players entry in the database
    public static void addWinsPacMan(String uuid) {
        update("UPDATE `PacMan` SET `winsPacMan` = '" + (getWinsPacMan(uuid) + 1) + "' WHERE `uuid` = '" + uuid + "';");
    }

    // Gets the wins as pacman of a player
    public static int getWinsPacMan(String uuid) {

        try {
            ResultSet resultSet = query("SELECT `winsPacMan` FROM `PacMan` WHERE `uuid` = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("winsPacMan");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    // Adds one lose as pacman to the players entry in the database
    public static void addLosesPacMan(String uuid) {
        update("UPDATE `PacMan` SET `losesPacMan` = '" + (getLosesPacMan(uuid) + 1) + "' WHERE `uuid` = '" + uuid + "';");
    }

    // Gets the loses as pacman of a player
    public static int getLosesPacMan(String uuid) {

        try {
            ResultSet resultSet = query("SELECT `losesPacMan` FROM `PacMan` WHERE `uuid` = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("losesPacMan");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    // Adds one win as a ghost to the players entry in the database
    public static void addWinsGhost(String uuid) {
        update("UPDATE `PacMan` SET `winsGhost` = '" + (getLosesPacMan(uuid) + 1) + "' WHERE `uuid` = '" + uuid + "';");
    }

    // Gets the wins as a ghost of a player
    public static int getWinsGhost(String uuid) {

        try {
            ResultSet resultSet = query("SELECT `winsGhost` FROM `PacMan` WHERE `uuid` = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("winsGhost");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    // Adds one lose as a ghost to the players entry in the database
    public static void addLosesGhost(String uuid) {
        update("UPDATE `PacMan` SET `losesGhost` = '" + (getLosesPacMan(uuid) + 1) + "' WHERE `uuid` = '" + uuid + "';");
    }

    // Gets the loses as a ghost of a player
    public static int getLosesGhost(String uuid) {

        try {
            ResultSet resultSet = query("SELECT `losesGhost` FROM `PacMan` WHERE `uuid` = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("losesGhost");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    // Adds one point to the score of PacMans eaten by a ghost to the players entry in the database
    public static void addPacmanEaten(String uuid) {
        update("UPDATE `PacMan` SET `pacManEaten` = '" + (getPacmanEaten(uuid) + 1) + "' WHERE `uuid` = '" + uuid + "';");
    }

    // Gets the score of PacMans eaten by a ghost of a player
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
