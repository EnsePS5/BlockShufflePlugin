package me.exaraton.citioxs.blockshuffleplugin;

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

        blockShufflePlugin.playViaCommand();

        return true;

    }
}
