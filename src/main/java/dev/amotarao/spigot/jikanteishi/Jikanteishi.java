package dev.amotarao.spigot.jikanteishi;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import dev.amotarao.spigot.jikanteishi.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public final class Jikanteishi extends JavaPlugin {
    /** 時間停止を実行中かどうか */
    protected Boolean enabled = false;

    /** 停止しないメンバーリスト */
    protected List<UUID> ignorePlayers = new ArrayList<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);

        for (Recipe recipe : Item.getRecipes(this)) {
            Bukkit.addRecipe(recipe);
        }
        getLogger().log(Level.INFO, String.valueOf(Bukkit.getWorlds().size()));

        for (World world : Bukkit.getWorlds()) {
            getLogger().log(Level.INFO, world.getEnvironment().toString());
        }
    }

    @Override
    public void onDisable() {
        stop();
    }

    protected void start(Player player) {
        ignorePlayers.add(player.getUniqueId());
        World world = player.getWorld();
        spawnParticleForPlayers(world, world.getPlayers());
        enabled = true;
        Bukkit.broadcastMessage("start");
    }

    protected void stop() {
        ignorePlayers.clear();
        enabled = false;
        Bukkit.broadcastMessage("stop");
    }

    protected void stop(Player player) {
        World world = player.getWorld();
        spawnParticleForPlayers(world, world.getPlayers());
        stop();
    }

    /**
     * パーティクル発生
     */
    protected void spawnParticleForPlayer(World world, Player player) {
        if (ignorePlayers.indexOf(player.getUniqueId()) >= 0) {
            return;
        }
        Location location = player.getLocation();
        location.setY(location.getY() + 1);
        world.spawnParticle(Particle.SPELL_INSTANT, location, 20, 0, 0, 0, 0.05);
    }

    /**
     * 複数プレイヤーにパーティクル発生
     */
    protected void spawnParticleForPlayers(World world, List<Player> players) {
        for (Player player : players) {
            spawnParticleForPlayer(world, player);
        }
    }
}
