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
        getLogger().log(Level.INFO, "Stop jikanteishi");
    }

    protected void start(Player player) {
        resetIgnoringPlayer();
        addIgnoringPlayer(player);

        for (World world : Bukkit.getWorlds()) {
            for (Player worldPlayer : world.getPlayers()) {
                if (isIgnoringPlayer(worldPlayer)) {
                    worldPlayer.sendTitle("§c§k＊§c 停止 §k＊", "", 0, 30, 10);
                } else {
                    spawnParticle(world, worldPlayer);
                }
            }
        }

        enabled = true;
        getLogger().log(Level.INFO, "Start jikanteishi by " + player.getDisplayName());
    }

    protected void stop() {
        for (World world : Bukkit.getWorlds()) {
            for (Player worldPlayer : world.getPlayers()) {
                if (isIgnoringPlayer(worldPlayer)) {
                    worldPlayer.sendTitle("§b解除", "", 0, 30, 10);
                } else {
                    spawnParticle(world, worldPlayer);
                }
            }
        }

        resetIgnoringPlayer();
        enabled = false;
    }

    protected void stop(Player player) {
        stop();
        getLogger().log(Level.INFO, "Stop jikanteishi by " + player.getDisplayName());
    }

    /**
     * パーティクル発生
     */
    protected void spawnParticle(World world, Player player) {
        Location location = player.getLocation();
        location.setY(location.getY() + 1);
        world.spawnParticle(Particle.SPELL_INSTANT, location, 20, 0, 0, 0, 0.05);
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
