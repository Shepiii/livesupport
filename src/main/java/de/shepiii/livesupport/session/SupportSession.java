package de.shepiii.livesupport.session;

import de.shepiii.livesupport.player.SupportPlayer;

public final class SupportSession {
  private SupportSessionState supportSessionState;
  private final SupportPlayer staff;
  private final SupportPlayer client;

  public static SupportSession create(
    SupportSessionState supportSessionState,
    SupportPlayer staff,
    SupportPlayer client
  ) {
    return new SupportSession(supportSessionState, staff, client);
  }

  private SupportSession(
    SupportSessionState supportSessionState,
    SupportPlayer staff,
    SupportPlayer client
  ) {
    this.supportSessionState = supportSessionState;
    this.staff = staff;
    this.client = client;
  }

  public void supportSessionState(SupportSessionState supportSessionState) {
    this.supportSessionState = supportSessionState;
  }

  public SupportSessionState supportSessionState() {
    return supportSessionState;
  }

  public SupportPlayer staff() {
    return staff;
  }

  public SupportPlayer client() {
    return client;
  }
}
