package de.shepiii.livesupport.support.staff;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import de.shepiii.livesupport.message.MessageRepository;
import de.shepiii.livesupport.player.SupportPlayer;
import de.shepiii.livesupport.queue.SupportQueue;
import de.shepiii.livesupport.support.subcommand.SubCommandExecutor;

import java.util.List;

public final class ListSubCommand implements SubCommandExecutor {
  private final SupportQueue supportQueue;
  private final MessageRepository messageRepository;

  @Inject
  private ListSubCommand(
    SupportQueue supportQueue, MessageRepository messageRepository
  ) {
    this.supportQueue = supportQueue;
    this.messageRepository = messageRepository;
  }

  @Override
  public void performSubCommand(SupportPlayer supportPlayer, String[] arguments) {
    if (!supportPlayer.hasPermission("support.onlineStaff.list")) {
      supportPlayer.sendMessage(messageRepository.findMessage("support.noPermission"));
      return;
    }
    var message = messageRepository.findMessage("support.staff.online");
    List<String> nameList = Lists.newArrayList();
    for (SupportPlayer staff : supportQueue.staffs()) {
      nameList.add(staff.proxiedPlayer().getDisplayName());
    }
    supportPlayer.sendMessage(message.replace("{0}", nameList.toString()));
  }
}
