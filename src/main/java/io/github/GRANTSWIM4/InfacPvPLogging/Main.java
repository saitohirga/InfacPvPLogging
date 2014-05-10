package io.github.GRANTSWIM4.InfacPvPLogging;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
 	@Override
	public void onEnable() {

		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
		}
 
		if (getServer().getPluginManager().getPlugin("Infractions") != null) {
			getLogger().info(
					"Protecting your server form PvP logger since 2014!");
			getServer().getPluginManager().registerEvents(
					new AntiLogEvent(this), this);
			this.saveDefaultConfig();
		    this.getConfig().options().copyDefaults(true);
		} else {
			getLogger()
					.info("You don't have Infractions get it at http://dev.bukkit.org/bukkit-plugins/infractions/ Bye bye ");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}

	{

	}

	@Override
	public void onDisable() {
		if (getServer().getPluginManager().getPlugin("Infractions") != null) {
			getLogger().info("Its been a honor protecting your server Bye!");
		}
		 else {
			getLogger().info("It will be a honnor protecting when you setup up everything");

	}

}

}