package net.minemora.entitytrackerfixer.util;

import net.minecraft.server.v1_14_R1.WorldServer;

public final class Util {
	
	private Util() {}
	
	public static boolean isChunkLoaded(WorldServer ws, int x, int z) {
        net.minecraft.server.v1_14_R1.Chunk chunk = ws.getChunkProvider().getChunkAt(x, z, false);
        return chunk != null;
    }

}
