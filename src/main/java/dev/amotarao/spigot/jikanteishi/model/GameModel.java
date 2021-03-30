package dev.amotarao.spigot.jikanteishi.model;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import dev.amotarao.spigot.jikanteishi.Jikanteishi;

public class GameModel {
    private Jikanteishi plugin;

    /** ゲームを実行中かどうか */
    public Boolean enabled;

    public GameModel(Jikanteishi plugin) {
        this.enabled = false;
        this.plugin = plugin;
    }

    private void _startGame(Player player) {
        plugin.ignorePlayer.resetIgnoring();
        plugin.ignorePlayer.addIgnoring(player);

        for (World world : Bukkit.getWorlds()) {
            for (Player worldPlayer : world.getPlayers()) {
                if (plugin.ignorePlayer.isIgnoring(worldPlayer)) {
                    worldPlayer.sendTitle("§c§k＊§c 停止 §k＊", "", 0, 30, 10);
                } else {
                    plugin.spawnParticle(world, worldPlayer);
                }
            }
        }

        enabled = true;
    }

    public void startGame(Player player) {
        _startGame(player);
        plugin.getLogger().info("Start jikanteishi by " + player.getDisplayName());
    }

    private void _stopGame() {
        for (World world : Bukkit.getWorlds()) {
            for (Player worldPlayer : world.getPlayers()) {
                if (plugin.ignorePlayer.isIgnoring(worldPlayer)) {
                    worldPlayer.sendTitle("§b解除", "", 0, 30, 10);
                } else {
                    plugin.spawnParticle(world, worldPlayer);
                }
            }
        }

        plugin.ignorePlayer.resetIgnoring();
        enabled = false;
    }

    public void stopGame() {
        _stopGame();
        plugin.getLogger().info("Stop jikanteishi");
    }

    public void stopGame(Player player) {
        _stopGame();
        plugin.getLogger().info("Stop jikanteishi by " + player.getDisplayName());
    }
}
