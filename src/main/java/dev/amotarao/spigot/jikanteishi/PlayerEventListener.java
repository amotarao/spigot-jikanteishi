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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

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
        RunFlag flag = new RunFlag("PlayerInteractEvent", player);

        if (flag.isActive()) {
            return;
        }
        flag.setActive();

        Action action = e.getAction();
        ItemStack item = player.getInventory().getItemInMainHand();

        // 空気クリック以外中止
        if (!action.equals(Action.LEFT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }

        if (Item.isClock(item)) {
            plugin.game.switchGame(player);
        }
    }

    /**
     * プレイヤーによるEntityに対するクリックの実行
     * 条件が合えば、時間停止対象の追加・削除
     */
    @EventHandler
    private void onPlayerEntityInteract(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        RunFlag flag = new RunFlag("PlayerInteractEntityEvent", player);

        if (flag.isActive()) {
            return;
        }
        flag.setActive();

        Entity target = e.getRightClicked();
        ItemStack item = player.getInventory().getItemInMainHand();

        // プレイヤー以外中止
        if (target.getType() != EntityType.PLAYER) {
            return;
        }

        if (Item.isStick(item)) {
            plugin.game.togglePlayer((Player) target);
        }
    }

    /**
     * プレイヤーの移動
     */
    @EventHandler
    private void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (plugin.game.isStoppingPlayer(player)) {
            e.setCancelled(true);
        }
    }

    /**
     * プレイヤーのスニーク
     */
    @EventHandler
    private void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if (plugin.game.isStoppingPlayer(player)) {
            e.setCancelled(true);
        }
    }

    private class RunFlag {
        String eventName;
        Player player;

        public RunFlag(String EventName, Player player) {
            this.eventName = "RunFlag" + EventName;
            this.player = player;
        }

        /** フラグ追加 */
        private void addFlag() {
            player.setMetadata(eventName, new FixedMetadataValue(plugin, true));
        }

        /** フラグ削除 */
        private void removeFlag() {
            player.removeMetadata(eventName, plugin);
        }

        /** アクティブ状態にセット */
        public void setActive() {
            addFlag();
            new Runnable().runTaskLater(plugin, 1);
        }

        /** アクティブ状態を取得 */
        public boolean isActive() {
            try {
                return (boolean) player.getMetadata(eventName).get(0).value();
            } catch (Exception e) {
                return false;
            }
        }

        private class Runnable extends BukkitRunnable {
            @Override
            public void run() {
                removeFlag();
            }
        }
    }
}
