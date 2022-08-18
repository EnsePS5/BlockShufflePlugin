package me.exaraton.citioxs.blockshuffleplugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TimerTask extends Thread  {

    BlockShufflePlugin blockShufflePlugin;

    public TimerTask(BlockShufflePlugin blockShufflePlugin){
        this.blockShufflePlugin = blockShufflePlugin;
        this.start();
    }

    int timet = 5 * 60;
    boolean isRunning = false;
    long delay = timet * 1000L;

    @Override
    public void run() {

        isRunning = true;

        do {
            int minutes = timet/60;
            int seconds = timet%60;

            for (Player player : blockShufflePlugin.currentPlayers){
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(minutes + " : " + seconds));
            }

            try {
                Thread.sleep(1000);
                timet = timet - 1;
                delay = delay - 1000;
            } catch (InterruptedException e) {
                return;
            }
        }
        while (delay != 0 || isRunning);
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "Time's up!");

    }
}
