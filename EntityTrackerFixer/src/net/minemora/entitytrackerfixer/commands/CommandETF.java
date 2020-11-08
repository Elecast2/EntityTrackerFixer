package net.minemora.entitytrackerfixer.commands;

import net.minemora.entitytrackerfixer.EntityTrackerFixer;
import net.minemora.entitytrackerfixer.vault.VaultManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandETF implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (commandSender instanceof Player) {
			if (!VaultManager.hasPermission((Player) commandSender, "entitytrackerfixer.reload")) {
				commandSender.sendMessage(ChatColor.RED + "You don't have permission to use that command.");
				return true;
			}
		}
		if (args.length == 0) {
			commandSender.sendMessage(ChatColor.RED + "No arguments provided. Available: " + ChatColor.GOLD + "/etf reload" + ChatColor.RED + ".");
			return true;
		}
		if (args[0].equalsIgnoreCase("reload")) {
			EntityTrackerFixer.plugin.reload();
			commandSender.sendMessage(ChatColor.GREEN + "The config has been reloaded sucessfully!");
		}
		commandSender.sendMessage(ChatColor.RED + "Invalid argument. Available: " + ChatColor.GOLD + "/etf reload" + ChatColor.RED + ".");
		return true;
	}
}
