package de.shepiii.livesupport.punish;

import com.google.inject.Inject;
import de.shepiii.livesupport.message.MessageRepository;
import de.shepiii.livesupport.player.SupportPlayer;
import de.shepiii.livesupport.punish.step.PunishStepConfigFile;
import de.shepiii.livesupport.support.subcommand.SubCommandExecutor;
import de.shepiii.livesupport.uuid.UUIDFetcher;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class PunishSubCommand implements SubCommandExecutor {
  private final MessageRepository messageRepository;
  private final PunishRepository punishRepository;
  private final PunishStepConfigFile punishStepConfigFile;

  @Inject
  private PunishSubCommand(
    MessageRepository messageRepository,
    PunishRepository punishRepository,
    PunishStepConfigFile punishStepConfigFile) {
    this.messageRepository = messageRepository;
    this.punishRepository = punishRepository;
    this.punishStepConfigFile = punishStepConfigFile;
  }

  @Override
  public void performSubCommand(SupportPlayer supportPlayer, String[] arguments) {
    if (!canPerform(supportPlayer, arguments)) {
      return;
    }
    UUIDFetcher.getUUID(arguments[1], targetId -> {
      if (targetId == null) {
        supportPlayer.proxiedPlayer().sendMessage(
          messageRepository.findMessage("support.punish.notFound")
            .replace("{0}", arguments[1]));
        return;
      }
      punishRepository.isPunished(targetId, punished -> {
        if (punished) {
          supportPlayer.proxiedPlayer().sendMessage(messageRepository.findMessage("support.punish.alreadypunished"));
          return;
        }
        try {
          punishTarget(supportPlayer, arguments[1], targetId);
        } catch (Exception exception) {
          exception.printStackTrace();
        }
      });
    });
  }

  private boolean canPerform(SupportPlayer supportPlayer, String[] arguments) {
    if (!supportPlayer.proxiedPlayer().hasPermission("support.punish")) {
      supportPlayer.proxiedPlayer().sendMessage(messageRepository.findMessage("support.noPermission"));
      return false;
    }
    if (arguments.length != 2) {
      supportPlayer.proxiedPlayer().sendMessage(messageRepository.findMessage("support.punish.wrongarguments"));
      return false;
    }
    return true;
  }

  private void punishTarget(
    SupportPlayer supportPlayer,
    String targetName,
    UUID targetId
  ) throws Exception {
    var punishSteps = punishStepConfigFile.read().getPunishSteps();
    punishRepository.findValidPunishList(targetId, punishes -> {
      try {
        long end = findEndOfPunish(punishes, punishSteps);
        var punish = new Punish(
          targetId.toString(), supportPlayer.id().toString(), end);
        punishRepository.saveOrUpdate(punish);
        supportPlayer.proxiedPlayer().sendMessage(
          messageRepository.findMessage("support.punish.punished")
            .replace("{0}", targetName));
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    });
  }

  private long findEndOfPunish(List<Punish> punishes, Map<Integer, Long> punishSteps) {
    if (punishes == null) {
      return punishSteps.get(0) + System.currentTimeMillis();
    } else if (punishes.size() > punishSteps.size()) {
      return -1L;
    } else {
      return punishSteps.get(punishes.size()) + System.currentTimeMillis();
    }
  }
}
