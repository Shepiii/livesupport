package de.shepiii.livesupport.support.staff;

import com.google.inject.Inject;
import de.shepiii.livesupport.message.MessageRepository;
import de.shepiii.livesupport.player.SupportPlayer;
import de.shepiii.livesupport.session.SupportSessionState;
import de.shepiii.livesupport.support.subcommand.SubCommandExecutor;

public final class FinishSubCommand implements SubCommandExecutor {
  private final MessageRepository messageRepository;

  @Inject
  private FinishSubCommand(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  @Override
  public void performSubCommand(SupportPlayer supportPlayer, String[] arguments) {
    if (!supportPlayer.hasPermission("support.chat.finish")) {
      supportPlayer.sendMessage(
        messageRepository.findMessage("support.noPermission"));
      return;
    }
    if (supportPlayer.supportSession().isEmpty()) {
      supportPlayer.sendMessage(
        messageRepository.findMessage("support.staff.finish.noSession"));
      return;
    }
    var supportSession = supportPlayer.supportSession().get();
    var client = supportSession.client();
    supportPlayer.supportSession(null);
    client.supportSession().get().supportSessionState(SupportSessionState.CLOSED);
    supportPlayer.sendMessage(
      messageRepository.findMessage("support.staff.finish.succesfully")
        .replace("{0}", client.proxiedPlayer().getDisplayName())
    );
    client.sendMessage(messageRepository.findMessage("support.chat.close"));
  }
}
