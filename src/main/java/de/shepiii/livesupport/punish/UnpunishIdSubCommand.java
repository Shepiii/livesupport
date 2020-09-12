package de.shepiii.livesupport.punish;

import com.google.inject.Inject;
import de.shepiii.livesupport.message.MessageRepository;
import de.shepiii.livesupport.player.SupportPlayer;
import de.shepiii.livesupport.support.subcommand.SubCommandExecutor;
import de.shepiii.livesupport.uuid.UUIDFetcher;

import java.util.UUID;

public final class UnpunishIdSubCommand implements SubCommandExecutor {
  private final MessageRepository messageRepository;
  private final PunishRepository punishRepository;

  @Inject
  private UnpunishIdSubCommand(
    MessageRepository messageRepository, PunishRepository punishRepository
  ) {
    this.messageRepository = messageRepository;
    this.punishRepository = punishRepository;
  }

  @Override
  public void performSubCommand(SupportPlayer supportPlayer, String[] arguments) {
    if (!canPerform(supportPlayer, arguments)) {
      return;
    }
    var idString = arguments[1];
    try {
      var id = Long.valueOf(idString);
      unPunishId(id, supportPlayer);
    } catch (NumberFormatException numberFormatException) {
      supportPlayer.sendMessage(messageRepository.findMessage("support.unpunish.wrongId"));
    }
  }

  private boolean canPerform(SupportPlayer supportPlayer, String[] arguments) {
    if (!supportPlayer.hasPermission("support.unpunish")) {
      supportPlayer.sendMessage(messageRepository.findMessage("support.noPermission"));
      return false;
    }
    if (arguments.length != 2) {
      supportPlayer.sendMessage(messageRepository.findMessage("support.punish.wrongarguments"));
      return false;
    }
    return true;
  }

  private void unPunishId(Long id, SupportPlayer supportPlayer) {
    punishRepository.findByIdNullable(id, punishOptional -> {
      if (punishOptional.isEmpty()) {
        supportPlayer.sendMessage(messageRepository.findMessage("support.unpunish.wrongId"));
        return;
      }
      var punish = punishOptional.get();
      if (punish.isUnPunished()) {
        supportPlayer.sendMessage(messageRepository.findMessage("support.unpunish.wrongId"));
        return;
      }
      punish.setUnPunished(true);
      punish.setUnPunishedId(supportPlayer.id().toString());
      punishRepository.saveOrUpdate(punish);
      sendSuccessfulMessage(supportPlayer, punish);
    });
  }

  private void sendSuccessfulMessage(SupportPlayer supportPlayer, Punish punish) {
    UUIDFetcher.getName(UUID.fromString(punish.getPunishedId()), targetName -> {
      if (targetName == null) {
        targetName = "N/A";
      }
      supportPlayer.sendMessage(
        messageRepository.findMessage("support.unpunish.succesfull")
          .replace("{0}", targetName)
      );
    });
  }
}
