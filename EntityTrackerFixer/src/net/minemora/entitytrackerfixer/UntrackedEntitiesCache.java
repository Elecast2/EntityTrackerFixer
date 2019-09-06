package net.minemora.entitytrackerfixer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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
	
	public boolean contains(UUID uid, String worldName) {
		if(!trackedWorlds.containsKey(worldName)) {
			return false;
		}
		TrackedWorld tw = trackedWorlds.get(worldName);
        return tw.contains(uid);
	}
	
	public void removeAll(Set<UUID> toRemove, String worldName) {
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
	
	public Map<UUID,UntrackedEntity> getCache(String worldName) {
		if(!trackedWorlds.containsKey(worldName)) {
			return new HashMap<>();
		}
		TrackedWorld tw = trackedWorlds.get(worldName);
        return tw.getCache();
	}
	
	public void addUFC(String worldName, UUID uid) {
		if(!trackedWorlds.containsKey(worldName)) {
			return;
		}
		TrackedWorld tw = trackedWorlds.get(worldName);
		tw.addUFC(uid);
	}
	
	public void removeUFC(String worldName, UUID uid) {
		if(!trackedWorlds.containsKey(worldName)) {
			return;
		}
		TrackedWorld tw = trackedWorlds.get(worldName);
        tw.removeUFC(uid);
	}
	
	public boolean containsUFC(String worldName, UUID uid) {
		if(!trackedWorlds.containsKey(worldName)) {
			return false;
		}
        return getUnloadedFromChunkCache(worldName).contains(uid);
	}
	
	public Set<UUID> getUnloadedFromChunkCache(String worldName) {
		if(!trackedWorlds.containsKey(worldName)) {
			return new HashSet<UUID>();
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
