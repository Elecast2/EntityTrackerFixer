package net.minemora.entitytrackerfixer.config;

import java.util.ArrayList;
import java.util.List;

import net.minemora.entitytrackerfixer.EntityTrackerFixer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.EnumUtils;
import org.bukkit.entity.EntityType;

public final class ConfigMain extends Config {
	
	private static ConfigMain instance;
	
	private static int untrackTicks;
	private static int checkFrequency;
	private static int trackingRange;
	private static double minTps;
	private static boolean logToConsole = true;
	private static boolean disableTickUntracked = true;
	private static List<String> worlds = new ArrayList<>();
	private static List<EntityType> blacklistedEntities = new ArrayList<>();

	private ConfigMain() {
		super("config.yml");
	}

	@Override
	public void load(boolean firstCreate) {
		untrackTicks = getConfig().getInt("untrack-ticks", 1000);
		checkFrequency = getConfig().getInt("check-untracked-entities-frequency", 60);
		trackingRange = getConfig().getInt("tracking-range", 25);
		minTps = getConfig().getDouble("tps-limit", 18.5);
		worlds = getConfig().getStringList("worlds");
		logToConsole = getConfig().getBoolean("log-to-console", true);
		disableTickUntracked = getConfig().getBoolean("disable-tick-for-untracked-entities", false);

		List<String> blacklist = getConfig().getStringList("blacklist");
		if(!blacklist.isEmpty()){
			for(String entityname : blacklist){
				if (EnumUtils.isValidEnum(EntityType.class, entityname)){
					blacklistedEntities.add(EntityType.valueOf(entityname));
				}else{
					EntityTrackerFixer.plugin.getServer().getLogger().severe(
							"[EntityTrackerFixer] Invalid entity name in blacklist: "+entityname);
				}
			}
		}
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

	public static List<EntityType> getBlacklistedEntities(){
		return blacklistedEntities;
	}
}