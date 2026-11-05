package com.enderdevelopment.plugin.skill;

import com.enderdevelopment.plugin.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles /skill and /skills commands.
 */
public class SkillCommand implements CommandExecutor {
    private final Main plugin;

    public SkillCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        SkillManager skills = plugin.getSkillManager();

        if (command.getName().equalsIgnoreCase("skills")) {
            player.sendMessage(Component.text("Skills:").color(NamedTextColor.GOLD));
            for (Skill skill : Skill.values()) {
                int level = skills.getLevel(player, skill);
                int xp = skills.getXp(player, skill);
                int needed = Math.max(1, skills.xpToNext(level));
                player.sendMessage(Component.text("  " + skill.getDisplayName())
                    .color(NamedTextColor.WHITE)
                    .append(Component.text("  Stufe " + level + "  (" + xp + "/" + needed + " XP)").color(NamedTextColor.GRAY)));
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("skill")) {
            if (args.length == 0) {
                player.sendMessage(Component.text("Usage: /skill <name>").color(NamedTextColor.RED));
                return true;
            }
            Skill skill = findSkill(args[0]);
            if (skill == null) {
                player.sendMessage(Component.text("Unknown skill. Use /skills to list them.").color(NamedTextColor.RED));
                return true;
            }
            int level = skills.getLevel(player, skill);
            int xp = skills.getXp(player, skill);
            int needed = Math.max(1, skills.xpToNext(level));
            player.sendMessage(Component.text(skill.getDisplayName())
                .color(NamedTextColor.GOLD)
                .append(Component.text(": Stufe " + level + " (" + xp + "/" + needed + " XP)").color(NamedTextColor.GRAY)));
            return true;
        }
        return false;
    }

    private Skill findSkill(String input) {
        String needle = input.toLowerCase().replace(" ", "_");
        for (Skill skill : Skill.values()) {
            if (skill.name().toLowerCase().equals(needle)
                    || skill.getDisplayName().toLowerCase().equals(input.toLowerCase())) {
                return skill;
            }
        }
        // partial match
        for (Skill skill : Skill.values()) {
            if (skill.name().toLowerCase().startsWith(needle)) {
                return skill;
            }
        }
        return null;
    }
}
