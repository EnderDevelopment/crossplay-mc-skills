package com.enderdevelopment.plugin.playerclass;

/**
 * The playable classes and the item used to trigger each class ability.
 */
public enum PlayerClass {
    ARCHER("Bogenschütze", "BOW"),
    CROSSBOW("Armbrust", "CROSSBOW"),
    PALADIN("Paladin", "SHIELD"),
    BERSERKER("Berserker", "IRON_AXE"),
    WARRIOR("Krieger", "IRON_SWORD"),
    NINJA("Ninja", "IRON_SHOVEL"),
    MAGE("Magier", "BLAZE_ROD"),
    HEALER("Heiler", "GOLDEN_APPLE");

    private final String displayName;
    private final String abilityItem;

    PlayerClass(String displayName, String abilityItem) {
        this.displayName = displayName;
        this.abilityItem = abilityItem;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAbilityItem() {
        return abilityItem;
    }
}
