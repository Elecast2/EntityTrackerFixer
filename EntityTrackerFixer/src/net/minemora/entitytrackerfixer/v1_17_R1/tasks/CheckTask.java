package net.minemora.entitytrackerfixer.v1_17_R1.tasks;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.level.ChunkProviderServer;
import net.minecraft.server.level.WorldServer;
import net.minemora.entitytrackerfixer.config.ConfigMain;
import net.minemora.entitytrackerfixer.v1_17_R1.NMSEntityTracker;
import net.minemora.entitytrackerfixer.v1_17_R1.entityTick.EntityTickManager;

public class CheckTask extends BukkitRunnable {

	@Override
	public void run() {
		if(UntrackerTask.isRunning()) {
			return;
		}
		if(ConfigMain.isEnableOnAllWorlds()) {
			for(World world : Bukkit.getWorlds()) {
				checkWorld(world.getName());
			}
		}
		else {
			for(String worldName : ConfigMain.getWorlds()) {
				if(Bukkit.getWorld(worldName) == null) {
					continue;
				}
				checkWorld(worldName);
			}
		}
	}
	
	public void checkWorld(String worldName) {
		WorldServer ws = ((CraftWorld)Bukkit.getWorld(worldName)).getHandle();
		ChunkProviderServer cps = ws.getChunkProvider();
		
		Set<net.minecraft.world.entity.Entity> trackAgain = new HashSet<>();
		
		int d = ConfigMain.getTrackingRange();
		for(Player player : Bukkit.getWorld(worldName).getPlayers()) {
			for(Entity ent : player.getNearbyEntities(d, d, d)) {
				net.minecraft.world.entity.Entity nms = ((CraftEntity)ent).getHandle();
				if(cps.a.G.containsKey(nms.getId()) || !nms.valid) {
					continue;
				}
				trackAgain.add(nms);
			}
		}
		NMSEntityTracker.trackEntities(cps, trackAgain);
		if(ConfigMain.isDisableTickUntracked()) {
			EntityTickManager.getInstance().enableTicking(trackAgain);
		}
	}

}