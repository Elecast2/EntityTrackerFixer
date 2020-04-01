package net.minemora.entitytrackerfixer.v1_15_R1.entityTick;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_15_R1.WorldServer;
import net.minemora.entitytrackerfixer.EntityTrackerFixer;
import net.minemora.entitytrackerfixer.util.ReflectionUtils;
import net.minemora.entitytrackerfixer.v1_15_R1.tasks.UntrackerTask;

public class EntityTickManager extends BukkitRunnable {
	
	private static Field tickingEntitiesField;
	private static Field entityCount;
	
	static {
		try {
			tickingEntitiesField = ReflectionUtils.getClassPrivateField(WorldServer.class, "tickingEntities");
			entityCount = ReflectionUtils.getClassPrivateField(net.minecraft.server.v1_15_R1.Entity.class, "entityCount");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private static EntityTickManager instance;
	
	private Map<String, EntityTickWorldCache> cache = new HashMap<>();

    private EntityTickManager(Plugin plugin) {
        this.runTaskTimer(plugin, 61, 61);
    }
    
    public void disableTicking(int id, String worldName) {
    	if(!cache.containsKey(worldName)){
			cache.put(worldName, new EntityTickWorldCache(((CraftWorld) Bukkit.getWorld(worldName)).getHandle()));
		}
    	cache.get(worldName).getToTick().remove(id);
    	cache.get(worldName).getToUntick().add(id);
    }
    
    public void enableTicking(net.minecraft.server.v1_15_R1.Entity entity, String worldName) {
		if(!cache.containsKey(worldName)){
			cache.put(worldName, new EntityTickWorldCache(((CraftWorld) Bukkit.getWorld(worldName)).getHandle()));
		}
    	cache.get(worldName).getToUntick().remove(entity.getId());
    	cache.get(worldName).getToTick().put(entity.getId(), entity);
    }

	@Override
	public void run() {
		if(UntrackerTask.isRunning()) {
			return;
		}
		for(String worldName : cache.keySet()) {
			EntityTickWorldCache ewc = cache.get(worldName);
			WorldServer ws = ewc.getWorldServer();
			if(ws.b()) {
				continue;
			}
			try {
				if(tickingEntitiesField.getBoolean(ws)) {
					continue;
				}
				//System.out.println("unticking: " + ewc.getToUntick().size() + " entities, ticking again: " + ewc.getToTick().size() + " entities");
				for(int i : ewc.getToUntick()) {
					ws.entitiesById.remove(i);
				}
				ewc.getToUntick().clear();
				for(int i : ewc.getToTick().keySet()) {
					net.minecraft.server.v1_15_R1.Entity entity = ewc.getToTick().get(i);
					if(entity == null) {
						continue;
					}
					if(!entity.valid) {
						continue;
					}
					if(ws.entitiesById.containsValue(entity)) {
						continue;
					}
					if(ws.entitiesById.containsKey(i)) {
						int id = ((AtomicInteger)entityCount.get(null)).incrementAndGet();
						ws.entitiesById.put(id, entity);
					}
					else {
						ws.entitiesById.put(i, entity);
					}
				}
				ewc.getToTick().clear();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}	
	}
	
	public static EntityTickManager getInstance() {
		if(instance == null) {
			instance = new EntityTickManager(EntityTrackerFixer.plugin);
		}
		return instance;
	}
	
	public Map<String, EntityTickWorldCache> getCache() {
		return cache;
	}

}
