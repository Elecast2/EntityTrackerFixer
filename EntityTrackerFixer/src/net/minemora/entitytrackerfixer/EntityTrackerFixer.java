package net.minemora.entitytrackerfixer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.minemora.entitytrackerfixer.config.ConfigMain;
import net.minemora.entitytrackerfixer.entitytick.EntityTickManager;
import net.minemora.entitytrackerfixer.entitytick.EntityTickWorldCache;

public class EntityTrackerFixer extends JavaPlugin {
	
	public static EntityTrackerFixer plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		ConfigMain.getInstance().setup(this);
		if(ConfigMain.isDisableTickUntracked()) {
			for(String worldName : ConfigMain.getWorlds()) {
				if(Bukkit.getWorld(worldName) == null) {
					continue;
				}
				EntityTickManager.getInstance().getCache().put(worldName, new EntityTickWorldCache(worldName));
			}
		}
		new UntrackerTask().runTaskTimer(this, ConfigMain.getUntrackTicks(), ConfigMain.getUntrackTicks());
		new CheckTask().runTaskTimer(this, ConfigMain.getUntrackTicks() + 1, ConfigMain.getCheckFrequency());
	}
}
