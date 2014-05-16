package io.github.GRANTSWIM4.InfacPvPLogging;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin
{
	private boolean infractionsInstalled;
	
	/**
	 * Bukkit enable.
	 */
 	@Override
	public void onEnable()
	{
		this.infractionsInstalled = getServer().getPluginManager().getPlugin("Infractions") != null;
		if (this.infractionsInstalled)
		{
			// Welcome message
			getLogger().info("Protecting your server form PvP logger since 2014!");
			
			// Register events
	        	getServer().getPluginManager().registerEvents(new AntiLogEvent(this), this);
	        	
	        	// Handle config
			this.saveDefaultConfig();
		    	this.getConfig().options().copyDefaults(true);
		}
		else
		{
			// Can't run without Infractions installed
			getLogger().info("You don't have Infractions get it at http://dev.bukkit.org/bukkit-plugins/infractions/ Bye bye ");
			getServer().getPluginManager().disablePlugin(this);
		}
	}
	
	/**
	 * Bukkit disable.
	 */
	@Override
	public void onDisable()
	{
		// Goodbye message
		getLogger().info(this.infractionsInstalled ? "It's been a honor protecting your server Bye!" : "It will be a honor protecting when you setup up everything");
	}
}
