package dev.amotarao.spigot.jikanteishi;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import dev.amotarao.spigot.jikanteishi.model.GameModel;
import dev.amotarao.spigot.jikanteishi.model.PlayerModel;
import dev.amotarao.spigot.jikanteishi.item.Item;

public final class Jikanteishi extends JavaPlugin {
    /** ゲーム管理 */
    public GameModel game;

    /** プレイヤー管理 */
    public PlayerModel ignorePlayer;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);

        game = new GameModel(this);
        ignorePlayer = new PlayerModel();

        for (Recipe recipe : Item.getRecipes(this)) {
            Bukkit.addRecipe(recipe);
        }
    }

    @Override
    public void onDisable() {
        game.stopGame();
    }

    /**
     * パーティクル発生
     */
    public void spawnParticle(World world, Player player) {
        Location location = player.getLocation();
        location.setY(location.getY() + 1);
        world.spawnParticle(Particle.SPELL_INSTANT, location, 20, 0, 0, 0, 0.05);
    }
}
