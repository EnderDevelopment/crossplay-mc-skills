package com.enderdevelopment.plugin.boss;

import com.enderdevelopment.plugin.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Spawns three map bosses on the boss world every ten minutes and two
 * world bosses per day in the overworld.
 */
public class BossManager implements CommandExecutor {
    private static final int MAP_BOSSES = 3;
    private static final long MAP_INTERVAL_TICKS = 12000L; // 10 minutes
    private static final long DAY = 432000L; // 12 hours in ticks -> two world bosses per day

    private final Main plugin;
    private int worldBossSpawn = 0;

    public BossManager(Main plugin) {
        this.plugin = plugin;
    }

    public void startScheduler() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::spawnMapBosses, 200L, MAP_INTERVAL_TICKS);
        Bukkit.getScheduler().runTaskTimer(plugin, this::spawnWorldBoss, 200L, DAY);
    }

    private void spawnMapBosses() {
        World world = getBossWorld();
        if (world == null) {
            return;
        }
        broadcast(Component.text("Map bosses are spawning in the Boss World!").color(NamedTextColor.RED));
        Location spawn = world.getSpawnLocation();
        for (int i = 0; i < MAP_BOSSES; i++) {
            Location loc = randomAround(spawn, 25);
            spawnBoss(loc, "Boss: Map " + (i + 1), 120.0, 12.0);
        }
    }

    private void spawnWorldBoss() {
        worldBossSpawn++;
        World world = Bukkit.getWorlds().get(0); // main overworld
        if (world == null) {
            return;
        }
        broadcast(Component.text("A World Boss has appeared in the overworld!").color(NamedTextColor.DARK_RED));
        spawnBoss(randomAround(world.getSpawnLocation(), 60), "Boss: World " + worldBossSpawn, 500.0, 30.0);
    }

    private void spawnBoss(Location loc, String name, double health, double damage) {
        Zombie boss = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
        boss.setCustomName(name);
        boss.setCustomNameVisible(true);
        boss.setRemoveWhenFarAway(false);
        boss.setPersistent(true);
        org.bukkit.attribute.AttributeInstance maxHealth = boss.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.setBaseValue(health);
        }
        boss.setHealth(boss.getAttribute(Attribute.MAX_HEALTH).getValue());
        org.bukkit.attribute.AttributeInstance attackDamage = boss.getAttribute(Attribute.ATTACK_DAMAGE);
        if (attackDamage != null) {
            attackDamage.setBaseValue(damage);
        }
        boss.setBaby(false);
    }

    private Location randomAround(Location center, int radius) {
        double angle = ThreadLocalRandom.current().nextDouble(Math.PI * 2);
        double dist = ThreadLocalRandom.current().nextDouble(radius);
        return center.clone().add(Math.cos(angle) * dist, 0, Math.sin(angle) * dist);
    }

    private World getBossWorld() {
        World world = Bukkit.getWorld("boss");
        if (world == null) {
            WorldCreator creator = new WorldCreator("boss");
            creator.generateStructures(false);
            world = Bukkit.createWorld(creator);
        }
        return world;
    }

    private void broadcast(Component message) {
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(message));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(Component.text("Boss spawn schedule:").color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("  • 3 map bosses spawn every 10 minutes in the Boss World.")
            .color(NamedTextColor.WHITE));
        sender.sendMessage(Component.text("  • 2 world bosses spawn per day in the overworld.")
            .color(NamedTextColor.WHITE));
        return true;
    }
}
