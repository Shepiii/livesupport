package de.shepiii.livesupport.support.staff;

import com.google.inject.Inject;
import de.shepiii.livesupport.message.MessageRepository;
import de.shepiii.livesupport.player.SupportPlayer;
import de.shepiii.livesupport.queue.SupportQueue;
import de.shepiii.livesupport.queue.SupportQueueState;
import de.shepiii.livesupport.support.subcommand.SubCommandExecutor;
import net.md_5.bungee.api.ProxyServer;

public final class LogoutSubCommand implements SubCommandExecutor {
  private final MessageRepository messageRepository;
  private final SupportQueue supportQueue;

  @Inject
  private LogoutSubCommand(
    MessageRepository messageRepository,
    SupportQueue supportQueue
  ) {
    this.messageRepository = messageRepository;
    this.supportQueue = supportQueue;
  }

  @Override
  public void performSubCommand(SupportPlayer supportPlayer, String[] args) {
    var player = supportPlayer.proxiedPlayer();
    if (!player.hasPermission("support.staff.logout")) {
      player.sendMessage(messageRepository.findMessage("support.noPermission"));
      return;
    }
    if (!supportQueue.staffs().contains(supportPlayer)) {
      player.sendMessage(
        messageRepository.findMessage("support.logout.already"));
      return;
    }
    supportQueue.removeStaff(supportPlayer);
    if (supportQueue.staffs().isEmpty()) {
      closeSupport();
    }
    var message = messageRepository.findMessage("support.logout.notify")
      .replace("{0}", player.getDisplayName());
    for (var proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
      if (proxiedPlayer.hasPermission("support.notify")) {
        proxiedPlayer.sendMessage(message);
      }
    }
  }

  private void closeSupport() {
    supportQueue.state(SupportQueueState.CLOSED);
    var message = messageRepository.findMessage("support.close.suddenly");
    for (SupportPlayer supportPlayer : supportQueue.waiting()) {
      supportPlayer.sendMessage(message);
      supportQueue.removeWaiting(supportPlayer);
    }
  }
}
