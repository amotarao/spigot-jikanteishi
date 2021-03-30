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

    /** 停止しないプレイヤーリスト */
    private List<UUID> ignoringPlayers = new ArrayList<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);

        for (Recipe recipe : Item.getRecipes(this)) {
            Bukkit.addRecipe(recipe);
        }
    }

    @Override
    public void onDisable() {
        stop();
    }

    protected void start(Player player) {
        resetIgnoringPlayer();
        addIgnoringPlayer(player);

        World world = player.getWorld();
        spawnParticleForPlayers(world, world.getPlayers());

        enabled = true;
        getLogger().log(Level.INFO, "Start jikanteishi by " + player.getDisplayName());
        player.sendTitle("§c§k＊§c 停止 §k＊", "", 0, 30, 10);
    }

    protected void stop() {
        resetIgnoringPlayer();

        enabled = false;
    }

    protected void stop(Player player) {
        World world = player.getWorld();
        spawnParticleForPlayers(world, world.getPlayers());

        stop();
        getLogger().log(Level.INFO, "Stop jikanteishi by " + player.getDisplayName());
        player.sendTitle("§b解除", "", 0, 30, 10);
    }

    /**
     * パーティクル発生
     */
    protected void spawnParticleForPlayer(World world, Player player) {
        if (ignoringPlayers.indexOf(player.getUniqueId()) >= 0) {
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

    /**
     * 除外プレイヤー判定
     */
    protected boolean isIgnoringPlayer(Player player) {
        return ignoringPlayers.indexOf(player.getUniqueId()) >= 0;
    }

    /**
     * 除外プレイヤー追加
     */
    protected void addIgnoringPlayer(Player player) {
        boolean ignoring = isIgnoringPlayer(player);
        if (!ignoring) {
            ignoringPlayers.add(player.getUniqueId());
        }
    }

    /**
     * 除外プレイヤー削除
     */
    protected void removeIgnoringPlayer(Player player) {
        boolean ignoring = isIgnoringPlayer(player);
        if (ignoring) {
            ignoringPlayers.remove(player.getUniqueId());
        }
    }

    /**
     * 除外プレイヤーリセット
     */
    protected void resetIgnoringPlayer() {
        ignoringPlayers.clear();
    }
}
