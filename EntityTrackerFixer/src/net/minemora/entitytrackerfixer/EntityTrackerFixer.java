package net.minemora.entitytrackerfixer;

import org.bukkit.plugin.java.JavaPlugin;

import net.minemora.entitytrackerfixer.config.ConfigMain;
import net.minemora.entitytrackerfixer.nms.NMS;
import net.minemora.entitytrackerfixer.nms.NMSCheck;

public class EntityTrackerFixer extends JavaPlugin {
	
	public static EntityTrackerFixer plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		ConfigMain.getInstance().setup(this);
		NMS nms = NMSCheck.getNMS(this);
		nms.startTasks(this);
	}
}
