package net.minemora.entitytrackerfixer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minemora.entitytrackerfixer.config.ConfigMain;

public final class UntrackedEntitiesCache {
	
	private static UntrackedEntitiesCache instance;
	
	private Map<String,TrackedWorld> trackedWorlds = new ConcurrentHashMap<>();
	
	private UntrackedEntitiesCache() {
		for(String worldName : ConfigMain.getWorlds()) {
			trackedWorlds.put(worldName, new TrackedWorld(worldName));
		}
	}
	
	public void add(net.minecraft.server.v1_14_R1.Entity entity) {
		String worldName = entity.getBukkitEntity().getWorld().getName();
		if(!trackedWorlds.containsKey(worldName)) {
			return;
		}
		TrackedWorld tw = trackedWorlds.get(worldName);
		tw.add(entity);
	}

	public void remove(UntrackedEntity ute) {
		String worldName = ute.getEntity().getBukkitEntity().getWorld().getName();
		if(!trackedWorlds.containsKey(worldName)) {
			return;
		}
		TrackedWorld tw = trackedWorlds.get(worldName);
        tw.remove(ute);
	}
	
	public void removeAll(Set<UntrackedEntity> toRemove, String worldName) {
		if(!trackedWorlds.containsKey(worldName)) {
			return;
		}
		TrackedWorld tw = trackedWorlds.get(worldName);
        tw.removeAll(toRemove);
	}
	
	public boolean isEmpty(String worldName) {
		if(!trackedWorlds.containsKey(worldName)) {
			return true;
		}
        return getCache(worldName).isEmpty();
	}
	
	public Set<UntrackedEntity> getCache(String worldName) {
		if(!trackedWorlds.containsKey(worldName)) {
			return new HashSet<UntrackedEntity>();
		}
		TrackedWorld tw = trackedWorlds.get(worldName);
        return tw.getCache();
	}
	
	public void addUFC(String worldName, int i) {
		if(!trackedWorlds.containsKey(worldName)) {
			return;
		}
		TrackedWorld tw = trackedWorlds.get(worldName);
		tw.addUFC(i);
	}
	
	public void removeUFC(String worldName, int i) {
		if(!trackedWorlds.containsKey(worldName)) {
			return;
		}
		TrackedWorld tw = trackedWorlds.get(worldName);
        tw.removeUFC(i);
	}
	
	public boolean containsUFC(String worldName, int i) {
		if(!trackedWorlds.containsKey(worldName)) {
			return true;
		}
        return getUnloadedFromChunkCache(worldName).contains(i);
	}
	
	public Set<Integer> getUnloadedFromChunkCache(String worldName) {
		if(!trackedWorlds.containsKey(worldName)) {
			return new HashSet<Integer>();
		}
		TrackedWorld tw = trackedWorlds.get(worldName);
        return tw.getUnloadedFromChunkCache();
	}

	public static UntrackedEntitiesCache getInstance() {
		if(instance == null) {
			instance = new UntrackedEntitiesCache();
		}
		return instance;
	}

	public Map<String,TrackedWorld> getTrackedWorlds() {
		return trackedWorlds;
	}
}
