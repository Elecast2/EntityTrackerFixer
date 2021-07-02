package net.minemora.entitytrackerfixer.v1_16_R2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import net.minecraft.server.v1_16_R2.ChunkProviderServer;
import net.minecraft.server.v1_16_R2.PlayerChunkMap;
import net.minemora.entitytrackerfixer.util.ReflectionUtils;

public final class NMSEntityTracker {
	
	private static Method addEntityMethod;
	private static Method removeEntityMethod;
	
	static {
		try {
		addEntityMethod = ReflectionUtils.getPrivateMethod(PlayerChunkMap.class, "addEntity", 
				new Class[] {net.minecraft.server.v1_16_R2.Entity.class});
		removeEntityMethod = ReflectionUtils.getPrivateMethod(PlayerChunkMap.class, "removeEntity", 
				new Class[] {net.minecraft.server.v1_16_R2.Entity.class});
		} catch (NoSuchMethodException | SecurityException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	private NMSEntityTracker() {}
	
	public static void trackEntities(ChunkProviderServer cps, Set<net.minecraft.server.v1_16_R2.Entity> trackList) {
		try {
			for(net.minecraft.server.v1_16_R2.Entity entity : trackList) {
				addEntityMethod.invoke(cps.playerChunkMap, entity);
			}
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static void untrackEntities(ChunkProviderServer cps, Set<net.minecraft.server.v1_16_R2.Entity> untrackList) {
		try {
			for(net.minecraft.server.v1_16_R2.Entity entity : untrackList) {
				removeEntityMethod.invoke(cps.playerChunkMap, entity);
			}
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
