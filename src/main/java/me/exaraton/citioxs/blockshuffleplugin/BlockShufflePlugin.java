package me.exaraton.citioxs.blockshuffleplugin;

import me.exaraton.citioxs.blockshuffleplugin.commands.CommandFBP_plugin_commands;
import me.exaraton.citioxs.blockshuffleplugin.commands.CommandRunBS;
import me.exaraton.citioxs.blockshuffleplugin.tasks.TimerTask;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class BlockShufflePlugin extends JavaPlugin implements Listener {

    public final ArrayList<List<Material>> allPossibleItems = new ArrayList<>();
    public final Map<Player, Material> playersGoals = new HashMap<>();
    public final Map<Player, Integer> playersPoints = new HashMap<>();
    public ArrayList<Player> currentPlayers = new ArrayList<>();

    private Map<Player, Boolean> isDone = new HashMap<>();

    public me.exaraton.citioxs.blockshuffleplugin.tasks.TimerTask timerTask;

    public static int COMPLETED_ROUNDS = 0;

    private static int GIVEN_POINTS_BASED_ON_OBTAINING = 0;




    public static void RESET_COMPLETED_ROUNDS(){
        COMPLETED_ROUNDS = 0;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        System.out.println("Initializing " + BlockShufflePlugin.class.getName());

        //IN PROGRESS -> MAKE TIER LIST TODO CONTINUE
        List<Material> tierI = Arrays.asList(Material.DIRT,Material.COBBLESTONE,Material.CLAY,Material.GRAVEL,Material.SAND,                    //Basic blocks
                Material.WOODEN_AXE,Material.WOODEN_HOE,Material.WOODEN_PICKAXE,Material.WOODEN_SHOVEL,Material.WOODEN_SWORD,                   //TOOLS
                Material.STONE_AXE,Material.STONE_HOE,Material.STONE_PICKAXE,Material.STONE_SHOVEL,Material.STONE_SWORD,
                Material.IRON_AXE,Material.IRON_HOE,Material.IRON_PICKAXE,Material.IRON_SHOVEL,Material.IRON_SWORD,
                Material.BOW,Material.BOWL,Material.FISHING_ROD,Material.LEVER,Material.STONE_BUTTON,                                           //RÓŻNE
                Material.ANDESITE,Material.ANDESITE_SLAB,Material.ANDESITE_STAIRS,Material.ANDESITE_WALL,Material.POLISHED_ANDESITE,            //SOTNE VARIANTS
                Material.DIORITE,Material.DIORITE_SLAB,Material.DIORITE_STAIRS,Material.DIORITE_WALL,Material.POLISHED_DIORITE,
                Material.GRANITE,Material.GRANITE_SLAB,Material.GRANITE_STAIRS,Material.GRANITE_WALL,Material.POLISHED_GRANITE,
                Material.DEEPSLATE,Material.COBBLED_DEEPSLATE,Material.STONE,Material.SMOOTH_STONE,Material.SANDSTONE,
                Material.RAW_IRON,Material.RAW_COPPER,Material.COAL,Material.CHARCOAL,Material.BUCKET,Material.EMERALD,                         //ORES AND BUCKET
                Material.COPPER_INGOT,Material.IRON_INGOT,Material.FLINT,Material.TORCH,Material.CAMPFIRE,Material.COPPER_INGOT,Material.IRON_INGOT,
                Material.PORKCHOP,Material.COOKED_PORKCHOP,Material.BEEF,Material.COOKED_BEEF,Material.MUTTON,Material.COOKED_MUTTON,           //FOOD
                Material.CHICKEN,Material.COOKED_CHICKEN,Material.COD,Material.COOKED_COD,Material.SALMON,Material.COOKED_SALMON,Material.APPLE,
                Material.SUGAR,Material.EGG,Material.BREAD,Material.HAY_BLOCK,Material.COCOA_BEANS,Material.PUMPKIN,Material.MELON,Material.CARROT,
                Material.BEETROOT,
                Material.BONE,Material.BONE_MEAL,Material.ROTTEN_FLESH,Material.STRING,Material.GUNPOWDER,Material.ENDER_PEARL,                 //MONSTER AND SPECIAL ANIMAL LOOT
                Material.LEATHER,Material.RABBIT_HIDE,Material.INK_SAC,
                Material.WHEAT_SEEDS,Material.DANDELION,Material.POPPY,Material.CORNFLOWER,Material.OXEYE_DAISY,Material.LILY_OF_THE_VALLEY,    //FLOWERS
                Material.ORANGE_TULIP,Material.PINK_TULIP,Material.RED_TULIP,Material.WHITE_TULIP,Material.ALLIUM,Material.BLUE_ORCHID,
                Material.SUNFLOWER,Material.LILAC,Material.ROSE_BUSH,Material.PEONY,Material.LILY_PAD,Material.CLAY_BALL,Material.SUGAR_CANE,
                Material.OAK_SAPLING,Material.BIRCH_SAPLING,Material.SPRUCE_SAPLING,Material.DARK_OAK_SAPLING,Material.JUNGLE_SAPLING,Material.CACTUS,
                Material.BLACK_WOOL,Material.WHITE_WOOL,Material.BLUE_WOOL,Material.BROWN_WOOL,Material.CYAN_WOOL,Material.GRAY_WOOL,           //WOOLS
                Material.GREEN_WOOL,Material.LIGHT_BLUE_WOOL,Material.LIGHT_GRAY_WOOL,Material.LIME_WOOL,Material.MAGENTA_WOOL,
                Material.ORANGE_WOOL,Material.PINK_WOOL,Material.PURPLE_WOOL,Material.RED_WOOL,Material.YELLOW_WOOL,
                Material.OAK_LOG,Material.OAK_DOOR,Material.OAK_BUTTON,Material.OAK_FENCE,Material.OAK_FENCE_GATE,Material.OAK_PLANKS,          //WOODS
                Material.BIRCH_LOG,Material.BIRCH_DOOR,Material.BIRCH_BUTTON,Material.BIRCH_FENCE,Material.BIRCH_FENCE_GATE,Material.BIRCH_PLANKS,
                Material.SPRUCE_LOG,Material.SPRUCE_DOOR,Material.SPRUCE_BUTTON,Material.SPRUCE_FENCE,Material.SPRUCE_FENCE_GATE,Material.SPRUCE_PLANKS,
                Material.JUNGLE_LOG,Material.JUNGLE_DOOR,Material.JUNGLE_BUTTON,Material.JUNGLE_FENCE,Material.JUNGLE_FENCE_GATE,Material.JUNGLE_PLANKS,
                Material.DARK_OAK_LOG,Material.DARK_OAK_DOOR,Material.DARK_OAK_BUTTON,Material.DARK_OAK_FENCE,Material.DARK_OAK_FENCE_GATE,Material.DARK_OAK_PLANKS,
                Material.OAK_SIGN,Material.BIRCH_SIGN,Material.SPRUCE_SIGN,Material.JUNGLE_SIGN,Material.DARK_OAK_SIGN,                         //SIGNS
                Material.CRAFTING_TABLE,Material.FURNACE,Material.CHEST,Material.BARREL,Material.GLASS,Material.GLASS_PANE,                     //SPECIAL BLOCKS / GLASS
                Material.PAPER,Material.BRICKS,Material.BRICK
                );

        List<Material> tierII = Arrays.asList(Material.IRON_DOOR,Material.IRON_TRAPDOOR,Material.TNT,Material.MINECART,

                Material.LAVA_BUCKET,Material.WATER_BUCKET,Material.DIAMOND,Material.REDSTONE,Material.REDSTONE_BLOCK,Material.RAW_GOLD,        //Deep ores
                Material.GOLD_INGOT,Material.GOLD_NUGGET,Material.COAL_BLOCK,Material.IRON_BLOCK,
                Material.BOOKSHELF,
                Material.BLACK_CONCRETE,Material.WHITE_CONCRETE,Material.BLUE_CONCRETE,Material.BROWN_CONCRETE,Material.CYAN_CONCRETE,
                Material.GRAY_CONCRETE,Material.GREEN_CONCRETE,Material.LIGHT_BLUE_CONCRETE,Material.LIGHT_GRAY_CONCRETE,Material.LIME_CONCRETE,
                Material.MAGENTA_CONCRETE,Material.ORANGE_CONCRETE,Material.PINK_CONCRETE,Material.PURPLE_CONCRETE,Material.RED_CONCRETE,Material.YELLOW_CONCRETE);
        System.out.println("added Items - tierI :\n" + tierI + "\nTierII : " + tierII);

        List<Material> tierIII = Arrays.asList(Material.OBSIDIAN,Material.NETHERRACK, Material.SOUL_SAND,Material.SOUL_SOIL,Material.BONE_BLOCK); //Nether Blocks

        allPossibleItems.add(tierI);
        allPossibleItems.add(tierII);
        allPossibleItems.add(tierIII);

        getServer().getPluginManager().registerEvents(this,this);
        Objects.requireNonNull(this.getCommand("runBS")).setExecutor(new CommandRunBS(this));
        System.out.println("Added runBS");
        Objects.requireNonNull(this.getCommand("FBP_plugin_commands")).setExecutor(new CommandFBP_plugin_commands(this));
        System.out.println("Added FBP");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        System.out.println(BlockShufflePlugin.class.getName() + " shutdown");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){
        System.out.println("Player " + playerJoinEvent.getPlayer().getDisplayName() + " joined to server");

        playerJoinEvent.getPlayer().sendMessage(ChatColor.GREEN + "Welcome back " + playerJoinEvent.getPlayer().getDisplayName()
                + " to " + ChatColor.BOLD + getServer().getName());
        playerJoinEvent.getPlayer().sendMessage(ChatColor.YELLOW + "" + ChatColor.ITALIC + "Type in /FBP_plugin_commands to get info about available commands!");
        playerJoinEvent.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "/FBP_plugin_commands is currently under develop");
        playerJoinEvent.getPlayer().sendMessage(ChatColor.GREEN + "Have a good time!");
        playerJoinEvent.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "FBP Version: " + getDescription().getVersion());

        playerJoinEvent.setJoinMessage(ChatColor.GREEN + "" + ChatColor.BOLD +
                playerJoinEvent.getPlayer().getDisplayName() + " joined " + getDescription().getName());

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent playerQuitEvent){

        playerQuitEvent.setQuitMessage(ChatColor.DARK_RED + "" + ChatColor.ITALIC + playerQuitEvent.getPlayer().getDisplayName() + " left" );
    }

    @EventHandler
    public void pickUpItem(PlayerPickupItemEvent playerPickupItemEvent){

        ItemStack item = playerPickupItemEvent.getItem().getItemStack();

        if (!currentPlayers.isEmpty()){

            for (Player player : currentPlayers){

                if ((playersGoals.get(player) == item.getType() && !isDone.get(player))){
                    isDone.put(player,true);

                    Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getDisplayName().toUpperCase(Locale.ROOT) +
                            " OBTAINED " + playersGoals.get(player) + " AND SCORED " + GIVEN_POINTS_BASED_ON_OBTAINING + " POINTS !");

                    player.spawnParticle(Particle.TOTEM,player.getLocation(),5);
                    player.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "ITEM OBTAINED", "+" +
                            GIVEN_POINTS_BASED_ON_OBTAINING + " point(s)",5,60,15);

                    playersPoints.put(player, playersPoints.get(player) + GIVEN_POINTS_BASED_ON_OBTAINING);
                    GIVEN_POINTS_BASED_ON_OBTAINING--;

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

                COMPLETED_ROUNDS++;

                if (timerTask.isRunning) {
                    timerTask.isRunning = false;
                    timerTask.delay = 0;
                    timerTask.interrupt();
                }

                this.playViaCommand();
            }
        }
    }

    public void playViaCommand(){

        if (COMPLETED_ROUNDS == 7){
            //TODO SHOW GAINED POINTS AND FINISH GAME
            RESET_COMPLETED_ROUNDS();
            return;
        }

            if (!currentPlayers.isEmpty())
                currentPlayers.clear();

            if (!playersGoals.isEmpty())
                playersGoals.clear();

            currentPlayers.addAll(getServer().getOnlinePlayers());
            isDone = new HashMap<>();

            timerTask = new TimerTask(this);

            RandomItemAssigning();

            GIVEN_POINTS_BASED_ON_OBTAINING = currentPlayers.size();

        System.out.println(playersGoals);
    }

    private void RandomItemAssigning(){

        for (Player player : currentPlayers){

            playersPoints.put(player,0);

            int goal_index = indexToChooseItemFrom();
            int randomMatIndex = (int)(Math.random() * allPossibleItems.get(goal_index).size());
            Material goal = allPossibleItems.get(goal_index).get(randomMatIndex);

            playersGoals.put(player,goal);
            isDone.put(player, false);

            player.sendMessage(ChatColor.YELLOW + "Item to obtain is -> " + playersGoals.get(player));
            player.sendTitle(ChatColor.WHITE + "" + playersGoals.get(player),ChatColor.YELLOW + "is the Item to obtain!",5,60,15);
        }
    }
    private static int indexToChooseItemFrom(){

        int randomNumber = (int)(Math.random() * 100);
        System.out.println("the random number is -> " + randomNumber);

        switch (COMPLETED_ROUNDS){
            case 0:
            case 1:
                return 0;
            case 2: return randomNumber > 90 ? 1 : 0;
            case 3: return randomNumber > 80 ? 1 : 0;
            case 4: return randomNumber > 70 ? randomNumber > 95 ? 2 : 1 : 0;
            case 5: return randomNumber > 60 ? randomNumber > 90 ? 2 : 1 : 0;
            default: return randomNumber > 50 ? randomNumber > 85 ? 2 : 1 : 0;
        }
    }
}
