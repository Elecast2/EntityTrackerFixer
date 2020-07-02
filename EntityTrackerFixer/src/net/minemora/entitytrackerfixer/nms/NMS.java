package net.minemora.entitytrackerfixer.nms;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public interface NMS {
	
	public BukkitTask startUntrackerTask(Plugin plugin);
	
	public BukkitTask startUCheckTask(Plugin plugin);

}
