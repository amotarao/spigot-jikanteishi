package dev.amotarao.spigot.jikanteishi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PlayerModel {
    /** 停止しないプレイヤーリスト */
    private List<UUID> ignoringPlayers;

    public PlayerModel() {
        ignoringPlayers = new ArrayList<UUID>();
    }

    /**
     * 除外プレイヤー判定
     */
    public boolean isIgnoring(Player player) {
        return ignoringPlayers.indexOf(player.getUniqueId()) >= 0;
    }

    /**
     * 除外プレイヤー追加
     */
    public void addIgnoring(Player player) {
        boolean ignoring = isIgnoring(player);
        if (!ignoring) {
            ignoringPlayers.add(player.getUniqueId());
        }
    }

    /**
     * 除外プレイヤー削除
     */
    public void removeIgnoring(Player player) {
        boolean ignoring = isIgnoring(player);
        if (ignoring) {
            ignoringPlayers.remove(player.getUniqueId());
        }
    }

    /**
     * 除外プレイヤーリセット
     */
    public void resetIgnoring() {
        ignoringPlayers.clear();
    }
}
