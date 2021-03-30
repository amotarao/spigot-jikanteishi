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

public class Item {
    static public List<Recipe> getRecipes(Plugin plugin) {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(getClockRecipe(plugin));
        recipes.add(getStickRecipe(plugin));
        return recipes;
    }

    static final private String ClockName = "§c§k*§c時間停止§c§k*§r §eストップウォッチ";

    static final private ItemStack getClockItem() {
        ItemStack item = new ItemStack(Material.WATCH);
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
        recipe.setIngredient('C', Material.WATCH);

        return recipe;
    }

    static final public Boolean isClock(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return item.getType() == Material.WATCH && meta.getDisplayName().equals(ClockName);
    }

    static final private String StickName = "§e時間停止切り替え棒";

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
        recipe.setIngredient('C', Material.WATCH);
        recipe.setIngredient('S', Material.STICK);

        return recipe;
    }

    static final public Boolean isStick(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return item.getType() == Material.STICK && meta.getDisplayName().equals(StickName);
    }
}
