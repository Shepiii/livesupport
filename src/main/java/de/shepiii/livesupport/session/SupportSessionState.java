package de.shepiii.livesupport.session;

public enum SupportSessionState {
  ACTIVE(false, true, false),
  CLOSED(true, false, true),
  LEFT_BY_STAFF(true, false, true);

  private final boolean canVote;
  private final boolean privateChatActive;
  private final boolean canCreateNewChat;

  SupportSessionState(boolean canVote, boolean privateChatActive, boolean canCreateNewChat) {
    this.canVote = canVote;
    this.privateChatActive = privateChatActive;
    this.canCreateNewChat = canCreateNewChat;
  }

  public boolean canVote() {
    return canVote;
  }

  public boolean privateChatActive() {
    return privateChatActive;
  }

  public boolean canCreateNewChat() {
    return canCreateNewChat;
  }
}
