package com.enderdevelopment.plugin.playerclass;

import com.enderdevelopment.plugin.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/**
 * Stores and manages the selected class of each player and gives the
 * ability item that triggers the class skill.
 */
public class ClassManager implements CommandExecutor {
    private static final long ABILITY_COOLDOWN_MS = 20L * 1000L; // 20 seconds

    private final Main plugin;

    public ClassManager(Main plugin) {
        this.plugin = plugin;
    }

    public NamespacedKey classKey() {
        return new NamespacedKey(plugin, "player_class");
    }

    public PlayerClass getClass(Player player) {
        String name = player.getPersistentDataContainer().get(classKey(), PersistentDataType.STRING);
        if (name == null) {
            return null;
        }
        try {
            return PlayerClass.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void setClass(Player player, PlayerClass playerClass) {
        player.getPersistentDataContainer().set(classKey(), PersistentDataType.STRING, playerClass.name());
    }

    public long getCooldown(Player player) {
        Long c = player.getPersistentDataContainer().get(cooldownKey(player), PersistentDataType.LONG);
        return c == null ? 0 : c;
    }

    private NamespacedKey cooldownKey(Player player) {
        return new NamespacedKey(plugin, "ability_cd_" + player.getUniqueId().toString());
    }

    public boolean tryAbility(Player player) {
        PlayerClass playerClass = getClass(player);
        if (playerClass == null) {
            player.sendMessage(Component.text("You have not chosen a class yet. Use /class to pick one.").color(NamedTextColor.RED));
            return false;
        }
        long now = System.currentTimeMillis();
        long cooldownUntil = getCooldown(player);
        if (cooldownUntil > now) {
            long remaining = (cooldownUntil - now) / 1000L;
            player.sendMessage(Component.text("Ability recharging. Wait " + remaining + "s.").color(NamedTextColor.GRAY));
            return false;
        }
        player.getPersistentDataContainer().set(cooldownKey(player), PersistentDataType.LONG, now + ABILITY_COOLDOWN_MS);
        return true;
    }

    public void giveAbilityItem(Player player, PlayerClass playerClass) {
        Material material = Material.matchMaterial(playerClass.getAbilityItem());
        if (material == null) {
            return;
        }
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(playerClass.getDisplayName() + " Skill")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD));
            meta.lore(List.of(
                Component.text("Right-click to use your class ability.").color(NamedTextColor.GRAY),
                Component.text("Cooldown: 20 seconds").color(NamedTextColor.DARK_GRAY)
            ));
            meta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "class_ability_item"),
                PersistentDataType.STRING,
                playerClass.name()
            );
            item.setItemMeta(meta);
        }
        player.getInventory().addItem(item);
    }

    public boolean isClassAbilityItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.has(new NamespacedKey(plugin, "class_ability_item"), PersistentDataType.STRING);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("classes")) {
            sender.sendMessage(Component.text("Available classes:").color(NamedTextColor.GOLD));
            for (PlayerClass playerClass : PlayerClass.values()) {
                PlayerClass current = sender instanceof Player p ? getClass(p) : null;
                Component line = Component.text("  " + playerClass.getDisplayName()).color(NamedTextColor.WHITE);
                if (current == playerClass) {
                    line = line.append(Component.text("  [active]").color(NamedTextColor.GREEN));
                }
                sender.sendMessage(line);
            }
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("class")) {
            if (args.length == 0) {
                PlayerClass current = getClass(player);
                if (current == null) {
                    player.sendMessage(Component.text("You have not chosen a class. Use /class <name>.").color(NamedTextColor.GOLD));
                } else {
                    player.sendMessage(Component.text("Your class: " + current.getDisplayName()).color(NamedTextColor.GOLD));
                }
                return true;
            }
            PlayerClass playerClass = findClass(args[0]);
            if (playerClass == null) {
                player.sendMessage(Component.text("Unknown class. Use /classes to list them.").color(NamedTextColor.RED));
                return true;
            }
            setClass(player, playerClass);
            giveAbilityItem(player, playerClass);
            player.sendMessage(Component.text("You chose the class " + playerClass.getDisplayName() + ".")
                .color(NamedTextColor.GREEN));
            return true;
        }
        return false;
    }

    private PlayerClass findClass(String input) {
        String needle = input.toLowerCase().replace(" ", "_");
        for (PlayerClass playerClass : PlayerClass.values()) {
            if (playerClass.name().toLowerCase().equals(needle)
                    || playerClass.getDisplayName().toLowerCase().equals(input.toLowerCase())) {
                return playerClass;
            }
        }
        for (PlayerClass playerClass : PlayerClass.values()) {
            if (playerClass.name().toLowerCase().startsWith(needle)) {
                return playerClass;
            }
        }
        return null;
    }
}
