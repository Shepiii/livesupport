package de.shepiii.livesupport.queue;

import net.md_5.bungee.api.ChatColor;

public enum SupportQueueState {
  OPEN(ChatColor.GREEN + "Geöffnet", true),
  CLOSED(ChatColor.RED + "Geschlossen", false);

  private final String display;
  private final boolean newPlayer;

  SupportQueueState(String display, boolean newPlayer) {
    this.display = display;
    this.newPlayer = newPlayer;
  }

  public String display() {
    return display;
  }

  public boolean newPlayer() {
    return newPlayer;
  }
}
