package net.minemora.entitytrackerfixer.listener;

import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import net.minemora.entitytrackerfixer.UntrackedEntitiesCache;
import net.minemora.entitytrackerfixer.config.ConfigMain;

public class ChunkEventListener implements Listener {
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		Chunk ch = event.getChunk();
		if(!ConfigMain.getWorlds().contains(ch.getWorld().getName())) {
			return;
		}
		for(Entity entity : ch.getEntities()) {
			if(UntrackedEntitiesCache.getInstance().containsUFC(ch.getWorld().getName(), entity.getUniqueId())) {
				UntrackedEntitiesCache.getInstance().add(((CraftEntity)entity).getHandle());
				UntrackedEntitiesCache.getInstance().removeUFC(ch.getWorld().getName(), entity.getUniqueId());
			}
		}
	}
}