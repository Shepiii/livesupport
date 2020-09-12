package de.shepiii.livesupport.support.subcommand;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.inject.Singleton;

import java.util.Map;
import java.util.Optional;

@Singleton
public final class SubCommandRepository {
  private final Map<String, SubCommandExecutor> subCommandExecutorMap
    = Maps.newHashMap();

  public void addSubCommandExecutor(
    SubCommandExecutor subCommandExecutor, String subCommand
  ) {
    Preconditions.checkNotNull(subCommandExecutor);
    Preconditions.checkNotNull(subCommand);
    subCommandExecutorMap.put(subCommand, subCommandExecutor);
  }

  public Optional<SubCommandExecutor> findSubCommand(String subCommand) {
    Preconditions.checkNotNull(subCommand);
    return Optional.ofNullable(subCommandExecutorMap.get(subCommand));
  }
}
