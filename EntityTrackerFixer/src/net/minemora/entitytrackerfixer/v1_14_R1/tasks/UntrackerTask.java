package net.minemora.entitytrackerfixer.v1_14_R1.tasks;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_14_R1.ChunkProviderServer;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.MinecraftServer;
import net.minecraft.server.v1_14_R1.WorldServer;
import net.minecraft.server.v1_14_R1.PlayerChunkMap.EntityTracker;
import net.minecraft.server.v1_14_R1.EntityArmorStand;
import net.minecraft.server.v1_14_R1.EntityEnderDragon;
import net.minecraft.server.v1_14_R1.EntityItemFrame;
import net.minemora.entitytrackerfixer.EntityTrackerFixer;
import net.minemora.entitytrackerfixer.config.ConfigMain;
import net.minemora.entitytrackerfixer.util.ReflectionUtils;
import net.minemora.entitytrackerfixer.v1_14_R1.entityTick.EntityTickManager;

public class UntrackerTask extends BukkitRunnable {
	
	private static boolean running = false;
	
	private static Field trackerField;
	
	static {
		try {
			trackerField = ReflectionUtils.getClassPrivateField(EntityTracker.class, "tracker");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

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
		Set<Integer> toRemove = new HashSet<>();
		int removed = 0;
		WorldServer ws = ((CraftWorld)Bukkit.getWorld(worldName)).getHandle();
		ChunkProviderServer cps = ws.getChunkProvider();

		try {
	        for(EntityTracker et : cps.playerChunkMap.trackedEntities.values()) {
	           net.minecraft.server.v1_14_R1.Entity nmsEnt = (net.minecraft.server.v1_14_R1.Entity) trackerField.get(et);
	           if(nmsEnt instanceof EntityPlayer || nmsEnt instanceof EntityItemFrame || nmsEnt instanceof EntityEnderDragon) {
        		   continue;
        	   }
	           if(nmsEnt instanceof EntityArmorStand && nmsEnt.getBukkitEntity().getCustomName() != null) {
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
	           if(remove) {
	        	   //System.out.println("untracked: " + nmsEnt.getBukkitEntity().getType().name());
	        	   toRemove.add(nmsEnt.getId());
	        	   removed++;
	           }
	        }
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		for(int id : toRemove) {
			cps.playerChunkMap.trackedEntities.remove(id);
			if(ConfigMain.isDisableTickUntracked()) {
				EntityTickManager.getInstance().disableTicking(id, worldName);
			}
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
        
        //System.out.println("cache now contains " + UntrackedEntitiesCache.getInstance().getCache(worldName).size() + " entities");
	}
	
	public static boolean isRunning() {
		return running;
	}

}
