package net.minemora.entitytrackerfixer.entitytick;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;

import net.minecraft.server.v1_14_R1.WorldServer;
import net.minemora.entitytrackerfixer.UntrackerTask;
import net.minemora.entitytrackerfixer.util.ReflectionUtils;

public class EntityTickManager  extends TimerTask {
	
	private static Field tickingField;
	
	static {
		try {
			tickingField = ReflectionUtils.getClassPrivateField(WorldServer.class, "tickingEntities");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private static EntityTickManager instance;
	
	private Map<String, EntityTickWorldCache> cache = new HashMap<>();
	
	private Timer timer;

    private EntityTickManager() {
        this.timer = new Timer();
        timer.schedule(this, 2069, 2069);
    }
    
    public void disableTicking(int id, String worldName) {
    	cache.get(worldName).getToTick().remove(id);
    	cache.get(worldName).getToUntick().add(id);
    }
    
    public void enableTicking(net.minecraft.server.v1_14_R1.Entity entity, String worldName) {
    	cache.get(worldName).getToUntick().remove(entity.getId());
    	cache.get(worldName).getToTick().put(entity.getId(), entity);
    }

	@Override
	public void run() {
		if(UntrackerTask.isRunning()) {
			return;
		}
		for(String worldName : cache.keySet()) {
			WorldServer ws = ((CraftWorld)Bukkit.getWorld(worldName)).getHandle();
			try {
				boolean ticking = tickingField.getBoolean(ws);
				if(ticking) {
					//System.out.println("ticking");
					continue;
				}
				EntityTickWorldCache ewc = cache.get(worldName);
				//System.out.println("unticking: " + ewc.getToUntick().size() + " entities, ticking again: " + ewc.getToTick().size() + " entities");
				for(int i : ewc.getToUntick()) {
					ws.entitiesById.remove(i);
				}
				ewc.getToUntick().clear();
				for(int i : ewc.getToTick().keySet()) {
					ws.entitiesById.put(i, ewc.getToTick().get(i));
				}
				ewc.getToTick().clear();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}	
	}
	
	public static EntityTickManager getInstance() {
		if(instance == null) {
			instance = new EntityTickManager();
		}
		return instance;
	}
	
	public Map<String, EntityTickWorldCache> getCache() {
		return cache;
	}

}
