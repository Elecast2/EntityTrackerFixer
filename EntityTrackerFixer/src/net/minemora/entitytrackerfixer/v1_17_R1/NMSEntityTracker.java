package net.minemora.entitytrackerfixer.v1_17_R1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import net.minecraft.server.level.ChunkProviderServer;
import net.minecraft.server.level.PlayerChunkMap;
import net.minemora.entitytrackerfixer.util.ReflectionUtils;

public final class NMSEntityTracker {
	
	private static Method addEntityMethod;
	private static Method removeEntityMethod;
	
	static {
		try {
		addEntityMethod = ReflectionUtils.getPrivateMethod(PlayerChunkMap.class, "addEntity", 
				new Class[] {net.minecraft.world.entity.Entity.class});
		removeEntityMethod = ReflectionUtils.getPrivateMethod(PlayerChunkMap.class, "removeEntity", 
				new Class[] {net.minecraft.world.entity.Entity.class});
		} catch (NoSuchMethodException | SecurityException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	private NMSEntityTracker() {}
	
	public static void trackEntities(ChunkProviderServer cps, Set<net.minecraft.world.entity.Entity> trackList) {
		try {
			for(net.minecraft.world.entity.Entity entity : trackList) {
				addEntityMethod.invoke(cps.a, entity);
			}
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static void untrackEntities(ChunkProviderServer cps, Set<net.minecraft.world.entity.Entity> untrackList) {
		try {
			for(net.minecraft.world.entity.Entity entity : untrackList) {
				removeEntityMethod.invoke(cps.a, entity);
			}
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
