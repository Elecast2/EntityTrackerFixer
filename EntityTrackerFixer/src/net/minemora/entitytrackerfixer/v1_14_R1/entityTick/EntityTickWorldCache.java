package net.minemora.entitytrackerfixer.v1_14_R1.entityTick;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EntityTickWorldCache {
	
	private String worldName;
	
	private Set<Integer> toUntick = new HashSet<>();
	private Map<Integer, net.minecraft.server.v1_14_R1.Entity> toTick = new HashMap<>();
	
	public EntityTickWorldCache(String worldName) {
		this.worldName = worldName;
	}

	public Set<Integer> getToUntick() {
		return toUntick;
	}

	public Map<Integer, net.minecraft.server.v1_14_R1.Entity> getToTick() {
		return toTick;
	}

	public String getWorldName() {
		return worldName;
	}

}
