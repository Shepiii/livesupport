package de.shepiii.livesupport.support;

import com.google.inject.Inject;
import de.shepiii.livesupport.message.MessageRepository;
import de.shepiii.livesupport.player.SupportPlayer;
import de.shepiii.livesupport.player.SupportPlayerRegistry;
import de.shepiii.livesupport.punish.PunishRepository;
import de.shepiii.livesupport.queue.SupportQueue;
import de.shepiii.livesupport.support.subcommand.SubCommandRepository;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class SupportCommand extends Command {
  private static final String COMMAND_NAME = "support";
  private final MessageRepository messageRepository;
  private final SupportQueue supportQueue;
  private final SupportPlayerRegistry supportPlayerRegistry;
  private final SubCommandRepository subCommandRepository;
  private final PunishRepository punishRepository;

  @Inject
  private SupportCommand(
    MessageRepository messageRepository,
    SupportQueue supportQueue,
    SupportPlayerRegistry supportPlayerRegistry,
    SubCommandRepository subCommandRepository,
    PunishRepository punishRepository) {
    super(COMMAND_NAME);
    this.messageRepository = messageRepository;
    this.supportQueue = supportQueue;
    this.supportPlayerRegistry = supportPlayerRegistry;
    this.subCommandRepository = subCommandRepository;
    this.punishRepository = punishRepository;
  }

  @Override
  public void execute(CommandSender sender, String[] arguments) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage("Du musst ein Spieler sein!");
      return;
    }
    var player = (ProxiedPlayer) sender;
    var supportPlayer = supportPlayerRegistry.getSupportPlayer(player.getUniqueId());
    if (arguments.length == 0) {
      checkBanAndPerformCommand(supportPlayer);
      return;
    }
    performSubCommand(sender, supportPlayer, arguments);
  }

  private void performSubCommand(CommandSender sender, SupportPlayer supportPlayer, String[] arguments) {
    var subCommand = arguments[0];
    var subCommandOptional = subCommandRepository.findSubCommand(subCommand);
    if (subCommandOptional.isEmpty()) {
      sender.sendMessage(messageRepository.findMessage("support.subcommand.invalid"));
    } else {
      subCommandOptional.get().performSubCommand(supportPlayer, arguments);
    }
  }

  private void checkBanAndPerformCommand(SupportPlayer supportPlayer) {
    punishRepository.findLatestValidPunish(supportPlayer.id(), punishOptional -> {
      if (punishOptional.isPresent()) {
        var punish = punishOptional.get();
        Date date = new Date(punish.getEnd());
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        supportPlayer.proxiedPlayer().sendMessage(
          messageRepository.findMessage("support.punish.client")
            .replace("{0}", format.format(date)));
        return;
      }
      performDefaultCommand(supportPlayer);
    });

  }

  private void performDefaultCommand(SupportPlayer supportPlayer) {
    var player = supportPlayer.proxiedPlayer();
    if (!performCheck(player, supportPlayer)) {
      return;
    }
    if (supportQueue.waiting().isEmpty()) {
      var message = messageRepository.findMessage("support.queue.join.first")
        .replace("{0}", player.getDisplayName());
      for (SupportPlayer staff : supportQueue.staffs()) {
        staff.proxiedPlayer().sendMessage(message);
      }
    }
    if (supportPlayer.supportSession().isPresent()
      && !supportPlayer.supportSession().get().supportSessionState().canCreateNewChat()) {
      player.sendMessage(messageRepository.findMessage("support.session.alreadyin"));
      return;
    }
    supportQueue.addWaiting(supportPlayer);
    player.sendMessage(messageRepository.findMessage("support.queue.joined"));
  }

  private boolean performCheck(ProxiedPlayer player, SupportPlayer supportPlayer) {
    if (player.hasPermission("support.staff")) {
      player.sendMessage(messageRepository.findMessage("support.current")
        .replace("%current%", supportQueue.state().display()));
      return false;
    }
    if (!supportQueue.state().newPlayer()) {
      player.sendMessage(
        messageRepository.findMessage("support.queue.closed"));
      return false;
    }
    if (supportQueue.waiting().contains(supportPlayer)) {
      player.sendMessage(
        messageRepository.findMessage("support.queue.alreadyjoined"));
      return false;
    }
    return true;
  }
}
