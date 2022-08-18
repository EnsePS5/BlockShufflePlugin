package me.exaraton.citioxs.blockshuffleplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class BlockShufflePlugin extends JavaPlugin implements Listener {

    private final ArrayList<Material> allPossibleItems = new ArrayList<>();
    private final Map<Player, Material> playersGoals = new HashMap<>();
    ArrayList<Player> currentPlayers = new ArrayList<>();
    Map<Player, Boolean> isDone = new HashMap<>();


    @Override
    public void onEnable() {
        // Plugin startup logic

        System.out.println("Initializing " + BlockShufflePlugin.class.getName());

        //TODO MAKE TIER LISTS
        for (Material material : Material.values()){
            if (material.isItem() || (material.isBlock() && !material.isAir()))
            allPossibleItems.add(material);
        }

        System.out.println("added Items : \n" + allPossibleItems);

        getServer().getPluginManager().registerEvents(this,this);
        Objects.requireNonNull(this.getCommand("runBS")).setExecutor(new CommandRunBS(this));
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

        System.out.println("Picked up Item by: ");
        ItemStack item = playerPickupItemEvent.getItem().getItemStack();

        if (!currentPlayers.isEmpty()){

            for (Player player : currentPlayers){

                System.out.println(player.getDisplayName());

                if (playersGoals.get(player) == item.getType() && !isDone.get(player)){
                    isDone.put(player,true);

                    Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + player.getDisplayName() + " OBTAINED " + item.getType() + " !");

                    for (Player player1 : currentPlayers) {
                        player1.playSound(player1.getLocation(), Sound.ITEM_TOTEM_USE, 0.5f, 0.5f);
                    }
                }
            }

            int countOfDone = 0;
            for (Player player : currentPlayers){

                if (isDone.get(player))
                    countOfDone++;
            }
            System.out.println("Curr players -> " + currentPlayers.size() + " | count of done -> " + countOfDone);
            if (currentPlayers.size() == countOfDone){
                this.playViaCommand();
            }
        }
    }

    public void playViaCommand(){

            if (!currentPlayers.isEmpty())
                currentPlayers.clear();

            if (!playersGoals.isEmpty())
                playersGoals.clear();

            currentPlayers.addAll(getServer().getOnlinePlayers());
            isDone = new HashMap<>();

            RandomItem(currentPlayers,allPossibleItems,playersGoals,isDone);

    }

    private static void RandomItem(ArrayList<Player> currentPlayers, ArrayList<Material> allPossibleItems,
                                   Map<Player, Material> playersGoals, Map<Player, Boolean> isDone){

        for (Player player : currentPlayers){

            Material goal;
            do {
                goal = allPossibleItems.get((int)(Math.random() * allPossibleItems.size()));
            }
            while (goal.isBlock() && goal.isSolid());

            playersGoals.put(player,goal);
            isDone.put(player, false);

            player.sendMessage(ChatColor.DARK_RED + "Item to obtain is -> " + playersGoals.get(player));

        }
    }
}
