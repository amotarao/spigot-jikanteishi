package dev.amotarao.spigot.jikanteishi.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class Item {
    static public List<Recipe> getRecipes(Plugin plugin) {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(getClockRecipe(plugin));
        recipes.add(getStickRecipe(plugin));
        return recipes;
    }

    // ToDo: 文字化けを回避する
    static final private String ClockName = ChatColor.DARK_AQUA + "\u00A7k*\u00A7rSTOP\u00A7k*\u00A7r " + ChatColor.YELLOW + "時計";

    static final private ItemStack getClockItem() {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ClockName);
        meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3, true);
        item.setItemMeta(meta);

        return item;
    }

    static final private Recipe getClockRecipe(Plugin plugin) {
        NamespacedKey key = new NamespacedKey(plugin, "jikanteishi_clock");
        ItemStack item = getClockItem();
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.shape(" S ", "SCS", " S ");
        recipe.setIngredient('S', Material.NETHER_STAR);
        recipe.setIngredient('C', Material.CLOCK);

        return recipe;
    }

    static final public Boolean isClock(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return item.getType() == Material.CLOCK && meta.getDisplayName().equals(ClockName);
    }

    // ToDo: 文字化けを回避する
    static final private String StickName = ChatColor.YELLOW + "切り替え棒";

    static final private ItemStack getStickItem() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(StickName);
        meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3, true);
        item.setItemMeta(meta);

        return item;
    }

    static final private Recipe getStickRecipe(Plugin plugin) {
        NamespacedKey key = new NamespacedKey(plugin, "jikanteishi_toggle_stick");
        ItemStack item = getStickItem();
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.shape("C", "S");
        recipe.setIngredient('C', Material.CLOCK);
        recipe.setIngredient('S', Material.STICK);

        return recipe;
    }

    static final public Boolean isStick(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return item.getType() == Material.STICK && meta.getDisplayName().equals(StickName);
    }
}
