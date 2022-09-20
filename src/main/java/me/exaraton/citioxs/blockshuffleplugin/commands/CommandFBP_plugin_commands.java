package me.exaraton.citioxs.blockshuffleplugin.commands;

import me.exaraton.citioxs.blockshuffleplugin.BlockShufflePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandFBP_plugin_commands implements CommandExecutor{

    BlockShufflePlugin blockShufflePlugin;

    public CommandFBP_plugin_commands(BlockShufflePlugin blockShufflePlugin){
        this.blockShufflePlugin = blockShufflePlugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            if (args.length == 0){
                sender.sendMessage(ChatColor.ITALIC +
                        "runbs - runs BlockShuffle with default settings");
                sender.sendMessage(ChatColor.ITALIC +
                        "runbs setTime [timeInSeconds] - sets timer to count down from [timeInSeconds] ");
                sender.sendMessage(ChatColor.ITALIC +
                        "runbs restart - restarts the game");
                sender.sendMessage(ChatColor.ITALIC +
                        "runbs terminate - terminates the game");
                sender.sendMessage(ChatColor.ITALIC +
                        "fbp_plugin_commands - shows available commands to player");
                sender.sendMessage(ChatColor.ITALIC +
                        "fbp_plugin_commands objectiveInfo - shows details about your current objective");
                sender.sendMessage(ChatColor.ITALIC + "" +
                        "fbp_plugin_commands locate [biomes] - shows coordinates of the closest [biomes]");
            }
            else if (args[0].equals("objectiveInfo")){

                if (blockShufflePlugin.playersGoals.get(sender) == null) {
                    sender.sendMessage(ChatColor.RED + "You don't have any item to obtain yet");
                    return true;
                }
                if (blockShufflePlugin.isDone.get(sender)){
                    sender.sendMessage(ChatColor.RED + "You have already obtained your item");
                    return true;
                }

                sender.sendMessage(ChatColor.YELLOW + "Item to obtain: " + blockShufflePlugin.playersGoals.get(sender));

                int tierLevel = 1;
                for (int i = 0; i < blockShufflePlugin.allPossibleItems.size(); i++) {

                    for (int j = 0; j < blockShufflePlugin.allPossibleItems.get(i).size(); j++) {

                        if (blockShufflePlugin.allPossibleItems.get(i).contains(blockShufflePlugin.playersGoals.get(sender))) {
                            sender.sendMessage(ChatColor.YELLOW + "Tier: " + tierLevel +
                                    "\nId in tier list: " + blockShufflePlugin.allPossibleItems.get(i).
                                    indexOf(blockShufflePlugin.playersGoals.get(sender)));
                            break;
                        }
                    }
                    tierLevel++;
                }
                return true;

            }else if (args[0].equals("locate")){

                if (blockShufflePlugin.timerTask == null){
                    sender.sendMessage(ChatColor.RED + "<!> game has not started yet");
                    return true;
                }

                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

                String biomes;
                if (args.length == 2)
                    biomes = args[1];
                else {
                    sender.sendMessage(ChatColor.RED + "<!> given biome is invalid");
                    return true;
                }

                if (!Bukkit.dispatchCommand(console, "locatebiome minecraft:" + biomes.toLowerCase(Locale.ROOT)))
                {
                    sender.sendMessage(ChatColor.RED + biomes + " does not exist");
                }
                else
                {
                    Thread thread = new Thread(() -> {
                        //sender.sendMessage(sender.getName() + " located " + biomes);
                        Set<String> uniqueBiomes = new HashSet<>();
                        Map<String, int[]> coords = new HashMap<>();
                        for (int i = -1000; i < 1000; i += 25) {
                            for (int j = -1000; j < 1000; j += 25) {

                                String currentlySelectedBiome = ((Player) sender).getWorld().getBiome(
                                                ((Player) sender).getLocation().getBlockX() + i,
                                                90,
                                                ((Player) sender).getLocation().getBlockZ() + j)
                                        .name();

                                int[] tempCoords = new int[3];
                                tempCoords[0] = ((Player) sender).getLocation().getBlockX() + i;
                                tempCoords[1] = 90;
                                tempCoords[2] = ((Player) sender).getLocation().getBlockZ() + j;

                                if (uniqueBiomes.contains(currentlySelectedBiome)) {
                                    double currentlyMinDistance =
                                            Math.sqrt(
                                                    Math.pow((((Player) sender).getLocation().getBlockX() - coords.get(currentlySelectedBiome)[0]), 2)
                                                            +
                                                            Math.pow((((Player) sender).getLocation().getBlockZ() - coords.get(currentlySelectedBiome)[2]), 2)
                                            );
                                    double candidateToMinDistance =
                                            Math.sqrt(
                                                    Math.pow((((Player) sender).getLocation().getBlockX() - tempCoords[0]), 2)
                                                            +
                                                            Math.pow((((Player) sender).getLocation().getBlockZ() - tempCoords[2]), 2)
                                            );

                                    if (candidateToMinDistance < currentlyMinDistance) {
                                        coords.put(currentlySelectedBiome, tempCoords);
                                    }

                                } else {
                                    coords.put(currentlySelectedBiome, tempCoords);
                                    uniqueBiomes.add(currentlySelectedBiome);
                                }
                            }
                        }
                        System.out.println(uniqueBiomes);
                        if (uniqueBiomes.contains(biomes.toUpperCase(Locale.ROOT))) {

                            Location compassTargetLocation = new Location(((Player) sender).getWorld(),
                                    coords.get(biomes.toUpperCase(Locale.ROOT))[0],
                                    coords.get(biomes.toUpperCase(Locale.ROOT))[1],
                                    coords.get(biomes.toUpperCase(Locale.ROOT))[2]);

                            ((Player) sender).setCompassTarget(compassTargetLocation);
                            sender.sendMessage(ChatColor.YELLOW + biomes.toLowerCase(Locale.ROOT) + " is " +
                                    (int) (Math.sqrt(
                                            Math.pow((((Player) sender).getLocation().getBlockX() - coords.get(biomes.toUpperCase(Locale.ROOT))[0]), 2)
                                                    +
                                                    Math.pow((((Player) sender).getLocation().getBlockZ() - coords.get(biomes.toUpperCase(Locale.ROOT))[2]), 2)
                                    )) + " blocks away from you");

                            System.out.println(Arrays.toString(coords.get(biomes.toUpperCase(Locale.ROOT))));
                        } else {
                            sender.sendMessage(ChatColor.YELLOW + "given biome is out of range");
                        }
                    });

                    thread.start();
                }

            }

            }else
                sender.sendMessage(ChatColor.RED + "<!> command does not exist");

        return true;
    }
}
