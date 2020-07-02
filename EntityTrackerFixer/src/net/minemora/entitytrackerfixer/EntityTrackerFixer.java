package net.minemora.entitytrackerfixer;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import net.minemora.entitytrackerfixer.commands.CommandETF;
import net.minemora.entitytrackerfixer.config.ConfigMain;
import net.minemora.entitytrackerfixer.nms.NMS;
import net.minemora.entitytrackerfixer.nms.NMSCheck;
import net.minemora.entitytrackerfixer.vault.VaultManager;

public class EntityTrackerFixer extends JavaPlugin {
	
	public static EntityTrackerFixer plugin;
	
	private NMS nms;
	
	private BukkitTask untrackerTask;
	private BukkitTask checkTask;
	
	@Override
	public void onEnable() {
		plugin = this;
		ConfigMain.getInstance().setup(this);
		nms = NMSCheck.getNMS(this);
		untrackerTask = nms.startUntrackerTask(this);
		checkTask = nms.startUCheckTask(this);
		VaultManager.setup(this);
		this.getCommand("entitytrackerfixer").setExecutor(new CommandETF());
	}
	
	@Override
	public void onDisable() {
		untrackerTask.cancel();
		checkTask.cancel();
	}
	
	public void reload() {
		untrackerTask.cancel();
		checkTask.cancel();
		ConfigMain.getInstance().setup(this);
		untrackerTask = nms.startUntrackerTask(this);
		checkTask = nms.startUCheckTask(this);
	}
}
