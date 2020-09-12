package de.shepiii.livesupport.session;

import com.google.inject.Inject;
import de.shepiii.livesupport.message.MessageRepository;
import de.shepiii.livesupport.player.SupportPlayerRegistry;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public final class SupportSessionTrigger implements Listener {
  private final SupportPlayerRegistry supportPlayerRegistry;
  private final MessageRepository messageRepository;

  @Inject
  private SupportSessionTrigger(
    SupportPlayerRegistry supportPlayerRegistry,
    MessageRepository messageRepository
  ) {
    this.supportPlayerRegistry = supportPlayerRegistry;
    this.messageRepository = messageRepository;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void performChatMessage(ChatEvent chat) {
    if (chat.getMessage().startsWith("/")) {
      return;
    }
    var player = (ProxiedPlayer) chat.getSender();
    var supportPlayer = supportPlayerRegistry.getSupportPlayer(player.getUniqueId());
    if (supportPlayer.supportSession().isEmpty()) {
      return;
    }
    var supportSession = supportPlayer.supportSession().get();
    if (!supportSession.supportSessionState().privateChatActive()) {
      return;
    }
    chat.setCancelled(true);
    var rawMessage = chat.getMessage();
    var message = messageRepository.findMessage("support.chat.format")
      .replace("{0}", player.getDisplayName())
      .replace("{nachricht}", rawMessage);
    supportSession.staff().proxiedPlayer().sendMessage(message);
    supportSession.client().proxiedPlayer().sendMessage(message);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void performLeave(PlayerDisconnectEvent playerDisconnect) {
    var player = playerDisconnect.getPlayer();
    var supportPlayer = supportPlayerRegistry.getSupportPlayer(player.getUniqueId());
    supportPlayer.supportSession().ifPresent(supportSession -> {
      if (supportSession.client() == supportPlayer) {
        supportSession.staff().proxiedPlayer().sendMessage(
          messageRepository.findMessage("support.staff.partner.leave"));
        supportSession.staff().supportSession(null);
        return;
      }
      supportSession.client().proxiedPlayer().sendMessage(
        messageRepository.findMessage("support.chat.close.suddenly"));
      supportSession.supportSessionState(SupportSessionState.LEFT_BY_STAFF);
    });
  }
}
