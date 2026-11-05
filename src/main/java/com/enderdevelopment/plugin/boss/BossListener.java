package com.enderdevelopment.plugin.boss;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

/**
 * Drops custom loot when a boss is killed.
 */
public class BossListener implements Listener {

    @EventHandler
    public void onBossDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Monster monster) || !monster.isCustomNameVisible()) {
            return;
        }
        String name = monster.getCustomName();
        if (name == null || !name.startsWith("Boss")) {
            return;
        }
        event.getDrops().clear();
        event.getDrops().add(new ItemStack(Material.NETHERITE_INGOT, 2));
        event.getDrops().add(new ItemStack(Material.DIAMOND, 4));
        event.getDrops().add(new ItemStack(Material.GOLD_INGOT, 8));
        event.getDrops().add(new ItemStack(Material.ENDER_PEARL, 3));

        Player killer = event.getEntity().getKiller();
        if (killer != null) {
            killer.sendMessage(Component.text("You defeated " + name + "!")
                .color(NamedTextColor.GOLD));
        }
    }
}
