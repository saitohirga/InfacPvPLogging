package io.github.GRANTSWIM4.InfacPvPLogging;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin
{

	/**
	 * Bukkit enable.
	 */
	@Override
	public void onEnable()
	{
		{
			// Welcome message
			getLogger().info("Protecting your server form PvP logger since 2014!");

			// Register events
			getServer().getPluginManager().registerEvents(new AntiLogEvent(this), this);
		}

	}

	/**
	 * Bukkit disable.
	 */
	@Override
	public void onDisable()
	{
		// Goodbye message
		getLogger().info("It's been a honor protecting your server Bye");
	}
}
