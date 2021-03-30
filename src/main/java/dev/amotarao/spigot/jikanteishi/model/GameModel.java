package dev.amotarao.spigot.jikanteishi.model;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import dev.amotarao.spigot.jikanteishi.Jikanteishi;

public class GameModel {
    private Jikanteishi plugin;

    /** プレイヤー管理 */
    private PlayerModel ignorePlayer;

    /** ゲームを実行中かどうか */
    private Boolean enabled;

    public GameModel(Jikanteishi plugin) {
        this.plugin = plugin;
        this.ignorePlayer = new PlayerModel();
        this.enabled = false;
    }

    /**
     * パーティクル発生
     */
    private void _spawnParticle(Player player) {
        Location location = player.getLocation();
        location.setY(location.getY() + 1);
        player.getWorld().spawnParticle(Particle.SPELL_INSTANT, location, 20, 0, 0, 0, 0.05);
    }

    private void _startGame(Player sender) {
        ignorePlayer.resetIgnoring();
        ignorePlayer.addIgnoring(sender);

        for (World world : Bukkit.getWorlds()) {
            for (Player player : world.getPlayers()) {
                if (ignorePlayer.isIgnoring(player)) {
                    player.sendTitle("§c§k＊§c 停止 §k＊", "", 0, 30, 10);
                } else {
                    _spawnParticle(player);
                }
            }
        }

        enabled = true;
    }

    public void startGame(Player player) {
        if (!enabled) {
            return;
        }
        _startGame(player);
        plugin.getLogger().info("Start jikanteishi by " + player.getDisplayName());
    }

    private void _stopGame() {
        for (World world : Bukkit.getWorlds()) {
            for (Player player : world.getPlayers()) {
                if (ignorePlayer.isIgnoring(player)) {
                    player.sendTitle("§b解除", "", 0, 30, 10);
                } else {
                    _spawnParticle(player);
                }
            }
        }

        ignorePlayer.resetIgnoring();
        enabled = false;
    }

    public void stopGame() {
        if (enabled) {
            return;
        }
        _stopGame();
        plugin.getLogger().info("Stop jikanteishi");
    }

    public void stopGame(Player player) {
        if (enabled) {
            return;
        }
        _stopGame();
        plugin.getLogger().info("Stop jikanteishi by " + player.getDisplayName());
    }

    public void switchGame(Player player) {
        if (!enabled) {
            startGame(player);
        } else {
            stopGame(player);
        }
    }

    public void togglePlayer(Player player) {
        if (!ignorePlayer.isIgnoring(player)) {
            ignorePlayer.addIgnoring(player);
        } else {
            ignorePlayer.removeIgnoring(player);
        }

        _spawnParticle(player);
    }

    public boolean isStoppingPlayer(Player player) {
        return enabled && !ignorePlayer.isIgnoring(player);
    }
}
