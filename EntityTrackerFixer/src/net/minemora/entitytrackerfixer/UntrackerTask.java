package net.minemora.entitytrackerfixer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_14_R1.ChunkProviderServer;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.MinecraftServer;
import net.minecraft.server.v1_14_R1.WorldServer;
import net.minecraft.server.v1_14_R1.PlayerChunkMap.EntityTracker;
import net.minemora.entitytrackerfixer.config.ConfigMain;
import net.minemora.entitytrackerfixer.util.ReflectionUtils;
import net.minemora.entitytrackerfixer.util.Util;

public class UntrackerTask extends BukkitRunnable {
	
	private static boolean running = false;

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if(MinecraftServer.getServer().recentTps[0] > ConfigMain.getMinTps()) {
			//String tps = String.format("%.2f", MinecraftServer.getServer().recentTps[0]);
			//EntityTrackerFixer.plugin.getLogger().info("Not untraking because tps = " + tps);
			return;
		}
		running = true;
		for(String worldName : ConfigMain.getWorlds()) {
			untrackProcess(worldName);
		}
		running = false;
	}
	
	private void untrackProcess(String worldName) {
		if(Bukkit.getWorld(worldName) == null) {
			return;
		}
		//Set<net.minecraft.server.v1_14_R1.Entity> toRemove = new HashSet<>();
		int removed = 0;
		WorldServer ws = ((CraftWorld)Bukkit.getWorld(worldName)).getHandle();
		ChunkProviderServer cps = ws.getChunkProvider();

        ObjectIterator<EntityTracker> objectiterator = cps.playerChunkMap.trackedEntities.values().iterator();
		try {
	        while (objectiterator.hasNext()) {
	           Object iterobj = objectiterator.next();
	           if(iterobj == null) {
	        	   objectiterator.remove();
	        	   continue;
	           }
	           EntityTracker et = (EntityTracker) iterobj;
	           net.minecraft.server.v1_14_R1.Entity nmsEnt = (net.minecraft.server.v1_14_R1.Entity) 
        			   ReflectionUtils.getPrivateField(et.getClass(), et, "tracker");
	           if(nmsEnt instanceof EntityPlayer) {
        		   continue;
        	   }
	           if(nmsEnt.getBukkitEntity().getCustomName() != null) {
        		   continue;
        	   }
	           boolean remove = false;
	           if(et.trackedPlayers.size() == 0) {
	        	   remove = true;
	           }
	           else if(et.trackedPlayers.size() == 1) {
	        	   for(EntityPlayer ep : et.trackedPlayers) {
	        		   if(!ep.getBukkitEntity().isOnline()) {
		        		   remove = true;
		        	   }
	               }
	        	   if(!remove) {
	        		   continue;
	        	   }
	           }
	           Location loc = nmsEnt.getBukkitEntity().getLocation();
	           if(!Util.isChunkLoaded(ws, loc.getBlockX() >> 4, loc.getBlockZ() >> 4)) {
	        	   UntrackedEntitiesCache.getInstance().addUFC(worldName, nmsEnt.getId());
        	   }
	           if(remove) {
	        	   //System.out.println("untracked: " + nmsEnt.getBukkitEntity().getType().name());
	        	   //toRemove.add(nmsEnt);
	        	   objectiterator.remove();
	        	   removed++;
	        	   UntrackedEntitiesCache.getInstance().add(nmsEnt);
	           }
	        }
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		/*
		new BukkitRunnable() {
			@Override
			public void run() {
				NMSEntityTracker.untrackEntities(cps, toRemove);
			}
		}.runTask(EntityTrackerFixer.plugin);
		*/
		
        if(ConfigMain.isLogToConsole()) {
        	EntityTrackerFixer.plugin.getLogger().info("Untracked " + removed + " entities in " + worldName);
        }
	}
	
	public static boolean isRunning() {
		return running;
	}

}
