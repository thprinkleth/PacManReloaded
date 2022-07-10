package de.minecraft.plugin.spigot.cmds;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.gamestate.GameState;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdStart implements CommandExecutor {

    private final PacMan INSTANCE = PacMan.getInstance();
    private int cooldownScheduler = 0;

    // Wird ausgeführt, wenn eine Person eine Nachricht schickt, welche mit "/" beginnt
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {

        // Überprüft, ob der Sender der Nachricht "/start ..." geschrieben hat
        if (!cmd.getName().equalsIgnoreCase("start")) {
            return true;
        }

        // Überprüft, ob der Sender ein Player ist (die Konsole kann auch Kommandos schicken)
        if (!(cs instanceof Player)) {

            // Sendet der Konsole eine Nachricht, dass nur ein Spieler das Kommando ausführen kann
            Bukkit.getConsoleSender().sendMessage(INSTANCE.getMessageFile().getValue("Commands.NoPlayer"));
            return true;
        }

        Player player = ((Player) cs);

        // Überprüft, ob der Spieler die nötigen Rechte hat, um den Befehl auszuführen
        if (!player.hasPermission("commands.start")) {

            // Sendet dem Spieler eine Nachricht, dass er nicht genug Rechte hat
            player.sendMessage(INSTANCE.getMessageFile().getValue("Commands.NoPerm", player));
            return true;
        }

        // Überprüft, ob die Syntax des Commands Argumente enthält (z.B. /start xyz)
        if (args.length != 0) {

            // Sendet dem Spieler eine Nachricht, dass er die falsche Syntax benutzt hat
            player.sendMessage(INSTANCE.getMessageFile().getValue("Commands.Start.Syntax", player));
            return true;
        }

        // Erzeugt einen Auftrag, welcher alle 20 "gameticks", oder einmal jede Sekunde, ausgeführt wird
        cooldownScheduler = Bukkit.getScheduler().scheduleAsyncRepeatingTask(INSTANCE, new Runnable() {

            int countdown = 5;

            @Override
            public void run() {

                // Überprüft die Restzeit des Countdowns, welcher den Beginn des Spielers ansagt
                if ((countdown % 60 == 0 && countdown != 0) || countdown == 30 || countdown == 20 || countdown == 15 || countdown == 10 ||
                        countdown == 5 || countdown == 4 || countdown == 3 || countdown == 2 || countdown == 1) {

                    // Überprüft, ob genug Spieler auf dem Server sind, um das Spiel zu starten
                    if (Bukkit.getServer().getOnlinePlayers().size() < INSTANCE.getConfigFile().getIntValue("Game.PlayersNeededToStart")) {

                        // Sendet dem Spieler eine Nachricht, dass mehr Spieler benötigt werden, um das Spiel zu starten
                        player.sendMessage(INSTANCE.getMessageFile().getValue("Lobby.Countdown.NotEnoughPlayers", player));

                        // Bricht den sich wiederholenden Auftrag ab, sodass keine Rechenleistung verschwendet wird
                        Bukkit.getScheduler().cancelTask(cooldownScheduler);
                        return;
                    }

                    // Geht jeden Spieler durch, der sich gerade auf dem Server befindet
                    for (Player player : Bukkit.getOnlinePlayers()) {

                        // Schickt dem Spieler eine Nachricht, wie lange es zum Spielbeginn dauert
                        player.sendMessage(INSTANCE.getMessageFile().getValue("Lobby.Countdown.Counting", player, countdown));
                    }
                }

                if (countdown == 0) {

                    // Bricht den sich wiederholenden Auftrag ab, sodass keine Rechenleistung verschwendet wird
                    Bukkit.getScheduler().cancelTask(cooldownScheduler);

                    // Setzt den derzeitigen Spielstatus auf Ingame
                    INSTANCE.getGameStateManager().setCurrent(GameState.INGAME_STATE);
                }
                countdown--;
            }
        }, 0, 20);

        return false;
    }
}
