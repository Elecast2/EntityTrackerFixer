package net.minemora.entitytrackerfixer;

import org.bukkit.plugin.java.JavaPlugin;

import net.minemora.entitytrackerfixer.config.ConfigMain;
import net.minemora.entitytrackerfixer.listener.ChunkEventListener;

public class EntityTrackerFixer extends JavaPlugin {
	
	public static EntityTrackerFixer plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		ConfigMain.getInstance().setup(this);
		new UntrackerTask().runTaskTimerAsynchronously(this, ConfigMain.getUntrackTicks(), ConfigMain.getUntrackTicks());
		new CheckTask().runTaskTimerAsynchronously(this, ConfigMain.getUntrackTicks() + 1, ConfigMain.getCheckFrequency());
		getServer().getPluginManager().registerEvents(new ChunkEventListener(), this);
	}
}
