package net.minemora.entitytrackerfixer.nms;

import org.bukkit.plugin.Plugin;

public final class NMSCheck {
	
	private NMSCheck() {}
	
	public static NMS getNMS(Plugin plugin) {
        String packageName = plugin.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);

        try {
            final Class<?> clazz = Class.forName("net.minemora.entitytrackerfixer." + version + ".NMSHandler");
            if (NMS.class.isAssignableFrom(clazz)) {
            	return (NMS) clazz.getConstructor().newInstance();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            plugin.getLogger().severe("Could not find support for this Spigot version.");
            return null;
        }
        return null;
	}
}
