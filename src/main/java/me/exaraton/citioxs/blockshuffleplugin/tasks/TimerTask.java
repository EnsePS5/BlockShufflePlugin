package me.exaraton.citioxs.blockshuffleplugin.tasks;

import me.exaraton.citioxs.blockshuffleplugin.BlockShufflePlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public class TimerTask extends Thread  {

    BlockShufflePlugin blockShufflePlugin;

    public TimerTask(BlockShufflePlugin blockShufflePlugin){
        this.blockShufflePlugin = blockShufflePlugin;
        this.start();
    }

    private int timet = 6 * 60;
    public boolean isRunning;
    public long delay = timet * 1000L;

    @Override
    public void run() {

        isRunning = true;

        do {

            int minutes = timet/60;
            int seconds = timet%60;

            for (Player player : blockShufflePlugin.currentPlayers){
                if (minutes >= 3) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "" + minutes + " : " + seconds));
                }else if (minutes >= 1)
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "" + minutes + " : " + seconds));
                else if (minutes == 0 && seconds > 10)
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "" + minutes + " : " + seconds));
                else if (seconds <= 10 && seconds >= 0){
                    for (Player player1 : blockShufflePlugin.currentPlayers){
                        player1.sendTitle((ChatColor.DARK_RED + "" + seconds), null,1,18,1);
                        player1.playSound(player1.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE,.5f,.5f);
                    }
                }
            }

            try {
                Thread.sleep(1000);
                timet = timet - 1;
                delay = delay - 1000;

                if (minutes == 0 && seconds == 0) {

                    if (blockShufflePlugin.currentPlayers.isEmpty()){
                        blockShufflePlugin.currentPlayers.addAll(blockShufflePlugin.getServer().getOnlinePlayers());
                    }

                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "Time's up!");
                    System.out.println("tutaj nic");
                    for (Player player : blockShufflePlugin.currentPlayers) {


                        if (blockShufflePlugin.isDone.get(player)) {
                            System.out.println("tutaj win");
                            player.sendTitle((ChatColor.DARK_RED + "" + ChatColor.BOLD + "TIME'S UP!"), "Your objective is fulfilled", 5, 60, 15);
                        }else {
                            System.out.println("tutaj lose");
                            player.sendTitle((ChatColor.DARK_RED + "" + ChatColor.BOLD + "TIME'S UP!"), "-1 point", 5, 60, 15);
                            player.playSound(player.getLocation(),Sound.ENTITY_WITCH_DEATH,.6f,.6f);

                            blockShufflePlugin.playersPoints.put(player, -1);
                            blockShufflePlugin.playersScore.get(player).setScore(blockShufflePlugin.playersScore.get(player).getScore() - 1);

                            //To make obtaining item after time impossible
                            blockShufflePlugin.isDone.clear();
                        }
                    }

                    BlockShufflePlugin.COMPLETED_ROUNDS++;

                    Thread.sleep(5000);

                    blockShufflePlugin.playViaCommand(); //starting new round

                    Thread.sleep(100);

                    this.interrupt();
                }
            } catch (InterruptedException e) {
                return;
            }
        }
        while (delay != -1 && isRunning);

        Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "Time's up!");
    }

    //In minutes
    public void changeTime(int time){
        timet = time;
    }
}
