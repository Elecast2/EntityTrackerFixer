package net.minemora.entitytrackerfixer.util;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public interface ChatUtils {

	static String format(String m) {
		return ChatColor.translateAlternateColorCodes('&', m);
	}

	static String[] format(String[] m) {
		String[] result;
		result = new String[m.length];
		for (int i = 0; i < m.length; i++) {
			result[i] = ChatColor.translateAlternateColorCodes('&', m[i]);
		}
		return result;
	}

	static String[] format(List<String> m) {
		String[] result;
		result = new String[m.size()];
		for (int i = 0; i < m.size(); i++) {
			result[i] = ChatColor.translateAlternateColorCodes('&', m.get(i));
		}
		return result;
	}
	
	static List<String> formatList(List<String> m) {
		return m.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
	}
	
	static String formatTime(int seconds) {
		return String.format("%02d:%02d", seconds / 60, seconds % 60);
	}

	static String formatLocation(Location loc) {
		return "[x:" + loc.getBlockX() + ", y:" + loc.getBlockY() + ", z:" + loc.getBlockZ() + "]";
	}
}