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
import org.bukkit.scoreboard.*;

import java.util.*;

//TODO COMPASS SHOWING BIOMES
//TODO SPECIAL OBJECTIVE (KILL MOB OR FIND ANOTHER ITEM ETC.)
public final class BlockShufflePlugin extends JavaPlugin implements Listener {

    public final ArrayList<List<Material>> allPossibleItems = new ArrayList<>();

    //PLAYERS VARIABLES
    public final Map<Player, Material> playersGoals = new HashMap<>();
    public final Map<Player, Integer> playersPoints = new HashMap<>();
    public final Map<Player, Score> playersScore = new HashMap<>();

    public ArrayList<Player> currentPlayers = new ArrayList<>();

    //SCOREBOARD
    public ScoreboardManager scoreboardManager;
    public Scoreboard scoreboard;
    public Objective objective;

    public Map<Player, Boolean> isDone = new HashMap<>();

    public me.exaraton.citioxs.blockshuffleplugin.tasks.TimerTask timerTask;

    public static int COMPLETED_ROUNDS = 0;
    private static int GIVEN_POINTS_BASED_ON_OBTAINING = 0;

    private static boolean HAS_GAME_ENDED = false;




    public static void RESET_ROUNDS_STATS(){
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

                Material.LAVA_BUCKET,Material.WATER_BUCKET,Material.COD_BUCKET,Material.SALMON_BUCKET,                                          //Buckets
                Material.DIAMOND,Material.REDSTONE,Material.REDSTONE_BLOCK,Material.RAW_GOLD,                                                   //Deep ores
                Material.GOLD_INGOT,Material.GOLD_NUGGET,Material.COAL_BLOCK,Material.LAPIS_LAZULI,Material.TUFF,
                Material.BOOKSHELF,Material.RAIL,Material.RAW_IRON_BLOCK,Material.RAW_COPPER_BLOCK,Material.TRAPPED_CHEST,                      //Different ones
                Material.BLACK_CONCRETE,Material.WHITE_CONCRETE,Material.BLUE_CONCRETE,Material.BROWN_CONCRETE,Material.CYAN_CONCRETE,          //Concrete
                Material.GRAY_CONCRETE,Material.GREEN_CONCRETE,Material.LIGHT_BLUE_CONCRETE,Material.LIGHT_GRAY_CONCRETE,Material.LIME_CONCRETE,
                Material.MAGENTA_CONCRETE,Material.ORANGE_CONCRETE,Material.PINK_CONCRETE,Material.PURPLE_CONCRETE,Material.RED_CONCRETE,Material.YELLOW_CONCRETE,
                Material.BLACK_CONCRETE_POWDER,Material.WHITE_CONCRETE_POWDER,Material.BLUE_CONCRETE_POWDER,Material.BROWN_CONCRETE_POWDER,     //Concrete powder
                Material.CYAN_CONCRETE_POWDER,Material.GRAY_CONCRETE_POWDER,Material.GREEN_CONCRETE_POWDER,Material.LIGHT_BLUE_CONCRETE_POWDER,
                Material.LIGHT_GRAY_CONCRETE_POWDER,Material.LIME_CONCRETE_POWDER,Material.MAGENTA_CONCRETE_POWDER,Material.ORANGE_CONCRETE_POWDER,
                Material.PINK_CONCRETE_POWDER,Material.PURPLE_CONCRETE_POWDER,Material.RED_CONCRETE_POWDER,Material.YELLOW_CONCRETE_POWDER,
                Material.IRON_AXE,Material.IRON_HOE,Material.IRON_PICKAXE,Material.IRON_SHOVEL,Material.IRON_SWORD,                             //Tools
                Material.SHEARS,Material.FLINT_AND_STEEL,Material.GOLDEN_AXE,Material.GOLDEN_PICKAXE,Material.GOLDEN_SHOVEL,
                Material.GOLDEN_SWORD,Material.GOLDEN_HOE);

        List<Material> tierIII = Arrays.asList(Material.OBSIDIAN,Material.NETHERRACK, Material.SOUL_SAND,Material.SOUL_SOIL,Material.BONE_BLOCK,//Nether Blocks
                Material.GOLD_BLOCK,Material.ANVIL,Material.BLAZE_POWDER,Material.BLAZE_ROD,
                Material.DARK_OAK_LEAVES,Material.OAK_LEAVES,Material.BIRCH_LEAVES,Material.JUNGLE_LEAVES,Material.SPRUCE_LEAVES,               //LEAVES
                Material.ENCHANTED_BOOK,
                Material.IRON_CHESTPLATE,Material.IRON_LEGGINGS,Material.IRON_BOOTS,Material.IRON_BOOTS,Material.SHIELD,Material.IRON_BLOCK     //Iron armor
                );

        allPossibleItems.add(tierI);
        allPossibleItems.add(tierII);
        allPossibleItems.add(tierIII);

        System.out.println("added Items - tierI :\n" + tierI + "\nTierII : " + tierII + "\nTierIII : ");
        //REJERSTRACJA KOMEND I EVENTOW
        getServer().getPluginManager().registerEvents(this,this);
        Objects.requireNonNull(this.getCommand("runBS")).setExecutor(new CommandRunBS(this));
        System.out.println("Added runBS");
        Objects.requireNonNull(this.getCommand("FBP_plugin_commands")).setExecutor(new CommandFBP_plugin_commands(this));
        System.out.println("Added FBP");

        //SCOREBOARD

        scoreboardManager = Bukkit.getScoreboardManager();

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
                            " OBTAINED " + playersGoals.get(player) + "!");

                    player.spawnParticle(Particle.TOTEM,player.getLocation(),150);
                    player.sendTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "ITEM OBTAINED", "+" +
                            GIVEN_POINTS_BASED_ON_OBTAINING + " point(s)",5,60,15);

                    playersPoints.put(player, playersPoints.get(player) + GIVEN_POINTS_BASED_ON_OBTAINING);
                    playersScore.get(player).setScore(playersScore.get(player).getScore() + GIVEN_POINTS_BASED_ON_OBTAINING);

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
            if (currentPlayers.size() == countOfDone && !HAS_GAME_ENDED){

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

        System.out.println(COMPLETED_ROUNDS + " -> number of completed rounds!");

        if (COMPLETED_ROUNDS == 7){//END CONDITIONS

            Player firstPlayer = null;
            int currentMax = -7;
            for (Player player : currentPlayers){
                if (playersPoints.get(player) > currentMax) {
                    currentMax = playersPoints.get(player);
                    firstPlayer = player;
                }
            }

            if (firstPlayer != null) {
                for (Player player : currentPlayers) {
                    player.sendTitle(ChatColor.GOLD + firstPlayer.getDisplayName() + " WINS!", "Congratulations!", 5, 80, 15);
                }

                firstPlayer.playSound(firstPlayer.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);
                firstPlayer.playSound(firstPlayer.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE,.5f,.5f);

                Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.ITALIC + "The BlockShuffle game has ended.");

                RESET_ROUNDS_STATS();

                HAS_GAME_ENDED = true;

                return;
            }
        }

        if (COMPLETED_ROUNDS == 6){

            for (Player player : currentPlayers){
                player.sendTitle(ChatColor.YELLOW + "STARTING LAST ROUND!", null, 5 ,40 ,15);
            }
            try {

                Thread.sleep(3000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        if (COMPLETED_ROUNDS >= 0 && COMPLETED_ROUNDS <= 5){

            for (Player player : currentPlayers){

                player.sendTitle(ChatColor.YELLOW + "STARTING ROUND " + (COMPLETED_ROUNDS + 1) + "!", null, 5 ,40 ,15);
                try {

                    Thread.sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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

            //Scoreboard
        if (COMPLETED_ROUNDS == 0){

            scoreboard = scoreboardManager.getNewScoreboard();

            objective = scoreboard.registerNewObjective("Points", "dummy", "Points");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(ChatColor.GOLD + "Points");

            ArrayList<Score> scores = new ArrayList<>();
            for (Player player : currentPlayers)
                scores.add(objective.getScore(ChatColor.YELLOW + player.getDisplayName() + ChatColor.GOLD + " -> "));

            int index = 0;
            for (Player player : currentPlayers){
                playersScore.put(player, scores.get(index));
                playersPoints.put(player,0);

                index++;

                playersScore.get(player).setScore(0);
                player.setScoreboard(scoreboard);
            }

        }

        System.out.println(playersGoals);
    }

    private void RandomItemAssigning(){

        for (Player player : currentPlayers){

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
