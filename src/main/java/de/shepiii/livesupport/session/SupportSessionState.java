package de.shepiii.livesupport.session;

public enum SupportSessionState {
  ACTIVE(true, false),
  CLOSED(false, true),
  LEFT_BY_STAFF(false, true);

  private final boolean privateChatActive;
  private final boolean canCreateNewChat;

  SupportSessionState(boolean privateChatActive, boolean canCreateNewChat) {
    this.privateChatActive = privateChatActive;
    this.canCreateNewChat = canCreateNewChat;
  }

  public boolean privateChatActive() {
    return privateChatActive;
  }
  public boolean canCreateNewChat() {
    return canCreateNewChat;
  }
}
