package net.minemora.entitytrackerfixer.v1_17_R1;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import net.minemora.entitytrackerfixer.config.ConfigMain;
import net.minemora.entitytrackerfixer.nms.NMS;
import net.minemora.entitytrackerfixer.v1_17_R1.tasks.CheckTask;
import net.minemora.entitytrackerfixer.v1_17_R1.tasks.UntrackerTask;

public class NMSHandler implements NMS {

	@Override
	public BukkitTask startUntrackerTask(Plugin plugin) {
		return new UntrackerTask().runTaskTimer(plugin, ConfigMain.getUntrackTicks(), ConfigMain.getUntrackTicks());
	}

	@Override
	public BukkitTask startUCheckTask(Plugin plugin) {
		return new CheckTask().runTaskTimer(plugin, ConfigMain.getUntrackTicks() + 1, ConfigMain.getCheckFrequency());
	}

}
