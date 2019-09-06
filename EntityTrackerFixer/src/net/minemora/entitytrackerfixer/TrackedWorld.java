package net.minemora.entitytrackerfixer;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TrackedWorld {
	
	private final String worldName;
	private Map<UUID,UntrackedEntity> cache = new ConcurrentHashMap<>();
	private Set<UUID> unloadedFromChunkCache = ConcurrentHashMap.newKeySet();
	
	public TrackedWorld(String worldName) {
		this.worldName = worldName;
	}
	
	public void add(net.minecraft.server.v1_14_R1.Entity entity) {
        cache.put(entity.getUniqueID(), new UntrackedEntity(entity));
	}
	
	public void remove(UntrackedEntity ute) {
        cache.remove(ute.getUniqueID());
	}
	
	public boolean contains(UUID uid) {
        return cache.containsKey(uid);
	}
	
	public void removeAll(Set<UUID> toRemove) {
        cache.keySet().removeAll(toRemove);
	}
	
	public boolean isEmpty(String worldName) {
        return cache.isEmpty();
	}
	
	public Map<UUID,UntrackedEntity> getCache() {
		return cache;
	}
	
	public void addUFC(UUID uid) {
		unloadedFromChunkCache.add(uid);
	}
	
	public void removeUFC(UUID uid) {
		unloadedFromChunkCache.remove(uid);
	}
	
	public boolean containsUFC(UUID uid) {
        return unloadedFromChunkCache.contains(uid);
	}
	
	public Set<UUID> getUnloadedFromChunkCache() {
		return unloadedFromChunkCache;
	}
	
	public String getWorldName() {
		return worldName;
	}

}
