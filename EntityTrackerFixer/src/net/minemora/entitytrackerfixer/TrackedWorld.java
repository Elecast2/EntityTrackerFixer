package net.minemora.entitytrackerfixer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TrackedWorld {
	
	private final String worldName;
	private Set<UntrackedEntity> cache = ConcurrentHashMap.newKeySet();
	private Set<Integer> unloadedFromChunkCache = ConcurrentHashMap.newKeySet();
	
	public TrackedWorld(String worldName) {
		this.worldName = worldName;
	}
	
	public void add(net.minecraft.server.v1_14_R1.Entity entity) {
        cache.add(new UntrackedEntity(entity));
	}
	
	public void remove(UntrackedEntity ute) {
        cache.remove(ute);
	}
	
	public void removeAll(Set<UntrackedEntity> toRemove) {
        cache.removeAll(toRemove);
	}
	
	public boolean isEmpty(String worldName) {
        return cache.isEmpty();
	}
	
	public Set<UntrackedEntity> getCache() {
		return cache;
	}
	
	public void addUFC(int i) {
		unloadedFromChunkCache.add(i);
	}
	
	public void removeUFC(int i) {
		unloadedFromChunkCache.remove(i);
	}
	
	public boolean containsUFC(int i) {
        return unloadedFromChunkCache.contains(i);
	}
	
	public Set<Integer> getUnloadedFromChunkCache() {
		return unloadedFromChunkCache;
	}
	
	public String getWorldName() {
		return worldName;
	}

}
