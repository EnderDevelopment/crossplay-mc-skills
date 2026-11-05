package com.enderdevelopment.plugin.skill;

import com.enderdevelopment.plugin.Main;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Grants skill experience whenever a player performs a relevant action.
 */
public class SkillListener implements Listener {
    private final Main plugin;
    private final SkillManager skills;

    public SkillListener(Main plugin) {
        this.plugin = plugin;
        this.skills = plugin.getSkillManager();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material type = block.getType();

        if (isLog(type)) {
            skills.addXp(player, Skill.WOODCUTTING, 15 + (type.name().startsWith("CRIMSON") || type.name().startsWith("WARPED") ? 8 : 0));
            return;
        }
        if (isOre(type)) {
            skills.addXp(player, Skill.MINING, 25);
            return;
        }
        if (isMineral(type)) {
            skills.addXp(player, Skill.MINING, 10);
            return;
        }
        if (type == Material.SHORT_GRASS || type == Material.TALL_GRASS
                || type == Material.FERN || type == Material.LARGE_FERN
                || isFlower(type)) {
            skills.addXp(player, Skill.HERBALISM, 4);
            return;
        }
        if (type == Material.SUSPICIOUS_SAND || type == Material.SUSPICIOUS_GRAVEL) {
            skills.addXp(player, Skill.ARCHAEOLOGY, 20);
            return;
        }
        if (type == Material.SAND || type == Material.GRAVEL) {
            skills.addXp(player, Skill.RUNES, 5);
        }
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent event) {
        skills.addXp(event.getPlayer(), Skill.WOOL_FARMING, 10);
        skills.addXp(event.getPlayer(), Skill.WEAVING, 6);
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            skills.addXp(event.getPlayer(), Skill.FISHING, 20);
        }
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        Player player = findInteractingPlayer(event.getBlock().getLocation());
        if (player == null) {
            return;
        }
        Material result = event.getResult().getType();
        if (isOre(result) || result == Material.IRON_INGOT || result == Material.GOLD_INGOT
                || result == Material.COPPER_INGOT || result == Material.NETHERITE_SCRAP) {
            skills.addXp(player, Skill.SMELTING, 12);
        } else if (result == Material.COOKED_BEEF || result == Material.COOKED_PORKCHOP
                || result == Material.COOKED_CHICKEN || result == Material.COOKED_MUTTON
                || result == Material.COOKED_RABBIT || result == Material.BAKED_POTATO
                || result == Material.COOKED_COD || result == Material.COOKED_SALMON) {
            skills.addXp(player, Skill.COOKING, 10);
        }
    }

    @EventHandler
    public void onInteractBrewingStand(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BREWING_STAND) {
            skills.addXp(event.getPlayer(), Skill.ALCHEMY, 5);
        }
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        skills.addXp(event.getEnchanter(), Skill.ENCHANTING, 15);
    }

    @EventHandler
    public void onCraft(FurnaceExtractEvent event) {
        if (event.getPlayer() instanceof Player player) {
            skills.addXp(player, Skill.SMELTING, 2);
        }
    }

    @EventHandler
    public void onCraftItem(org.bukkit.event.inventory.CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        ItemStack result = event.getCurrentItem();
        if (result == null) {
            return;
        }
        Material type = result.getType();
        if (isLog(type) || type == Material.OAK_PLANKS || type.name().endsWith("_PLANKS")
                || type == Material.STICK || type == Material.OAK_SLAB
                || type == Material.OAK_STAIRS || type == Material.OAK_FENCE
                || type == Material.OAK_DOOR || type == Material.OAK_TRAPDOOR
                || type == Material.OAK_SIGN) {
            skills.addXp(player, Skill.WOOD_PROCESSING, 8);
        } else if (type == Material.STRING || type == Material.WHITE_WOOL
                || type == Material.WHITE_BED || type == Material.WHITE_CARPET) {
            skills.addXp(player, Skill.WEAVING, 7);
        } else if (type == Material.IRON_SWORD || type == Material.IRON_PICKAXE
                || type == Material.IRON_AXE || type == Material.IRON_SHOVEL
                || type == Material.IRON_HOE || type == Material.IRON_HELMET
                || type == Material.IRON_CHESTPLATE || type == Material.IRON_LEGGINGS
                || type == Material.IRON_BOOTS || type == Material.GOLDEN_SWORD
                || type == Material.DIAMOND_SWORD || type == Material.DIAMOND_PICKAXE
                || type.name().startsWith("NETHERITE_")) {
            skills.addXp(player, Skill.SMITHING, 30);
        } else {
            skills.addXp(player, Skill.CRAFTING, 5);
        }
    }

    @EventHandler
    public void onBucketFilled(PlayerBucketEmptyEvent event) {
        if (event.getBucket() == Material.MILK_BUCKET) {
            skills.addXp(event.getPlayer(), Skill.ALCHEMY, 4);
        }
    }

    private Player findInteractingPlayer(org.bukkit.Location loc) {
        double radius = 6.0;
        for (Player online : plugin.getServer().getOnlinePlayers()) {
            if (online.getWorld().equals(loc.getWorld()) && online.getLocation().distanceSquared(loc) <= radius * radius) {
                return online;
            }
        }
        return null;
    }

    private boolean isLog(Material type) {
        return type.name().endsWith("_LOG") || type.name().endsWith("_WOOD");
    }

    private boolean isOre(Material type) {
        return type.name().endsWith("_ORE") || type == Material.NETHER_QUARTZ_ORE
                || type == Material.ANCIENT_DEBRIS;
    }

    private boolean isMineral(Material type) {
        return type == Material.STONE || type == Material.DEEPSLATE
                || type == Material.COBBLESTONE || type == Material.COBBLED_DEEPSLATE
                || type == Material.GRANITE || type == Material.DIORITE
                || type == Material.ANDESITE || type == Material.TUFF;
    }

    private boolean isFlower(Material type) {
        switch (type) {
            case POPPY:
            case DANDELION:
            case BLUE_ORCHID:
            case ALLIUM:
            case AZURE_BLUET:
            case RED_TULIP:
            case ORANGE_TULIP:
            case WHITE_TULIP:
            case PINK_TULIP:
            case OXEYE_DAISY:
            case CORNFLOWER:
            case LILY_OF_THE_VALLEY:
            case WITHER_ROSE:
            case TORCHFLOWER:
            case PINK_PETALS:
                return true;
            default:
                return false;
        }
    }
}
