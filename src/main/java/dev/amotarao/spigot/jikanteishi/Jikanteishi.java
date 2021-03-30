package dev.amotarao.spigot.jikanteishi;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import dev.amotarao.spigot.jikanteishi.model.GameModel;
import dev.amotarao.spigot.jikanteishi.item.Item;

public final class Jikanteishi extends JavaPlugin {
    /** ゲーム管理 */
    public GameModel game;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);
        game = new GameModel(this);
        for (Recipe recipe : Item.getRecipes(this)) {
            Bukkit.addRecipe(recipe);
        }
    }

    @Override
    public void onDisable() {
        game.stopGame();
    }
}
