package net.minemora.entitytrackerfixer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_14_R1.ChunkProviderServer;
import net.minecraft.server.v1_14_R1.WorldServer;
import net.minemora.entitytrackerfixer.UntrackedEntitiesCache;
import net.minemora.entitytrackerfixer.util.ChatUtils;

public class CommandETF implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("etf.command")) {
			return true;
		}
		if(args.length < 2) {
			return true;
			//TODO command list
		}
		if(args[0].equalsIgnoreCase("check")) {
			if (!sender.hasPermission("etf.command.check")) {
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatUtils.format("&cOnly players can use this command"));
				return true;
			}
			int r;
			try {
				r = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatUtils.format("&4" + args[1] + " &cis not a number"));
				return true;
			}
			Player player = (Player)sender;
			WorldServer ws = ((CraftWorld)player.getWorld()).getHandle();
			ChunkProviderServer cps = ws.getChunkProvider();
			int trackedCount = 0;
			int untrackedCount = 0;
			int cachedCount = 0;
			for(Entity ent : player.getNearbyEntities(r, r, r)) {
				if(cps.playerChunkMap.trackedEntities.containsKey(ent.getEntityId())) {
					trackedCount++;
				}
				else {
					untrackedCount++;
				}
				if(UntrackedEntitiesCache.getInstance().contains(ent.getUniqueId(), ent.getWorld().getName())) {
					cachedCount++;
				}
			}
			player.sendMessage(ChatUtils.format(new String[] {
					"&bChecking entities in radius &f" + r,
					"   &f> &9Tracked Entities: &a" + trackedCount,
					"   &f> &9Untracked Entities &c" + untrackedCount,
					"   &f> &9Cached Entities &e" + cachedCount,
					"&7(Untracked and cache should be the same number)"
			}));
			
		}
		return true;
	}
}
