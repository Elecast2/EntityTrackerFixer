package net.minemora.entitytrackerfixer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import net.minecraft.server.v1_14_R1.ChunkProviderServer;
import net.minecraft.server.v1_14_R1.PlayerChunkMap;
import net.minemora.entitytrackerfixer.util.ReflectionUtils;

public final class NMSEntityTracker {
	
	private NMSEntityTracker() {}
	
	public static void trackEntities(ChunkProviderServer cps, Set<net.minecraft.server.v1_14_R1.Entity> trackList) {
		try {
			Method method = ReflectionUtils.getPrivateMethod(PlayerChunkMap.class, "addEntity", 
					new Class[] {net.minecraft.server.v1_14_R1.Entity.class});
			for(net.minecraft.server.v1_14_R1.Entity entity : trackList) {
				if(cps.playerChunkMap.trackedEntities.containsKey(entity.getId())) {
					continue;
				}
				method.invoke(cps.playerChunkMap, entity);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static void untrackEntities(ChunkProviderServer cps, Set<net.minecraft.server.v1_14_R1.Entity> untrackList) {
		try {
			Method method = ReflectionUtils.getPrivateMethod(PlayerChunkMap.class, "removeEntity", 
					new Class[] {net.minecraft.server.v1_14_R1.Entity.class});
			for(net.minecraft.server.v1_14_R1.Entity entity : untrackList) {
				method.invoke(cps.playerChunkMap, entity);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
