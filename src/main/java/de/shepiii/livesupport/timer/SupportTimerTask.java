package de.shepiii.livesupport.timer;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.shepiii.livesupport.message.MessageRepository;
import de.shepiii.livesupport.player.SupportPlayer;
import de.shepiii.livesupport.queue.SupportQueue;

import java.util.TimerTask;

@Singleton
public final class SupportTimerTask extends TimerTask {
  private final SupportQueue supportQueue;
  private final String message;
  private final String clientMessage;

  @Inject
  private SupportTimerTask(MessageRepository messageRepository, SupportQueue supportQueue) {
    this.supportQueue = supportQueue;
    this.message = messageRepository.findMessage("support.queue.message");
    this.clientMessage = messageRepository.findMessage("support.queue.message.client");
  }

  private static final String placeHolder = "{WartendeSpieler}";

  @Override
  public void run() {
    for (SupportPlayer staff : supportQueue.staffs()) {
      staff.proxiedPlayer().sendMessage(message.replace(
        placeHolder, String.valueOf(supportQueue.waiting().size())));
    }
    for (SupportPlayer client : supportQueue.waiting()) {
      client.proxiedPlayer().sendMessage(clientMessage);
    }
  }
}
