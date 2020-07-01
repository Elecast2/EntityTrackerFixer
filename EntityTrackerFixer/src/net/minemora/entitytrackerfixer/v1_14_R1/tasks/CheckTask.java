package net.minemora.entitytrackerfixer.v1_14_R1.tasks;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_14_R1.ChunkProviderServer;
import net.minecraft.server.v1_14_R1.WorldServer;
import net.minemora.entitytrackerfixer.config.ConfigMain;
import net.minemora.entitytrackerfixer.v1_14_R1.NMSEntityTracker;

public class CheckTask extends BukkitRunnable {

	@Override
	public void run() {
		if(UntrackerTask.isRunning()) {
			return;
		}
		for(String worldName : ConfigMain.getWorlds()) {
			if(Bukkit.getWorld(worldName) == null) {
				continue;
			}
			checkWorld(worldName);
		}
	}
	
	public void checkWorld(String worldName) {
		WorldServer ws = ((CraftWorld)Bukkit.getWorld(worldName)).getHandle();
		ChunkProviderServer cps = ws.getChunkProvider();
		
		Set<net.minecraft.server.v1_14_R1.Entity> trackAgain = new HashSet<>();
		
		int d = ConfigMain.getTrackingRange();
		for(Player player : Bukkit.getWorld(worldName).getPlayers()) {
			for(Entity ent : player.getNearbyEntities(d, d, d)) {
				trackAgain.add(((CraftEntity)ent).getHandle());
			}
		}
		NMSEntityTracker.trackEntities(cps, trackAgain);
	}

}