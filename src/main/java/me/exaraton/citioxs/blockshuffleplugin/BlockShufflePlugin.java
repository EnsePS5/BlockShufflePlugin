package me.exaraton.citioxs.blockshuffleplugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class BlockShufflePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        System.out.println("Initializing " + BlockShufflePlugin.class.getName());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        System.out.println(BlockShufflePlugin.class.getName() + " shutdown");
    }
}
