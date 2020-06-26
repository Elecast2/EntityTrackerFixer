package net.minemora.entitytrackerfixer.v1_16_R1.entityTick;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.server.v1_16_R1.WorldServer;

public class EntityTickWorldCache {
	
	private String worldName;
	private WorldServer worldServer;
	
	private Set<Integer> toUntick = new HashSet<>();
	private Map<Integer, net.minecraft.server.v1_16_R1.Entity> toTick = new HashMap<>();
	
	public EntityTickWorldCache(WorldServer worldServer) {
		this.worldName = worldServer.getWorld().getName();
		this.worldServer = worldServer;
	}

	public Set<Integer> getToUntick() {
		return toUntick;
	}

	public Map<Integer, net.minecraft.server.v1_16_R1.Entity> getToTick() {
		return toTick;
	}

	public String getWorldName() {
		return worldName;
	}

	public WorldServer getWorldServer() {
		return worldServer;
	}
}
