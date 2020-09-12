package de.shepiii.livesupport.punish;

import com.google.inject.Inject;
import de.shepiii.livesupport.message.MessageRepository;
import de.shepiii.livesupport.player.SupportPlayer;
import de.shepiii.livesupport.support.subcommand.SubCommandExecutor;
import de.shepiii.livesupport.uuid.UUIDFetcher;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public final class PunishLogSubCommand implements SubCommandExecutor {
  private final MessageRepository messageRepository;
  private final PunishRepository punishRepository;

  @Inject
  private PunishLogSubCommand(
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
    var targetName = arguments[1];
    UUIDFetcher.getUUID(targetName, targetId -> {
      if (targetId == null) {
        supportPlayer.sendMessage(messageRepository.findMessage("support.punish.notFound"));
        return;
      }
      supportPlayer.sendMessage(
        messageRepository.findMessage("support.punishlog.firstMessage")
          .replace("{0}", targetName));
      punishRepository.findPunishList(targetId, punishes -> {
        if (punishes == null || punishes.isEmpty()) {
          supportPlayer.sendMessage(messageRepository.findMessage("support.punishlog.noPunishes"));
          return;
        }
        for (Punish punish : punishes) {
          displayPunish(supportPlayer, punishes.indexOf(punish), punish);
        }
      });
    });
  }

  private boolean canPerform(SupportPlayer supportPlayer, String[] arguments) {
    if (!supportPlayer.hasPermission("support.punishlog")) {
      supportPlayer.sendMessage(messageRepository.findMessage("support.noPermission"));
      return false;
    }
    if (arguments.length != 2) {
      supportPlayer.sendMessage(messageRepository.findMessage("support.punishlog.wrongarguments"));
      return false;
    }
    return true;
  }

  private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

  private void displayPunish(SupportPlayer supportPlayer, int index, Punish punish) {
    var date = new Date(punish.getPronounced());
    UUIDFetcher.getName(UUID.fromString(punish.getWhoPunishedId()), whoPunishedName -> {
      if (whoPunishedName == null) {
        whoPunishedName = "N/A";
      }
      supportPlayer.sendMessage(
        messageRepository.findMessage("support.punishlog.singlePunish")
          .replace("{Anzahl}", String.valueOf(index))
          .replace("{Datum}", format.format(date))
          .replace("{0}", whoPunishedName)
      );
      displayUnPunishState(supportPlayer, punish);
    });
  }

  private void displayUnPunishState(SupportPlayer supportPlayer, Punish punish) {
    if (!punish.isUnPunished()) {
      sendRemovePunishTextComponent(supportPlayer, punish);
      return;
    }
    sendAlreadyRemovedMessage(supportPlayer, punish);
  }

  private void sendRemovePunishTextComponent(SupportPlayer supportPlayer, Punish punish) {
    var textComponent = new TextComponent();
    textComponent.setText(messageRepository.findMessage("support.punishlog.removePunish"));
    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
      "/support unpunishid " + punish.getId())
    );
    supportPlayer.proxiedPlayer().sendMessage(textComponent);
    return;
  }

  private void sendAlreadyRemovedMessage(SupportPlayer supportPlayer, Punish punish) {
    try {
      var whoUnpunishedName =
        UUIDFetcher.getName(UUID.fromString(punish.getUnPunishedId()));
      if (whoUnpunishedName == null) {
        whoUnpunishedName = "N/A";
      }
      sendRemovedPunishLogMessage(supportPlayer, whoUnpunishedName);
    } catch (Exception exception) {
      sendRemovedPunishLogMessage(supportPlayer, "N/A");
    }
  }

  private void sendRemovedPunishLogMessage(
    SupportPlayer supportPlayer, String whoUnpunishedName
  ) {
    supportPlayer.sendMessage(
      messageRepository.findMessage("support.punishlog.removed")
        .replace("{0}", whoUnpunishedName)
    );
  }
}
