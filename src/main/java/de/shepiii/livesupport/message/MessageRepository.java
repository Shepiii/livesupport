package de.shepiii.livesupport.message;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.md_5.bungee.api.ChatColor;

@Singleton
public final class MessageRepository {
  private final MessageConfiguration messageConfiguration;

  @Inject
  private MessageRepository(
    MessageConfiguration messageConfiguration
  ) {
    this.messageConfiguration = messageConfiguration;
  }

  private static final String defaultMessage = ChatColor.RED + "Ein unerwarteter Fehler ist aufgetreten!";

  public String findMessage(String key) {
    return translateShortcuts(
      messageConfiguration.getMessages().getOrDefault(key, defaultMessage));
  }

  private String translateShortcuts(String string) {
    if (string == null || string.equals("")) {
      return defaultMessage;
    }
    return string
      .replace("%prefix%", messageConfiguration.getPrefix())
      .replace("&", "§");
  }
}
