package net.minemora.entitytrackerfixer.vault;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.permission.Permission;

public final class VaultManager {
	
	private static boolean enabled = true;
	
	private static Permission perms = null;
	
	private VaultManager() {}
	
	public static void setup(Plugin plugin) {
		if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
			enabled = false;
			plugin.getLogger().info("Vault not found! using default permission system...");
			return;
        }
		setupPermissions(plugin);
	}
	
	public static boolean hasPermission(Player player, String permission) {
		if(enabled) {
			return getPermissions().has(player, permission);
		}
		else {
			return player.hasPermission(permission);
		}
	}

	
	private static boolean setupPermissions(Plugin plugin) {
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
	
	public static Permission getPermissions() {
        return perms;
    }

	public static boolean isEnabled() {
		return enabled;
	}

}