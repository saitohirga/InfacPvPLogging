package io.github.GRANTSWIM4.InfacPvPLogging;

import java.util.ArrayList; 
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.censoredsoftware.infractions.bukkit.Infraction;
import com.censoredsoftware.infractions.bukkit.Infractions;
import com.censoredsoftware.infractions.bukkit.dossier.CompleteDossier;
import com.censoredsoftware.infractions.bukkit.issuer.Issuer;
import com.censoredsoftware.infractions.bukkit.issuer.IssuerType;

public class AntiLogEvent implements Listener {

	ArrayList<Player> inCombat = new ArrayList<Player>();
	Main plugin;
	public AntiLogEvent(Main plugin){
		this.plugin = plugin;
	}


	@EventHandler
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent event){

		final Player attacker = (Player) event.getDamager();
		final Player attacked = (Player) event.getEntity();
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){

			if(!(inCombat.contains(attacker))){

				inCombat.add(attacker);
				inCombat.add(attacked);
				attacker.sendMessage(ChatColor.RED+"You are now in combat with " + attacked.getName());
				attacked.sendMessage(ChatColor.RED+"You are now in combat with " + attacker.getName());	


				plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {

					@Override
					public void run() {
						attacker.sendMessage(ChatColor.RED+"You are no longer in combat!");
						attacked.sendMessage(ChatColor.RED+"You are no longer in combat!");

						inCombat.remove(attacker);
						inCombat.remove(attacked);

					}
				},  plugin.getConfig().getLong("Time_In_Combat") * 20);
			}     
		}
	}

	@EventHandler
	public void onPlayerLogEvent(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(inCombat.contains(player)){
			if (inCombat.contains(player)) {
				CompleteDossier dossier = Infractions.getCompleteDossier(player.getName());
				dossier.cite(new Infraction(player.getUniqueId(), System.currentTimeMillis(), "PvP Loged", 5, new Issuer(IssuerType.CUSTOM, "PvPLogPlugin")));
			}
		}
	}
}