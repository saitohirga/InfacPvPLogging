package io.github.GRANTSWIM4.InfacPvPLogging;

import com.censoredsoftware.infractions.bukkit.Infraction;
import com.censoredsoftware.infractions.bukkit.Infractions;
import com.censoredsoftware.infractions.bukkit.dossier.CompleteDossier;
import com.censoredsoftware.infractions.bukkit.issuer.Issuer;
import com.censoredsoftware.infractions.bukkit.issuer.IssuerType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Listener class.
 */
public class AntiLogEvent implements Listener
{
	// Constants
	private static final String IN_COMBAT = ChatColor.GOLD + "You're now in Combat!";
	private static final String SAFE = ChatColor.GREEN + "You can now log out safely.";

	// Variables
	private List<String> antilog = new ArrayList<String>();
	private Plugin plugin;

	/**
	 * Constructor
	 */
	AntiLogEvent(Plugin plugin)
	{
		this.plugin = plugin;
	}

	/**
	 * Quit event.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onAntiLogQuit(PlayerQuitEvent event)
	{
		Player p = event.getPlayer();
		if(this.antilog.contains(p.getName()))
		{
			CompleteDossier dossier = Infractions.getCompleteDossier(p.getName());
			dossier.cite(new Infraction(p.getUniqueId(), System.currentTimeMillis(),
					"PvP Logged", 1, new Issuer(IssuerType.CUSTOM, "PvPLogPlugin")));
		}
	}

	/**
	 * Damage by entity event.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onAntiLogDmg(EntityDamageByEntityEvent event)
	{
		// Return if invalid
		if(event.isCancelled() || !(event.getEntity() instanceof Player)) return;

		Player target = (Player) event.getEntity();
		Player damager;

		// Arrow
		if(event.getDamager() instanceof Arrow && ((Arrow) event.getDamager()).getShooter() instanceof Player)
		{
			damager = (Player) ((Arrow) event.getDamager()).getShooter();
		}

		// Player
		else if(((event.getDamager() instanceof Player)))
		{
			damager = (Player) event.getEntity();
		}

		// Anything else
		else return;

		// Antilog
		processPlayer(target);
		processPlayer(damager);
	}

	private void processPlayer(final Player player)
	{
		// Antilog
		if(!this.antilog.contains(player.getName()))
		{
			this.antilog.add(player.getName());
			player.sendMessage(IN_COMBAT);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
			{
				public void run()
				{
					if(antilog.contains(player.getName()))
					{
						antilog.remove(player.getName());
						player.sendMessage(SAFE);
					}
				}
			}, 1000L);
		}
	}
}
