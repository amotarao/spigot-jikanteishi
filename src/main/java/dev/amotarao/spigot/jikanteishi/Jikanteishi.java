package dev.amotarao.spigot.jikanteishi;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
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
        World world = player.getWorld();
        spawnParticleForPlayers(world, world.getPlayers());
        active = true;
        Bukkit.broadcastMessage("start");
    }

    private void stop() {
        members.clear();
        active = false;
        Bukkit.broadcastMessage("stop");
    }

    private void stop(Player player) {
        World world = player.getWorld();
        spawnParticleForPlayers(world, world.getPlayers());
        stop();
    }

    private void spawnParticleForPlayer(World world, Player player) {
        if (members.indexOf(player.getUniqueId().toString()) >= 0) {
            return;
        }
        Location location = player.getLocation();
        location.setY(location.getY() + 1);
        world.spawnParticle(Particle.SPELL_INSTANT, location, 20, 0, 0, 0, 0.05);
    }

    private void spawnParticleForPlayers(World world, List<Player> players) {
        for (Player player : players) {
            spawnParticleForPlayer(world, player);
        }
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

        if (item.getType() == Material.CLOCK) {
            if (!active) {
                start(player);
            } else {
                stop(player);
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
        String targetUuid = targetPlayer.getUniqueId().toString();

        if (item.getType() == Material.STICK) {
            int index = members.indexOf(targetUuid);

            spawnParticleForPlayer(player.getWorld(), targetPlayer);

            if (index < 0) {
                members.add(targetUuid);
            } else {
                members.remove(index);
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
