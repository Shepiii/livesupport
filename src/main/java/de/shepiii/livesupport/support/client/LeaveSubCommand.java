package de.shepiii.livesupport.support.client;

import com.google.inject.Inject;
import de.shepiii.livesupport.message.MessageRepository;
import de.shepiii.livesupport.player.SupportPlayer;
import de.shepiii.livesupport.queue.SupportQueue;
import de.shepiii.livesupport.support.subcommand.SubCommandExecutor;

public final class LeaveSubCommand implements SubCommandExecutor {
  private final SupportQueue supportQueue;
  private final MessageRepository messageRepository;

  @Inject
  private LeaveSubCommand(
    SupportQueue supportQueue, MessageRepository messageRepository
  ) {
    this.supportQueue = supportQueue;
    this.messageRepository = messageRepository;
  }

  @Override
  public void performSubCommand(SupportPlayer supportPlayer, String[] arguments) {
    if (!supportQueue.waiting().contains(supportPlayer)) {
      supportPlayer.sendMessage(
        messageRepository.findMessage("support.queue.leave.notinqueue"));
      return;
    }
    supportQueue.removeWaiting(supportPlayer);
    supportPlayer.sendMessage(
      messageRepository.findMessage("support.queue.leave"));
  }
}
