package me.exaraton.citioxs.blockshuffleplugin.commands;

import me.exaraton.citioxs.blockshuffleplugin.BlockShufflePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class CommandRunBS implements CommandExecutor{

    BlockShufflePlugin blockShufflePlugin;

    public CommandRunBS(BlockShufflePlugin blockShufflePlugin){
        this.blockShufflePlugin = blockShufflePlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender.isOp())
        {
            if (args.length == 0) {
                BlockShufflePlugin.RESET_ROUNDS_STATS();
                blockShufflePlugin.playViaCommand();
            }else if (args[0].equals("setTime") && Integer.parseInt(args[1]) >= 0){
                blockShufflePlugin.timerTask.changeTime(Integer.parseInt(args[1]));
            }else if (args[0].equals("restart")){
                blockShufflePlugin.restart();
            }else
                sender.sendMessage(ChatColor.DARK_RED + "given command is incorrect");
        }
        else
        {
            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "YOU ARE NOT ALLOWED TO USE THAT COMMAND");
            return false;
        }

        return true;

    }
}
