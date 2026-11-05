package com.enderdevelopment.plugin.skill;

import com.enderdevelopment.plugin.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * Stores and advances the skill level and experience of each player
 * using persistent data on the player.
 */
public class SkillManager {
    private static final int MAX_LEVEL = 100;

    private final Main plugin;

    public SkillManager(Main plugin) {
        this.plugin = plugin;
    }

    private NamespacedKey levelKey(Skill skill) {
        return new NamespacedKey(plugin, "skill_" + skill.name().toLowerCase());
    }

    private NamespacedKey xpKey(Skill skill) {
        return new NamespacedKey(plugin, "xp_" + skill.name().toLowerCase());
    }

    public int getLevel(Player player, Skill skill) {
        Integer level = player.getPersistentDataContainer().get(levelKey(skill), PersistentDataType.INTEGER);
        return level == null ? 1 : Math.min(level, MAX_LEVEL);
    }

    public int getXp(Player player, Skill skill) {
        Integer xp = player.getPersistentDataContainer().get(xpKey(skill), PersistentDataType.INTEGER);
        return xp == null ? 0 : xp;
    }

    public int xpToNext(int level) {
        return 50 + (level - 1) * 25;
    }

    /**
     * Adds experience to the player for a skill and handles level ups.
     * Returns true when at least one level up happened.
     */
    public boolean addXp(Player player, Skill skill, int amount) {
        int level = getLevel(player, skill);
        int xp = getXp(player, skill) + amount;
        boolean leveled = false;
        while (level < MAX_LEVEL && xp >= xpToNext(level)) {
            xp -= xpToNext(level);
            level++;
            leveled = true;
        }
        if (level >= MAX_LEVEL) {
            xp = 0;
        }
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        pdc.set(levelKey(skill), PersistentDataType.INTEGER, level);
        pdc.set(xpKey(skill), PersistentDataType.INTEGER, xp);
        if (leveled) {
            player.sendMessage(
                Component.text("+" + skill.getDisplayName() + " ist jetzt Stufe ")
                    .color(NamedTextColor.GOLD)
                    .append(Component.text(level).color(NamedTextColor.GREEN))
            );
        }
        return leveled;
    }
}
