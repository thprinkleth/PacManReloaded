package de.minecraft.plugin.spigot.cmds;

import de.minecraft.plugin.spigot.PacMan;
import de.minecraft.plugin.spigot.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;

public class CmdStats implements CommandExecutor {

    private final PacMan INSTANCE = PacMan.getInstance();

    // Wird ausgeführt, wenn eine Person eine Nachricht schickt, welche mit "/" beginnt
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

        // Überprüft, ob der Sender der Nachricht "/stats ..." geschrieben hat
        if (!cmd.getName().equalsIgnoreCase("stats")) {
            return true;
        }

        // Überprüft, ob der Sender ein Player ist (die Konsole kann auch Kommandos schicken)
        if (!(cs instanceof Player)) {

            // Sendet der Konsole eine Nachricht, dass nur ein Spieler das Kommando ausführen kann
            Bukkit.getConsoleSender().sendMessage(INSTANCE.getMessageFile().getValue("Commands.NoPlayer"));
            return true;
        }

        Player player = (Player) cs;

        // Überprüft, ob die Syntax des Commands mehr als ein Argument hat (z.B. /stats xyz 123)
        if (args.length > 1) {

            // Sendet dem Spieler eine Nachricht, dass er die falsche Syntax benutzt hat
            player.sendMessage(INSTANCE.getMessageFile().getValue("Commands.Stats.Syntax", player));
            return true;
        }

        // Überprüft die Länge der Argumente
        switch (args.length) {

            case 0: {

                // Erstellt ein Inventar
                Inventory statInventory = Bukkit.getServer().createInventory(null, 9 * 3, INSTANCE.getConfigFile().getValue("Inventory.StatsInventory.Self.Name", player));

                // Speichert die verschiedene Werte zwischen
                int playedGames = INSTANCE.getMySQL().getGames(player);
                int winsPacMan = INSTANCE.getMySQL().getWinsPacMan(player);
                int losesPacMan = INSTANCE.getMySQL().getLosesPacMan(player);
                int winsGhost = INSTANCE.getMySQL().getWinsGhost(player);
                int losesGhost = INSTANCE.getMySQL().getLosesGhost(player);

                DecimalFormat decimalFormat = new DecimalFormat("0.00");

                // Rechnet die Gewinnwahrscheinlichkeit aus
                float winrate = ((float) winsPacMan + (float) winsGhost) / (float) playedGames;

                winrate *= 100;
                decimalFormat.format(winrate);

                // Speichert die Daten in einem Array
                String[] lore = {
                        "§7Gespielte Spiele: §6" + playedGames,
                        "§7Wins als PacMan: §6" + winsPacMan,
                        "§7Loses als PacMan: §6" + losesPacMan,
                        "§7Wins als Geist: §6" + winsGhost,
                        "§7Loses als Geist: §6" + losesGhost,
                        "§7Gewinn-wahrscheinlichkeit: §6" + winrate + "%"
                        };

                // Erzeugt ein Kopf-Item
                ItemStack skull = new ItemBuilder(Material.SKULL_ITEM).setLore(lore).setName("§b" + player.getName()).setDurability((short) 3).toItemStack(); // TODO:
                SkullMeta skullMeta = ((SkullMeta) skull.getItemMeta());

                skullMeta.setOwner(player.getName());
                skull.setItemMeta(skullMeta);

                // Setzt das Item in das Inventar
                statInventory.setItem(8 + 5, skull);

                // Öffent das Inventar für den Spieler
                player.openInventory(statInventory);

                break;
            }

            case 1: {

                if (Bukkit.getOfflinePlayer(args[0]) == null) {
                    // TODO:
                    return true;
                }

                OfflinePlayer otherPlayer = Bukkit.getOfflinePlayer(Bukkit.getOfflinePlayer(args[0]).getUniqueId());

                // Erstellt ein Inventar
                Inventory statInventory = Bukkit.getServer().createInventory(null, 9 * 3, INSTANCE.getConfigFile().getValue("Inventory.StatsInventory.Other.Name", player, args[0]));

                // Speichert die verschiedene Werte zwischen
                int playedGames = INSTANCE.getMySQL().getGames(otherPlayer.getUniqueId().toString());
                int winsPacMan = INSTANCE.getMySQL().getWinsPacMan(otherPlayer.getUniqueId().toString());
                int losesPacMan = INSTANCE.getMySQL().getLosesPacMan(otherPlayer.getUniqueId().toString());
                int winsGhost = INSTANCE.getMySQL().getWinsGhost(otherPlayer.getUniqueId().toString());
                int losesGhost = INSTANCE.getMySQL().getLosesGhost(otherPlayer.getUniqueId().toString());

                DecimalFormat decimalFormat = new DecimalFormat("#.##");

                // Rechnet die Gewinnwahrscheinlichkeit aus
                float winrate = ((float) winsPacMan + (float) winsGhost) / (float) playedGames;

                winrate *= 100;
                decimalFormat.format(winrate);

                // Speichert die Daten in einem Array
                String[] lore = {
                        "§7Gespielte Spiele: §6" + playedGames,
                        "§7Wins als PacMan: §6" + winsPacMan,
                        "§7Loses als PacMan: §6" + losesPacMan,
                        "§7Wins als Geist: §6" + winsGhost,
                        "§7Loses als Geist: §6" + losesGhost,
                        "§7Gewinn-wahrscheinlichkeit: §6" + winrate + "%"
                };

                // Erzeugt ein Kopf-Item
                ItemStack skull = new ItemBuilder(Material.SKULL_ITEM).setLore(lore).setName("§b" + otherPlayer.getName()).setDurability((short) 3).toItemStack(); // TODO:
                SkullMeta skullMeta = ((SkullMeta) skull.getItemMeta());

                skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(args[0]));
                skull.setItemMeta(skullMeta);

                // Setzt das Item in das Inventar
                statInventory.setItem(8 + 5, skull);

                // Öffent das Inventar für den Spieler
                player.openInventory(statInventory);

                break;
            }
        }
        return false;
    }
}
