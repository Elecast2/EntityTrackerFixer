package net.minemora.entitytrackerfixer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_14_R1.ChunkProviderServer;
import net.minecraft.server.v1_14_R1.WorldServer;
import net.minemora.entitytrackerfixer.config.ConfigMain;
import net.minemora.entitytrackerfixer.util.Util;

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
			if(UntrackedEntitiesCache.getInstance().isEmpty(worldName)) {
				continue;
			}
			checkWorld(worldName);
		}
	}
	
	public void checkWorld(String worldName) {
		WorldServer ws = ((CraftWorld)Bukkit.getWorld(worldName)).getHandle();
		ChunkProviderServer cps = ws.getChunkProvider();
		
		Set<UUID> toRemove = new HashSet<>();
		Set<net.minecraft.server.v1_14_R1.Entity> trackAgain = new HashSet<>();
		
        Iterator<UntrackedEntity> it = UntrackedEntitiesCache.getInstance().getCache(worldName).values().iterator();
        while (it.hasNext()) {
        	UntrackedEntity ute = it.next();
        	net.minecraft.server.v1_14_R1.Entity nmsEnt = ute.getEntity();
        	UUID uid = nmsEnt.getUniqueID();
        	if(cps.playerChunkMap.trackedEntities.containsKey(nmsEnt.getId())) {
        		//System.out.println("removed (et contains): " + nmsEnt.getBukkitEntity().getType().name());
        		toRemove.add(uid);
		    	continue;
		    }
        	World world = nmsEnt.getBukkitEntity().getWorld();
			Location loc = nmsEnt.getBukkitEntity().getLocation();
			if(!Util.isChunkLoaded(ws, loc.getBlockX() >> 4, loc.getBlockZ() >> 4)) {
				//System.out.println("removed (unloaded chunk x:"+(loc.getBlockX() >> 4)+" z:"+(loc.getBlockZ() >> 4)+"): " + nmsEnt.getBukkitEntity().getType().name());
				UntrackedEntitiesCache.getInstance().addUFC(worldName, uid);
				toRemove.add(uid);
				continue;
			}
			if(!worldName.equals(world.getName())) {
				//System.out.println("removed (different world): " + nmsEnt.getBukkitEntity().getType().name());
				toRemove.add(uid);
				continue;
			}
			if(nmsEnt.getBukkitEntity().isDead()) {
				//System.out.println("removed (is dead): " + nmsEnt.getBukkitEntity().getType().name());
				toRemove.add(uid);
				continue;
			}
			boolean track = false;
			int d = ConfigMain.getTrackingRange();
			
			List<Entity> ents;
			try {
				ents = nmsEnt.getBukkitEntity().getNearbyEntities(d, d, d);
			} catch (NoSuchElementException e) {
				continue;
			}
			
			if(ents.isEmpty()) {
				continue;
			}
			for(Entity le : ents) {
				if(le == null) {
					continue;
				}
				if(le instanceof Player) {
					track = true;
					break;
				}
			}
			if(track) {
				trackAgain.add(nmsEnt);
			}
        }
		
		UntrackedEntitiesCache.getInstance().removeAll(toRemove, worldName);

		new BukkitRunnable() {
			@Override
			public void run() {
				NMSEntityTracker.trackEntities(cps, trackAgain);
			}
		}.runTask(EntityTrackerFixer.plugin);
	}

}