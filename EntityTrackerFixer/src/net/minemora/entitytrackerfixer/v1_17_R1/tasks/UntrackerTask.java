package net.minemora.entitytrackerfixer.v1_17_R1.tasks;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.server.network.ServerPlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.level.ChunkProviderServer;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.boss.EntityComplexPart;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderDragon;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.level.PlayerChunkMap.EntityTracker;
import net.minemora.entitytrackerfixer.EntityTrackerFixer;
import net.minemora.entitytrackerfixer.config.ConfigMain;
import net.minemora.entitytrackerfixer.util.ReflectionUtils;
import net.minemora.entitytrackerfixer.v1_17_R1.entityTick.EntityTickManager;

public class UntrackerTask extends BukkitRunnable {
	
	private static boolean running = false;
	
	private static Field trackerField;
	
	static {
		try {
			trackerField = ReflectionUtils.getClassPrivateField(EntityTracker.class, "c");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "deprecation", "resource" })
	@Override
	public void run() {
		if(MinecraftServer.getServer().recentTps[0] > ConfigMain.getMinTps()) {
			return;
		}
		running = true;
		if(ConfigMain.isEnableOnAllWorlds()) {
			for(World world : Bukkit.getWorlds()) {
				untrackProcess(world.getName());
			}
		}
		else {
			for(String worldName : ConfigMain.getWorlds()) {
				untrackProcess(worldName);
			}
		}
		
		running = false;
	}

	private void untrackProcess(String worldName) {
		if(Bukkit.getWorld(worldName) == null) {
			return;
		}
		//Set<net.minecraft.server.v1_14_R2.Entity> toRemove = new HashSet<>();
		Set<Integer> toRemove = new HashSet<>();
		int removed = 0;
		WorldServer ws = ((CraftWorld)Bukkit.getWorld(worldName)).getHandle();
		ChunkProviderServer cps = ws.getChunkProvider();

		try {
	        for(EntityTracker et : cps.a.G.values()) {
	           net.minecraft.world.entity.Entity nmsEnt = (net.minecraft.world.entity.Entity) trackerField.get(et);
	           if(nmsEnt instanceof EntityPlayer || nmsEnt instanceof EntityEnderDragon || nmsEnt instanceof EntityComplexPart) {
        		   continue;
        	   }
	           if(nmsEnt instanceof EntityArmorStand && nmsEnt.getBukkitEntity().getCustomName() != null) {
        		   continue;
        	   }
	           boolean remove = false;
	           if(et.f.size() == 0) {
	        	   remove = true;
	           }
	           else if(et.f.size() == 1) {
	        	   for(ServerPlayerConnection ep : et.f) {
	        		   if(!ep.d().getBukkitEntity().isOnline()) {
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
			cps.a.G.remove(id);
			if(ConfigMain.isDisableTickUntracked()) {
				EntityTickManager.getInstance().disableTicking(ws.getEntities().a(id));
			}
		}
		
        if(ConfigMain.isLogToConsole()) {
        	if(removed > 0) {
        		EntityTrackerFixer.plugin.getLogger().info("Untracked " + removed + " entities in " + worldName);
        	}
        }
        
        //System.out.println("cache now contains " + UntrackedEntitiesCache.getInstance().getCache(worldName).size() + " entities");
	}
	
	public static boolean isRunning() {
		return running;
	}

}
