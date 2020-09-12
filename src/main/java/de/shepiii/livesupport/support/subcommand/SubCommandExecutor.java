package de.shepiii.livesupport.support.subcommand;

import de.shepiii.livesupport.player.SupportPlayer;

public interface SubCommandExecutor {
  void performSubCommand(SupportPlayer supportPlayer, String[] arguments);
}
