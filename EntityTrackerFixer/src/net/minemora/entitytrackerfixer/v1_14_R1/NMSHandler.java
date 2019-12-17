package net.minemora.entitytrackerfixer.v1_14_R1;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.plugin.Plugin;

import net.minemora.entitytrackerfixer.config.ConfigMain;
import net.minemora.entitytrackerfixer.nms.NMS;
import net.minemora.entitytrackerfixer.v1_14_R1.entityTick.EntityTickManager;
import net.minemora.entitytrackerfixer.v1_14_R1.entityTick.EntityTickWorldCache;
import net.minemora.entitytrackerfixer.v1_14_R1.tasks.CheckTask;
import net.minemora.entitytrackerfixer.v1_14_R1.tasks.UntrackerTask;

public class NMSHandler implements NMS {

	@Override
	public void startTasks(Plugin plugin) {
		new UntrackerTask().runTaskTimer(plugin, ConfigMain.getUntrackTicks(), ConfigMain.getUntrackTicks());
		new CheckTask().runTaskTimer(plugin, ConfigMain.getUntrackTicks() + 1, ConfigMain.getCheckFrequency());
	}

	@Override
	public void loadWorldCache() {
		for(String worldName : ConfigMain.getWorlds()) {
			if(Bukkit.getWorld(worldName) == null) {
				continue;
			}
			EntityTickManager.getInstance().getCache().put(worldName, 
					new EntityTickWorldCache(((CraftWorld)Bukkit.getWorld(worldName)).getHandle()));
		}
	}

}
