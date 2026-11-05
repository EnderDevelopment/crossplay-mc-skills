package com.enderdevelopment.plugin.map;

import com.enderdevelopment.plugin.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Manages the Farming World map.
 */
public class WorldManager implements CommandExecutor {
    private final Main plugin;
    private World farmingWorld;

    public WorldManager(Main plugin) {
        this.plugin = plugin;
    }

    public World getFarmingWorld() {
        if (farmingWorld == null || farmingWorld.getPlayers().isEmpty()) {
            World existing = Bukkit.getWorld("farming");
            if (existing != null) {
                farmingWorld = existing;
            } else {
                WorldCreator creator = new WorldCreator("farming");
                creator.type(WorldType.FLAT);
                creator.generateStructures(false);
                World created = Bukkit.createWorld(creator);
                if (created != null) {
                    created.setSpawnFlags(true, true);
                    created.setMonsterSpawnLimit(0);
                    farmingWorld = created;
                }
            }
        }
        return farmingWorld;
    }

    public void startRunnable() {
        // Nothing periodic needed for the farming world.
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        World target = getFarmingWorld();
        if (target == null) {
            player.sendMessage(Component.text("Could not load the Farming World.").color(NamedTextColor.RED));
            return true;
        }
        player.teleport(target.getSpawnLocation());
        player.sendMessage(Component.text("Welcome to the Farming World.").color(NamedTextColor.GREEN));
        return true;
    }
}
