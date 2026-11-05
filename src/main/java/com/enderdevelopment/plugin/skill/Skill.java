package com.enderdevelopment.plugin.skill;

/**
 * Every skill a player can level up.
 */
public enum Skill {
    WOODCUTTING("Holzabbau"),
    MINING("Bergbau"),
    SMITHING("Schmieden"),
    WOOL_FARMING("Wolle farmen"),
    SMELTING("Schmelzen"),
    FISHING("Angeln"),
    RUNES("Runen"),
    RUNESMITH("Runenschmied"),
    HERBALISM("Kräuterkunde"),
    ARCHAEOLOGY("Archäologie"),
    CRAFTING("Handwerk"),
    WOOD_PROCESSING("Holzverarbeitung"),
    WEAVING("Weben"),
    COOKING("Kochen"),
    ALCHEMY("Alchemie"),
    ENCHANTING("Verzaubern");

    private final String displayName;

    Skill(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
