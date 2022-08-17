package me.exaraton.citioxs.blockshuffleplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class BlockShufflePlugin extends JavaPlugin implements Listener {

    private final ArrayList<Material> allPossibleItems = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private Map<Player, Material> playersGoals = new HashMap<Player, Material>();
    ArrayList<Player> currentPlayers = new ArrayList<>();
    Map<Player, Boolean> isDone = new HashMap<>();


    @Override
    public void onEnable() {
        // Plugin startup logic

        System.out.println("Initializing " + BlockShufflePlugin.class.getName());

        allPossibleItems.addAll(Arrays.asList(Material.values()));

        getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        System.out.println(BlockShufflePlugin.class.getName() + " shutdown");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){
        System.out.println("Player " + playerJoinEvent.getPlayer().getDisplayName() + " joined to server\n" +
                " Adding him to BlockShufflePlugin list!");

        playerJoinEvent.getPlayer().sendMessage(ChatColor.GREEN + "WELCOME " + playerJoinEvent.getPlayer().getDisplayName()
                + " TO " + ChatColor.BOLD + getServer().getName() + " <3 ");
        playerJoinEvent.getPlayer().sendMessage(ChatColor.GREEN + "HAVE A GOOD TIME!");

    }

    @EventHandler
    public void pickUpItem(PlayerPickupItemEvent playerPickupItemEvent){

        Item item = playerPickupItemEvent.getItem();

        if (!currentPlayers.isEmpty()){

            for (Player player : currentPlayers){

                if (playersGoals.get(player) == item.getItemStack().getType() && !isDone.get(player)){
                    isDone.put(player,true);

                    player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + player.getDisplayName() + " OBTAINED " + item + " !");
                    player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 0.5f, 0.5f);
                }
            }

            int countOfDone = 0;
            for (Player player : currentPlayers){

                if (isDone.get(player))
                    countOfDone++;
            }
            if (currentPlayers.size() == countOfDone){

                RandomItem(currentPlayers,allPossibleItems,playersGoals,isDone);
                currentPlayers.clear();
                isDone.clear();
                playersGoals.clear();
            }
        }
    }

    public void onCommand(CommandSender commandSender, Command command){

        if (command.getName().equalsIgnoreCase("runblockshuffle")) {

            currentPlayers.addAll(getServer().getOnlinePlayers());
            isDone = new HashMap<Player, Boolean>();

            RandomItem(currentPlayers,allPossibleItems,playersGoals,isDone);

        }

    }

    private static void RandomItem(ArrayList<Player> currentPlayers, ArrayList<Material> allPossibleItems,
                                   Map<Player, Material> playersGoals, Map<Player, Boolean> isDone){

        for (Player player : currentPlayers){

            Material goal;
            do {
                goal = allPossibleItems.get((int)(Math.random() * allPossibleItems.size()));
            }
            while (!goal.isBlock());

            playersGoals.put(player,goal);
            isDone.put(player, false);

            player.sendMessage(ChatColor.DARK_RED + "Item to obtain is -> " + playersGoals.get(player).getData().getName());

        }
    }
}
