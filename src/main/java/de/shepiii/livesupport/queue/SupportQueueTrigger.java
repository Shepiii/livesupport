package de.shepiii.livesupport.queue;

import com.google.inject.Inject;
import de.shepiii.livesupport.player.SupportPlayerRegistry;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public final class SupportQueueTrigger implements Listener {
  private final SupportQueue supportQueue;
  private final SupportPlayerRegistry supportPlayerRegistry;

  @Inject
  private SupportQueueTrigger(
    SupportQueue supportQueue, SupportPlayerRegistry supportPlayerRegistry
  ) {
    this.supportQueue = supportQueue;
    this.supportPlayerRegistry = supportPlayerRegistry;
  }

  @EventHandler
  public void performPlayerLeave(PlayerDisconnectEvent playerDisconnect) {
    var player = playerDisconnect.getPlayer();
    var supportPlayer = supportPlayerRegistry.getSupportPlayer(player.getUniqueId());
    supportQueue.waiting().remove(supportPlayer);
    supportQueue.staffs().remove(supportPlayer);
    if (supportQueue.staffs().isEmpty()) {
      supportQueue.state(SupportQueueState.CLOSED);
    }
  }
}
