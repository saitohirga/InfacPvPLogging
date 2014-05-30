package io.github.GRANTSWIM4.InfacPvPLogging;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Arrow;

import com.censoredsoftware.infractions.bukkit.Infraction;
import com.censoredsoftware.infractions.bukkit.Infractions;
import com.censoredsoftware.infractions.bukkit.dossier.CompleteDossier;
import com.censoredsoftware.infractions.bukkit.issuer.Issuer;
import com.censoredsoftware.infractions.bukkit.issuer.IssuerType;

/**
 * Listener class.
 */
public class AntiLogEvent implements Listener {
	// Constants
	private static final String IN_COMBAT = ChatColor.GOLD
			+ "You're now in Combat!";
	private static final String SAFE = ChatColor.GREEN
			+ "You can now log out safely.";

	// Variables
	private List<String> antilog = new ArrayList<String>();
	private Plugin plugin;

	/**
	 * Constructor
	 */
	AntiLogEvent(Plugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Quit event.
	 */
	@EventHandler
	public void onAntiLogQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (this.antilog.contains(p.getName())) {
			CompleteDossier dossier = Infractions.getCompleteDossier(p
					.getName());
			dossier.cite(new Infraction(p.getUniqueId(), System
					.currentTimeMillis(), "PvP Logged", 1, new Issuer(
					IssuerType.CUSTOM, "PvPLogPlugin")));
		}
	}

	/**
	 * Damage by entity event.
	 */
	@EventHandler
	public void onAntiLogDmg(EntityDamageByEntityEvent event) {
		if (!event.isCancelled() || !(event.getEntity() instanceof Player))
			return;
		Player target = (Player) event.getEntity();
		Player damager = null;

		// Arrow
		if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			if (arrow.getShooter() instanceof Player) {
				damager = (Player) arrow.getShooter();
			}
		}

		// Player
		if (((event.getDamager() instanceof Player))) {
			damager = (Player) event.getEntity();
		}

		// Antilog
		if ((!this.antilog.contains(damager.getName()))
				&& (!this.antilog.contains(target.getName()))) {
			final String DamagerName = damager.getName();
			final String TargetName = target.getName();
			this.antilog.add(DamagerName);
			this.antilog.add(TargetName);
			target.sendMessage(IN_COMBAT);
			Bukkit.getServer().getScheduler()
					.scheduleSyncDelayedTask(this.plugin, new Runnable() {
						public void run() {
							if (antilog.contains(DamagerName)
									&& antilog.contains(TargetName)) {
								antilog.remove(DamagerName);
								antilog.remove(TargetName);
								Player damage = Bukkit.getPlayer(DamagerName);
								if (damage != null)
									damage.sendMessage(SAFE);
								Player targ = Bukkit.getPlayer(TargetName);
								if (targ != null)
									targ.sendMessage(SAFE);
							}
						}
					}, 1000L);
		}
	}
}
