# EnderSurvival Minecraft Plugin

EnderSurvival is a Minecraft plugin for Paper servers that adds RPG-style skills, player classes, custom boss encounters, and a dedicated Farming World. It works without any client-side mods and is compatible with Java Edition, Bedrock Edition (via crossplay plugins), and consoles that support Minecraft servers. The plugin includes 16 levelable skills such as Woodcutting, Mining, Smithing, Fishing, Alchemy, and Enchanting, 8 player classes with unique abilities (Archer, Crossbow, Paladin, Berserker, Warrior, Ninja, Mage, Healer), map bosses that spawn every 10 minutes, world bosses that spawn twice daily, and a flat Farming World for resource gathering. All data is stored persistently on each player, and the plugin is designed to run on a standard Ubuntu server with an Intel i9 and 32 GB RAM.

## Install

1. Download the latest `plugin.jar` from the release page.
2. Stop your Paper server (version 1.21 or newer).
3. Place the `plugin.jar` file into the `plugins` folder of your server directory.
4. Restart your server. The plugin will create its default configuration files automatically.

## Use

### Selecting a class
- Use `/class <name>` to choose a class (e.g., `/class mage`).
- Use `/classes` to list all available classes.
- After choosing a class, you receive a special ability item in your inventory.
- Right-click the ability item to activate your class skill (20-second cooldown).

### Checking skills
- Use `/skills` to see all skills and your current levels and experience.
- Use `/skill <name>` to check a specific skill (e.g., `/skill mining`).
- Skills level up automatically as you perform relevant actions: breaking blocks, fishing, smelting, crafting, enchanting, shearing, and more.

### Boss encounters
- Three map bosses spawn on the Boss World every 10 minutes.
- Two world bosses spawn in the overworld per day (every 12 hours).
- Bosses have high health and damage, and drop valuable loot including Netherite Ingots, Diamonds, Gold Ingots, and Ender Pearls.
- Use `/boss` to see the current spawn schedule.

### Farming World
- Use `/farm` to teleport to the Farming World.
- This flat world has no monster spawns, making it safe for farming and resource collection.
- The world is automatically generated on first use.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/f293e446-47f6-4b93-b76f-0ec02a23b61d?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=f293e446-47f6-4b93-b76f-0ec02a23b61d
