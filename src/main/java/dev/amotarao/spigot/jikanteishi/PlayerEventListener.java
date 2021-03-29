package dev.amotarao.spigot.jikanteishi;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import dev.amotarao.spigot.jikanteishi.item.Item;

public class PlayerEventListener implements Listener {
    Jikanteishi plugin;

    public PlayerEventListener(Jikanteishi plugin) {
        this.plugin = plugin;
    }

    /**
     * プレイヤーによるクリックの実行
     * 条件が合えば、時間停止開始・終了
     */
    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack item = player.getInventory().getItemInMainHand();

        // 空気クリック以外中止
        if (!action.equals(Action.LEFT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }

        if (Item.isClock(item)) {
            if (!plugin.enabled) {
                plugin.start(player);
            } else {
                plugin.stop(player);
            }
        }
    }

    /**
     * プレイヤーによるEntityに対するクリックの実行
     * 条件が合えば、時間停止対象の追加・削除
     */
    @EventHandler
    private void onPlayerEntityInteract(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        Entity target = e.getRightClicked();
        ItemStack item = player.getInventory().getItemInMainHand();

        // プレイヤー以外中止
        if (target.getType() != EntityType.PLAYER) {
            return;
        }

        Player targetPlayer = (Player) target;

        if (Item.isStick(item)) {
            boolean ignoring = plugin.isIgnoringPlayer(targetPlayer);

            plugin.spawnParticleForPlayer(player.getWorld(), targetPlayer);

            if (!ignoring) {
                plugin.addIgnoringPlayer(targetPlayer);
            } else {
                plugin.removeIgnoringPlayer(targetPlayer);
            }
        }
    }

    /**
     * プレイヤーの移動
     */
    @EventHandler
    private void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (plugin.enabled && !plugin.isIgnoringPlayer(player)) {
            e.setCancelled(true);
        }
    }

    /**
     * プレイヤーのスニーク
     */
    @EventHandler
    private void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if (plugin.enabled && !plugin.isIgnoringPlayer(player)) {
            e.setCancelled(true);
        }
    }
}
