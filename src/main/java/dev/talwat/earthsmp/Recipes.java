package dev.talwat.earthsmp;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

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

        shulker.shape("   ", "PDP", "   ");
        shulker.setIngredient('D', Material.DIAMOND);
        shulker.setIngredient('P', Material.PURPLE_DYE);

        getServer().addRecipe(shulker);
    }

    public static void glowstone() {
        ItemStack result = new ItemStack(Material.GLOWSTONE, 16);
        ShapedRecipe glowstone = new ShapedRecipe(new NamespacedKey(plugin, "glowstone"), result);

        glowstone.shape("SSS", "STS", "SSS");
        glowstone.setIngredient('S', Material.STICK);
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
}
