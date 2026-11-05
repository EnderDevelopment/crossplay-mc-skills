package com.enderdevelopment.plugin.playerclass;

import com.enderdevelopment.plugin.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 * Triggers class abilities when the ability item is right-clicked and
 * grants passive bonuses tied to the selected class.
 */
public class ClassListener implements Listener {
    private final Main plugin;
    private final ClassManager classes;

    public ClassListener(Main plugin) {
        this.plugin = plugin;
        this.classes = plugin.getClassManager();
    }

    @EventHandler
    public void onAbilityUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (!classes.isClassAbilityItem(event.getItem())) {
            return;
        }
        event.setCancelled(true);
        Player player = event.getPlayer();
        if (!classes.tryAbility(player)) {
            return;
        }
        PlayerClass playerClass = classes.getClass(player);
        if (playerClass == null) {
            return;
        }
        activate(player, playerClass);
    }

    private void activate(Player player, PlayerClass playerClass) {
        switch (playerClass) {
            case ARCHER -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 1));
                player.sendMessage(Component.text("Archer: Speed boost for 10 seconds.").color(NamedTextColor.GREEN));
            }
            case CROSSBOW -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 10, 1));
                player.sendMessage(Component.text("Crossbow: Strength for 10 seconds.").color(NamedTextColor.GREEN));
            }
            case PALADIN -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20 * 8, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 12, 1));
                player.sendMessage(Component.text("Paladin: Resistance and absorption granted.").color(NamedTextColor.GREEN));
            }
            case BERSERKER -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 12, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20 * 12, 0));
                player.sendMessage(Component.text("Berserker: Rage - Strength III, but slower.").color(NamedTextColor.GREEN));
            }
            case WARRIOR -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 10, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20 * 10, 0));
                player.sendMessage(Component.text("Warrior: Strength and resistance.").color(NamedTextColor.GREEN));
            }
            case NINJA -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 20 * 10, 1));
                player.sendMessage(Component.text("Ninja: Super speed and jump boost.").color(NamedTextColor.GREEN));
            }
            case MAGE -> {
                player.getWorld().strikeLightning(player.getTargetBlockExact(30) == null
                        ? player.getLocation()
                        : player.getTargetBlockExact(30).getLocation());
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                player.sendMessage(Component.text("Mage: Lightning strikes your target.").color(NamedTextColor.GREEN));
            }
            case HEALER -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 8, 2));
                for (Entity nearby : player.getNearbyEntities(6, 6, 6)) {
                    if (nearby instanceof Player ally) {
                        ally.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 6, 1));
                    }
                }
                player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1.2, 0), 30, 1, 1, 1);
                player.sendMessage(Component.text("Healer: Regeneration for you and nearby allies.").color(NamedTextColor.GREEN));
            }
        }
    }
}
