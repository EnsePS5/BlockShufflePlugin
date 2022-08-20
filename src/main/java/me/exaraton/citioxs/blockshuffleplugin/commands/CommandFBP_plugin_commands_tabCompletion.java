package me.exaraton.citioxs.blockshuffleplugin.commands;

import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CommandFBP_plugin_commands_tabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> result = new ArrayList<>();

        if (args.length == 1){
            result.add("locate");
            result.add("objectiveInfo");
            return result;
        }

        if (args[0].equals("locate") && args.length == 2){

            String startsWith = args[1];

            Biome[] allBiomes = Biome.values();

            for (Biome biome : allBiomes){
                if (!biome.name().equals("CUSTOM")) {
                    if (biome.name().toLowerCase(Locale.ROOT).startsWith(startsWith))
                        result.add(biome.name().toLowerCase(Locale.ROOT));
                }
            }

            return result;
        }

        return null;

    }
}
