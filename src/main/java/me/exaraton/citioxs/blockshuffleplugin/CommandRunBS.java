package me.exaraton.citioxs.blockshuffleplugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandRunBS implements CommandExecutor{

    BlockShufflePlugin blockShufflePlugin;

    public CommandRunBS(BlockShufflePlugin blockShufflePlugin){
        this.blockShufflePlugin = blockShufflePlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender.isOp())
            blockShufflePlugin.playViaCommand();
        else
        {
            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "YOU ARE NOT ALLOWED TO USE THAT COMMAND");
            return false;
        }

        return true;

    }
}
