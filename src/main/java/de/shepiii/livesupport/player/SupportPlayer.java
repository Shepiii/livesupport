package de.shepiii.livesupport.player;

import de.shepiii.livesupport.session.SupportSession;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Optional;
import java.util.UUID;

public final class SupportPlayer {
  private final ProxiedPlayer proxiedPlayer;
  private Optional<SupportSession> supportSession = Optional.empty();

  public static SupportPlayer forPlayer(ProxiedPlayer proxiedPlayer) {
    return new SupportPlayer(proxiedPlayer);
  }

  private SupportPlayer(ProxiedPlayer proxiedPlayer) {
    this.proxiedPlayer = proxiedPlayer;
  }

  public void sendMessage(String message) {
    proxiedPlayer.sendMessage(message);
  }

  public void supportSession(SupportSession supportSession) {
    this.supportSession = Optional.ofNullable(supportSession);
  }

  public boolean hasPermission(String permission) {
    return proxiedPlayer.hasPermission(permission);
  }

  public ProxiedPlayer proxiedPlayer() {
    return proxiedPlayer;
  }

  public UUID id() {
    return proxiedPlayer.getUniqueId();
  }

  public Optional<SupportSession> supportSession() {
    return supportSession;
  }
}
