package de.shepiii.livesupport.support.staff;

import com.google.inject.Inject;
import de.shepiii.livesupport.message.MessageRepository;
import de.shepiii.livesupport.player.SupportPlayer;
import de.shepiii.livesupport.queue.SupportQueue;
import de.shepiii.livesupport.session.SupportSession;
import de.shepiii.livesupport.session.SupportSessionState;
import de.shepiii.livesupport.support.subcommand.SubCommandExecutor;

public final class NextSubCommand implements SubCommandExecutor {
  private final SupportQueue supportQueue;
  private final MessageRepository messageRepository;

  @Inject
  private NextSubCommand(SupportQueue supportQueue, MessageRepository messageRepository) {
    this.supportQueue = supportQueue;
    this.messageRepository = messageRepository;
  }

  @Override
  public void performSubCommand(SupportPlayer supportPlayer, String[] arguments) {
    if (!supportPlayer.proxiedPlayer().hasPermission("support.support.next")) {
      supportPlayer.proxiedPlayer().sendMessage(messageRepository.findMessage("support.noPermission"));
      return;
    }
    if (!supportQueue.staffs().contains(supportPlayer)) {
      supportPlayer.proxiedPlayer().sendMessage(
        messageRepository.findMessage("support.staff.next.notloggedin"));
      return;
    }
    if (supportPlayer.supportSession().isPresent()) {
      supportPlayer.proxiedPlayer().sendMessage(messageRepository.findMessage("support.staff.next.alreadyinTalk"));
      return;
    }
    supportNext(supportPlayer);
  }

  private void supportNext(SupportPlayer staff) {
    if (supportQueue.waiting().isEmpty()) {
      staff.proxiedPlayer().sendMessage(messageRepository.findMessage("support.staff.next.notopen"));
      return;
    }
    var client = supportQueue.waiting().iterator().next();
    var supportSession = SupportSession.create(SupportSessionState.ACTIVE, staff, client);
    client.supportSession(supportSession);
    staff.supportSession(supportSession);
    client.proxiedPlayer().sendMessage(messageRepository.findMessage("support.client.opensupport")
      .replace("{0}", staff.proxiedPlayer().getDisplayName()));
    staff.proxiedPlayer().sendMessage(messageRepository.findMessage("support.staff.next.succesfull")
      .replace("{0}", client.proxiedPlayer().getDisplayName()));
    supportQueue.removeWaiting(client);
  }
}
