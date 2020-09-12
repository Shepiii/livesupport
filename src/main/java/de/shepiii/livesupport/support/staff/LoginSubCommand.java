package de.shepiii.livesupport.support.staff;

import com.google.inject.Inject;
import de.shepiii.livesupport.message.MessageRepository;
import de.shepiii.livesupport.player.SupportPlayer;
import de.shepiii.livesupport.queue.SupportQueue;
import de.shepiii.livesupport.queue.SupportQueueState;
import de.shepiii.livesupport.support.subcommand.SubCommandExecutor;
import net.md_5.bungee.api.ProxyServer;

public final class LoginSubCommand implements SubCommandExecutor {
  private final MessageRepository messageRepository;
  private final SupportQueue supportQueue;

  @Inject
  private LoginSubCommand(
    MessageRepository messageRepository,
    SupportQueue supportQueue
  ) {
    this.messageRepository = messageRepository;
    this.supportQueue = supportQueue;
  }

  @Override
  public void performSubCommand(SupportPlayer supportPlayer, String[] args) {
    var player = supportPlayer.proxiedPlayer();
    if (!player.hasPermission("support.staff.login")) {
      player.sendMessage(messageRepository.findMessage("support.noPermission"));
      return;
    }
    if (supportQueue.staffs().contains(supportPlayer)) {
      player.sendMessage(messageRepository.findMessage("support.join.alreadyin"));
      return;
    }
    supportQueue.addStaff(supportPlayer);
    supportQueue.state(SupportQueueState.OPEN);
    var message = messageRepository.findMessage("support.join.notify")
      .replace("{0}", player.getDisplayName());
    for (var proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
      if (proxiedPlayer.hasPermission("support.notify")) {
        proxiedPlayer.sendMessage(message);
      }
    }
  }
}
