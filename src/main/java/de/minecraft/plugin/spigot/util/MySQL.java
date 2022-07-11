package de.minecraft.plugin.spigot.util;

import de.minecraft.plugin.spigot.PacMan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;

public class MySQL {

    private final PacMan INSTANCE = PacMan.getInstance();

    private Connection connection;
    private final String USERNAME;
    private final int PORT;
    private final String DATABASE;
    private final String HOST;
    private final String PASSWORD;

    public MySQL() {

        this.USERNAME = INSTANCE.getMySqlFile().getValue("Login.Username");
        this.PORT = INSTANCE.getMySqlFile().getIntValue("Login.Port");
        this.DATABASE = INSTANCE.getMySqlFile().getValue("Login.Database");
        this.HOST = INSTANCE.getMySqlFile().getValue("Login.Host");
        this.PASSWORD = INSTANCE.getMySqlFile().getValue("Login.Password");

        connect();
        createTable();
    }

    private boolean isConnected() {
        return (connection != null);
    }

    // Erstellt eine Verbindung zu dem MySQL-Server
    public void connect() {

        if (isConnected()) {
            return;
        }

        try {

            Bukkit.getConsoleSender().sendMessage(INSTANCE.getMessageFile().getValue("MySQL.Connect.Wait"));
            connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?autoReconnect=true", USERNAME, PASSWORD);
            Bukkit.getConsoleSender().sendMessage(INSTANCE.getMessageFile().getValue("MySQL.Connect.Success"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    // Schließt die Verbindung zu dem Server
    public void disconnect() {

        if (!isConnected()) {
            return;
        }

        try {
            connection.close();
            Bukkit.getConsoleSender().sendMessage(INSTANCE.getMessageFile().getValue("MySQL.Disconnect.Success"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    // Aktualisiert die Datenbank
    public void update(String query) {

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Erzeugt ein neuen Table in der Datenbank
    public void createTable() {
        update("CREATE TABLE IF NOT EXISTS PacMan (uid VARCHAR(64), uuid VARCHAR(64), playedGames INT, winsPacMan INT, losesPacMan INT, winsGhost INT, losesGhost INT, pacManEaten INT)");
    }

    // Führt eine Datenabfrage an der Datenbank aus
    public ResultSet query(String query) {

        ResultSet resultSet = null;

        try {

            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return resultSet;
    }

    // Überprüft, ob ein Spieler bereits in der Datenbank vorhanden ist
    public boolean exists(Player player) {

        ResultSet resultSet = query("SELECT * FROM PacMan WHERE uuid = '" + player.getUniqueId().toString() + "'");

        try {
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }


    // Fügt einem Spieler ein Spiel hinzu
    public void addGame(Player player) {

        if (exists(player)) {
            update("UPDATE PacMan SET playedGames = " + (getGames(player) + 1) + " WHERE uuid = '" + player.getUniqueId().toString() + "'");
        } else {
            update("INSERT INTO PacMan (uid, uuid, playedGames, winsPacMan, losesPacMan, winsGhost, losesGhost, pacManEaten) VALUES ('" + player.getName() + "', '" + player.getUniqueId().toString() + "', 1, 0, 0, 0, 0, 0)");
        }
    }

    // Gibt die Menge an gespielter Spiele eines Spielers zurück
    public int getGames(Player player) {

        try {
            ResultSet resultSet = query("SELECT playedGames FROM PacMan WHERE uuid = '" + player.getUniqueId().toString() + "'");
            if (resultSet.next()) {
                return resultSet.getInt("playedGames");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    // Gibt die Menge an gespielter Spiele eines Spielers zurück
    public int getGames(String uuid) {

        try {
            ResultSet resultSet = query("SELECT playedGames FROM PacMan WHERE uuid = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("playedGames");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    // Fügt einem Spieler ein Sieg als PacMan hinzu
    public void addWinsPacMan(Player player) {
        update("UPDATE PacMan SET winsPacMan = " + (getWinsPacMan(player) + 1) + " WHERE uuid = '" + player.getUniqueId().toString() + "'");
    }

    // Gibt die Menge an gewonnener Spiele als PacMan eines Spielers zurück
    public int getWinsPacMan(Player player) {

        try {

            ResultSet resultSet = query("SELECT winsPacMan FROM PacMan WHERE uuid = '" + player.getUniqueId().toString() + "'");

            if (resultSet == null) {
                return 0;
            }

            if (resultSet.next()) {
                return resultSet.getInt("winsPacMan");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    // Gibt die Menge an gewonnener Spiele als PacMan eines Spielers zurück
    public int getWinsPacMan(String uuid) {

        try {

            ResultSet resultSet = query("SELECT winsPacMan FROM PacMan WHERE uuid = '" + uuid + "'");

            if (resultSet == null) {
                return 0;
            }

            if (resultSet.next()) {
                return resultSet.getInt("winsPacMan");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    // Fügt einem Spieler eine Niederlage als PacMan hinzu
    public void addLosesPacMan(Player player) {
        update("UPDATE PacMan SET losesPacMan = " + (getLosesPacMan(player) + 1) + " WHERE uuid = '" + player.getUniqueId().toString() + "'");
    }

    // Gibt die Menge an verlorener Spiele als PacMan eines Spielers zurück
    public int getLosesPacMan(Player player) {

        try {
            ResultSet resultSet = query("SELECT losesPacMan FROM PacMan WHERE uuid = '" + player.getUniqueId().toString() + "'");
            if (resultSet.next()) {
                return resultSet.getInt("losesPacMan");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    // Gibt die Menge an verlorener Spiele als PacMan eines Spielers zurück
    public int getLosesPacMan(String uuid) {

        try {
            ResultSet resultSet = query("SELECT losesPacMan FROM PacMan WHERE uuid = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("losesPacMan");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    // Fügt einem Spieler einen Sieg als Geist hinzu
    public void addWinsGhost(Player player) {
        update("UPDATE PacMan SET winsGhost = " + (getWinsGhost(player) + 1) + " WHERE uuid = '" + player.getUniqueId().toString() + "'");
    }

    // Gibt die Menge an gewonnenen Spiele als Geist eines Spielers zurück
    public int getWinsGhost(Player player) {

        try {
            ResultSet resultSet = query("SELECT winsGhost FROM PacMan WHERE uuid = '" + player.getUniqueId().toString() + "'");
            if (resultSet.next()) {
                return resultSet.getInt("winsGhost");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    // Gibt die Menge an gewonnenen Spiele als Geist eines Spielers zurück
    public int getWinsGhost(String uuid) {

        try {
            ResultSet resultSet = query("SELECT winsGhost FROM PacMan WHERE uuid = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("winsGhost");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    // Fügt einem Spieler eine Niederlage als Geist hinzu
    public void addLosesGhost(Player player) {
        update("UPDATE PacMan SET losesGhost = " + (getLosesPacMan(player) + 1) + " WHERE uuid = '" + player.getUniqueId().toString() + "'");
    }

    // Gibt die Menge an verlorener Spiele als Geist eines Spielers zurück
    public int getLosesGhost(Player player) {

        try {
            ResultSet resultSet = query("SELECT losesGhost FROM PacMan WHERE uuid = '" + player.getUniqueId().toString() + "'");
            if (resultSet.next()) {
                return resultSet.getInt("losesGhost");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    // Gibt die Menge an verlorener Spiele als Geist eines Spielers zurück
    public int getLosesGhost(String uuid) {

        try {
            ResultSet resultSet = query("SELECT losesGhost FROM PacMan WHERE uuid = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("losesGhost");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    // Fügt einem Spieler einen gegessenen PacMan
    public void addPacmanEaten(Player player) {
        update("UPDATE PacMan SET pacManEaten = " + (getPacmanEaten(player) + 1) + " WHERE uuid = '" + player.getUniqueId().toString() + "'");
    }

    // Gibt die Menge an gegessenen PacMans eines Spielers zurück
    public int getPacmanEaten(Player player) {

        try {
            ResultSet resultSet = query("SELECT pacManEaten FROM PacMan WHERE uuid = '" + player.getUniqueId().toString() + "'");
            if (resultSet.next()) {
                return resultSet.getInt("pacManEaten");
            }
        } catch (SQLException ex) {}

        return 0;
    }

    // Gibt die Menge an gegessenen PacMans eines Spielers zurück
    public int getPacmanEaten(String uuid) {

        try {
            ResultSet resultSet = query("SELECT pacManEaten FROM PacMan WHERE uuid = '" + uuid + "'");
            if (resultSet.next()) {
                return resultSet.getInt("pacManEaten");
            }
        } catch (SQLException ex) {}

        return 0;
    }
}
