package dev.amotarao.spigot.jikanteishi;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class Jikanteishi extends JavaPlugin implements Listener {
    /** 時間停止を実行中かどうか */
    Boolean active = false;

    /** 停止しないメンバーリスト */
    List<String> members = new ArrayList<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        stop();
    }

    private void start(Player player) {
        members.add(player.getUniqueId().toString());
        active = true;
        Bukkit.broadcastMessage("start");
    }

    private void stop() {
        members.clear();
        active = false;
        Bukkit.broadcastMessage("stop");
    }

    /**
     * プレイヤーによるクリックの実行
     * 条件が合えば、時間停止開始・終了
     */
    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();

        // 空気クリック以外中止
        if (!action.equals(Action.LEFT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.CLOCK) {
            if (!active) {
                start(player);
            } else {
                stop();
            }
        }
    }

    /**
     * プレイヤーの移動
     */
    @EventHandler
    private void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();
        if (active && members.indexOf(uuid) < 0) {
            e.setCancelled(true);
        }
        getLogger().log(Level.INFO, "PlayerMoveEvent: " + player.getName());
    }

    /**
     * プレイヤーのスニーク
     */
    @EventHandler
    private void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();
        if (active && members.indexOf(uuid) < 0) {
            e.setCancelled(true);
        }
        getLogger().log(Level.INFO, "PlayerToggleSneakEvent: " + player.getName());
    }
}
