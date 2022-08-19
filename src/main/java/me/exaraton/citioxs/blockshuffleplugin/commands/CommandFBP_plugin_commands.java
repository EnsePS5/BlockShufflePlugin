package me.exaraton.citioxs.blockshuffleplugin.commands;

import me.exaraton.citioxs.blockshuffleplugin.BlockShufflePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

public class CommandFBP_plugin_commands implements CommandExecutor {

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
                        "fbp_plugin_commands - shows available commands to player");
                sender.sendMessage(ChatColor.ITALIC +
                        "fbp_plugin_commands objectiveInfo - shows details about your current objective");
                sender.sendMessage(ChatColor.ITALIC + "" +
                        "fbp_plugin_commands locate [biomes] - shows coordinates of the closest [biomes]");
            }
            else if (args[0].equals("objectiveInfo")){                                                                  //ObjectiveInfo

                sender.sendMessage(ChatColor.DARK_RED + "Item to obtain: " + blockShufflePlugin.playersGoals.get(sender));

                int tierLevel = 1;
                for (int i = 0; i < blockShufflePlugin.allPossibleItems.size(); i++){

                    for (int j = 0; j < blockShufflePlugin.allPossibleItems.get(i).size() ; j++) {

                        if (blockShufflePlugin.allPossibleItems.get(i).contains(blockShufflePlugin.playersGoals.get(sender)))
                        {
                        sender.sendMessage(ChatColor.DARK_RED + "Tier: " + tierLevel +
                                "\nId in tier list: " + blockShufflePlugin.allPossibleItems.get(i).
                                indexOf(blockShufflePlugin.playersGoals.get(sender)));
                        break;
                        }
                    }
                    tierLevel++;
                }
            //TODO ZNAJDZ SPOSOB BY INFO O LOKALIZACJI DAWAC DO PLEYERA
            }else if (args[0].equals("locatebiome")){

                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

                String biomes = args[1];
                if (!Bukkit.dispatchCommand(console, "locatebiome minecraft:" + biomes.toLowerCase(Locale.ROOT))){
                    sender.sendMessage(ChatColor.RED + biomes + " does not exist");
                }else
                    sender.sendMessage(sender.getName() + " located " + biomes);

            }

            }else
                sender.sendMessage(ChatColor.RED + "<!> command does not exist");

        return true;
    }
}
