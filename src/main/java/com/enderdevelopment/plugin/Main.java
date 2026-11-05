package com.enderdevelopment.plugin;

import com.enderdevelopment.plugin.boss.BossListener;
import com.enderdevelopment.plugin.boss.BossManager;
import com.enderdevelopment.plugin.map.WorldManager;
import com.enderdevelopment.plugin.playerclass.ClassListener;
import com.enderdevelopment.plugin.playerclass.ClassManager;
import com.enderdevelopment.plugin.skill.SkillCommand;
import com.enderdevelopment.plugin.skill.SkillListener;
import com.enderdevelopment.plugin.skill.SkillManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for EnderSurvival: skills, classes, maps and bosses.
 */
public final class Main extends JavaPlugin {
    private static Main instance;

    private SkillManager skillManager;
    private ClassManager classManager;
    private BossManager bossManager;
    private WorldManager worldManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        skillManager = new SkillManager(this);
        classManager = new ClassManager(this);
        bossManager = new BossManager(this);
        worldManager = new WorldManager(this);

        Bukkit.getPluginManager().registerEvents(new SkillListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ClassListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BossListener(), this);

        getCommand("class").setExecutor(classManager);
        getCommand("classes").setExecutor(classManager);
        getCommand("skill").setExecutor(new SkillCommand(this));
        getCommand("skills").setExecutor(new SkillCommand(this));
        getCommand("boss").setExecutor(bossManager);
        getCommand("farm").setExecutor(worldManager);

        bossManager.startScheduler();
        worldManager.startRunnable();

        getLogger().info("EnderSurvival enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("EnderSurvival disabled.");
    }

    public static Main getInstance() {
        return instance;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public ClassManager getClassManager() {
        return classManager;
    }

    public BossManager getBossManager() {
        return bossManager;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }
}
