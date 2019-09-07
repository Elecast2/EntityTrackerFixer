package net.minemora.entitytrackerfixer;

import org.bukkit.plugin.java.JavaPlugin;

import net.minemora.entitytrackerfixer.config.ConfigMain;

public class EntityTrackerFixer extends JavaPlugin {
	
	public static EntityTrackerFixer plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		ConfigMain.getInstance().setup(this);
		new UntrackerTask().runTaskTimer(this, ConfigMain.getUntrackTicks(), ConfigMain.getUntrackTicks());
		new CheckTask().runTaskTimerAsynchronously(this, ConfigMain.getUntrackTicks() + 1, ConfigMain.getCheckFrequency());
	}
}
