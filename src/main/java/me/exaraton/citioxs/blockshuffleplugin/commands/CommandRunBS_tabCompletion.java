package me.exaraton.citioxs.blockshuffleplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommandRunBS_tabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> result = new ArrayList<>();

        if (args.length == 1){
            result.add("restart");
            result.add("setTime");
            result.add("terminate");
            return result;
        }

        return result;
    }
}
