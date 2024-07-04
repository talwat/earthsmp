package dev.talwat.earthsmp;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import static org.bukkit.Bukkit.getServer;

public class Recipes {
    static Earthsmp plugin;

    public static void register(Earthsmp plugin) {
        Recipes.plugin = plugin;

        rails();
        saddle();
        rootedDirt();
        shulker();
        glowstone();
        basalt();
        villagers();
        blackstone();
        warpedStem();
        prismarine();
        blaze();
        seaLantern();
        web();
        soulsand();
        calcite();
        quartz();
        stonebricks();
        brick();
        netherbrick();
        netherwart();
    }

    public static void rails() {
        ItemStack result = new ItemStack(Material.POWERED_RAIL, 16);
        ShapedRecipe rails = new ShapedRecipe(new NamespacedKey(plugin, "rails"), result);

        rails.shape("G G", "GSG", "GRG");
        rails.setIngredient('S', Material.STICK);
        rails.setIngredient('R', Material.REDSTONE);
        rails.setIngredient('G', Material.GOLD_INGOT);

        getServer().addRecipe(rails);
    }

    public static void saddle() {
        ItemStack result = new ItemStack(Material.SADDLE);
        ShapedRecipe saddle = new ShapedRecipe(new NamespacedKey(plugin, "saddle"), result);

        saddle.shape("LLL", "S S", "I I");
        saddle.setIngredient('S', Material.STRING);
        saddle.setIngredient('L', Material.LEATHER);
        saddle.setIngredient('I', Material.IRON_INGOT);

        getServer().addRecipe(saddle);
    }

    public static void rootedDirt() {
        ItemStack result = new ItemStack(Material.ROOTED_DIRT, 16);
        ShapedRecipe dirt = new ShapedRecipe(new NamespacedKey(plugin, "dirt"), result);

        dirt.shape("DDD", "DDD", "DDD");
        dirt.setIngredient('D', Material.DIRT);

        getServer().addRecipe(dirt);
    }

    public static void shulker() {
        ItemStack result = new ItemStack(Material.SHULKER_SHELL);
        ShapedRecipe shulker = new ShapedRecipe(new NamespacedKey(plugin, "shulker"), result);

        shulker.shape("PDP");
        shulker.setIngredient('D', Material.DIAMOND);
        shulker.setIngredient('P', Material.PURPLE_DYE);

        getServer().addRecipe(shulker);
    }

    public static void glowstone() {
        ItemStack result = new ItemStack(Material.GLOWSTONE, 16);
        ShapedRecipe glowstone = new ShapedRecipe(new NamespacedKey(plugin, "glowstone"), result);

        glowstone.shape("SSS", "STS", "SSS");
        glowstone.setIngredient('S', Material.CLAY_BALL);
        glowstone.setIngredient('T', Material.TORCH);

        getServer().addRecipe(glowstone);
    }

    public static void basalt() {
        ItemStack result = new ItemStack(Material.BASALT, 16);
        ShapedRecipe basalt = new ShapedRecipe(new NamespacedKey(plugin, "basalt"), result);

        basalt.shape("SSS", "STS", "SSS");
        basalt.setIngredient('S', Material.COBBLESTONE);
        basalt.setIngredient('T', Material.COAL);

        getServer().addRecipe(basalt);
    }

    public static void villagers() {
        ItemStack result = new ItemStack(Material.VILLAGER_SPAWN_EGG, 1);
        ShapedRecipe villager = new ShapedRecipe(new NamespacedKey(plugin, "villager"), result);

        villager.shape("STS", "TTT", "STS");
        villager.setIngredient('S', Material.CHEST);
        villager.setIngredient('T', Material.DIAMOND);

        getServer().addRecipe(villager);
    }

    public static void blackstone() {
        ItemStack result = new ItemStack(Material.BLACKSTONE, 8);
        ShapedRecipe blackstone = new ShapedRecipe(new NamespacedKey(plugin, "blackstone"), result);

        blackstone.shape("SSS", "STS", "SSS");
        blackstone.setIngredient('S', Material.STONE);
        blackstone.setIngredient('T', Material.COAL);

        getServer().addRecipe(blackstone);
    }

    public static void warpedStem() {
        ItemStack result = new ItemStack(Material.WARPED_STEM, 1);
        ShapelessRecipe warpedStem = new ShapelessRecipe(new NamespacedKey(plugin, "warpedstem"), result);

        warpedStem.addIngredient(Material.OAK_LOG);
        warpedStem.addIngredient(Material.WHEAT_SEEDS);

        getServer().addRecipe(warpedStem);
    }

    public static void brick() {
        ItemStack result = new ItemStack(Material.BRICK, 16);
        ShapelessRecipe brick = new ShapelessRecipe(new NamespacedKey(plugin, "brick"), result);

        brick.addIngredient(Material.CLAY_BALL);
        brick.addIngredient(Material.CLAY_BALL);

        getServer().addRecipe(brick);
    }

    public static void quartz() {
        ItemStack result = new ItemStack(Material.QUARTZ, 9);
        ShapelessRecipe quartz = new ShapelessRecipe(new NamespacedKey(plugin, "quartz"), result);

        quartz.addIngredient(Material.QUARTZ_BLOCK);

        getServer().addRecipe(quartz);
    }

    public static void stonebricks() {
        ItemStack result = new ItemStack(Material.STONE_BRICKS, 18);
        ShapedRecipe stonebricks = new ShapedRecipe(new NamespacedKey(plugin, "stonebricks"), result);

        stonebricks.shape("SSS", "SBS", "SSS");
        stonebricks.setIngredient('S', Material.COBBLESTONE);
        stonebricks.setIngredient('B', Material.BRICK);

        getServer().addRecipe(stonebricks);
    }

    public static void prismarine() {
        ItemStack result = new ItemStack(Material.DARK_PRISMARINE, 32);
        ShapedRecipe prismarine = new ShapedRecipe(new NamespacedKey(plugin, "prismarine"), result);

        prismarine.shape("SSS", "STS", "SSS");
        prismarine.setIngredient('S', Material.COBBLESTONE);
        prismarine.setIngredient('T', Material.IRON_INGOT);

        getServer().addRecipe(prismarine);
    }

    public static void blaze() {
        ItemStack result = new ItemStack(Material.BLAZE_ROD, 2);
        ShapedRecipe blaze = new ShapedRecipe(new NamespacedKey(plugin, "blaze"), result);

        blaze.shape("G", "S", "C");
        blaze.setIngredient('C', Material.COAL);
        blaze.setIngredient('S', Material.STICK);
        blaze.setIngredient('G', Material.GUNPOWDER);

        getServer().addRecipe(blaze);
    }

    public static void seaLantern() {
        ItemStack result = new ItemStack(Material.SEA_LANTERN, 1);
        ShapedRecipe lantern = new ShapedRecipe(new NamespacedKey(plugin, "sealantern"), result);

        lantern.shape("SI", "IS");
        lantern.setIngredient('I', Material.IRON_INGOT);
        lantern.setIngredient('S', Material.GLOW_INK_SAC);

        getServer().addRecipe(lantern);
    }

    public static void web() {
        ItemStack result = new ItemStack(Material.COBWEB, 1);
        ShapedRecipe web = new ShapedRecipe(new NamespacedKey(plugin, "web"), result);

        web.shape("S S", " S ", "S S");
        web.setIngredient('S', Material.STRING);

        getServer().addRecipe(web);
    }

    public static void soulsand() {
        ItemStack result = new ItemStack(Material.SOUL_SAND, 4);
        ShapedRecipe soulsand = new ShapedRecipe(new NamespacedKey(plugin, "soulsand"), result);

        soulsand.shape(" S ", "SRS", " S ");
        soulsand.setIngredient('S', Material.SAND);
        soulsand.setIngredient('R', Material.REDSTONE);

        getServer().addRecipe(soulsand);
    }

    public static void calcite() {
        ItemStack result = new ItemStack(Material.CALCITE, 16);
        ShapedRecipe calcite = new ShapedRecipe(new NamespacedKey(plugin, "calcite"), result);

        calcite.shape("SSS", "SMS", "SSS");
        calcite.setIngredient('S', Material.COBBLESTONE);
        calcite.setIngredient('M', Material.BONE_MEAL);

        getServer().addRecipe(calcite);
    }

    public static void netherbrick() {
        ItemStack result = new ItemStack(Material.NETHER_BRICK, 12);
        ShapedRecipe netherbrick = new ShapedRecipe(new NamespacedKey(plugin, "netherbrick"), result);

        netherbrick.shape("BBB", "BRB", "BBB");
        netherbrick.setIngredient('B', Material.BRICK);
        netherbrick.setIngredient('R', Material.REDSTONE);

        getServer().addRecipe(netherbrick);
    }

    public static void netherwart() {
        ItemStack result = new ItemStack(Material.NETHER_WART, 1);
        ShapelessRecipe netherwart = new ShapelessRecipe(new NamespacedKey(plugin, "netherwart"), result);

        netherwart.addIngredient(Material.DIAMOND);
        netherwart.addIngredient(Material.REDSTONE);

        getServer().addRecipe(netherwart);
    }
}
