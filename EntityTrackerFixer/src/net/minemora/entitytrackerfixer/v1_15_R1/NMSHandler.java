package net.minemora.entitytrackerfixer.v1_15_R1;

import org.bukkit.plugin.Plugin;

import net.minemora.entitytrackerfixer.config.ConfigMain;
import net.minemora.entitytrackerfixer.nms.NMS;
import net.minemora.entitytrackerfixer.v1_15_R1.tasks.CheckTask;
import net.minemora.entitytrackerfixer.v1_15_R1.tasks.UntrackerTask;

public class NMSHandler implements NMS {

	@Override
	public void startTasks(Plugin plugin) {
		new UntrackerTask().runTaskTimer(plugin, ConfigMain.getUntrackTicks(), ConfigMain.getUntrackTicks());
		new CheckTask().runTaskTimer(plugin, ConfigMain.getUntrackTicks() + 1, ConfigMain.getCheckFrequency());
	}

}
