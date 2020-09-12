package de.shepiii.livesupport.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

@Singleton
public final class SupportPlayerTrigger implements Listener {
  private final SupportPlayerRegistry supportPlayerRegistry;

  @Inject
  private SupportPlayerTrigger(SupportPlayerRegistry supportPlayerRegistry) {
    this.supportPlayerRegistry = supportPlayerRegistry;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void performPlayerJoin(PostLoginEvent login) {
    var supportPlayer = SupportPlayer.forPlayer(login.getPlayer());
    supportPlayerRegistry.addSupportPlayer(supportPlayer);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void performPlayerQuit(PlayerDisconnectEvent playerDisconnect) {
    var player = playerDisconnect.getPlayer();
    var supportPlayer = supportPlayerRegistry.getSupportPlayer(player.getUniqueId());
    supportPlayerRegistry.removeSupportPlayer(supportPlayer);
  }
}
