package net.minemora.entitytrackerfixer.v1_16_R1.entityTick;

import java.util.Set;

import org.bukkit.craftbukkit.v1_16_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import net.minecraft.server.v1_16_R1.EntityInsentient;
import net.minecraft.server.v1_16_R1.MinecraftServer;
import net.minemora.entitytrackerfixer.EntityTrackerFixer;

public class EntityTickManager implements Listener {
	
	private static EntityTickManager instance;
	
	private EntityTickManager() {
		EntityTrackerFixer.plugin.getServer().getPluginManager().registerEvents(this, EntityTrackerFixer.plugin);
	}
    
    public void disableTicking(net.minecraft.server.v1_16_R1.Entity entity) {
    	if(entity == null) {
			return;
		}
		if(!entity.valid) {
			return;
		}
    	entity.activatedTick = -2147483648L;
    	if(entity instanceof EntityInsentient) {
    		//System.out.println("disable tick for insentient entity currently aware is = " + ((EntityInsentient)entity).aware + " should be true");
    		((EntityInsentient)entity).aware = false;
    	}
    }
    
    public void enableTicking(Set<net.minecraft.server.v1_16_R1.Entity> entities) {
    	for(net.minecraft.server.v1_16_R1.Entity entity : entities) {
    		if(entity == null) {
    			continue;
    		}
    		if(!entity.valid) {
    			continue;
    		}
        	entity.activatedTick = MinecraftServer.currentTick;
        	if(entity instanceof EntityInsentient) {
        		//System.out.println("enabling tick for insentient entity currently aware is = " + ((EntityInsentient)entity).aware + " should be false");
        		((EntityInsentient)entity).aware = true;
        	}
    	}
    }
    
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
    	for(Entity entity : event.getChunk().getEntities()) {
    		net.minecraft.server.v1_16_R1.Entity nms = ((CraftEntity)entity).getHandle();
    		if(nms instanceof EntityInsentient) {
    			if(!((EntityInsentient)nms).aware) {
    				((EntityInsentient)nms).aware = true;
    			}
    		}
    	}
    }
	
	public static EntityTickManager getInstance() {
		if(instance == null) {
			instance = new EntityTickManager();
		}
		return instance;
	}

}
