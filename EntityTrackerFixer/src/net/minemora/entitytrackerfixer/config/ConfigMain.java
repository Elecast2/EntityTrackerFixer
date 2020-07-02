package net.minemora.entitytrackerfixer.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import net.minemora.entitytrackerfixer.EntityTrackerFixer;

public final class ConfigMain extends Config {
	
	private static ConfigMain instance;
	
	private static int untrackTicks;
	private static int checkFrequency;
	private static int trackingRange;
	private static double minTps;
	private static boolean logToConsole = true;
	private static boolean disableTickUntracked = true;
	private static List<String> worlds = new ArrayList<>();

	private ConfigMain() {
		super("config.yml");
	}

	@Override
	public void load(boolean firstCreate) {
		untrackTicks = getConfig().getInt("untrack-ticks", 1000);
		EntityTrackerFixer.plugin.getLogger().info("Setting untrack ticks to: " + untrackTicks);
		checkFrequency = getConfig().getInt("check-untracked-entities-frequency", 60);
		EntityTrackerFixer.plugin.getLogger().info("Setting check frequency to: " + checkFrequency);
		trackingRange = getConfig().getInt("tracking-range", 25);
		EntityTrackerFixer.plugin.getLogger().info("Setting tracking range to: " + trackingRange);
		minTps = getConfig().getDouble("tps-limit", 18.5);
		EntityTrackerFixer.plugin.getLogger().info("Setting minmun TPS to: " + minTps);
		worlds = getConfig().getStringList("worlds");
		for(String world : worlds) {
			EntityTrackerFixer.plugin.getLogger().info("Adding world: " + world);
		}
		logToConsole = getConfig().getBoolean("log-to-console", true);
		EntityTrackerFixer.plugin.getLogger().info("Setting log to console to: " + logToConsole);
		disableTickUntracked = getConfig().getBoolean("disable-tick-for-untracked-entities", true);
		EntityTrackerFixer.plugin.getLogger().info("Setting disable tick for untracked to: " + disableTickUntracked);
	}
	
	public static FileConfiguration get() {
		return getInstance().config;
	}

	public static ConfigMain getInstance() {
		if (instance == null) {
            instance = new ConfigMain();
        }
        return instance;
	}

	@Override
	public void update() {
		return;
	}

	public static int getUntrackTicks() {
		return untrackTicks;
	}

	public static int getCheckFrequency() {
		return checkFrequency;
	}

	public static double getMinTps() {
		return minTps;
	}

	public static List<String> getWorlds() {
		return worlds;
	}

	public static int getTrackingRange() {
		return trackingRange;
	}

	public static boolean isLogToConsole() {
		return logToConsole;
	}

	public static boolean isDisableTickUntracked() {
		return disableTickUntracked;
	}
}